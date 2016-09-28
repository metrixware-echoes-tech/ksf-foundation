package fr.echoes.labs.ksf.cc.extensions.services.project;

import com.tocea.corolla.products.utils.EntityKeyGenerator;
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
        return new EntityKeyGenerator(projectName).generate();
    }

}
