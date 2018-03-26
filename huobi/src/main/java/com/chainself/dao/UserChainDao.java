package com.chainself.dao;

import org.springframework.data.repository.CrudRepository;

import com.chainself.entity.UserChain;

public interface UserChainDao extends CrudRepository<UserChain, Long> {
}
