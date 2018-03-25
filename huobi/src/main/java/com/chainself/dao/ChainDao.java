package com.chainself.dao;

import org.springframework.data.repository.CrudRepository;

import com.chainself.entity.Chain;

public interface ChainDao extends CrudRepository<Chain, Long> {

}
