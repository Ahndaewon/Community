package com.ktds.member.web;

import java.io.Reader;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.community.constants.Member;
import com.ktds.community.service.CommunityService;
import com.ktds.member.service.MemberService;
import com.ktds.member.vo.MemberVO;

@Controller
public class MemberController {
	
	private MemberService memberService; //의존 만들어줌
	private CommunityService communityService;
	
	
	
	
	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}

	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String viewLoginPage(HttpSession sesstion) { //세션을 바로 가지고 올 수 있음
		
		if( sesstion.getAttribute(Member.USER) != null ) {
			return "redirect:/";
		}//로그인이 필요한 페이지에 모두 넣어주면됨
		
		return "member/login";
	}
	
	
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String viewRegistPage() {
		return "member/regist";
	}
	
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public ModelAndView doRegistAction(@ModelAttribute("registForm")
									@Valid MemberVO memberVO, Errors errors) {
		
		if ( errors.hasErrors() ) {
			return new ModelAndView("member/regist");
			
		}
		
		if ( memberService.createMember(memberVO) ) {
			return new ModelAndView("redirect:/login");
		}
		
		return new ModelAndView("redirect:/login");
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String doLoginAction(@ModelAttribute("loginForm") @Valid MemberVO memberVO, 
					Errors error, HttpServletRequest request) {

		HttpSession session = request.getSession();
		
		MemberVO loginMember = memberService.readMember(memberVO);
		if ( loginMember != null ) {
			session.setAttribute(Member.USER, loginMember);
			return "redirect:/";
		}
		return "redirect:/login";
		
	/*	if( memberVO.getId() == null || memberVO.getId().length() == 0 ) {
			session.setAttribute("status", "emptyId");
			return new ModelAndView("redirect:/login");
		}
		
		if( memberVO.getPassword() == null || memberVO.getPassword().length() == 0 ) {
			session.setAttribute("status", "emptyPassword");
			return new ModelAndView("redirect:/login");
		}*/
		
		
		
		//FIXME DB에 계정이 존재하지 않을 경우로 변경
	/*		
		if( memberVO.getEmail().equals("admin") &&
			memberVO.getPassword().equals("1234")) {
			
			memberVO.setNickname("관리자");
			session.setAttribute(Member.USER, memberVO);//인터페이스 만들어준거
			//세션에 로그인 저장 상태유지
			
			session.removeAttribute("status");
			//로그인에 성공하면 세션을 지워줌 
			
			return new ModelAndView("redirect:/");
		}
		
		
		if( error.hasErrors() ) {
			ModelAndView view = new ModelAndView();
			view.setViewName("member/login");
			return view;
		}

	
		//로그인이 실패했을때
		session.setAttribute("status", "fail");
		
//		return new ModelAndView("redirect:/login");
		return new ModelAndView("redirect:/login");
		*/
		
	
	}
	
	@RequestMapping("/logout")
	public String doLogoutAction(HttpSession session) {
		
		//삭제는 하나의 키만 지우는 것
		//세션 소멸 - 세션자체를 날림
		
		session.invalidate();
		return "redirect:/login";
		
	}
	
	@RequestMapping("/leave/process1")
	public String viewVerifyPage() {
		
		
		
		return "member/delete/process1";
				
	}
	
	@RequestMapping("/leave/process2")
	public ModelAndView viewDeleteMyCommunitiesPage(@RequestParam(required=false, defaultValue="" ) 
												String password, HttpSession session) {
		
		if ( password.length() == 0 ) {
			//잘못된 접근에 대한 처리
			return new ModelAndView("error/404");
		}
		
		MemberVO member = (MemberVO)session.getAttribute(Member.USER);
		
		member.setPassword(password);
		
		MemberVO verifyMember = memberService.readMember(member);
		
		if ( verifyMember == null ) {//패스워드 확인
			return new ModelAndView("redirect:/delete/process1");
			
		}
		
		//TODO 내가 작성한 게시글의 개수 가져오기
		int myCommunitiesCount = communityService
							.readMyCommunitiesConut(verifyMember.getId());
		
		ModelAndView view = new ModelAndView();
		
		view.setViewName("member/delete/process2");
		view.addObject("myCommunitiesCount", myCommunitiesCount);
		
		String uuid = UUID.randomUUID().toString();
		session.setAttribute("__TOKEN__", uuid);
		view.addObject("token", uuid);
		//절대 중복이 잃어나지 않는 난수를 주는 것이 중요
		//UUID 중복 잃어나지 않음 시간을 베이스로 해서 발생시킴
		
	
		return view;
	
		
	}	
	
	
	@RequestMapping("/account/delete/{deleteFlag}")
	public String remove(HttpSession session, 
					@PathVariable String deleteFlag,
					@RequestParam(required= false, defaultValue="") String token) {
		
		String sessionToken = (String) session.getAttribute("__TOKEN__");
		
		if ( sessionToken == null || !sessionToken.equals(token) ) {
			return "error/404";
		}
		
		
		MemberVO member = (MemberVO)session.getAttribute(Member.USER);
		
		if( member==null) {//널체크 안하면 에러 발생
			return "redirect:/login";
		}

		int id = member.getId();
		
		if ( memberService.leaveMember(id, deleteFlag) ) {
			session.invalidate();
		}
		return "member/delete/delete";
		
		
	}
	


}
