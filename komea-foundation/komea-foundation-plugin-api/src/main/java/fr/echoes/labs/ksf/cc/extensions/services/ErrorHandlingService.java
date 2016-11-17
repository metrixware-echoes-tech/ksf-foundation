package fr.echoes.labs.ksf.cc.extensions.services;

import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorHandlingService {

	private final static String END_LINE = System.getProperty("line.separator");
	private final static String PREFIX = "error-";
	
	@Autowired
	private HttpSession session;
	
	private static String getErrorKey(final String pluginID) {
		return PREFIX + pluginID;
	}
	
	public void registerError(final String pluginID, final Exception e) {
		registerError(pluginID, e.getMessage());
	}
	
	public void registerError(final String pluginID, final String errorMsg) {
		session.setAttribute(getErrorKey(pluginID), errorMsg);
	}

	public <T> void registerError(final String pluginID, final String errorMsg, final Set<ConstraintViolation<T>> errors) {
		final StringBuilder sb = new StringBuilder(errorMsg);
		for (ConstraintViolation<T> error : errors) {
			sb.append(END_LINE).append(error.getMessage());
		}
		registerError(pluginID, sb.toString());
	}
	
	public String retrieveError(final String pluginID) {
		final String errorKey = getErrorKey(pluginID);
		final String error = (String) session.getAttribute(errorKey);
		session.setAttribute(errorKey, null);
		return error;
	}
	
}
