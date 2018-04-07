package com.chainself.main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PriceCache {

	private static DateFormat datesf = new SimpleDateFormat("yyyy-MM-dd");
	public static ConcurrentHashMap<String, JSONObject> priceMap = new ConcurrentHashMap<String, JSONObject>();
	public static ConcurrentHashMap<String, Double> priceMapUnitRmb = new ConcurrentHashMap<String, Double>();
	public static ConcurrentHashMap<String, String> priceMapOpen = new ConcurrentHashMap<String, String>();

	public static java.util.Date parseDateTime(String paramString) {
		if ((paramString == null) || (paramString.trim().equals(""))) {
			return null;
		}
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		localSimpleDateFormat.setLenient(false);
		try {
			return localSimpleDateFormat.parse(paramString);
		} catch (ParseException localParseException) {
			System.out.println("parseDateTime error " + paramString);
		}
		return null;
	}

	private static final String[] ALL_UNIT = new String[] { ".*usdt", ".*btc", ".*eth", ".*bnb", ".*ht", ".*okb" };

	public static void main2(String[] args) {
		String key = "huobi_btcusdt";
		for (int i = 0; i < ALL_UNIT.length; i++) {
			String unitLike = ALL_UNIT[i];
			if (key.matches(unitLike)) {
				String[] keys = key.split("_");
				String unitRmbPriceKey = keys[0] + unitLike.replace(".*", "_");
				System.out.println(unitRmbPriceKey);
			}
		}

		System.out.println(String.format("%.2f", 0.329121211d));
	}

	public static JSONObject getPrice(String market, String chain, String unit) {
		String key = (market + "_" + chain + unit).toLowerCase();
		return priceMap.get(key);
	}

	public static void initMap(String mapStr) {
		long time0 = System.currentTimeMillis();
		if (mapStr == null || mapStr.isEmpty()) {
			return;
		}
		boolean isDayOpen = getIsDayOpen();

		JSONObject json = JSON.parseObject(mapStr);
		for (Entry<String, Object> jsonItem : json.entrySet()) {
			String key = jsonItem.getKey();
			JSONObject jsonValue = (JSONObject) jsonItem.getValue();
			priceMap.put(key, jsonValue);
		}

		long time1 = System.currentTimeMillis();
		System.out.println("initMap success,size=" + priceMap.size() + " time=" + (time1 - time0));
		ChainServer.chainService.saveUnitPrice();

		priceMap.entrySet().forEach(pme -> {
			String key = pme.getKey();
			JSONObject value = pme.getValue();
			for (int i = 0; i < ALL_UNIT.length; i++) {
				String unitLike = ALL_UNIT[i];
				if (key.matches(unitLike)) {
					String[] keys = key.split("_");
					String unitRmbPriceKey = keys[0] + unitLike.replace(".*", "_");
					if (priceMapUnitRmb.containsKey(unitRmbPriceKey)) {
						Double unitPriceRmb = priceMapUnitRmb.get(unitRmbPriceKey);
						Double closePrice = value.getDouble("close");
						value.put("closeRmb", unitPriceRmb * closePrice);
					}
				}
			}

		});

		if (isDayOpen) {
			ChainServer.chainService.saveDayOpenPrice();
		}
	}

	private static boolean getIsDayOpen() {
		boolean isDayOpen = false;
		String todayStartStr = datesf.format(new Date()) + " 00:00:00";
		Long todayStart = parseDateTime(todayStartStr).getTime();
		Long now = System.currentTimeMillis();
		if ((now - todayStart < 5100) && (now - todayStart > 0)) {
			isDayOpen = true;
		}
		return isDayOpen;
	}
}
