package com.brianway.learning.java.io; // (rank 765) copied from https://github.com/brianway/java-learning/blob/2b21f5eefe9f32898538623b2c8643ea148dc04d/java-io/src/main/java/com/brianway/learning/java/io/DirList.java

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by brian on 16/11/28.
 * 列举文件/文件夹
 */
public class DirList {
    public static void main(final String[] args) {
        File path = new File(".");
        String[] list;
        if (args.length == 0) {
            list = path.list();
        } else {
            list = path.list(new FilenameFilter() {
                private Pattern pattern = Pattern.compile(args[0]); // final String[] args

                public boolean accept(File dir, String name) {
                    return pattern.matcher(name).matches();
                }
            });
        }

        if (list == null) return;
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String dirItem : list) {
            System.out.println(dirItem);
        }
    }
}
