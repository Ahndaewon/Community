package com.ktds.member.web;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.actionhistory.service.ActionHistoryService;
import com.ktds.actionhistory.vo.ActionHistory;
import com.ktds.actionhistory.vo.ActionHistoryVO;
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
	
	
	//어떠한 데이터도 넣을 수 있다. json형태로 값을 넘겨준다. 객체리너럴을 하나의 문자의 형태로 보내줌 JSON
	//모든 언어들이 JSON지원 서버가 다르고 언어들이 다른때 전송을 위해 사용
	//라이브러리 하나 추가 필요 pom.xml
	//jackson-databind 추가 해당라이브러리가 ResponseBody가 있으면 JSON형태로 변화해줌
	//타입 bundle 지워줌
	@RequestMapping("/api/exists/email")//ajax는 리턴타입이 여러가지
	@ResponseBody  
	public Map<String, Boolean> apiIsExistsEmail(@RequestParam String email){
		
		boolean isExists = memberService.readCountMemberEmail(email);
		
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		
		response.put("response", isExists);
		
		return response;
		
		/*{
		 * 	"isExists" : true
		 * } 이러한 형태로 값을 보내줄거임
		 *
		 */
	}
	
	@RequestMapping("/api/exists/nickname")
	@ResponseBody
	public Map<String, Boolean> apiIsExistsNickname(@RequestParam String nickname){

		boolean isExist = memberService.readCountMemberNickname(nickname);
		
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		
		response.put("response", isExist);
		
		
		
		return response;
	}
	
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String viewLoginPage(HttpSession session, @RequestAttribute ActionHistoryVO actionHistoryVO) { //세션을 바로 가지고 올 수 있음
		
	
		if( session.getAttribute(Member.USER) != null ) {
			return "redirect:/";
			
		}//로그인이 필요한 페이지에 모두 넣어주면됨
		
		
		return "member/login";
	}
	
	
	
	
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String viewRegistPage() {
		return "member/regist";
	}
	
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public String doRegistAction(@ModelAttribute("registForm")
									@Valid MemberVO memberVO, Errors errors
									, @RequestAttribute ActionHistoryVO actionHistoryVO) {
		
		if ( errors.hasErrors() ) {
			return "member/regist";
			
		}
		
		
		if ( memberService.createMember(memberVO) ) {
			actionHistoryVO.setReqType(ActionHistory.ReqType.MEMBER);
			String log = String.format(ActionHistory.Log.REGIST, memberVO.getEmail(), memberVO.getNickname(), "true");
			actionHistoryVO.setLog(log);
			return "redirect:/login";
		}
		actionHistoryVO.setReqType(ActionHistory.ReqType.MEMBER);
		String log = String.format(ActionHistory.Log.REGIST, memberVO.getEmail(), memberVO.getNickname() ,"false");
		actionHistoryVO.setLog(log);
		return "redirect:/login";
	}
	
	
	/*@RequestMapping("/logout")
	public String doLogoutAction(HttpSession session, HttpServletRequest request, Model model) {
		
		MemberVO member = (MemberVO)session.getAttribute(Member.USER);
		
		
		ActionHistoryVO history = 
				(ActionHistoryVO) request.getAttribute("actionHistory");
		
		history.setReqType(ActionHistory.ReqType.MEMBER);
		
		String log = String.format(ActionHistory.Log.LOGOUT, member.getEmail());
		history.setLog(log);
		model.addAttribute("actionHistory", history);
		//삭제는 하나의 키만 지우는 것
		//세션 소멸 - 세션자체를 날림
		
		session.invalidate();
		return "redirect:/login";
		
	}*/
	
	@RequestMapping("/logout")
	public String doLogoutAction(HttpSession session, @RequestAttribute ActionHistoryVO actionHistoryVO) {
		
		MemberVO member = (MemberVO)session.getAttribute(Member.USER);
		
		
		
		actionHistoryVO.setReqType(ActionHistory.ReqType.MEMBER);
		String log = String.format(ActionHistory.Log.LOGOUT, member.getEmail());
		actionHistoryVO.setLog(log);
		
		//삭제는 하나의 키만 지우는 것
		//세션 소멸 - 세션자체를 날림
		
		session.invalidate();
		return "redirect:/login";
		
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String doLoginAction(@ModelAttribute("loginForm") @Valid MemberVO memberVO, 
					Errors error, HttpServletRequest request, Model model) {

		HttpSession session = request.getSession();
		
		ActionHistoryVO history = 
				(ActionHistoryVO) request.getAttribute("actionHistory");
		history.setReqType(ActionHistory.ReqType.MEMBER);
		
		String log = String.format(ActionHistory.Log.LOGIN, memberVO.getEmail());
		history.setLog(log);
		model.addAttribute("actionHistory", history);
		
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
