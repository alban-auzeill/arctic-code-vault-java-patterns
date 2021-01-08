package com.ijpay.jdpay.util; // (rank 973) copied from https://github.com/Javen205/IJPay/blob/08059d9a31837d8669deadab1af17887d4a9178c/IJPay-JDPay/src/main/java/com/ijpay/jdpay/util/JdPayXmlUtil.java

import java.util.Scanner;
import java.util.regex.Pattern;

public class JdPayXmlUtil {
    private static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static String XML_JDPAY_START = "<jdpay>";
    private static String XML_JDPAY_END = "</jdpay>";
    private static Pattern PATTERN = Pattern.compile("\t|\r|\n");

    public static String fomatXmlStr(String xml) {
        StringBuilder formatStr = new StringBuilder();
        Scanner scanner = new Scanner(xml);
        scanner.useDelimiter(PATTERN);
        while (scanner.hasNext()) {
            formatStr.append(scanner.next().trim());
        }
        return formatStr.toString();
    }


    public static String addXmlHead(String xml) {
        if (xml != null && !"".equals(xml) &&
                !xml.trim().startsWith("<?xml")) {
            xml = XML_HEAD + xml;
        }

        return xml;
    }


    public static String addXmlHeadAndElJdPay(String xml) {
        if (xml != null && !"".equals(xml)) {
            if (!xml.contains(XML_JDPAY_START)) {
                xml = XML_JDPAY_START + xml;
            }
            if (!xml.contains(XML_JDPAY_END)) {
                xml = xml + XML_JDPAY_END;
            }
            if (!xml.trim().startsWith("<?xml")) {
                xml = XML_HEAD + xml;
            }
        }
        return xml;
    }


    public static String getXmlElm(String xml, String elName) {
        String result = "";
        String elStart = "<" + elName + ">";
        String elEnd = "</" + elName + ">";
        if (xml.contains(elStart) && xml.contains(elEnd)) {
            int from = xml.indexOf(elStart) + elStart.length();
            int to = xml.lastIndexOf(elEnd);
            result = xml.substring(from, to);
        }
        return result;
    }


    public static String delXmlElm(String xml, String elmName) {
        String elStart = "<" + elmName + ">";
        String elEnd = "</" + elmName + ">";
        if (xml.contains(elStart) && xml.contains(elEnd)) {
            int i1 = xml.indexOf(elStart);
            int i2 = xml.lastIndexOf(elEnd);
            String start = xml.substring(0, i1);
            int length = elEnd.length();
            String end = xml.substring(i2 + length, xml.length());
            xml = start + end;
        }
        return xml;
    }
}
