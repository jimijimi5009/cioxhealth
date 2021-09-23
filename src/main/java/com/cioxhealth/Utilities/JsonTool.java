/**
 * 
 */
package com.cioxhealth.Utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonTool {

	public static JSONObject readJson(String filePath) throws IOException, ParseException {

		JSONParser jsonParser = new JSONParser();
		FileReader fileReader = new FileReader(filePath);
		Object object = jsonParser.parse(fileReader);
		JSONArray jsonArray = (JSONArray) object;

		String jsonText = jsonArray.toString().replaceAll("^[\\[]","").replaceAll("]$", "");

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(jsonText);

		return jsonObject;
	}
}
