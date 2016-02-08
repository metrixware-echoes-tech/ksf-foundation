package fr.echoes.labs.ksf.cc.sf.exceptions;

import com.tocea.corolla.utils.domain.KsfDomainException;

public class ProductionLineAlreadyExistsException extends KsfDomainException {

	private final static String MESSAGE = "This production line already exists";
	
	public ProductionLineAlreadyExistsException() {
		super(MESSAGE);
	}
	
}
