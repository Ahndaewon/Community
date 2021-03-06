package com.ktds.community.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.ktds.community.vo.CommunitySearchVO;
import com.ktds.community.vo.CommunityVO;

public class CommunityDaoImplForOracle extends SqlSessionDaoSupport implements CommunityDao {

	@Override
	public List<CommunityVO> selectAll(CommunitySearchVO communitySerchVO) {
		return getSqlSession().selectList("CommunityDao.selectAll", communitySerchVO);
	}

	@Override
	public CommunityVO selectOne(int id) {
		return getSqlSession().selectOne("CommunityDao.selectOne", id);
	}

	@Override
	public int increamentViewCount(int id) {
		return getSqlSession().update("CommunityDao.increamentViewCount", id);
	}

	@Override
	public int insertCommunity(CommunityVO communityVO) {
		return getSqlSession().insert("CommunityDao.insertCommunity", communityVO);
	}

	@Override
	public int increamentRecommendCount(int id) {
		return getSqlSession().update("CommunityDao.increamentRecommendCount", id);
	}

	@Override
	public int deletePage(int id) {
		return getSqlSession().delete("CommunityDao.deletePage", id);
	}

	@Override
	public int deleteMyCommunities(int userId) {
		return getSqlSession().delete("CommunityDao.deleteMyCommunities", userId);
	}

	@Override
	public int updateCommunity(CommunityVO communityVO) {
		return getSqlSession().update("CommunityDao.updateCommunity", communityVO);
	}

	@Override
	public int selectMyCommunitiesCount(int userId) {
		return getSqlSession().
				selectOne("CommunityDao.selectMyCommunitiesCount", userId);
	}

	@Override
	public List<CommunityVO> selectMyCommunities(int userId) {

		return getSqlSession().selectList("CommunityDao.selectMyCommunities", userId);
	}

	@Override
	public int deleteCommunities(List<Integer> ids, int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", ids);
		params.put("userId", userId);
		
		return getSqlSession().delete("CommunityDao.deleteCommunities", params);
	}

	@Override
	public int selectCountAll(CommunitySearchVO communitySearchVO) {
		
		return getSqlSession().selectOne("CommunityDao.selectCountAll", communitySearchVO);
	}

	

	/*
	 * SqlSessionDaoSupport sqlSessionTemplate Bean 객체를 가지고 있음
	 * 
	 */

}
