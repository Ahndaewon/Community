package com.ktds.community.service;

import java.util.List;

import com.ktds.community.dao.CommunityDao;
import com.ktds.community.vo.CommunityVO;

public interface CommunityService {
	
	public List<CommunityVO> getAll();
	public CommunityVO getOne(int id);
	
	
	public CommunityVO recommend(int id);
	public CommunityVO refresh(int id);
	
	public int readMyCommunitiesConut(int userId);
	public List<CommunityVO> readMyCommunities(int userId);
	
	public boolean createCommunity(CommunityVO communityVO);
	public boolean deleteCommunity(int id);
	public boolean updateCommunity(CommunityVO communityVO);
	public boolean deleteCommunities(List<Integer> ids, int userId);

}
