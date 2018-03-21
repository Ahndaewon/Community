package com.ktds.member.dao;

import com.ktds.member.vo.MemberVO;

public interface MemberDao {
	
	public int insertMember(MemberVO memverVO);
	
	public int selectCountMemberEmail(String email);

	public int selectCountMemberNickname(String nickname);
	
	public String selectSalt(String email);
	
	public MemberVO selectMember(MemberVO memberVO);
	
	public int deleteMember(int id);

}
