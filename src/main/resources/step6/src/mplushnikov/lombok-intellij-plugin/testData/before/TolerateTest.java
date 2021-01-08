import java.util.regex.Pattern; // (rank 819) copied from https://github.com/mplushnikov/lombok-intellij-plugin/blob/ff0ac206568b3fd2ee238a0b6efed7ae01a3f8bf/testData/before/TolerateTest.java

@lombok.Setter
@lombok.Getter
class Tolerate {
	private Pattern pattern;
	
	@lombok.experimental.Tolerate public void setPattern(String pattern) {
		setPattern(Pattern.compile(pattern));
	}
}

@lombok.Getter
@lombok.experimental.Wither
@lombok.AllArgsConstructor
class Tolerate2 {
	private final Pattern pattern;
	
	@lombok.experimental.Tolerate public Tolerate2 withPattern(String pattern) {
		return withPattern(Pattern.compile(pattern));
	}

	public Tolerate2 withPattern(String nameGlob, String extensionGlob) {
		return withPattern(nameGlob.replace("*", ".*") + "\\." + extensionGlob.replace("*", ".*"));
	}
}
