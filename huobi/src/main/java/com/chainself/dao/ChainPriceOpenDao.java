package com.chainself.dao;

import org.springframework.data.repository.CrudRepository;

import com.chainself.entity.ChainPriceOpen;

public interface ChainPriceOpenDao extends CrudRepository<ChainPriceOpen, Long> {
}
