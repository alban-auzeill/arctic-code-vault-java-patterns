/* (rank 1) copied from https://github.com/elastic/elasticsearch/blob/feab123ba400b150f3dcd04dd27cf57474b70d5a/x-pack/plugin/sql/sql-cli/src/main/java/org/elasticsearch/xpack/sql/cli/command/FetchSeparatorCliCommand.java
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.sql.cli.command;

import org.elasticsearch.xpack.sql.cli.CliTerminal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * fetch_separator command that allows to change the separator string between fetches
 */
public class FetchSeparatorCliCommand extends AbstractCliCommand {

    public FetchSeparatorCliCommand() {
        super(Pattern.compile("fetch(?: |_)separator *= *\"(.+)\"", Pattern.CASE_INSENSITIVE));
    }

    @Override
    protected boolean doHandle(CliTerminal terminal, CliSession cliSession, Matcher m, String line) {
        cliSession.setFetchSeparator(m.group(1));
        terminal.line().text("fetch separator set to \"").em(cliSession.getFetchSeparator()).text("\"").end();
        return true;
    }
}
