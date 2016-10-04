package fr.echoes.labs.ksf.foreman.api.model;

public class ForemanHostGroup {

	private Integer id;
	private String name;
	
	public ForemanHostGroup() {
		// default constructor
	}
	
	public ForemanHostGroup(final Integer id, final String name) {
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "{ id="+id+", name="+name+"}";
	}
}
