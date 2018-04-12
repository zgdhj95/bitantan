package com.chainself.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chainself.entity.UserChain;

public interface UserChainDao extends CrudRepository<UserChain, Long> {

	List<UserChain> findByOpenid(String openid);

	@Query(value = " SELECT * FROM user_chain WHERE market = ?1 AND chain = ?2 AND price_unit =?3 AND openid = ?4", nativeQuery = true)
	UserChain findByMcu(String market, String chain, String unit, String openid);
}
