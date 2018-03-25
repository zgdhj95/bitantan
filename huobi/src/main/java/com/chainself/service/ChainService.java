package com.chainself.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chainself.dao.ChainDao;

@Component
@Transactional(readOnly = true)
public class ChainService {

	@Autowired
	private ChainDao chainDao;

	public ChainDao getChainDao() {
		return chainDao;
	}

	public void setChainDao(ChainDao chainDao) {
		this.chainDao = chainDao;
	}

	public JSONArray findAll() {
		return (JSONArray) JSON.toJSON(chainDao.findAll());
	}

}
