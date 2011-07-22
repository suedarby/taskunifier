package com.leclercb.taskunifier.gui.components.modelnote.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text2HTML {
	
	public static String convert(String text) {
		if (text == null || text.length() == 0)
			return " ";
		
		text = convertNlToBr(text);
		text = convertToHtmlUrl(text);
		
		return text;
	}
	
	private static String convertToHtmlUrl(String text) {
		StringBuffer buffer = new StringBuffer(text);
		
		Pattern p = Pattern.compile("(href=['\"]{1})?((https?|ftp|file):((//)|(\\\\))+[\\w\\d:#@%/;$~_?\\+\\-=\\\\.&]*)");
		Matcher m = null;
		int position = 0;
		
		while (true) {
			m = p.matcher(buffer.toString());
			
			if (!m.find(position))
				break;
			
			position = m.end();
			String firstGroup = m.group(1);
			
			if (firstGroup == null)
				firstGroup = "";
			
			if (firstGroup.contains("href"))
				continue;
			
			String url = firstGroup
					+ "<a href=\""
					+ m.group(2)
					+ "\">"
					+ m.group(2)
					+ "</a>";
			
			buffer.replace(m.start(), m.end(), url);
			
			position = m.start() + url.length() - 1;
		}
		
		return buffer.toString();
	}
	
	private static String convertNlToBr(String text) {
		StringBuffer buffer = new StringBuffer();
		
		text = text.replace("\n", "\n ");
		String[] lines = text.split("\n");
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			
			line = line.trim();
			buffer.append(line);
			if (line.startsWith("<"))
				if (i + 1 < lines.length && lines[i + 1].trim().startsWith("<"))
					continue;
			
			buffer.append("<br />");
		}
		
		return buffer.toString();
	}
	
}