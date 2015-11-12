package com.tocea.corolla.products.utils;

public class EntityKeyGenerator implements IEntityKeyGenerator {

	private String name;
	
	public EntityKeyGenerator(String name) {
		this.name = name;
	}
	
	@Override
	public String generate() {
		return name.replaceAll("[^a-zA-Z0-9]", "");
	}

}
