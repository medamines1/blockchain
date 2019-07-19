package com.example.med.blockchainproj.DAPP;

import java.sql.Timestamp;

public class utils {
	
	
	public static Timestamp getTimeStamp() {
		return  new Timestamp(System.currentTimeMillis());
	}

	public static float get_percent_of_value(float diff,int perc) {
		return (diff*perc)/100;
	}
	public static String formatString(String text){

		StringBuilder json = new StringBuilder();
		String indentString = "";

		boolean inQuotes = false;
		boolean isEscaped = false;

		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);

			switch (letter) {
				case '\\':
					isEscaped = !isEscaped;
					break;
				case '"':
					if (!isEscaped) {
						inQuotes = !inQuotes;
					}
					break;
				default:
					isEscaped = false;
					break;
			}

			if (!inQuotes && !isEscaped) {
				switch (letter) {
					case '{':
					case '[':
						json.append("\n" + indentString + letter + "\n");
						indentString = indentString + "\t";
						json.append(indentString);
						break;
					case '}':
					case ']':
						indentString = indentString.replaceFirst("\t", "");
						json.append("\n" + indentString + letter);
						break;
					case ',':
						json.append(letter + "\n" + indentString);
						break;
					default:
						json.append(letter);
						break;
				}
			} else {
				json.append(letter);
			}
		}

		return json.toString();
	}
}
