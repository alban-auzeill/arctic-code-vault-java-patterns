/* (rank 142) copied from https://github.com/java-decompiler/jd-gui/blob/b3c1ced04e571fc316648d114ff0c8121e051d8f/services/src/main/java/org/jd/gui/service/treenode/SpiFileTreeNodeFactoryProvider.java
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.gui.service.treenode;

import java.util.regex.Pattern;

public class SpiFileTreeNodeFactoryProvider extends TextFileTreeNodeFactoryProvider {
    @Override public String[] getSelectors() {
        return appendSelectors("*:file:*");
    }

    @Override
    public Pattern getPathPattern() {
        if (externalPathPattern == null) {
            return Pattern.compile("(.*\\/)?META-INF\\/services\\/.*");
        } else {
            return externalPathPattern;
        }
    }
}