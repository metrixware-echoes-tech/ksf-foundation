package fr.echoes.labs.ksf.cc.extensions.services.project;

import com.vividsolutions.jts.util.Assert;
import org.junit.Test;

public class ProjectUtilsTest {

    @Test
    public void testCreateIdentifier() {
        Assert.equals("komea", ProjectUtils.createIdentifier("Komea"));
        Assert.equals("gradle_cpp_plugin", ProjectUtils.createIdentifier("Gradle-cpp-plugin"));
        Assert.equals("echoes_labs___common_libs", ProjectUtils.createIdentifier("Echoes Labs - Common Libs"));
        Assert.equals("rapid_scertify__eclipse", ProjectUtils.createIdentifier("RAPID(Scertify) Eclipse"));
    }

}
