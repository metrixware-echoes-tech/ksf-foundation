/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tocea.corolla.cqrs.handler.spring;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import com.sun.corba.se.impl.activation.CommandHandler;
import com.tocea.corolla.cqrs.gate.spring.api.HandlersProvider;
import com.tocea.corolla.cqrs.handler.ICommandHandler;

/**
 * This class retrieves the appropriate {@link CommandHandler} based on the type
 * of the command.
 * 
 * @author sleroy
 *        
 */
@Component
public class SpringHandlersProvider implements HandlersProvider {
	
	@Autowired
	private ConfigurableListableBeanFactory beanFactory;
	
	private final Map<Class<?>, String> handlers = new HashMap<Class<?>, String>();
	
	@SuppressWarnings("unchecked")
	@Override
	public ICommandHandler<Object, Object> getHandler(final Object command) {
		final String beanName = handlers.get(command.getClass());
		if (beanName == null) {
			throw new RuntimeException("command handler not found. Command class is " + command.getClass());
		}
		final ICommandHandler<Object, Object> handler = beanFactory.getBean(beanName, ICommandHandler.class);
		return handler;
	}
	
	@PostConstruct
	public void onApplicationEvent() {
		handlers.clear();
		final String[] commandHandlersNames = beanFactory.getBeanNamesForType(ICommandHandler.class);
		for (final String beanName : commandHandlersNames) {
			final BeanDefinition commandHandler = beanFactory.getBeanDefinition(beanName);
			try {
				final Class<?> handlerClass = Class.forName(commandHandler.getBeanClassName());
				handlers.put(getHandledCommandType(handlerClass), beanName);
			} catch (final ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private ParameterizedType findByRawType(final Type[] genericInterfaces, final Class<?> expectedRawType) {
		for (final Type type : genericInterfaces) {
			if (type instanceof ParameterizedType) {
				final ParameterizedType parametrized = (ParameterizedType) type;
				if (expectedRawType.equals(parametrized.getRawType())) {
					return parametrized;
				}
			}
		}
		throw new RuntimeException();
	}
	
	private Class<?> getHandledCommandType(final Class<?> clazz) {
		final Type[] genericInterfaces = clazz.getGenericInterfaces();
		final ParameterizedType type = findByRawType(genericInterfaces, ICommandHandler.class);
		return (Class<?>) type.getActualTypeArguments()[0];
	}
}
