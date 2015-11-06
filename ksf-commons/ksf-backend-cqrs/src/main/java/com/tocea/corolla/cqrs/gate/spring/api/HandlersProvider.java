package com.tocea.corolla.cqrs.gate.spring.api;

import com.tocea.corolla.cqrs.handler.ICommandHandler;

public interface HandlersProvider {
	ICommandHandler<Object, Object> getHandler(Object command);
}