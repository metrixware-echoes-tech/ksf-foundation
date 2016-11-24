package fr.echoes.labs.ksf.foreman.api.model;

public class SmartVariableWrapper {

	private String variable;
	private String puppetClass;
	private String puppetModule;
	private String type;
	private String value;
	
	public SmartVariableWrapper() {
		// default constructor
	}
	
	public SmartVariableWrapper(final SmartVariable smartVariable, final PuppetClass puppetClass) {
		this.variable = smartVariable.getVariable();
		this.type = smartVariable.getType();
		this.value = smartVariable.getDefaultValue();
		if (puppetClass != null) {
			this.puppetClass = puppetClass.getName();
			this.puppetModule = puppetClass.getModuleName();
		}
	}
	
	public SmartVariableWrapper(final SmartVariable smartVariable, final PuppetClass puppetClass, final String value) {
		this(smartVariable, puppetClass);
		this.value = value;
	}

	public String getVariable() {
		return variable;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public String getPuppetClass() {
		return puppetClass;
	}

	public void setPuppetClass(String puppetClass) {
		this.puppetClass = puppetClass;
	}
	
	public String getPuppetModule() {
		return puppetModule;
	}
	
	public void setPuppetModule(String puppetModule) {
		this.puppetModule = puppetModule;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
