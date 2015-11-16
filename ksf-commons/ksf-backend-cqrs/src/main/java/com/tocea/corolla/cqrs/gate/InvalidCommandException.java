/**
 *
 */
package com.tocea.corolla.cqrs.gate;

/**
 * The Class InvalidCommandException notify when an command is invalid.
 *
 * @author sleroy
 */
public class InvalidCommandException extends CqrsException {
	
	private static final long serialVersionUID = 1168607468748361052L;
	
	/**
	 * Instantiates a new invalid command exception.
	 *
	 * @param _command
	 *            the _command
	 */
	public InvalidCommandException(final Object _command) {
		super("Command invalid : " + _command);
	}
}
