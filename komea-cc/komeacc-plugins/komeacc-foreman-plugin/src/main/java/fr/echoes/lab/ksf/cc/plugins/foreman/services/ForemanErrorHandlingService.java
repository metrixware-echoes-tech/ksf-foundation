package fr.echoes.lab.ksf.cc.plugins.foreman.services;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ForemanErrorHandlingService {

	private final static String END_LINE = System.getProperty("line.separator");

	private final String SESSION_ITEM = "foremanError";
	
	@Autowired
	private HttpSession session;
	
	public void registerError(Exception e) {
		registerError(e.getMessage());
	}
	
	public void registerError(String errorMsg) {
		session.setAttribute(SESSION_ITEM, errorMsg);
	}

	public <T> void registerError(String errorMsg, Set<ConstraintViolation<T>> errors) {
		StringBuilder sb = new StringBuilder(errorMsg);
		for (ConstraintViolation<T> error : errors) {
			sb.append(END_LINE).append(error.getMessage());
		}
		session.setAttribute(SESSION_ITEM, sb.toString());
	}
	
	public String retrieveError() {
		String ex = (String) session.getAttribute(SESSION_ITEM);
		session.setAttribute(SESSION_ITEM, null);
		return ex;
	}
	
}
