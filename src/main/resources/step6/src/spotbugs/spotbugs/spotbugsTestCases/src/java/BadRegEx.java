import java.util.regex.Pattern; // (rank 359) copied from https://github.com/spotbugs/spotbugs/blob/a6f9acb2932b54f5b70ea8bc206afb552321a222/spotbugsTestCases/src/java/BadRegEx.java

import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;

class BadRegEx {

    @ExpectWarning("RE")
    boolean f(String s) {
        return s.matches("][");
    }

    @ExpectWarning("RE")
    String g(String s) {
        return s.replaceAll("][", "xx");
    }

    @ExpectWarning("RE")
    String h(String s) {
        return s.replaceFirst("][", "xx");
    }

    @ExpectWarning("RE")
    void x(String s) throws Exception {
        Pattern.matches("][", s);
    }

    @ExpectWarning("RE")
    Pattern y(String s) throws Exception {
        return Pattern.compile("][", Pattern.CASE_INSENSITIVE);
    }

    @ExpectWarning("RE")
    Pattern z(String s) {
        return Pattern.compile("][");
    }

    @NoWarning("RE")
    Pattern literalOne(String s) throws Exception {
        return Pattern.compile("][", Pattern.LITERAL); // not a bug
    }

    @NoWarning("RE")
    Pattern literalTwo(String s) throws Exception {
        return Pattern.compile("][", Pattern.CASE_INSENSITIVE | Pattern.LITERAL); // not
                                                                                  // a
                                                                                  // bug
    }

    // this is OK; we shouldn't report a warning here
    @NoWarning("RE")
    String passwordMasking(String s) {
        return s.replaceAll(".", "x");
    }

    private StringBuilder allStatesPost;

    Pattern test() {
        String statesPost = allStatesPost.append(").*$").toString();

        return Pattern.compile(statesPost, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }
}
