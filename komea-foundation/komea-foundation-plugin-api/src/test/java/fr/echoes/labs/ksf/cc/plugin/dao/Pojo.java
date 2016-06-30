package fr.echoes.labs.ksf.cc.plugin.dao;

public class Pojo {

	public String	key;
	public String	name;
	public Integer	id;

	public Pojo() {

	}

	public Integer getId() {
		return this.id;
	}

	public String getKey() {
		return this.key;
	}

	public String getName() {
		return this.name;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
