package fr.echoes.labs.ksf.cc.pluginmanager;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.HashMap;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class PluginPropertiesBeanImplTest {

	public static class TestBeanA {

		String	field1;

		String	field2;

		Integer	field3;

		public String getField1() {
			return this.field1;
		}

		public String getField2() {
			return this.field2;
		}

		public Integer getField3() {
			return this.field3;
		}

		public void setField1(final String field1) {
			this.field1 = field1;
		}

		public void setField2(final String field2) {
			this.field2 = field2;
		}

		public void setField3(final Integer field3) {
			this.field3 = field3;
		}

		@Override
		public String toString() {
			return "TestBeanA [field1=" + this.field1 + ", field2=" + this.field2 + ", field3=" + this.field3 + "]";
		}
	}

	@Test
	public void testConvertAsBean() throws Exception {
		final HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("field1", "12");
		hashMap.put("field2", "13");
		hashMap.put("field3", "14");

		final PluginPropertiesBeanImpl pluginPropertiesBeanImpl = new PluginPropertiesBeanImpl(File.createTempFile("gni", "gna"));
		pluginPropertiesBeanImpl.setProperties(hashMap);
		final TestBeanA testBeanA = pluginPropertiesBeanImpl.convertAsBean(TestBeanA.class);
		System.out.println(testBeanA);
		assertThat(testBeanA.field1, CoreMatchers.equalTo(hashMap.get("field1")));
		assertThat(testBeanA.field2, CoreMatchers.equalTo(hashMap.get("field2")));
		assertThat(testBeanA.field3, CoreMatchers.equalTo(Integer.parseInt((String) hashMap.get("field3"))));
	}

}
