package com.ktds.actionhistory.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.actionhistory.service.ActionHistoryService;
import com.ktds.actionhistory.vo.ActionHistorySearchVO;
import com.ktds.util.DateUtil;

import io.github.seccoding.web.pager.explorer.PageExplorer;

@Controller
public class ActionHistoryController {
	
	ActionHistoryService actionHistoryService;

	public void setActionHistoryService(ActionHistoryService actionHistoryService) {
		this.actionHistoryService = actionHistoryService;
	}
	
	
	
	@RequestMapping("/admin/actionhistory")
	public ModelAndView viewActionHistoryPage(ActionHistorySearchVO actionHistorySearchVO, HttpSession session) {
		
		ModelAndView view = new ModelAndView();
		
		view.setViewName("actionhistory/actionhistory");
		
		if( actionHistorySearchVO.getPageNo() == -1 ) {
			
			//검색날짜 초기화
			actionHistorySearchVO = 
					(ActionHistorySearchVO) session.getAttribute("__AH_SEARCH__");
			
			if( actionHistorySearchVO == null ) {
				actionHistorySearchVO = new ActionHistorySearchVO();
				actionHistorySearchVO.setPageNo(0);
				
				setInitiateSearchData(view, actionHistorySearchVO);
			}
		}
		
		actionHistorySearchVO.setStartDate(DateUtil.makeDate(
				actionHistorySearchVO.getStartDateYear(),
				actionHistorySearchVO.getStartDateMonth(),
				actionHistorySearchVO.getStartDateDate())
				+"00:00:00");
		
		actionHistorySearchVO.setEndDate(DateUtil.makeDate(
				actionHistorySearchVO.getEndDateYear(),
				actionHistorySearchVO.getEndDateMonth(), 
				actionHistorySearchVO.getEndDateDate())
				+ "23:59:59");
		
		
		view.addObject("search", actionHistorySearchVO);
		
		int startDateMaximumDate = DateUtil.getActualMaximunDate(
				Integer.parseInt(actionHistorySearchVO.getStartDateYear()), 
				Integer.parseInt(actionHistorySearchVO.getStartDateMonth()) 
				);
		System.out.println("~~!!!!");
		int endDateMaximumDate = DateUtil.getActualMaximunDate(
				Integer.parseInt(actionHistorySearchVO.getStartDateYear()),
				Integer.parseInt(actionHistorySearchVO.getEndDateMonth()) 
				);
		
		view.addObject("startDateMaximumDate", startDateMaximumDate);
		view.addObject("endDateMaximumDate", endDateMaximumDate);
		
		session.setAttribute("__AH_SEARCH__", actionHistorySearchVO);
		
		PageExplorer explorer = 
					actionHistoryService.readAllActionHistory(actionHistorySearchVO);
		
		view.addObject("explorer", explorer);
		return view;
	}
	
	
	@RequestMapping("/api/date/max/{year}/{month}")
	public void getMaxDate(@PathVariable int year,
								@PathVariable int month, HttpServletResponse response) {
		
		int maxDate = DateUtil.getActualMaximunDate(year, month);
		
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
			out.write(maxDate + "");
			out.flush();
		} catch (IOException e) {
			//일반 Exception 타입은? Runtime차이 현상 복구 가능
			//현상을 복구 할수 있다 없다
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if( out != null )
				out.close();
		}
		
	}
	
	private void setInitiateSearchData(ModelAndView view
	         , ActionHistorySearchVO actionHistorySearchVO) {
	      
	      //검색 날자 초기화.
	      String endDate = DateUtil.getNowDate();
	      String[] splitedEndDate= endDate.split("-");
	      actionHistorySearchVO.setEndDateYear(splitedEndDate[0]);
	      actionHistorySearchVO.setEndDateMonth(splitedEndDate[1]);
	      actionHistorySearchVO.setEndDateDate(splitedEndDate[2]);
	      actionHistorySearchVO.setEndDate(endDate);
	      
	      String startDate= DateUtil.getComputedDate(Integer.parseInt(splitedEndDate[0])
	            , Integer.parseInt(splitedEndDate[1])
	            , Integer.parseInt(splitedEndDate[2])
	            , DateUtil.DATE, -7);
	      String[] splitedStartDate = startDate.split("-");
	      actionHistorySearchVO.setStartDateYear(splitedStartDate[0]);
	      actionHistorySearchVO.setStartDateMonth(splitedStartDate[1]);
	      actionHistorySearchVO.setStartDateDate(splitedStartDate[2]);
	      actionHistorySearchVO.setStartDate(startDate);
	      
	      view.addObject("search", actionHistorySearchVO);
	      
	      int startDateMaximumDate = DateUtil.getActualMaximunDate(Integer.parseInt(splitedStartDate[0])
	            , Integer.parseInt(splitedStartDate[1]));
	      
	      int endDateMaximumDate = DateUtil.getActualMaximunDate(Integer.parseInt(splitedEndDate[0]
	            ), Integer.parseInt(splitedEndDate[1]));
	      
	      
	      view.addObject("startDateMaximumDate", startDateMaximumDate);
	      view.addObject("endDateMaximumDate", endDateMaximumDate);
	      
	   }
	



}