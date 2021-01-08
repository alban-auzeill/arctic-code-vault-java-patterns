import java.io.IOException; // (rank 229) copied from https://github.com/robovm/robovm/blob/ef091902377c00dc0fb2db87e8d79c8afb5e9010/tests/libcore/luni/src/test/java/RoboVMAllTests.java
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.runner.Version;
import libcore.io.Libcore;
import libcore.util.BasicLruCache;

import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.AllTests;
import org.junit.runners.model.TestClass;
import org.robovm.rt.VM;

/**
 * Builds a JUnit {@link TestSuite} from the libcore tests. Tests listed in the 
 * <code>/robovm-excluded-tests</code> file in the classpath will not be run.
 * The <code>ROBOVM_TESTS_INCLUDE_PATTERN</code> and <code>ROBOVM_TESTS_EXCLUDE_PATTERN</code>
 * environment variables can be used to specify regular expression pattern that are used to
 * include/exclude tests. These are matched against a test's fully qualified class name.
 */
@RunWith(AllTests.class)
public class RoboVMAllTests {
    static List<String> excludedTests = null;
    static List<Class<?>> allClasses;
    
    static {
        // Read in the list of tests that should be excluded
        excludedTests = readLines("/robovm-excluded-tests");
        // Read in the list of classes generated by the ANT build script.
        allClasses = new ArrayList<Class<?>>(3000);
        for (String line : readLines("/all-classes.txt")) {
            try {
                allClasses.add(Class.forName(line));
            } catch (Throwable t) {
            }
        }

        // Many tests call InetAddress.getLocalHost() but it may fail if the
        // hostname doesn't resolve to an IP address. This will install a fake
        // InetAddress for the hostname if needed.
        try {
            InetAddress.getLocalHost();
        } catch (UnknownHostException uhe) {
            try {
                installFakeLocalHostAddressCacheEntry();
            } catch (Throwable e) {
                throw new Error(e);
            }
        }

        // user.dir is supposed to be the current working dir. On iOS user.dir 
        // gets set to /. Some tests write files to this dir and when it's /
        // those tests fail with a "permission denied" error.
        if (System.getProperty("os.name").contains("iOS") && "/".equals(System.getProperty("user.dir"))) {
            System.setProperty("user.dir", VM.basePath() + "/../Library");
        }
    }

    private static Field field(Class<?> cls, String name) throws Throwable {
        Field f = cls.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }
    
    private static InetAddress getFakeLocalHost() throws Throwable {
        Constructor<?> inet4AddressConstructor = Inet4Address.class.getDeclaredConstructor(byte[].class, String.class);
        inet4AddressConstructor.setAccessible(true);
        String[] candidates = new String[] {"eth0", "eth1", "wlan0", "wlan1", "en0", "en1", "lo", "lo0"};
        String hostname = Libcore.os.uname().nodename;
        for (int i = 0; i < candidates.length; i++) {
            NetworkInterface ni = NetworkInterface.getByName(candidates[i]);
            if (ni != null && !ni.isLoopback()) {
                Enumeration<InetAddress> addrEn = ni.getInetAddresses();
                while (addrEn.hasMoreElements()) {
                    InetAddress addr = addrEn.nextElement();
                    if (addr instanceof Inet4Address) {
                        return (InetAddress) inet4AddressConstructor.newInstance(addr.getAddress(), hostname);
                    }
                }
            }
        }
        return (InetAddress) inet4AddressConstructor.newInstance(new byte[] {127, 0, 0, 1}, hostname);
    }
    
    private static void installFakeLocalHostAddressCacheEntry() throws Throwable {
        Class<?> addressCacheClass = Class.forName("java.net.AddressCache");
        Class<?> addressCacheEntryClass = Class.forName("java.net.AddressCache$AddressCacheEntry");
        Constructor<?> entryConstructor = addressCacheEntryClass.getDeclaredConstructor(Object.class);
        entryConstructor.setAccessible(true);
        InetAddress[] addresses = new InetAddress[] { getFakeLocalHost() };
        Object entry = entryConstructor.newInstance(new Object[] {addresses});
        field(addressCacheEntryClass, "expiryNanos").set(entry, Long.MAX_VALUE);
        BasicLruCache<Object, Object> cache = new BasicLruCache<Object, Object>(64);
        cache.put(Libcore.os.uname().nodename, entry);
        field(addressCacheClass, "cache").set(field(InetAddress.class, "addressCache").get(null), cache);
    }
    
    @SuppressWarnings("unchecked")
    private static void cleanupHttpConnectionPool() throws Throwable {
        // Class<?> poolClass = Class.forName("libcore.net.http.HttpConnectionPool");
        // Map<Object, List<Object>> connections = 
        //         (Map<Object, List<Object>>) field(poolClass, "connectionPool").get(field(poolClass, "INSTANCE").get(null));
        // Method closeSocketAndStreamsMethod = Class.forName("libcore.net.http.HttpConnection").getDeclaredMethod("closeSocketAndStreams");
        // closeSocketAndStreamsMethod.setAccessible(true);
        // for (List<Object> l : connections.values()) {
        //     for (Object connection : l) {
        //         closeSocketAndStreamsMethod.invoke(connection);
        //     }
        // }
        // connections.clear();
    }
    
    /**
     * Wraps a {@link Test} and "forgets" it after it's been run in order to make it reclaimable
     * by the GC (unless it's reachable from somewhere other than JUnit).
     */
    private static class ForgettingTestWrapper implements Test {
        private Test t;
        private int count;

        ForgettingTestWrapper(Test t) {
            this.t = t;
            this.count = t.countTestCases();
        }

        @Override
        public int countTestCases() {
            return count;
        }

        @Override
        public void run(TestResult result) {
            if (t == null) {
                Assert.fail("Test has already been run");
            }
            System.err.println(t);
            // Some tests set the default locale without resetting it which causes subsequent 
            // tests to fail. Remember the default locale and restore it after each test.
            Locale defLoc = Locale.getDefault();
            try {
                t.run(result);
            } finally {
                t = null;
                Locale.setDefault(defLoc);
                try {
                    // Close any connections created by the test and now pooled by 
                    // libcore.net.http.HttpConnectionPool. If we don't do this
                    // we'll end up with "Too many open files" errors on iOS when 
                    // running the tests.
                    cleanupHttpConnectionPool();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static List<Test> findTests(Pattern include, Pattern exclude) throws IOException {
        List<Test> tests = new ArrayList<Test>();
        for (Class<?> c : allClasses) {
            String name = c.getName();
            if (!include.matcher(name).matches() || exclude.matcher(name).matches()) {
                continue;
            }
            try {
                if (!c.isInterface() && Modifier.isPublic(c.getModifiers()) && ! Modifier.isAbstract(c.getModifiers())) {
                    Test test = createTest(c);
                    if (hasTests(test)) {
                        tests.add(test);
                    }
                }
            } catch (Throwable t) {
            }
        }
        return tests;
    }
    
    private static Test createTest(Class<?> c) {
        if (TestCase.class.isAssignableFrom(c)) {
            return new TestSuite(c);
        } else if (!new TestClass(c).getAnnotatedMethods(org.junit.Test.class).isEmpty()) {
            return new JUnit4TestAdapter(c);
        }
        return null;
    }
    
    private static boolean hasTests(Test test) {
        if (test == null) {
            return false;
        }
        if (test.countTestCases() > 1) {
            return true;
        }
        if (!(test instanceof TestSuite)) {
            return true;
        }
        TestSuite suite = (TestSuite) test;
        if (!(suite.testAt(0) instanceof TestCase)) {
            return true;
        }
        TestCase singleTest = (TestCase) suite.testAt(0);
        return !singleTest.getName().equals("warning");
    }
    
    private static void removeExcludedTests(TestSuite tests, TestSuite result) {
        Enumeration<Test> en = tests.tests();
        while (en.hasMoreElements()) {
            Test test = en.nextElement();
            if (test instanceof TestSuite) {
                removeExcludedTests((TestSuite) test, result);
            } else if (test instanceof TestCase) {
                String fullName = test.getClass().getName() + "#" + ((TestCase) test).getName();
                boolean match = false;
                for (String excluded : excludedTests) {
                    if (fullName.equals(excluded)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    result.addTest(test);
                }
            } else {
                // TODO: Filter Junit 4 tests?
                result.addTest(test);
            }
        }
    }
    
    private static TestSuite removeExcludedTests(TestSuite tests) {
        TestSuite result = new TestSuite();
        removeExcludedTests(tests, result);
        return result;
    }
    
    private static TestSuite wrapTests(TestSuite tests) {
        TestSuite result = new TestSuite();
        Enumeration<Test> en = tests.tests();
        while (en.hasMoreElements()) {
            Test test = en.nextElement();
            result.addTest(new ForgettingTestWrapper(test));
        }
        return result;
    }
    
    private static TestSuite suite(Pattern include, Pattern exclude, boolean runExcluded) throws IOException {
        TestSuite suite = new TestSuite();
        for (Test test : findTests(include, exclude)) {
            suite.addTest(test);
        }
        if (runExcluded) {
            return wrapTests(suite);
        }
        return wrapTests(removeExcludedTests(suite));
    }
    
    public static TestSuite suite() throws IOException {
        String include = System.getenv("ROBOVM_TESTS_INCLUDE_PATTERN");
        if (include == null) {
            include = ".*";
        }
        String exclude = System.getenv("ROBOVM_TESTS_EXCLUDE_PATTERN");
        if (exclude == null) {
            exclude = " ";
        }
        boolean runExcluded = Boolean.parseBoolean(System.getenv("ROBOVM_TESTS_RUN_EXCLUDED"));
        return suite(Pattern.compile(include), Pattern.compile(exclude), runExcluded);
    }
    
    private static long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[4096];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    private static List<String> readLines(String path) {
        try {
            InputStreamReader in = new InputStreamReader(RoboVMAllTests.class.getResourceAsStream(path), "UTF8");
            StringWriter out = new StringWriter();
            copy(in, out);
            ArrayList<String> lines = new ArrayList<String>();
            for (String s : out.toString().split("\\s+")) {
                s = s.trim();
                if (!s.startsWith("#") && s.length() > 0) {
                    lines.add(s);
                }
            }
            return lines;
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("JUnit version " + Version.id());
        JUnitCore core = new JUnitCore();
        core.addListener(new TextListener(System.out) {
            @Override
            public void testStarted(Description description) {
                System.out.print(description + " ... ");
            }
            @Override
            public void testFinished(Description description) {
                System.out.println();
            }
            @Override
            public void testFailure(Failure failure) {
                System.out.print("FAILED!");
            }
            @Override
            public void testIgnored(Description description) {
                System.out.println("IGNORED!");
            }
        });
        core.run(
            suite(
                Pattern.compile(args.length > 0 ? args[0] : ".*"), 
                Pattern.compile(args.length > 1 ? args[1] : " "), 
                false));
    }
    
}
