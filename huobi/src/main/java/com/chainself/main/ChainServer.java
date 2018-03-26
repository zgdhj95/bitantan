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

	public static void startSparkHttpServer() throws Exception {

		int maxThreads = 10;
		threadPool(maxThreads);
		port(80);
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

		get("/querychainlist", (req, res) -> {

			List<JSONObject> coinList = new ArrayList<JSONObject>();

			ChainServer.chainService.findUserChainAll().stream().forEach(uc -> {
				JSONObject price = PriceCache.getPrice(uc.getMarket(), uc.getChain(), uc.getPriceUnit());
				if (price != null) {
					String priceJson = price.toJSONString();
					JSONObject result = JSON.parseObject(priceJson);
					Double open = price.getDouble("open");
					Double close = price.getDouble("close");
					Double rate = (close - open) * 100 / close;
					String rateStr = df.format(rate);
					result.put("priceRate", rateStr);
					result.put("icon", uc.getIcon());
					result.put("coinUnit", uc.getPriceUnit());
					result.put("coinName", uc.getChain());
					result.put("marketName", uc.getMarket());
					result.put("price", price.getString("close"));
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

		// setPort(9998);
		// get(new Route("/query") {
		// @Override
		// public Object handle(Request request, Response response) {
		// if (request.queryParams("SN") == null ||
		// "".equals(request.queryParams("SN"))) {
		// return "";
		// }
		// try {
		// ChainService service = (ChainService) ChainServer.getService("chainService");
		// String result = 123 + request.queryParams("SN") +
		// service.findAll().toJSONString();
		// return result;
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return "";
		// }
		// });
	}

}