package fr.echoes.lab.foremanapi.util;


import java.util.List;


/**
 * @author Metrixware R&D Team
 *
 */
public class ProcessLaunchResult implements IProcessLaunchResult {

	private Integer exitValue;
	private List<String> inputStreamLines = null;
	private List<String> errorStreamLines = null;
	private String commandLine;

	@Override
	public Integer getExitValue() {
		return this.exitValue;
	}

	@Override
	public List<String> getInputStreamLines() {
		return this.inputStreamLines;
	}

	@Override
	public List<String> getErrorStreamLines() {
		return this.errorStreamLines;
	}

	void setExitValue(Integer exitValue) {
		this.exitValue = exitValue;
	}

	void setErrorStream(List<String> streamErrorLines) {
		this.errorStreamLines = streamErrorLines;
	}

	void setInputStream(List<String> streamInputLines) {
		this.inputStreamLines = streamInputLines;
	}

}
