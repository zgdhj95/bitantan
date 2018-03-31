package com.chainself.entity;

import javax.persistence.Entity;
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
	private Long id;
	private String chainkey;// 参数代码
	private String price;

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

}