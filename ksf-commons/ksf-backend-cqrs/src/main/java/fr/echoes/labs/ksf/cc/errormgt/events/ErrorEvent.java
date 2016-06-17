package fr.echoes.labs.ksf.cc.errormgt.events;

/**
 * This event is fired when an error is caught by the KSF system.
 *
 * @author sleroy
 *
 */
public class ErrorEvent {

	public String		message;

	public Throwable	trace;

	public String		errorID;

	public ErrorEvent(final String message, final Throwable trace, final String errorID) {
		super();
		this.message = message;
		this.trace = trace;
		this.errorID = errorID;
	}

	public ErrorEvent(final Throwable error) {
		this.trace = error;
		this.errorID = error.getClass().getName();
		this.message = error.getMessage();
	}

	public String getErrorID() {
		return this.errorID;
	}

	public String getMessage() {
		return this.message;
	}

	public Throwable getTrace() {
		return this.trace;
	}

	public void setErrorID(final String errorID) {
		this.errorID = errorID;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setTrace(final Throwable trace) {
		this.trace = trace;
	}

	@Override
	public String toString() {
		return "ErrorEvent [message=" + this.message + ", trace=" + this.trace + ", errorID=" + this.errorID + "]";
	}

}
