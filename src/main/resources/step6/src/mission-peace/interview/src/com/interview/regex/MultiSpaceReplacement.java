package com.interview.regex; // (rank 637) copied from https://github.com/mission-peace/interview/blob/94be5deb0c0df30ade2a569cf3056b7cc1e012f4/src/com/interview/regex/MultiSpaceReplacement.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiSpaceReplacement {

    public void replace(String str){
        Pattern pattern = Pattern.compile("^ +|  +| +$");
        Matcher matcher = pattern.matcher(str);
        System.out.println(matcher.replaceAll(""));
        
    }
    
    public static void main(String args[]){
        String str = "     This is Tushar  Roy  ";
        MultiSpaceReplacement mrs = new MultiSpaceReplacement();
        mrs.replace(str);
    }
}
