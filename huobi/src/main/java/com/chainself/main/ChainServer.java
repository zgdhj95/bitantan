package com.chainself.main;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, eiather express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.threadPool;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chainself.service.ChainService;
import com.chainself.timer.QueryTimer;
import com.chainself.util.CorsFilter;

import io.itit.itf.okhttp.FastHttpClient;

/**
 * An HTTP server that sends back the content of the received HTTP request in a
 * pretty plaintext form.
 */
public class ChainServer {

	public static DecimalFormat df = new DecimalFormat("#.00");
	private static ApplicationContext context;

	public static Object getService(String serviceName) {
		return context.getBean(serviceName);
	}

	public static ChainService chainService = null;

	public static void main(String[] args) throws Exception {

		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "production");
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		chainService = (ChainService) ChainServer.getService("chainService");
		chainService.initPriceMapOpen();
		FastHttpClient.okHttpClient.dispatcher().setMaxRequestsPerHost(10);
		startTimer();
		startSparkHttpServer();

	}

	/**
	 * 启动定时器，定时打印心跳信息，获取微信的accessToken。
	 */
	private static void startTimer() {
		Timer queryTimer = new Timer();
		queryTimer.schedule(new QueryTimer(), 1000, 5000);
	}

	public static String clearZero(String price) {
		int j = 0;
		for (int i = price.length(); i > 0; i--) {
			if (price.substring(i - 1, i).equals("0")) {
				continue;
			} else {
				j = i;
				break;
			}
		}
		return price.substring(0, j);
	}

	/**
	 * @param args
	 */
	public static double getDoubleOfObj(Object obj) {
		try {
			if (obj == null) {
				return 0d;
			}
			return new Double(obj.toString()).doubleValue();
		} catch (Exception e) {
			return 0;
		}
	}

	private static String getUnitStr(String unit) {
		if ("btc".equals(unit)) {
			return "฿";
		} else if ("eth".equals(unit)) {
			return "E";
		} else if ("usdt".equals(unit)) {
			return "$";
		} else if ("bnb".equals(unit)) {
			return "B";
		} else {
			return "";
		}
	}

	public static void startSparkHttpServer() throws Exception {

		int maxThreads = 10;
		threadPool(maxThreads);
		port(8080);
		new CorsFilter().apply();
		get("/query", (req, res) -> {
			String market = req.queryParams("market");
			String chain = req.queryParams("chain");
			String unit = req.queryParams("unit");
			if (market == null || "".equals(market) || chain == null || "".equals(chain) || unit == null
					|| "".equals(unit)) {
				return "";
			}
			String key = market + "_" + chain + unit;
			System.out.println("query:" + key);
			try {
				JSONObject json = PriceCache.getPrice(market, chain, unit);
				if (json != null) {
					System.out.println("chain is:" + json.toJSONString());
					return json.toJSONString();
				} else {
					return "chain not exists:" + key;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		});

		get("/queryuser", (req, res) -> {
			JSONObject result = new JSONObject();
			String code = req.queryParams("code");
			String openid = ChainServer.chainService.getWechatUser(code);
			if (openid == null) {
				result.put("state", "error");
			} else {

				result.put("state", "success");
				result.put("openid", openid);
			}
			return result;
		});

		get("/asset", (req, res) -> {
			String userid = req.queryParams("userid");
			return ChainServer.chainService.calcUserAsset(userid);
		});

		get("/querychainlist", (req, res) -> {

			List<JSONObject> coinList = new ArrayList<JSONObject>();
			System.out.println("openid=" + req.queryParams("openid"));

			ChainServer.chainService.findUserChainAll().stream().forEach(uc -> {
				JSONObject price = PriceCache.getPrice(uc.getMarket(), uc.getChain(), uc.getPriceUnit());
				if (price != null) {
					String priceJson = price.toJSONString();
					JSONObject result = JSON.parseObject(priceJson);

					// Double open = price.getDouble("open");
					// if (open == null || open == 0d) {
					String key = (uc.getMarket() + "_" + uc.getChain() + uc.getPriceUnit()).toLowerCase();
					String priceOpen = PriceCache.priceMapOpen.get(key);
					Double open = getDoubleOfObj(priceOpen);
					// }

					if (priceOpen == null) {
						open = price.getDouble("open");
					}
					Double close = price.getDouble("close");
					Double rate = 100d;
					if (open > 0d) {
						rate = (close - open) * 100 / open;
					}
					String rateStr = df.format(rate) + "%";
					result.put("priceRate", rateStr);
					result.put("icon", uc.getIcon());
					result.put("coinUnit", uc.getPriceUnit().toUpperCase());
					result.put("coinUnitStr", uc.getPriceUnit().toUpperCase());
					result.put("coinName", uc.getChain());
					result.put("marketName", uc.getMarket());
					result.put("price", clearZero(price.getString("close")));
					result.put("unitStr", getUnitStr(uc.getPriceUnit().toLowerCase()));
					Double closeRmb = price.getDouble("closeRmb");
					result.put("priceRmb", String.format("%.2f", closeRmb));
					result.put("marketTitle", uc.getMarket());
					if (close < open) {
						result.put("result", "price-down");
					} else {
						result.put("result", "price-up");
					}
					coinList.add(result);
				}
			});
			return coinList;
		});

	}

}