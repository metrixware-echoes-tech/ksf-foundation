package com.tocea.corolla.cqrs.gate.spring.api;

public interface ICommandProfilingService {
	
	<R> ICommandCallback<R> decorate(Object _command, ICommandCallback<R> _callback);
	
}
