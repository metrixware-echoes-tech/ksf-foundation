/**
 *
 */
package com.tocea.corolla.users.service

import spock.lang.Specification


/**
 * @author sleroy
 *
 */
class EmailValidationServiceTest extends Specification {

	def validation = new EmailValidationService()

	def "test valid email"() {
		expect:
		validation.validateEmail "test@dummy.org"
		!validation.validateEmail("!dfsdf3)à)à)àtest@dummy.org")
		!validation.validateEmail("!")
		!validation.validateEmail("")
		!validation.validateEmail(null)
		!validation.validateEmail("!test@dummy.org@dummy.org")
	}
}
