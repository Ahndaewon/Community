package com.ktds.member.service;

import com.ktds.member.vo.MemberVO;

public interface MemberService {

	public boolean createMember(MemberVO memberVO);

	public MemberVO readMember(MemberVO memberVO);

	public boolean leaveMember(int id, String deleteFlag);
	
	public boolean readCountMemberEmail(String email);
	public boolean readCountMemberNickname(String nickname);
	
	

}
