package com.ilsid.poc.uidiscovery.common;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Provides JSON processing routines.
 * 
 * @author illia.sydorovych
 *
 */
public class JsonUtil {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();



	/**
	 * Converts an object to JSON string.
	 * 
	 * @param obj
	 *            an object to convert
	 * @return JSON string
	 * @throws IOException
	 *             in case an object can't be converted
	 */
	public static String toJsonString(Object obj) throws IOException {
		return OBJECT_MAPPER.writeValueAsString(obj);
	}
	
	
	public static <T> T toObject(String json, Class<T> type) throws IOException {
		return OBJECT_MAPPER.readValue(json, type);
    }
	
	/**
	 * Checks whether a passed content is a valid JSON string.
	 * 
	 * @param content
	 *            string to examine
	 * @return <code>true</code> if content is valid and <code>false</code> otherwise
	 */
	public static boolean isValidJsonString(String content) {
		try {
			OBJECT_MAPPER.readTree(content);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

}
