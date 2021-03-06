package com.chainself.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 开盘价格
 * 
 * @author yejianfei
 *
 */
@Entity
@Table(name = "chain_price_open")
public class ChainPriceOpen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String chainkey;// 参数代码
	private String price;
	private String priceRmb;
	private String chain;
	private String unit;
	private String market;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChainkey() {
		return chainkey;
	}

	public void setChainkey(String chainkey) {
		this.chainkey = chainkey;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceRmb() {
		return priceRmb;
	}

	public void setPriceRmb(String priceRmb) {
		this.priceRmb = priceRmb;
	}

	public String getChain() {
		return chain;
	}

	public void setChain(String chain) {
		this.chain = chain;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

}
