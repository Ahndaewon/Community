/*package com.ktds.community.dao;

import java.util.ArrayList;
import java.util.List;

import com.ktds.community.vo.CommunityVO;

public class CommunityDaoImpl implements CommunityDao {

	private List<CommunityVO> communityList;
	//private final Logger logger = 
	public CommunityDaoImpl() {
		communityList = new ArrayList<CommunityVO>();
	}

	@Override
	public List<CommunityVO> sellectAll() {
		return communityList;
	}

	@Override
	public int insertCommunity(CommunityVO communityVO) {
		communityVO.setId(communityList.size() + 1);
		communityList.add(communityVO);
		return 1;
	}

	@Override
	public CommunityVO selectOne(int id) {

		for (CommunityVO community : communityList) {
			if (community.getId() == id) {
				return community;
			}

		}
		return null;

	}

	@Override
	public int increamentViewCount(int id) {

		for (CommunityVO community : communityList) {
			if (community.getId() == id) {
				community.setViewCount(community.getViewCount() + 1);
				return 1;
			}

		}

		return 0;
	}

	@Override
	public int increamentRecommendCount(int id) {
		
		
		for (CommunityVO community : communityList) {
			if (community.getId() == id) {
				community.setRecommendCount(community.getRecommendCount() + 1);
				return 1;
			}

		}
		
		return 0;
	}

	@Override
	public int deletePage(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteMyCommunities(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateCommunity(CommunityVO communityVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectMyCommunitiesCount(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CommunityVO> selectMyCommunities(int userId) {
		// TODO Auto-generated method stub
		return null;
	}




}
*/