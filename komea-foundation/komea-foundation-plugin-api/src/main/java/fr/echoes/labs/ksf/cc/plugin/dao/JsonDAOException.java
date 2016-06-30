package fr.echoes.labs.ksf.cc.plugin.dao;

import com.tocea.corolla.utils.domain.KsfDomainException;

public class JsonDAOException extends KsfDomainException {

	public JsonDAOException() {

	}

	public JsonDAOException(final String _message) {
		super(_message);

	}

	public JsonDAOException(final String _message, final Throwable _cause) {
		super(_message, _cause);

	}

}
