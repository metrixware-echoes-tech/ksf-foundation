/**
 *
 */
package com.tocea.corolla.test.utils;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tocea.corolla.utils.functests.FunctionalTestDoc;

/**
 * @author sleroy
 *
 */
public class FunctionalDocRule implements MethodRule {

	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionalDocRule.class);

	/*
	 * (non-Javadoc)
	 * @see org.junit.rules.MethodRule#apply(org.junit.runners.model.Statement, org.junit.runners.model.FrameworkMethod, java.lang.Object)
	 */
	@Override
	public Statement apply(final Statement _base, final FrameworkMethod _method, final Object _target) {
		final FunctionalTestDoc annotation = _method.getDeclaringClass().getAnnotation(FunctionalTestDoc.class);
		if (annotation != null) {
			LOGGER.info("Functional test started :\n\trequirementId={}\n\tFeature={}\n\tticket={}\n\ttestCaseId={}", annotation.requirementId(),
					annotation.requirementName(), annotation.ticketNumber(), annotation.testCaseId());
		}
		return _base;
	}
}
