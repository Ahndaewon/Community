package com.ktds.community.dao;

import java.util.List;

import com.ktds.community.vo.CommunityVO;

public interface CommunityDao {

	public List<CommunityVO> sellectAll();

	public CommunityVO selectOne(int id);
	
	public int selectMyCommunitiesCount(int userId);
	
	public List<CommunityVO> selectMyCommunities(int userId);

	public int increamentViewCount(int id);
	
	public int insertCommunity(CommunityVO communityVO);
	
	public int increamentRecommendCount(int id);
	
	public int deletePage(int id);
	
	public int deleteMyCommunities(int userId);
	
	public int deleteCommunities(List<Integer> ids, int userId);
	
	public int updateCommunity(CommunityVO communityVO);

}
