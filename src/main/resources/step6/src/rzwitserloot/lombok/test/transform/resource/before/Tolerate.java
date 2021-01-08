import java.util.regex.Pattern; // (rank 645) copied from https://github.com/rzwitserloot/lombok/blob/4f763367f1092546350324f3040fa84cb0380409/test/transform/resource/before/Tolerate.java

@lombok.Setter @lombok.Getter
class Tolerate {
	private Pattern pattern;
	
	@lombok.experimental.Tolerate public void setPattern(String pattern) {
		setPattern(Pattern.compile(pattern));
	}
}

@lombok.Getter @lombok.experimental.Wither @lombok.AllArgsConstructor
class Tolerate2 {
	private final Pattern pattern;
	
	@lombok.experimental.Tolerate public Tolerate2 withPattern(String pattern) {
		return withPattern(Pattern.compile(pattern));
	}

	public Tolerate2 withPattern(String nameGlob, String extensionGlob) {
		return withPattern(nameGlob.replace("*", ".*") + "\\." + extensionGlob.replace("*", ".*"));
	}
}
