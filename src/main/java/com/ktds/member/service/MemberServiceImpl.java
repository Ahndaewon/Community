package com.ktds.member.service;

import com.ktds.community.dao.CommunityDao;
import com.ktds.member.dao.MemberDao;
import com.ktds.member.vo.MemberVO;
import com.ktds.util.SHA256Util;

public class MemberServiceImpl  implements MemberService{

	private MemberDao memberDao;
	private CommunityDao communityDao;
	
	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	
	@Override
	public boolean createMember(MemberVO memberVO) {
		//SALT값을 만든다.
		
		String salt = SHA256Util.generateSalt();
		memberVO.setSalt(salt);
		
		String password = memberVO.getPassword();
		
		password = SHA256Util.getEncrypt(password, salt);
		memberVO.setPassword(password); //암호화 완료
		
		return memberDao.insertMember(memberVO) > 0;
	}


	@Override
	public MemberVO readMember(MemberVO memberVO) {
		
		
		//TNS Listener Error
		//1. 시작 - Oracle - Stop Database
		//2. 시작 - Oracle - Start Database
		
		
		//1. 사용자의 ID로 SALT가져오기
		
		String salt = memberDao.selectSalt(memberVO.getEmail());
		if( salt == null ) {
			salt = "";
		}
		
		
		//2. SALT로 암호화 하기
		String password = memberVO.getPassword();
		password = SHA256Util.getEncrypt(password, salt);
		memberVO.setPassword(password);
		//비밀번호를 못찾아서 비밀번호를 새로 설정함
		
		
		
		return memberDao.selectMember(memberVO);
	}


	@Override
	public boolean leaveMember(int id, String deleteFlag) {
		
		
		if ( deleteFlag.equals("y") ) {
			communityDao.deleteMyCommunities(id);
		}
		
		
		
		if( memberDao.deleteMember(id) > 0 ) {
			return true;
		}
		return false;
	}

	@Override
	public boolean readCountMemberEmail(String email) {
		return memberDao.selectCountMemberEmail(email) > 0;
	}

	@Override
	public boolean readCountMemberNickname(String nickname) {
		return memberDao.selectCountMemberNickname(nickname) > 0 ;
	}

}
