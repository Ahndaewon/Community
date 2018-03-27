package com.ktds.community.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.actionhistory.service.ActionHistoryService;
import com.ktds.actionhistory.vo.ActionHistory;
import com.ktds.actionhistory.vo.ActionHistoryVO;
import com.ktds.community.constants.Member;
import com.ktds.member.vo.MemberVO;

public class PassInterceptor extends HandlerInterceptorAdapter{
	
	private ActionHistoryService actionHistoryService; 
	private ActionHistoryVO history2;
	
	
	public void setActionHistoryService(ActionHistoryService actionHistoryService) {
		this.actionHistoryService = actionHistoryService;
	}



	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		
		
		if ( member == null ) {
			member = new MemberVO();
		} 
		ActionHistoryVO history = new ActionHistoryVO();
		history.setReqType(ActionHistory.ReqType.VIEW);
		history.setIp(request.getRemoteAddr());
		history.setUserId(member.getId());
		history.setEmail(member.getEmail());
		
		// VIEW -- "view : %s, Method : %s ";
		String log = String.format(ActionHistory.Log.VIEW, 
					request.getRequestURI(), request.getMethod());
		
		history.setLog(log);
		request.setAttribute("actionHistory", history);
		
		actionHistoryService.createHistory(history);
		
		// Controller 에게 IP를 포함한 ActionHistoryVO 전달
		
		
		history2 = new ActionHistoryVO();
		history2.setIp(request.getRemoteAddr());
		history2.setUserId(member.getId());
		history2.setEmail(member.getEmail());
		request.setAttribute("actionHistoryVO", history2);
		
		if ( history2.getReqType() != null ) {
			
			actionHistoryService.createHistory(history2);
		}
		
		
		//성공을 하던말던 ture
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		
		if ( modelAndView == null ) {
			return;
		}
		
		/*
		Map<String, Object> model = modelAndView.getModel();
		ActionHistoryVO history = (ActionHistoryVO)model.get("actionHistory");*/
		if ( history2 != null && history2.getReqType() != null ) {
			actionHistoryService.createHistory(history2);
		}
	}
}
