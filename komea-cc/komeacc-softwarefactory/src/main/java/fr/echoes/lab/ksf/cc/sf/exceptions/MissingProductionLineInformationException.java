package fr.echoes.lab.ksf.cc.sf.exceptions;

import com.tocea.corolla.utils.domain.CorollaDomainException;

public class MissingProductionLineInformationException extends CorollaDomainException {

	public MissingProductionLineInformationException() {
		super();
	}
	
	public MissingProductionLineInformationException(String message) {
		super(message);
	}
	
}
