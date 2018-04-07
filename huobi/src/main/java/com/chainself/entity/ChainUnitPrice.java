package com.chainself.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chain_unit_price")
public class ChainUnitPrice {
	@Id
	private Long id;
	private String market;// 市场名称
	private String unit;// 市场名称
	private Double priceRmb;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public Double getPriceRmb() {
		return priceRmb;
	}

	public void setPriceRmb(Double priceRmb) {
		this.priceRmb = priceRmb;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
