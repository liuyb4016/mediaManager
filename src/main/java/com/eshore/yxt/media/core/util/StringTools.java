package com.eshore.yxt.media.core.util;

import java.text.DecimalFormat;

public final class StringTools {

	private static DecimalFormat format = new DecimalFormat("#0.00");
	
	public static String formatAmount(double d) {
		return format.format(d);
	}
}
