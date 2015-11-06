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
/**
 *
 */
package com.tocea.corolla.cqrs.gate.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.gate.IEventBusService;

/**
 * This class defines the gate where the commands are dispatched for execution.
 *
 * @author sleroy
 *
 */
@Service
public class SpringGate implements Gate {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringGate.class);

    @Autowired
    private SequentialCommandExecutorService sequentialCommandExecutorService;
    @Autowired
    private IEventBusService eventBusService;

    /**
     * Executes sequentially.
     */
    @Override
    public <R> R dispatch(final Object _command) {
        return sequentialCommandExecutorService.run(_command);

    }

    @Override
    public void dispatchEvent(final Object _event) {
        LOGGER.trace("Received event {}", _event);
        eventBusService.dispatchEvent(_event);
    }

}
