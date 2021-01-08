package edu.stanford.nlp.international.arabic.pipeline; // (rank 38) copied from https://github.com/stanfordnlp/CoreNLP/blob/f362675e891b720668e90cf774e480fc38ff8c85/src/edu/stanford/nlp/international/arabic/pipeline/BiesModifiedMapper.java

import java.util.regex.Pattern;

public class BiesModifiedMapper extends LDCPosMapper {

	public BiesModifiedMapper() {
		mapping = Pattern.compile("(\\S+)\t(\\S+)");
	}
}
