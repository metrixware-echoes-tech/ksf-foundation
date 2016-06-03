package fr.echoes.labs.ksf.cc.extensions.services.project;

import java.text.Normalizer;
import java.util.Objects;

/**
 * @author dcollard
 *
 */
public class ProjectUtils {


	private ProjectUtils() {

	}

	public static String createIdentifier(String projectName) {
		Objects.requireNonNull(projectName);
		return  Normalizer.normalize(projectName, Normalizer.Form.NFD).replaceAll("[^\\dA-Za-z\\-]", "").replaceAll("\\s+","-" ).toLowerCase();
	}

}
