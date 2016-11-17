package fr.echoes.labs.ksf.extensions.api;

import java.util.Comparator;

import org.springframework.core.annotation.Order;

import fr.echoes.labs.pluginfwk.api.extension.Extension;

public final class ExtensionComparators {

	public static Comparator<Extension> byOrderAnnotations() {

		return new Comparator<Extension>() {
			@Override
			public int compare(Extension o1, Extension o2) {
				final int orderValueExt1 = getOrderAnnotationValue(o1);
				final int orderValueExt2 = getOrderAnnotationValue(o2);
	    		return Integer.compare(orderValueExt1, orderValueExt2);
			}
    	};
		
	}
	
	private static Integer getOrderAnnotationValue(final Object object) {
		
		final Order order = object.getClass().getAnnotation(Order.class);
		Integer value = null;
		
		if (order != null) {
			value = order.value();
		}
		
		return value == null ? Integer.MAX_VALUE : value;
	}
	
}
