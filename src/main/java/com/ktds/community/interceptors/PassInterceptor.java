package com.ktds.community.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.actionhistory.service.ActionHistoryService;
import com.ktds.actionhistory.vo.ActionHistory;
import com.ktds.actionhistory.vo.ActionHistoryVO;
import com.ktds.community.constants.Member;
import com.ktds.member.vo.MemberVO;

public class PassInterceptor extends HandlerInterceptorAdapter{
	
	private ActionHistoryService actionHistoryService; 
	
	
	
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
		
		actionHistoryService.createHistory(history);
		
		//성공을 하던말던 ture
		return true;
	}

}
