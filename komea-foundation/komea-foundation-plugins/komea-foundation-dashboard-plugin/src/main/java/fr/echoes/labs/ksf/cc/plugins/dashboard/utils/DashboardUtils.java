package fr.echoes.labs.ksf.cc.plugins.dashboard.utils;

import java.text.Normalizer;

public class DashboardUtils {

	public static String createIdentifier(String projectName) {
		return  Normalizer.normalize(projectName, Normalizer.Form.NFD).replaceAll("[^\\dA-Za-z\\-]", "").replaceAll("\\s+","-" ).toLowerCase();
	}
	
}
