package fr.echoes.labs.ksf.cc.extensions.gui;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment;

import spock.lang.Specification;;

public class KomeaFoundationContextTest extends Specification {

	public static class BeanTest {
		
		@Value("\${ksf.conf.myProp}")
		private String myProp;

		public String getMyProp() {
			return myProp;
		}
		
		public void setMyProp(String myProp) {
			this.myProp = myProp;
		}
		
	}
	
	private Environment env = Mock(Environment);
	
	def "it should complete a bean properties with the environment properties"() {
	
		given:
			final KomeaFoundationContext foundation = new KomeaFoundationContext();
			foundation.env = env;
		
		and:
			def expectedValue = "blblbl"
			final BeanTest bean = new BeanTest();
			
		when:
			foundation.completeProperties(bean);
		
		then:
			env.get('ksf.conf.myProp') >> expectedValue
			
		then:
			bean.getMyProp() >> expectedValue
		
	}
	
}
