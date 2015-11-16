package fr.echoes.lab.util;


import java.util.Map;


public interface IExternalProcessLauncher {

    /**
     * Execute the process.
     *
     * @return the process result.
     * @throws ExternalProcessLauncherException
     */
    public IProcessLaunchResult launchSync() throws ExternalProcessLauncherException;

    /**
     * Execute the process.
     *
     * @param redirectErrorStream if <code>true</code> any error output generated will be merged with the standard output.
     * @return the process result.
     * @throws ExternalProcessLauncherException
     */
    public IProcessLaunchResult launchSync(boolean redirectErrorStream) throws ExternalProcessLauncherException;

    /**
     * Returns a string map of this process environment.<br/>
     * This map can be used to modify this process environment.
     *
     * @return the process environment as a string map.
     */
    public Map<String, String> getEnvironements();

    /**
     * Sets the process working directory.
     *
     * @param directory the working directory.
     */
    public void setWorkingDirectory(String directory);

}
