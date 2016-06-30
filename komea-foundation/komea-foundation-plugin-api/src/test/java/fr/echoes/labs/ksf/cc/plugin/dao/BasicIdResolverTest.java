package fr.echoes.labs.ksf.cc.plugin.dao;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

public class BasicIdResolverTest {

	public static class A {

		int	id	= CONSTANT;
		int	id2;

		public int getId() {
			return this.id;
		}

		public int getId2() {
			return this.id2;
		}

		public void setId(final int id) {
			this.id = id;
		}

		public void setId2(final int id2) {
			this.id2 = id2;
		}
	}

	private static final int	CONSTANT	= 12;
	private static final int	CONSTANT2	= 14;

	@Test
	public void testGetID() throws Exception {
		final A a = new A();

		final BasicIdResolver<Object> basicIdResolver = new BasicIdResolver<>();
		assertThat(basicIdResolver.getID(a), Matchers.equalTo(CONSTANT));

	}

	@Test
	public void testSetID() throws Exception {
		final A a = new A();

		final BasicIdResolver<Object> basicIdResolver = new BasicIdResolver<>();
		assertThat(basicIdResolver.getID(a), Matchers.equalTo(CONSTANT));

		basicIdResolver.setID(a, CONSTANT2);

		assertThat(basicIdResolver.getID(a), Matchers.equalTo(CONSTANT2));

	}

}
