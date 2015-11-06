package com.tocea.corolla.cqrs.gate.spring;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tocea.corolla.cqrs.gate.spring.api.ICommandCallback;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandProfilingService;

@Service
public class CommandProfilingService implements ICommandProfilingService {
	
	public static final class ProfilingCallBack<R> implements ICommandCallback<R> {
		
		private final ICommandCallback<R>	callback;
		private final Object				command;
		private final StopWatch				stopWatch;
		
		public ProfilingCallBack(final Object _command, final ICommandCallback<R> _callback) {
			command = _command;
			callback = _callback;
			stopWatch = new StopWatch();
			stopWatch.start();
			
		}
		
		@Override
		public R call() throws Exception {
			try {
				return callback.call();
			} finally {
				stopWatch.stop();
				LOGGER.info("[PROFILING][{}]={} ms", command, stopWatch.getTime());
			}
		}
		
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandProfilingService.class);
	
	@Override
	public <R> ICommandCallback<R> decorate(final Object _command, final ICommandCallback<R> _callback) {
		
		return new ProfilingCallBack<R>(_command, _callback);
	}
	
}
