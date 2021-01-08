package sfBugsNew; // (rank 359) copied from https://github.com/spotbugs/spotbugs/blob/a6f9acb2932b54f5b70ea8bc206afb552321a222/spotbugsTestCases/src/java/sfBugsNew/Bug1262.java

import java.util.regex.Pattern;

import edu.umd.cs.findbugs.annotations.NoWarning;

public class Bug1262 {

    @NoWarning("RE_BAD_SYNTAX_FOR_REGULAR_EXPRESSION")
    public Pattern falsePositive(String s) {
        Pattern somePattern = Pattern.compile("(?<someGroup>\\w*)");
        return somePattern;
    }

}
