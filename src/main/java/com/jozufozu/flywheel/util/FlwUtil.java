package com.jozufozu.flywheel.util;

import java.util.Arrays;

public class FlwUtil {

	public static String repeatChar(char c, int n) {
		char[] arr = new char[n];

		Arrays.fill(arr, c);

		return new String(arr);
	}

	public static int numDigits(int number) {
		// cursed but allegedly the fastest algorithm, taken from https://www.baeldung.com/java-number-of-digits-in-int
		if (number < 100000) {
			if (number < 100) {
				if (number < 10) {
					return 1;
				} else {
					return 2;
				}
			} else {
				if (number < 1000) {
					return 3;
				} else {
					if (number < 10000) {
						return 4;
					} else {
						return 5;
					}
				}
			}
		} else {
			if (number < 10000000) {
				if (number < 1000000) {
					return 6;
				} else {
					return 7;
				}
			} else {
				if (number < 100000000) {
					return 8;
				} else {
					if (number < 1000000000) {
						return 9;
					} else {
						return 10;
					}
				}
			}
		}
	}
}
