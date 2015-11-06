package com.tocea.corolla.utils.domain

import static org.junit.Assert.*

import javax.validation.ConstraintViolationException
import javax.validation.constraints.NotNull

import spock.lang.Specification

class TestUser {
	@NotNull
	def String
}

class ObjectValidationTest extends Specification {
	def "object is not valid"()  {
		expect:
		new ObjectValidation().isValid(new TestUser()) == false
	}
	def "object is valid"()  {
		expect:
		new ObjectValidation().isValid("bla") == true
	}

	def "object  :validation failed "()  {
		when:
		new ObjectValidation().validate(new TestUser())
		then:
		thrown(ConstraintViolationException)
	}
	def "object : validation success"()  {
		when:
		new ObjectValidation().validate("bla")
		then:
		notThrown(ConstraintViolationException)
	}
}
