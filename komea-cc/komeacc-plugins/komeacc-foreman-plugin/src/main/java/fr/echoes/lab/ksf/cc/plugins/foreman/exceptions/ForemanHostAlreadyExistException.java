package fr.echoes.lab.ksf.cc.plugins.foreman.exceptions;

public class ForemanHostAlreadyExistException extends RuntimeException {

	public ForemanHostAlreadyExistException(String hostName) {
		super("[foreman] host "+hostName+" already exists.");
	}
	
}
