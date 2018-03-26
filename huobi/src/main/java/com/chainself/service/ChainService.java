package com.chainself.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chainself.dao.ChainDao;
import com.chainself.dao.UserChainDao;
import com.chainself.entity.UserChain;

@Component
@Transactional(readOnly = true)
public class ChainService {

	@Autowired
	private ChainDao chainDao;

	@Autowired
	private UserChainDao userChainDao;

	public JSONArray findChainAll() {
		return (JSONArray) JSON.toJSON(chainDao.findAll());
	}

	public List<UserChain> findUserChainAll() {
		return (List<UserChain>) userChainDao.findAll();
	}
}
