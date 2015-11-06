package com.tocea.corolla.cqrs.gate.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.cqrs.gate.conf.CorollaCqrsConfiguration;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutionListener;

@Service
public class CommandLoggingService implements ICommandExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandLoggingService.class);

	@Autowired
	private CorollaCqrsConfiguration configuration;

	@Override
	public void onFailure(final Object _command, final Throwable _cause) {
		if (configuration.isLoggingEnabled()) {
			LOGGER.error("Command  has failed {} for the reason {}", _command, _cause);
		}

	}

	@Override
	public void onSuccess(final Object _command, final Object _result) {
		if (configuration.isLoggingEnabled()) {
			LOGGER.info("Command  has been executed with success {} with the result {}", _command, _result);
		}

	}

}
