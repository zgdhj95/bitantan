package com.chainself.main;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PriceCache {

	private static ConcurrentHashMap<String, JSONObject> priceMap = new ConcurrentHashMap<String, JSONObject>();

	public static JSONObject getPrice(String market, String chain, String unit) {
		String key = (market + "_" + chain + unit).toLowerCase();
		return priceMap.get(key);
	}

	public static void initMap(String mapStr) {
		long time0 = System.currentTimeMillis();
		if (mapStr == null || mapStr.isEmpty()) {
			return;
		}
		JSONObject json = JSON.parseObject(mapStr);
		for (Entry<String, Object> jsonItem : json.entrySet()) {
			String key = jsonItem.getKey();
			JSONObject jsonValue = (JSONObject) jsonItem.getValue();
			priceMap.put(key, jsonValue);
		}
		long time1 = System.currentTimeMillis();
		System.out.println("initMap success,size=" + priceMap.size() + " time=" + (time1 - time0));
	}
}
