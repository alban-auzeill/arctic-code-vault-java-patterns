/** (rank 330) copied from https://github.com/pmd/pmd/blob/d5729e65dc838bf963a85b4aae61cb87e44b811e/pmd-core/src/test/java/net/sourceforge/pmd/properties/RegexPropertyTest.java
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.properties;

import java.util.List;
import java.util.regex.Pattern;


/**
 * Since there's no RegexMultiProperty the base class is only partially implemented,
 * and some tests are overridden with no-op ones.
 *
 * @author Clément Fournier
 * @since 6.2.0
 */
@Deprecated
public class RegexPropertyTest extends AbstractPropertyDescriptorTester<Pattern> {
    public RegexPropertyTest() {
        super("Regex");
    }


    @Override
    protected Pattern createValue() {
        return Pattern.compile("abc++");
    }


    @Override
    protected Pattern createBadValue() {
        return null;
    }


    @Override
    protected PropertyDescriptor<Pattern> createProperty() {
        return RegexProperty.named("foo").defaultValue("(ec|sa)+").desc("the description").build();
    }


    @Override
    protected PropertyDescriptor<Pattern> createBadProperty() {
        return RegexProperty.named("foo").defaultValue("(ec|sa").desc("the description").build();
    }


    // The following are deliberately unimplemented, since they are only relevant to the tests of the multiproperty

    @Override
    protected PropertyDescriptor<List<Pattern>> createMultiProperty() {
        throw new UnsupportedOperationException();
    }


    @Override
    protected PropertyDescriptor<List<Pattern>> createBadMultiProperty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddAttributesMulti() {
    }


    @Override
    public void testAsDelimitedString() {
    }


    @Override
    public void testErrorForBadMulti() {
    }


    @Override
    public void testErrorForCorrectMulti() {
    }


    @Override
    public void testFactoryMultiValueDefaultDelimiter() {
    }


    @Override
    public void testFactoryMultiValueCustomDelimiter() {
    }


    @Override
    public void testTypeMulti() {
    }


    @Override
    public void testIsMultiValueMulti() {
    }


}
