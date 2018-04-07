package com.chainself.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chainself.entity.User;

public interface UserDao extends CrudRepository<User, Long> {
	public List<User> findByOpenid(String openid);
}
