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
		System.out.println("initMap success, json.entrySet()=" + json.entrySet().size());
		for (Entry<String, Object> jsonItem : json.entrySet()) {
			String key = jsonItem.getKey();
			System.out.print("initMapkey=" + key);
			JSONObject jsonValue = (JSONObject) jsonItem.getValue();
			System.out.println(" jsonValue=" + jsonValue);
			priceMap.put(key, jsonValue);
		}
		System.out.println("initMap success, size=" + priceMap.size());
	}
}
