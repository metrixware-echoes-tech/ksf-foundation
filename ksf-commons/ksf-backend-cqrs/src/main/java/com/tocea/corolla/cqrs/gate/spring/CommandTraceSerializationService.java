/*
 * Corolla - A Tool to manage software requirements and test cases
 * Copyright (C) 2015 Tocea
 *
 * This file is part of Corolla.
 *
 * Corolla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License,
 * or any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with Corolla.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.cqrs.gate.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tocea.corolla.cqrs.gate.conf.CqrsConfiguration;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutionListener;

/**
 * This command listener provides a facility to log and serialize every actions
 * executed by Corolla in order to replay them in tests.
 *
 * @author sleroy
 *
 */
@Service
public class CommandTraceSerializationService implements ICommandExecutionListener {
	
	/**
	 * The Class CommandTrace represents the POJO stored into the trace.
	 */
	public static class CommandTrace {
		private List<Object> commands = new ArrayList<>();
		
		/**
		 * Adds the command.
		 *
		 * @param _command the _command
		 */
		public void addCommand(final Object _command) {
			commands.add(_command);
		}
		
		/**
		 * Gets the commands.
		 *
		 * @return the commands
		 */
		public List<Object> getCommands() {
			return commands;
		}
		
		/**
		 * Sets the commands.
		 *
		 * @param _commands the new commands
		 */
		public void setCommands(final List<Object> _commands) {
			commands = _commands;
		}
		
		@Override
		public String toString() {
			return "CommandTrace [commands=" + commands + "]";
		}
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandTraceSerializationService.class);
	

	private CqrsConfiguration configuration;
	
	private ObjectMapper objectMapper;
	
	/**
	 * Instantiates a new command trace serialization service.
	 */
	public CommandTraceSerializationService() {
		super();
	}
	
	/**
	 * Instantiates a new command trace serialization service.
	 *
	 * @param _cqrsConfiguration the _cqrs configuration
	 */
	@Autowired
	public CommandTraceSerializationService(final CqrsConfiguration _cqrsConfiguration) {
		configuration = _cqrsConfiguration;
		
	}
	
	/**
	 * Inits the service
	 */
	@PostConstruct
	public void init() {
		if (configuration.isTracingEnabled()) {
			LOGGER.warn("[WARNING] Trace serialization mode enabled!!");
			LOGGER.warn("[WARNING] It may have a huge impact on the performances");
			LOGGER.warn("[WARNING] DO NOT USE it in production");
			LOGGER.warn("[WARNING] Trace wil be stored in ---> {}", configuration.getTraceFile());
			objectMapper = new ObjectMapper();
			try {
				if (configuration.getTraceFile().createNewFile()) {
					final CommandTrace trace = new CommandTrace();
					objectMapper.writeValue(configuration.getTraceFile(), trace);
				}
			} catch (final IOException e) {
				LOGGER.error("Could not create the trace, already existing -> {}", e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutionListener#onFailure(java.lang.Object, java.lang.Throwable)
	 */
	@Override
	public void onFailure(final Object _command, final Throwable _cause) {
		if (configuration.isTracingEnabled()) {
			serializeTrace(_command);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.tocea.corolla.cqrs.gate.spring.api.ICommandExecutionListener#onSuccess(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void onSuccess(final Object _command, final Object _result) {
		if (configuration.isTracingEnabled()) {
			serializeTrace(_command);
		}
		
	}
	
	/**
	 * Serialize trace.
	 *
	 * @param _command the _command
	 */
	private void serializeTrace(final Object _command) {
		try {
			final CommandTrace trace = objectMapper.readValue(configuration.getTraceFile(), CommandTrace.class);
			trace.addCommand(_command);
			objectMapper.writeValue(configuration.getTraceFile(), trace);
		} catch (final Exception e) {
			LOGGER.error("Error during the serialization of the command {} -> {}", configuration.getTraceFile(),
					_command, e);
		}
	}
	
}
