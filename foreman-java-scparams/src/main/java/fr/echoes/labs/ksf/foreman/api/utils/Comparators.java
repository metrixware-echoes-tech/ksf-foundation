package fr.echoes.labs.ksf.foreman.api.utils;

import java.util.Comparator;

import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;

public final class Comparators {

	private Comparators() {
		// static class
	}
	
	public static Comparator<SmartClassParameterWrapper> smartClassParameterComparator() {
		return new Comparator<SmartClassParameterWrapper>() {
			@Override
			public int compare(final SmartClassParameterWrapper o1, final SmartClassParameterWrapper o2) {
				
				final String m1 = o1.getPuppetModule();
				final String m2 = o2.getPuppetModule();
				
				if (m1.equals(m2)) {
					
					final String c1 = o1.getPuppetClass();
					final String c2 = o2.getPuppetClass();
					
					if (c1.equals(c2)) {
						return o1.getParameter().compareTo(o2.getParameter());
					}
					
					return c1.compareTo(c2);
				}
				
				return m1.compareTo(m2);
			}
		};
	}
	
}
