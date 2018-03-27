package com.ktds.community.web;



import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.View;
import javax.validation.Valid;


import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.actionhistory.vo.ActionHistory;
import com.ktds.actionhistory.vo.ActionHistoryVO;
import com.ktds.community.constants.Member;
import com.ktds.community.service.CommunityService;
import com.ktds.community.vo.CommunitySearchVO;
import com.ktds.community.vo.CommunityVO;
import com.ktds.member.vo.MemberVO;
import com.ktds.util.DownloadUtil;

import io.github.seccoding.web.pager.explorer.PageExplorer;
import javafx.scene.input.KeyCombination.ModifierValue;

@Controller
public class CommunityController {
	
	private CommunityService communityService;

	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}
	
	
	@RequestMapping("/reset")
	public String viewInitListPage(HttpSession session) {
		session.removeAttribute("__SEARCH__");
		return "redirect:/";
	}
	
	@RequestMapping("/")//젤 첫화면이 리스트 페이지
	public ModelAndView viewListPage(CommunitySearchVO communitySearchVO, HttpSession session) {
		
		
		//데이터가 안넘어왔을 때 
		//1. 리스트페이지에 처음 접근했을 때
		//2. 글 내용을 보고, 목록보기 링크를 클릭했을 때
		
		if ( communitySearchVO.getPageNo() < 0 ) {
			//Session 에 저장된 CommunitySerachVO를 가져옴
			communitySearchVO = (CommunitySearchVO) session.getAttribute("__SEARCH__");
			//Session 에 저장된 CommunitySerachVO가 없을 경우, PageN = 0으로 초기화
			if ( communitySearchVO == null ) {
				communitySearchVO = new CommunitySearchVO();
				communitySearchVO.setPageNo(0);
			}
			
		}
		session.setAttribute("__SEARCH__", communitySearchVO);
		
		ModelAndView view = new ModelAndView();
		// WEB-INF/view/community/list.jsp
		view.setViewName("community/list");
		view.addObject("search", communitySearchVO);
		
		PageExplorer pageExplorer = communityService.getAll(communitySearchVO);
		
		view.addObject("pageExplorer", pageExplorer);
		
		return view;
	}
	
	//@GetMapping("/write")
	@RequestMapping(value = "/write" , method = RequestMethod.GET)
	public String viewWritePage() {
		

		return "/community/write";
		
	}
	
//	@PostMapping("/write") //spring 4.3부터 쓸수 있음
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public ModelAndView doWrite(@ModelAttribute("writeFrom") @Valid CommunityVO communityVO, 
			Errors error, HttpServletRequest request
			,@RequestAttribute ActionHistoryVO actionHistoryVO) { //클래스에 맞게 스프링이 가져옴 그래서 이름을 똑같이 해줘야함
		
		ModelAndView view = new ModelAndView();
		
		if ( error.hasErrors() ) {
			view.setViewName("community/write");
			view.addObject("communityVO", communityVO);
			return view;
		}
		
		/*ActionHistoryVO history = 
				(ActionHistoryVO) request.getAttribute(Member.USER);
		history.setReqType(ActionHistory.ReqType.COMMUNITY);
		
		String log = String.format(ActionHistory.Log.WRITE
					, communityVO.getTitle(), communityVO.getBody());
		
		history.setLog(log);*/
		
		actionHistoryVO.setReqType(ActionHistory.ReqType.COMMUNITY);
		String log = String.format(ActionHistory.Log.WRITE, communityVO.getTitle(), communityVO.getBody());
		actionHistoryVO.setLog(log);
		
		
		
		String requestIp = request.getRemoteAddr();
		communityVO.setRequestIP(requestIp);
		communityVO.save();
		boolean isSuccess = communityService.createCommunity(communityVO);
		
		if( isSuccess ) {
			
			return new ModelAndView("redirect:/"); //해당 url로 이동
		
		}
		return new ModelAndView("redirect:/write");
	}
	
	@RequestMapping("/view/{id}")
	public ModelAndView viewViewPage(@PathVariable int id) {
		
		
		
		ModelAndView view = new ModelAndView();
		view.setViewName("community/view");
						
		CommunityVO community = communityService.refresh(id);
		
		view.addObject("community", community);
		
		return view;
	}
	
	
	@RequestMapping("/recommend/{id}")
	public String recommend( @PathVariable int id,
								@RequestAttribute ActionHistoryVO actionHistoryVO) {
		
		
		actionHistoryVO.setReqType(ActionHistory.ReqType.COMMUNITY);
		String log = String.format(ActionHistory.Log.RECOMMEND, id);
		actionHistoryVO.setLog(log);
		
		communityService.recommend(id);
		
		return "redirect:/view/"+id;
	}
	
	
	@RequestMapping("/read/{id}")
	public String read(@PathVariable int id) {
		
		
		CommunityVO community = communityService.getOne(id);
		
		
		return "redirect:/view/"+id;
	}
	
	@RequestMapping("/get/{id}")
	public void download(@PathVariable int id,
						 HttpServletRequest request,
						 HttpServletResponse response) {
		
		CommunityVO community = communityService.getOne(id);
		String filename = community.getDisplayFilename();
		
		DownloadUtil download = new DownloadUtil("D:\\uploadFiles/"+ filename);
		
		
		
		try {
			download.download(request, response, filename);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);//감탄코드
		}
		
	}
	@RequestMapping("/remove/{id}")
	public String remove(@PathVariable int id, HttpSession session) {
		
		MemberVO member = (MemberVO)session.getAttribute(Member.USER);
		CommunityVO community = communityService.getOne(id);
		
		boolean isMyCommunity = member.getId() == community.getUserId();
		//내가 쓴건지 확인
		if( isMyCommunity && communityService.deleteCommunity(id) ) {
			return "redirect:/";
		}
		
		return "/WEB-INF/view/error/404";
		
	}
	
	
	@RequestMapping(value="/modify/{id}", method = RequestMethod.GET)
	public ModelAndView viewModifyPage(@PathVariable int id, HttpSession session) {
		
		MemberVO member = (MemberVO)session.getAttribute(Member.USER);
		CommunityVO community = communityService.getOne(id);
		String body = community.getBody();
		body = body.replace("<br/>", "\n" );
		community.setBody(body);
		
		int userId = member.getId();
		
		if ( userId != community.getUserId() ) {
			return new ModelAndView("error/404");
		}
		
		ModelAndView view = new ModelAndView();
		view.setViewName("community/write");
		view.addObject("communityVO", community);
		view.addObject("mode", "modify");
		//왜줄까요
		return view;
		
	}
	
	@RequestMapping(value="/modify/{id}", method = RequestMethod.POST)
	public String doModifyAction(@PathVariable int id, 
								HttpSession session,
								@ModelAttribute("writeFrom") @Valid CommunityVO communityVO,
								Errors errors,
								HttpServletRequest request,
								@RequestAttribute ActionHistoryVO actionHistoryVO) {
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		CommunityVO originalVO = communityService.getOne(id);
		String asIs ="";
		String toBe ="";
		
		
		if ( member.getId() != originalVO.getUserId() ) {	//내글이 맞는지
			return "error/404";
		}
		
		if ( errors.hasErrors() ) {	//에러체크
			return "redirect:/modify/" + id;
		}
		/*
		0. 아이피 변경 확인  
		1. 제목 변경확인
		2. 내용 변경 확인
		3. 파일변경 확인
		4. 변경이 모두 안됬는지
		*/
		
		CommunityVO newCommunity = new CommunityVO();
		newCommunity.setId(originalVO.getId());
		newCommunity.setUserId(member.getId());
		boolean isModify = false;
		// 1. 아이피 변경 확인
		
		String ip = request.getRemoteAddr();
		if ( !ip.equals(originalVO.getRequestIP() ) ) {
			newCommunity.setRequestIP(ip);
			isModify = true;
			asIs += "ip : " + originalVO.getRequestIP() + "<br/>";
			toBe += "ip : " + communityVO.getRequestIP() + "<br/>";
			
		}
		
		// 2. 제목 변경 확인
		
		if ( !originalVO.getTitle().equals( communityVO.getTitle() ) ) {
			newCommunity.setTitle( communityVO.getTitle() );
			isModify = true;
			asIs += "Title : " + originalVO.getTitle() + "<br/>";
			toBe += "Title : " + communityVO.getTitle() + "<br/>";
		}
		// 3. 내용변경 확인
		if ( !originalVO.getBody().equals( communityVO.getBody() ) ) {
			String body = communityVO.getBody();
			body = body.replace("\n", "<br/>");
			communityVO.setBody(body);
			
			newCommunity.setBody( communityVO.getBody() );
			isModify = true;
			asIs += "body : " + originalVO.getBody() + "<br/>";
			toBe += "body : " + communityVO.getBody() + "<br/>";
		}
		
		if( communityVO.getDisplayFilename().length() > 0 ) {
			File file =  
					new File("D:\\uploadFiles/"+ communityVO.getDisplayFilename());
			file.delete();
			communityVO.setDisplayFilename("");
			asIs += "file : " + originalVO.getDisplayFilename() + "<br/>";
			toBe += "file : " + communityVO.getDisplayFilename() + "<br/>";
		}
		else {
			communityVO.setDisplayFilename(originalVO.getDisplayFilename() );
		}
		
		
		// 4. 파일 변경 없는지 확인
		communityVO.save();
		if ( !originalVO.getDisplayFilename().equals( communityVO.getDisplayFilename()) ) {
			newCommunity.setDisplayFilename( communityVO.getDisplayFilename() );
			isModify = true;
			asIs += "file : " + originalVO.getDisplayFilename() + "<br/>";
			toBe += "file : " + communityVO.getDisplayFilename() + "<br/>";
		}
		
		// 5. 변경이 없는지 확인
		if ( isModify ) {
			// 6. UPDATE 하는 Service Code 호출
			communityService.updateCommunity(newCommunity);
			
		}
		
		actionHistoryVO.setReqType(ActionHistory.ReqType.COMMUNITY);
		String log = String.format(ActionHistory.Log.UPDATE, communityVO.getTitle(), communityVO.getBody());
		actionHistoryVO.setLog(log);
		actionHistoryVO.setAsIs(asIs);
		actionHistoryVO.setToBe(toBe);
		
		
		return "redirect:/view/" + id;
	}
	
	
//	@RequestMapping("/read/{id}")
//	public ModelAndView read(@PathVariable int id, HttpSession session) {
//		
//		if( session.getAttribute(Member.USER) == null) {
//			return new ModelAndView("redirect:/login"); 
//		}
//		
//		ModelAndView view = new ModelAndView();
//		view.setViewName("community/view");
//		CommunityVO community = communityService.refresh(id);
//		
//		view.addObject("community",community);
//		
//	
//		
//		return view;
	
	
		
//	}
	


}
