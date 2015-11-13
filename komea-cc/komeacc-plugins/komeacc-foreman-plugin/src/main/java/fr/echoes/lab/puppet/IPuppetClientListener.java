package fr.echoes.lab.puppet;

public interface IPuppetClientListener {

	void moduleInstalled(String name, String version, String environment);
}
