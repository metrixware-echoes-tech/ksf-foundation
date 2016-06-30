package fr.echoes.labs.ksf.cc.plugin.dao;

import com.tocea.corolla.utils.domain.KsfDomainException;

public class InvalidPojoException extends KsfDomainException {

	public InvalidPojoException() {
		super();
	}

	public InvalidPojoException(final String _message) {
		super(_message);
	}

	public InvalidPojoException(final String _message, final Throwable _cause) {
		super(_message, _cause);
	}

}
