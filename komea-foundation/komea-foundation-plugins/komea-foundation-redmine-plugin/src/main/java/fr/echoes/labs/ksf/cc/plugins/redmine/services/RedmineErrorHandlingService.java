package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Throwables;

/**
 * @author dcollard
 *
 */
@Service
public class RedmineErrorHandlingService {

	private final static String END_LINE = System.getProperty("line.separator");

	private final String SESSION_ITEM = "redmineError";

	@Autowired
	private HttpSession session;

	public void registerError(Exception e) {
		registerError(e.getMessage());
	}

	public void registerException(Exception e) {
		final Throwable rootCause = Throwables.getRootCause(e);
		registerError(rootCause.getMessage());
	}

	public void registerError(String errorMsg, Throwable throwable) {
		String message = errorMsg != null ? errorMsg : StringUtils.EMPTY;
		if (throwable != null) {
			final Throwable rootCause = Throwables.getRootCause(throwable);
			if (rootCause != null && rootCause.getMessage() != null) {
				message =  message + END_LINE + rootCause.getMessage();
			}
		}
		registerError(message);
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
