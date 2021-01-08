package org.cryptomator.ui.controls; // (rank 437) copied from https://github.com/cryptomator/cryptomator/blob/2720a999d190978fa2db649e4b5f35c1f88d3320/main/ui/src/main/java/org/cryptomator/ui/controls/NumericTextField.java

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import java.util.regex.Pattern;

public class NumericTextField extends TextField {

	private final static Pattern DIGIT_PATTERN = Pattern.compile("\\d*");

	public NumericTextField() {
		this.setTextFormatter(new TextFormatter<>(this::filterNumericTextChange));
	}

	private TextFormatter.Change filterNumericTextChange(TextFormatter.Change change) {
		return DIGIT_PATTERN.matcher(change.getText()).matches() ? change : null;
	}

}
