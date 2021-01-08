package io.onedev.server.buildspec.job.action.condition; // (rank 413) copied from https://github.com/theonedev/onedev/blob/f34af86d0d952ec210e84e98fbd02102e6fe230a/server-core/src/main/java/io/onedev/server/buildspec/job/action/condition/ErrorMessageCriteria.java

import java.util.regex.Pattern;

import io.onedev.server.model.Build;
import io.onedev.server.util.criteria.Criteria;

public class ErrorMessageCriteria extends Criteria<Build> {

	private static final long serialVersionUID = 1L;
	
	private final String value;
	
	public ErrorMessageCriteria(String value) {
		this.value = value;
	}

	@Override
	public boolean matches(Build build) {
		return Pattern.compile(value).matcher(build.getErrorMessage()).find();
	}

	@Override
	public String toStringWithoutParens() {
		return quote(Build.NAME_ERROR_MESSAGE) + " " 
				+ ActionCondition.getRuleName(ActionConditionLexer.Contains) + " "
				+ quote(value);
	}

}
