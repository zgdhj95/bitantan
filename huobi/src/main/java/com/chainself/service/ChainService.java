package com.chainself.service;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.chainself.dao.ChainUnitPriceDao;
import com.chainself.dao.UsdtPriceDao;
import com.chainself.dao.UserAssetDao;
import com.chainself.dao.UserChainDao;
import com.chainself.dao.UserDao;
import com.chainself.entity.Chain;
import com.chainself.entity.ChainPriceOpen;
import com.chainself.entity.ChainUnitPrice;
import com.chainself.entity.UsdtPrice;
import com.chainself.entity.User;
import com.chainself.entity.UserAsset;
import com.chainself.entity.UserChain;
import com.chainself.main.ChainServer;
import com.chainself.main.PriceCache;
import com.chainself.util.ConstantVar;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

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
	private UsdtPriceDao usdtPriceDao;

	@Autowired
	private ChainUnitPriceDao chainUnitPriceDao;

	@Autowired
	private ChainPriceOpenDao chainPriceOpenDao;

	@Autowired
	private UserDao userDao;

	public boolean isUserExists(String openid) {
		return !userDao.findByOpenid(openid).isEmpty();
	}

	public JSONArray findChainAll() {
		return (JSONArray) JSON.toJSON(chainDao.findAll());
	}

	public List<UserChain> findUserChainAll(String openid) {
		return (List<UserChain>) userChainDao.findByOpenid(openid);
	}

	public void initPriceMapOpen() {
		List<ChainPriceOpen> cpoList = ((List<ChainPriceOpen>) chainPriceOpenDao.findAll());
		cpoList.stream().forEach(cpo -> {
			PriceCache.priceMapOpen.put(cpo.getChainkey(), cpo.getPrice());
		});
	}

	public Map<String, Chain> getChainMap() {
		List<Chain> chainList = (List<Chain>) chainDao.findAll();
		Map<String, Chain> result = new HashMap<String, Chain>();
		chainList.forEach(chain -> {
			result.put(chain.getChain(), chain);
		});
		return result;
	}

	@Transactional(readOnly = false)
	public void saveUnitPrice() {

		List<UsdtPrice> upList = (List<UsdtPrice>) usdtPriceDao.findAll();
		Map<String, Double> baseRmpPriceMap = new HashMap<String, Double>();
		upList.forEach(up -> {
			baseRmpPriceMap.put(up.getMarket().toLowerCase() + "_usdt", up.getPriceRmb());
			PriceCache.priceMapUnitRmb.put(up.getMarket().toLowerCase() + "_usdt", up.getPriceRmb());
		});

		List<ChainUnitPrice> cupList = (List<ChainUnitPrice>) chainUnitPriceDao.findAll();
		cupList.forEach(up -> {
			String key = (up.getMarket() + "_" + up.getUnit() + "usdt").toLowerCase();
			JSONObject priceJson = PriceCache.priceMap.get(key);
			if (priceJson != null) {
				Double priceUsdtRmb = baseRmpPriceMap.get(up.getMarket().toLowerCase() + "_usdt");
				Double priceRmb = priceJson.getDouble("close") * priceUsdtRmb;
				up.setPriceRmb(priceRmb);
				PriceCache.priceMapUnitRmb.put(up.getMarket().toLowerCase() + "_" + up.getUnit(), priceRmb);
			}
		});
		chainUnitPriceDao.save(cupList);
	}

	public static void main(String[] args) {
		String chainUnit = "trxusdt";
		System.out.println(chainUnit.substring(chainUnit.length() - 3, chainUnit.length()));
		System.out.println(chainUnit.substring(0, chainUnit.length() - 4));
	}

	private boolean isUsdt(String chainUnit) {
		return "usdt".equals(chainUnit.substring(chainUnit.length() - 4, chainUnit.length()));
	}

	private boolean isEth(String chainUnit) {
		return "eth".equals(chainUnit.substring(chainUnit.length() - 3, chainUnit.length()));
	}

	private boolean isBtc(String chainUnit) {
		return "btc".equals(chainUnit.substring(chainUnit.length() - 3, chainUnit.length()));
	}

	private boolean isBnb(String chainUnit) {
		return "bnb".equals(chainUnit.substring(chainUnit.length() - 3, chainUnit.length()));
	}

	private String getChainName(String chainUnit, int inx) {
		return chainUnit.substring(0, chainUnit.length() - inx);
	}

	@Transactional(readOnly = false)
	public void selectChain(String openid, String market, String chain, String unit) {
		UserChain uc = userChainDao.findByMcu(market, chain, unit, openid);
		if (uc == null) {
			uc = new UserChain();
			uc.setChain(chain);
			uc.setMarket(market);
			uc.setPriceUnit(unit);
			uc.setOpenid(openid);
			userChainDao.save(uc);
		}
	}

	@Transactional(readOnly = false)
	public void unSelectChain(String openid, String market, String chain, String unit) {
		UserChain uc = userChainDao.findByMcu(market, chain, unit, openid);
		if (uc != null) {
			userChainDao.delete(uc);
		}
	}

	@Transactional(readOnly = false)
	public void saveDayOpenPrice() {
		Map<String, List<ChainPriceOpen>> mapCpo = ((List<ChainPriceOpen>) chainPriceOpenDao.findAll()).stream()
				.collect(Collectors.groupingBy(ChainPriceOpen::getChainkey));
		List<ChainPriceOpen> savedList = new ArrayList<ChainPriceOpen>();

		for (Entry<String, JSONObject> entry : PriceCache.priceMap.entrySet()) {
			String key = entry.getKey().toLowerCase();
			List<ChainPriceOpen> cpoList = mapCpo.get(key);
			if (cpoList == null) {
				ChainPriceOpen newCpo = new ChainPriceOpen();
				newCpo.setChainkey(key);
				newCpo.setPrice(entry.getValue().getString("close"));
				String[] marketChainstr = key.split("_");
				newCpo.setMarket(marketChainstr[0]);
				String chainUnit = marketChainstr[1];
				if (isUsdt(chainUnit)) {
					newCpo.setChain(getChainName(chainUnit, 4));
					newCpo.setUnit("usdt");
				} else {
					newCpo.setChain(getChainName(chainUnit, 3));
					if (isBtc(chainUnit)) {
						newCpo.setUnit("btc");
					} else if (isBnb(chainUnit)) {
						newCpo.setUnit("bnb");
					} else if (isEth(chainUnit)) {
						newCpo.setUnit("eth");
					}
				}
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
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("query asset error userid=" + userid + " error=" + e.getMessage());
			return 0d;
		}
	}

	public List<JSONObject> searchChain(String code, String openid) {
		Map<String, List<UserChain>> ucMap = userChainDao.findByOpenid(openid).stream()
				.collect(Collectors.groupingBy(uc -> {
					return (uc.getMarket() + "_" + uc.getChain() + uc.getPriceUnit()).toLowerCase();
				}));
		List<ChainPriceOpen> copList = chainPriceOpenDao.findByChain(code);
		return copList.stream().map(cpo -> {
			JSONObject json = new JSONObject();
			json.put("coinName", cpo.getChain());
			json.put("coinUnit", cpo.getUnit());
			json.put("marketTitle", ChainServer.marketNameMap.get(cpo.getMarket()));
			Chain chain = ChainServer.chainMap.get(cpo.getChain());
			if (chain != null) {
				json.put("icon", chain.getIcon());
			}
			String key = cpo.getMarket() + "_" + cpo.getChain() + cpo.getUnit();
			json.put("selected", ucMap.containsKey(key));
			return json;
		}).collect(Collectors.toList());
	}

	@Transactional(readOnly = false)
	public String getWechatUser(String code) {

		try {
			String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + ConstantVar.APP_ID + "&secret="
					+ ConstantVar.SECRET_KEY + "&js_code=" + code + "&grant_type=authorization_code";
			System.out.println("url is " + url);
			Response response = FastHttpClient.get().url(url).build().execute();

			String responseStr = response.body().string();
			System.out.print("responseStr=" + responseStr);
			JSONObject openidJson = JSON.parseObject(responseStr);
			String openid = openidJson.getString("openid");
			if (userDao.findByOpenid(openid).isEmpty()) {
				User user = new User();
				user.setOpenid(openid);
				userDao.save(user);
			}
			return openid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
