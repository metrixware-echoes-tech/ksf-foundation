package fr.echoes.lab.foremanapi.util;


import java.util.List;


/**
 * TODO comment for type .
 *
 * @author Metrixware R&D Team
 */
public interface IProcessLaunchResult {
    /**
     * @return
     */
    public Integer getExitValue();
    /**
     * @return
     */
    public List<String> getInputStreamLines();
    /**
     * @return
     */
    public List<String> getErrorStreamLines();

}
