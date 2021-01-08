package biz.bokhorst.xprivacy; // (rank 802) copied from https://github.com/M66B/XPrivacy/blob/b88626cde79f3b2d249298292902ba02678ec1af/src/biz/bokhorst/xprivacy/IniFile.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IniFile {

	private Map<String, String> mIni = new HashMap<String, String>();

	public IniFile(File file) throws IOException {
		String line;
		Pattern pattern = Pattern.compile("\\s*([^=]*)=(.*)");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		while ((line = br.readLine()) != null)
			if (!line.startsWith("#")) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					String key = matcher.group(1).trim();
					String value = matcher.group(2).trim();
					mIni.put(key, value);
				}
			}
		br.close();
		fr.close();
	}

	public String get(String key, String defaultvalue) {
		return mIni.get(key);
	}
}