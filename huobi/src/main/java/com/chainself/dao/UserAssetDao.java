package com.chainself.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chainself.entity.UserAsset;

public interface UserAssetDao extends CrudRepository<UserAsset, Long> {
	public List<UserAsset> findByUserid(String userid);
}
