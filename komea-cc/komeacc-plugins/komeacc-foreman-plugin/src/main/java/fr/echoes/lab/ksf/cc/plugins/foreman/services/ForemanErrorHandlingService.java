package fr.echoes.lab.ksf.cc.plugins.foreman.services;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForemanErrorHandlingService {

	private final String SESSION_ITEM = "foremanError";
	
	@Autowired
	private HttpSession session;
	
	public void registerError(Exception e) {
		registerError(e.getMessage());
	}
	
	public void registerError(String errorMsg) {
		session.setAttribute(SESSION_ITEM, errorMsg);
	}
	
	public String retrieveError() {
		String ex = (String) session.getAttribute(SESSION_ITEM);
		session.setAttribute(SESSION_ITEM, null);
		return ex;
	}
	
}
