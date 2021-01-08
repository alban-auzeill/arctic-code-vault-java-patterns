package io.onedev.server.util; // (rank 413) copied from https://github.com/theonedev/onedev/blob/c51a329c1c242b31603b8b59347a807f895d67bb/server-core/src/main/java/io/onedev/server/util/SimpleLogger.java

import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;

public abstract class SimpleLogger {

	private static final Pattern EOL_PATTERN = Pattern.compile("\r?\n");

	public void log(Throwable t) {
		log(null, t);
	}
		
	public void log(@Nullable String message, Throwable t) {
		StringBuilder builder = new StringBuilder();
		if (message != null)
			builder.append(message);
		boolean firstLine = true;
		for (String line: Splitter.on(EOL_PATTERN).split(Throwables.getStackTraceAsString(t))) {
			if (firstLine)
				builder.append(line);
			else
				builder.append("\n    ").append(line);
			firstLine = false;
		}
		log(builder.toString());
	}
	
	public abstract void log(String message);
	
}
