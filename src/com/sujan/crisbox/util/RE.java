package com.sujan.crisbox.util;

import java.util.regex.*;
import java.util.*;

public class RE {
	public boolean setPattern(String pat) {
		pattern = null;
		try {
			pattern = Pattern.compile(pat);
		} catch (PatternSyntaxException e) {
			System.out.println("Pattern syntax error");
			System.exit(1);// return false;
		}
		return true;
	}

	public boolean match(String str) {
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	Pattern pattern = null;

	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("Err");
			System.exit(0);
		}
		RE re = new RE();
		re.setPattern(args[0]);
		if (re.match(args[1])) {
			System.out.println("Match");
		} else {
			System.out.println("No Match");
		}
	}
}
