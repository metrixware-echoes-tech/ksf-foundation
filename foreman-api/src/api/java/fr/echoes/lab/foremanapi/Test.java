package fr.echoes.lab.foremanapi;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import fr.echoes.lab.foremanapi.model.Filter;
import fr.echoes.lab.foremanapi.model.NewRole;
import fr.echoes.lab.foremanapi.model.Role;
import fr.echoes.lab.foremanapi.model.RoleWrapper;

public class Test {

	public static void main(String[] args) {
		try {
			ForemanHelper.createProject(ForemanHelper.URL, ForemanHelper.USERNAME, ForemanHelper.PASSWORD, "test create ksf project 1", "testksf");
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
