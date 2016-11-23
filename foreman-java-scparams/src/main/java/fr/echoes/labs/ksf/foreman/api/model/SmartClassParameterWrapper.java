package fr.echoes.labs.ksf.foreman.api.model;

public class SmartClassParameterWrapper {

	private String puppetModule;
	private String puppetClass;
	private String type;
	private String parameter;
	private String value;
	private Boolean usePuppetDefault;
	
	public SmartClassParameterWrapper() {
		// default constructor
	}
	
	public SmartClassParameterWrapper(final SmartClassParameter param) {
		this(param.getParameter(), param.getType(), param.getPuppetClass(), param.getDefaultValue(), param.isUsePuppetDefault());
	}
	
	public SmartClassParameterWrapper(final SmartClassParameter param, final SmartClassParameterOverrideValue value) {
		this(param.getParameter(), param.getType(), param.getPuppetClass(), value.getValue(), value.getUsePuppetDefault());	
	}
	
	public SmartClassParameterWrapper(final String parameter, final String type, final PuppetClass puppetClass, final String value, final Boolean usePuppetDefault) {
		this.parameter = parameter;
		this.type = type;
		this.puppetClass = puppetClass.getName();
		this.puppetModule = puppetClass.getModuleName();
		this.value = value;
		this.usePuppetDefault = usePuppetDefault;
	}
	
	public String getPuppetModule() {
		return puppetModule;
	}
	
	public void setPuppetModule(String puppetModule) {
		this.puppetModule = puppetModule;
	}
	
	public String getPuppetClass() {
		return puppetClass;
	}
	
	public void setPuppetClass(String puppetClass) {
		this.puppetClass = puppetClass;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Boolean getUsePuppetDefault() {
		return usePuppetDefault;
	}
	
	public void setUsePuppetDefault(Boolean usePuppetDefault) {
		this.usePuppetDefault = usePuppetDefault;
	}
	
	public void setUsePuppetDefault(String usePuppetDefault) {
		this.usePuppetDefault = Boolean.valueOf(usePuppetDefault);
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {		
		return new StringBuilder("{ ")
			.append("puppetModule: ").append(this.puppetModule)
			.append(", puppetClass: ").append(this.puppetClass)
			.append(", type: ").append(this.type)
			.append(", parameter: ").append(this.parameter)
			.append(", value: ").append(this.value)
			.append(", usePuppetDefault: ").append(this.usePuppetDefault)
			.append(" }")
			.toString();
	}
	
}
