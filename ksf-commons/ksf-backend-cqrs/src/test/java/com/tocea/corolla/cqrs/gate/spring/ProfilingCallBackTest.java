package com.tocea.corolla.cqrs.gate.spring;

import org.junit.Test;

import com.tocea.corolla.cqrs.gate.spring.CommandProfilingService.ProfilingCallBack;
import com.tocea.corolla.cqrs.gate.spring.api.ICommandCallback;

public class ProfilingCallBackTest {
	
	@Test(expected = RuntimeException.class)
	
	public void onFailureWithException() throws Exception {
		final ProfilingCallBack<String> callBack = new ProfilingCallBack<String>("", new ICommandCallback<String>() {
			
			@Override
			public String call() throws Exception {
				throw new RuntimeException();
			}

		});
		
		callBack.call();
	}
	
	@Test
	public void onSuccessNoException() throws Exception {
		final ProfilingCallBack<String> callBack = new ProfilingCallBack<String>("", new ICommandCallback<String>() {
			
			@Override
			public String call() throws Exception {
				return null;
			}
			
		});
		
		callBack.call();
	}
}
