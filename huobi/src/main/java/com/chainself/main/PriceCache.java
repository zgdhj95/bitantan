package com.chainself.main;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PriceCache {

	private static ConcurrentHashMap<String, JSONObject> priceMap = new ConcurrentHashMap<String, JSONObject>();

	public static JSONObject getPrice(String market, String chain, String unit) {
		String key = (market + "_" + chain + "_" + unit).toUpperCase();
		return priceMap.get(key);
	}

	public static void initMap(String mapStr) {
		if (mapStr == null || mapStr.isEmpty()) {
			return;
		}
		JSONObject json = JSON.parseObject(mapStr);
		for (Entry<String, Object> jsonItem : json.entrySet()) {
			String key = jsonItem.getKey();
			JSONObject jsonValue = (JSONObject) jsonItem.getValue();
			priceMap.put(key, jsonValue);
		}
	}
}
