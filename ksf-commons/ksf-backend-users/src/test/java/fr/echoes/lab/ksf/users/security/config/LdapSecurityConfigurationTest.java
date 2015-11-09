/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.users.security.config;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

/**
 *
 * @author sleroy
 */
public class LdapSecurityConfigurationTest {
    
    public LdapSecurityConfigurationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of hasUserDN method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testHasUserDN() {
        System.out.println("hasUserDN");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        boolean expResult = false;
        boolean result = instance.hasUserDN();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of hasUserSearch method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testHasUserSearch() {
        System.out.println("hasUserSearch");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        boolean expResult = false;
        boolean result = instance.hasUserSearch();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of hasConfigurationProvided method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testHasConfigurationProvided() {
        System.out.println("hasConfigurationProvided");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        boolean expResult = false;
        boolean result = instance.hasConfigurationProvided();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isAuthRequired method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testIsAuthRequired() {
        System.out.println("isAuthRequired");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        boolean expResult = false;
        boolean result = instance.isAuthRequired();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of clearManagerDN method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testClearManagerDN() {
        System.out.println("clearManagerDN");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.clearManagerDN();
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getProviderUrl method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetProviderUrl() {
        System.out.println("getProviderUrl");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getProviderUrl();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of buildLdapContext method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testBuildLdapContext() {
        System.out.println("buildLdapContext");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        DefaultSpringSecurityContextSource expResult = null;
        DefaultSpringSecurityContextSource result = instance.buildLdapContext();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUrl method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetUrl() {
        System.out.println("getUrl");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getUrl();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setUrl method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetUrl() {
        System.out.println("setUrl");
        String url = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setUrl(url);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getManagerDn method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetManagerDn() {
        System.out.println("getManagerDn");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getManagerDn();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setManagerDn method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetManagerDn() {
        System.out.println("setManagerDn");
        String managerDn = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setManagerDn(managerDn);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getManagerPassword method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetManagerPassword() {
        System.out.println("getManagerPassword");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getManagerPassword();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setManagerPassword method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetManagerPassword() {
        System.out.println("setManagerPassword");
        String managerPassword = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setManagerPassword(managerPassword);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getRoot method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetRoot() {
        System.out.println("getRoot");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getRoot();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setRoot method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetRoot() {
        System.out.println("setRoot");
        String root = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setRoot(root);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getPort method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetPort() {
        System.out.println("getPort");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        Integer expResult = null;
        Integer result = instance.getPort();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setPort method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetPort() {
        System.out.println("setPort");
        Integer port = null;
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setPort(port);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUserDnPattern method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetUserDnPattern() {
        System.out.println("getUserDnPattern");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getUserDnPattern();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setUserDnPattern method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetUserDnPattern() {
        System.out.println("setUserDnPattern");
        String userDnPattern = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setUserDnPattern(userDnPattern);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUserSearchFilter method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetUserSearchFilter() {
        System.out.println("getUserSearchFilter");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getUserSearchFilter();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setUserSearchFilter method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetUserSearchFilter() {
        System.out.println("setUserSearchFilter");
        String userSearchFilter = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setUserSearchFilter(userSearchFilter);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUserSearchBase method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetUserSearchBase() {
        System.out.println("getUserSearchBase");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getUserSearchBase();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setUserSearchBase method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetUserSearchBase() {
        System.out.println("setUserSearchBase");
        String userSearchBase = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setUserSearchBase(userSearchBase);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getGroupSearchBase method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetGroupSearchBase() {
        System.out.println("getGroupSearchBase");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getGroupSearchBase();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setGroupSearchBase method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetGroupSearchBase() {
        System.out.println("setGroupSearchBase");
        String groupSearchBase = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setGroupSearchBase(groupSearchBase);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getGroupRoleAttribute method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetGroupRoleAttribute() {
        System.out.println("getGroupRoleAttribute");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getGroupRoleAttribute();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setGroupRoleAttribute method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetGroupRoleAttribute() {
        System.out.println("setGroupRoleAttribute");
        String groupRoleAttribute = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setGroupRoleAttribute(groupRoleAttribute);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getGroupSearchFilter method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testGetGroupSearchFilter() {
        System.out.println("getGroupSearchFilter");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        String expResult = "";
        String result = instance.getGroupSearchFilter();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setGroupSearchFilter method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetGroupSearchFilter() {
        System.out.println("setGroupSearchFilter");
        String groupSearchFilter = "";
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setGroupSearchFilter(groupSearchFilter);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isPooled method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testIsPooled() {
        System.out.println("isPooled");
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        boolean expResult = false;
        boolean result = instance.isPooled();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setPooled method, of class LdapSecurityConfiguration.
     */
    @Test
    public void testSetPooled() {
        System.out.println("setPooled");
        boolean pooled = false;
        LdapSecurityConfiguration instance = new LdapSecurityConfiguration();
        instance.setPooled(pooled);
        // TODO review the generated test code and remove the default call to //fail.
        //fail("The test case is a prototype.");
    }
    
}
