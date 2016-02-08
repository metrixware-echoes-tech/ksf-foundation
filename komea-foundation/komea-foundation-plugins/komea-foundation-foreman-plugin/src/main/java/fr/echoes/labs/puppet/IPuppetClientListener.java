package fr.echoes.labs.puppet;

public interface IPuppetClientListener {

	void moduleInstalled(String name, String version, String environment);
}
