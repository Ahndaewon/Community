package com.ktds.community.interceptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.community.constants.IdMap;
import com.ktds.community.constants.Member;
import com.ktds.member.vo.MemberVO;

public class IdBlockInterceptor extends HandlerInterceptorAdapter {
	
	private static Map<String, Integer> idBlock = new HashMap<String, Integer>();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(request.getMethod() == "POST"){
			
			
			String id = request.getParameter("id");
			int i = 0;
			
	
			if (request.getSession().getAttribute(Member.USER) == null && (id != null)) {
				
	
				if( idBlock.get(id) == null ||  idBlock.get(id)  < 3 ) {
					
					System.out.println("vmfl");
					idBlock = IdMap.idExist(idBlock, id);
				}
				
				else if( idBlock.get(id) == 3 ) {
					request.setAttribute("id", id);
					request.setAttribute("Map", idBlock.get(id));
					RequestDispatcher rd = 
							request.getRequestDispatcher("/WEB-INF/view/member/login.jsp");
					rd.forward(request, response);
					return false;
				}
			}
		}
	
		
		return true;
	
	}

	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		String id = request.getParameter("id");
		
		if( request.getSession().getAttribute(Member.USER) != null && idBlock.get(id) != null) {
			
			idBlock.remove(id);
			
			
			System.out.println("초기화~~~~~~~~~~~~~~~~~~~~");
		}
		
		
		super.postHandle(request, response, handler, modelAndView);
	}

	

}



