package com.tocea.corolla.backend.support.impl;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupportServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportServiceTest.class.getName());

    @Test
    public void test() throws Exception {
        final SupportService service = new SupportService();
        service.init();
        final File file = service.buildZipSupport();
        LOGGER.debug("Support data zip created at " + file.getAbsolutePath());
        Assert.assertTrue(file.exists());
        Assert.assertTrue(file.length() > 0);
        FileUtils.deleteQuietly(file);
        LOGGER.debug("Support data zip deleted at " + file.getAbsolutePath());
        service.close();
    }

}
