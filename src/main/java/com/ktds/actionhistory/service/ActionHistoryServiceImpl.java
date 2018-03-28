package com.ktds.actionhistory.service;

import java.util.List;

import com.ktds.actionhistory.dao.ActionHistoryDao;
import com.ktds.actionhistory.vo.ActionHistorySearchVO;
import com.ktds.actionhistory.vo.ActionHistoryVO;

import io.github.seccoding.web.pager.Pager;
import io.github.seccoding.web.pager.PagerFactory;
import io.github.seccoding.web.pager.explorer.ListPageExplorer;
import io.github.seccoding.web.pager.explorer.PageExplorer;

public class ActionHistoryServiceImpl implements ActionHistoryService {
	
	ActionHistoryDao actionHistoryDao;

	public void setActionHistoryDao(ActionHistoryDao actionHistoryDao) {
		this.actionHistoryDao = actionHistoryDao;
	}

	@Override
	public boolean createHistory(ActionHistoryVO actionHistoryVO) {
		return actionHistoryDao.insertActionHistory(actionHistoryVO) > 0 ;
	}

	@Override
	public PageExplorer readAllActionHistory(ActionHistorySearchVO actionHistorySearchVO) {
		
		Pager pager = PagerFactory.getPager(Pager.OTHER, 
								actionHistorySearchVO.getPageNo() + "", 
								actionHistoryDao.selectAllActionHistoryCount(actionHistorySearchVO));
		PageExplorer pageExplorer = pager.makePageExplorer(
					ListPageExplorer.class, actionHistorySearchVO);
		
		List<ActionHistoryVO> list = actionHistoryDao.selectActionHistory(actionHistorySearchVO);
		pageExplorer.setList(list);
		
		System.out.println(pageExplorer.list);
		System.out.println(list.size());
		return pageExplorer;
		
		
	}
	
	

}
