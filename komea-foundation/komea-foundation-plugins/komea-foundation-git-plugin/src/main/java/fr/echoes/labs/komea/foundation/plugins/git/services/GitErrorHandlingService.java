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

	public static final String SESSION_ITEM_GIT_ERROR = "gitError";
	public static final String SESSION_ITEM_GIT_MERGE_ERROR = "gitMergeError";

	@Autowired
	private HttpSession session;


	public void registerError(Exception e) {
		registerError(SESSION_ITEM_GIT_ERROR, e.getMessage());
	}

	public void registerError(String errorType, Exception e) {
		registerError(errorType, e.getMessage());
	}

	public void registerError(String errorMsg) {
		registerError(SESSION_ITEM_GIT_ERROR, errorMsg);
	}

	public void registerError(String errorType, String errorMsg) {
		this.session.setAttribute(errorType, errorMsg);
	}

	public <T> void registerError(String errorMsg, Set<ConstraintViolation<T>> errors) {
		registerError(SESSION_ITEM_GIT_ERROR, errorMsg, errors);
	}

	public <T> void registerError(String errorType, String errorMsg, Set<ConstraintViolation<T>> errors) {
		final StringBuilder sb = new StringBuilder(errorMsg);
		for (final ConstraintViolation<T> error : errors) {
			sb.append(END_LINE).append(error.getMessage());
		}
		this.session.setAttribute(errorType, sb.toString());
	}

	public String retrieveError() {
		return retrieveError(SESSION_ITEM_GIT_ERROR);
	}

	public String retrieveError(String errorType) {
		final String ex = (String) this.session.getAttribute(errorType);
		this.session.setAttribute(errorType, null);
		return ex;
	}

}
