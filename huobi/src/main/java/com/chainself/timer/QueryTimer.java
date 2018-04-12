package com.chainself.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chainself.main.ChainServer;
import com.chainself.main.PriceCache;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.itit.itf.okhttp.callback.Callback;
import okhttp3.Call;

public class QueryTimer extends java.util.TimerTask {

	private static Logger logger = LoggerFactory.getLogger(QueryTimer.class);

	@Override
	public void run() {
		FastHttpClient.get().url("http://119.28.55.163/queryall").build().executeAsync(new Callback() {
			@Override
			public void onFailure(Call call, Exception e, long id) {
				logger.error("get huobi error!", e);
			}

			@Override
			public void onResponse(Call call, Response response, long id) {
				try {
					String responseStr = response.body().string();
					PriceCache.initMap(responseStr);

					int i = 0;
					if (i % 600 == 0) {
						ChainServer.initChainMap();
						i = 0;
					}
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
