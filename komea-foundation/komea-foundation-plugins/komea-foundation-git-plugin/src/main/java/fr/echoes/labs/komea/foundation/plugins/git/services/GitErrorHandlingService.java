package fr.echoes.labs.komea.foundation.plugins.git.services;

import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dcollard
 *
 */
@Service
public class GitErrorHandlingService {

	private final static String END_LINE = System.getProperty("line.separator");

	private final String SESSION_ITEM = "gitError";

	@Autowired
	private HttpSession session;

	public void registerError(Exception e) {
		registerError(e.getMessage());
	}

	public void registerError(String errorMsg) {
		this.session.setAttribute(this.SESSION_ITEM, errorMsg);
	}

	public <T> void registerError(String errorMsg, Set<ConstraintViolation<T>> errors) {
		final StringBuilder sb = new StringBuilder(errorMsg);
		for (final ConstraintViolation<T> error : errors) {
			sb.append(END_LINE).append(error.getMessage());
		}
		this.session.setAttribute(this.SESSION_ITEM, sb.toString());
	}

	public String retrieveError() {
		final String ex = (String) this.session.getAttribute(this.SESSION_ITEM);
		this.session.setAttribute(this.SESSION_ITEM, null);
		return ex;
	}

}
