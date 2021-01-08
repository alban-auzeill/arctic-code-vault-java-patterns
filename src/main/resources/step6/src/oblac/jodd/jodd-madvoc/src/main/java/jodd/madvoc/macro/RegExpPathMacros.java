// Copyright (c) 2003-present, Jodd Team (http://jodd.org) (rank 170) copied from https://github.com/oblac/jodd/blob/903a10f42b1d8e4f0aed16749d8a7ce9690f52e1/jodd-madvoc/src/main/java/jodd/madvoc/macro/RegExpPathMacros.java
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.madvoc.macro;

import java.util.regex.Pattern;

/**
 * Regular expression path macro.
 * Matches paths using regular expressions.
 */
public class RegExpPathMacros extends BasePathMacros {

	protected Pattern[] regexpPattern;

	@Override
	public boolean init(final String actionPath, final String[] separators) {
		boolean hasMacros = super.init(actionPath, separators);
		if (hasMacros) {
			regexpPattern = new Pattern[macrosCount];
		}
		return hasMacros;
	}

	@Override
	protected boolean matchValue(final int macroIndex, final String value) {
		if (regexpPattern[macroIndex] == null) {
			regexpPattern[macroIndex] = Pattern.compile(patterns[macroIndex]);
		}

		return regexpPattern[macroIndex].matcher(value).matches();
	}
}
