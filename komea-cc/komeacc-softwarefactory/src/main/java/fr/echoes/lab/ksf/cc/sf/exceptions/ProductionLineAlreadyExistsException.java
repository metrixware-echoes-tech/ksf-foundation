package fr.echoes.lab.ksf.cc.sf.exceptions;

import com.tocea.corolla.utils.domain.CorollaDomainException;

public class ProductionLineAlreadyExistsException extends CorollaDomainException {

	private final static String MESSAGE = "This production line already exists";
	
	public ProductionLineAlreadyExistsException() {
		super(MESSAGE);
	}
	
}
