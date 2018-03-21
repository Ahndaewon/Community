package com.ktds.community.constants;

import java.util.Map;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class IdMap {
	
	public static Map<String, Integer> idExist(Map<String, Integer> idBlock, String id) {
		
				
		if( idBlock.get(id) == null ) {
			idBlock.put(id, 1);
			System.out.println("처음");
			return idBlock;
		}
		else if( idBlock.get(id) == 3 ) {
			System.out.println("다참");
			return idBlock;
			
		}
		else {
			idBlock.put(id, idBlock.get(id)+1);
			System.out.println("다음 + 1");
			return idBlock;
		}
		
		
	}
	

}
