package fr.echoes.labs.ksf.cc.sf.exceptions;

import com.tocea.corolla.utils.domain.KsfDomainException;

public class MissingProductionLineInformationException extends KsfDomainException {
	
	public MissingProductionLineInformationException() {
		super();
	}

	public MissingProductionLineInformationException(final String message) {
		super(message);
	}

}
