package com.ktds.actionhistory.web;

import org.springframework.stereotype.Controller;

import com.ktds.actionhistory.service.ActionHistoryService;

@Controller
public class ActionHistoryController {
	
	ActionHistoryService actionHistoryService;

	public void setActionHistoryService(ActionHistoryService actionHistoryService) {
		this.actionHistoryService = actionHistoryService;
	}
	
	
	
	
	
}
