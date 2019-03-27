package com.ilsid.poc.uidiscovery.common;

import java.util.List;

/**
 * 
 * @author illia.sydorovych
 *
 */
public class ExceptionUtil {
	
	private static final String LF = "\n";

	private static final String CAUSEDBY_STR = "   Caused by: ";


	public static String getExceptionMessageChain(Throwable exception) {
		StringBuilder chain = new StringBuilder();
		chain.append(exception.getMessage());

		Throwable cause = exception.getCause();
		while (cause != null) {
			String causeMsg = cause.getMessage();
			if (!isBlank(causeMsg)) {
				chain.append(LF).append(CAUSEDBY_STR).append(causeMsg);
			}
			cause = cause.getCause();
		}

		return chain.toString();
	}

	public static Exception toException(List<String> messages) {
		StringBuilder sb = new StringBuilder();
		for (String msg : messages) {
			sb.append(msg).append(LF);
		}

		return new Exception(sb.toString());
	}
	
	private static boolean isBlank(String str) {
		return str != null && !str.isEmpty();
	}



}