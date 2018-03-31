package com.chainself.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chainself.dao.ChainDao;
import com.chainself.dao.ChainPriceOpenDao;
import com.chainself.dao.UserAssetDao;
import com.chainself.dao.UserChainDao;
import com.chainself.entity.ChainPriceOpen;
import com.chainself.entity.UserAsset;
import com.chainself.entity.UserChain;
import com.chainself.main.PriceCache;

@Component
@Transactional(readOnly = true)
public class ChainService {

	@Autowired
	private ChainDao chainDao;

	@Autowired
	private UserChainDao userChainDao;

	@Autowired
	private UserAssetDao userAssetDao;

	@Autowired
	private ChainPriceOpenDao chainPriceOpenDao;

	public JSONArray findChainAll() {
		return (JSONArray) JSON.toJSON(chainDao.findAll());
	}

	public List<UserChain> findUserChainAll() {
		return (List<UserChain>) userChainDao.findAll();
	}

	public void initPriceMapOpen() {
		List<ChainPriceOpen> cpoList = ((List<ChainPriceOpen>) chainPriceOpenDao.findAll());
		cpoList.stream().forEach(cpo -> {
			PriceCache.priceMapOpen.put(cpo.getChainkey(), cpo.getPrice());
		});
	}

	@Transactional(readOnly = false)
	public void saveDayOpenPrice() {
		Map<String, List<ChainPriceOpen>> mapCpo = ((List<ChainPriceOpen>) chainPriceOpenDao.findAll()).stream()
				.collect(Collectors.groupingBy(ChainPriceOpen::getChainkey));
		List<ChainPriceOpen> savedList = new ArrayList<ChainPriceOpen>();

		for (Entry<String, JSONObject> entry : PriceCache.priceMap.entrySet()) {
			String key = entry.getKey();
			List<ChainPriceOpen> cpoList = mapCpo.get(key);
			if (cpoList == null) {
				ChainPriceOpen newCpo = new ChainPriceOpen();
				newCpo.setChainkey(key);
				newCpo.setPrice(entry.getValue().getString("close"));
				savedList.add(newCpo);
			} else {
				ChainPriceOpen editCpo = cpoList.get(0);
				editCpo.setPrice(entry.getValue().getString("close"));
				savedList.add(editCpo);
			}
		}
		if (!savedList.isEmpty()) {
			savedList.forEach(item -> {
				PriceCache.priceMapOpen.put(item.getChainkey(), item.getPrice());
			});
			chainPriceOpenDao.save(savedList);
		}
	}

	public Double calcUserAsset(String userid) {
		List<UserAsset> uaList = userAssetDao.findByUserid(userid);
		if (uaList.isEmpty()) {
			return 0d;
		} else {
			return uaList.stream().collect(Collectors.summarizingDouble(ua -> {
				if ("btc".equals(ua.getChain().toLowerCase())) {
					return ua.getCount();
				} else {
					JSONObject priceJson = PriceCache.getPrice(ua.getMarket(), ua.getChain(), "btc");
					Double price = priceJson.getDouble("close");
					return price * ua.getCount();
				}
			})).getSum();
		}
	}
}
