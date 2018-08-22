package com.instrument.process.business;

import org.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Object value = "{"Type":"Spread","Id":1,"Name":"P","Offset":7}";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "Spread");
		jsonObject.put("id", "6");
		System.out.println(jsonObject);
		Object temp = jsonObject;
		  /* JSONObject json = (JSONObject) new JSONParser().parse(temp);
           System.out.println(json);*/
		
		
		String str = (String)((JSONObject)temp).get("type");
		System.out.println(str);
	}

}
