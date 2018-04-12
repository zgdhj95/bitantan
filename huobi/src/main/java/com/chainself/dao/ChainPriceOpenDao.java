package com.chainself.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chainself.entity.ChainPriceOpen;

public interface ChainPriceOpenDao extends CrudRepository<ChainPriceOpen, Long> {

	@Query(value = " SELECT * FROM chain_price_open WHERE chain = ?1 ORDER BY market,chain,unit LIMIT 0,30", nativeQuery = true)
	List<ChainPriceOpen> findByChain(String code);
}
