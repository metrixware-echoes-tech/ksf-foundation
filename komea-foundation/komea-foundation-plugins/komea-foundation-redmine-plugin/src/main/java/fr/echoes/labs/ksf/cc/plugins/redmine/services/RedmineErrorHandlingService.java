package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Throwables;
import com.taskadapter.redmineapi.RedmineException;
import com.tocea.corolla.cqrs.gate.IEventBus;
import com.tocea.corolla.utils.domain.KsfDomainException;

/**
 * @author dcollard
 *
 */
@Service
public class RedmineErrorHandlingService {

	private final static String	END_LINE		= System.getProperty("line.separator");

	private final String		SESSION_ITEM	= "redmineError";

	private final HttpSession	session;

	private final IEventBus		eventBus;

	@Autowired
	public RedmineErrorHandlingService(final HttpSession session, final IEventBus eventBus) {
		super();
		this.session = session;
		this.eventBus = eventBus;
	}

	public <T> void registerConstraintViolationError(final String errorMsg, final Set<ConstraintViolation<T>> errors) {
		this.eventBus.dispatchErrorEvent(new ConstraintViolationException(errors));
		final StringBuilder sb = new StringBuilder(errorMsg);
		for (final ConstraintViolation<T> error : errors) {
			sb.append(END_LINE).append(error.getMessage());
		}
		this.session.setAttribute(this.SESSION_ITEM, sb.toString());
	}

	public void registerError(final Throwable e) {
		this.eventBus.dispatchErrorEvent(new KsfDomainException("", e));
		this.session.setAttribute(this.SESSION_ITEM, e.getMessage());
	}

	public void registerErrorMessage(final String errorMsg) {
		this.registerError(new RedmineException(errorMsg));
	}

	public void registerErrorMessageAndException(final String errorMsg, final Throwable throwable) {
		String message = errorMsg != null ? errorMsg : StringUtils.EMPTY;
		if (throwable != null) {
			final Throwable rootCause = Throwables.getRootCause(throwable);
			if (rootCause != null && rootCause.getMessage() != null) {
				message = message + END_LINE + rootCause.getMessage();
			}
		}
		this.eventBus.dispatchErrorEvent(new RedmineException(message, throwable));
		this.registerErrorMessage(message);
	}

	/**
	 * Register exception.
	 *
	 * @param exception
	 *            the exception
	 */
	public void registerRootException(final Exception exception) {
		final Throwable rootCause = Throwables.getRootCause(exception);
		if (rootCause != null) {
			this.registerError(rootCause);
		} else {
			this.registerError(exception);
		}
	}

	public String retrieveError() {
		final String ex = (String) this.session.getAttribute(this.SESSION_ITEM);
		this.session.setAttribute(this.SESSION_ITEM, null);
		return ex;
	}

}
