package com.tocea.corolla.products.utils;

import java.text.Normalizer;

public class EntityKeyGenerator implements IEntityKeyGenerator {

    private String name;

    public EntityKeyGenerator(String name) {
        this.name = name;
    }

    @Override
    public String generate() {
        return Normalizer.normalize(this.name, Normalizer.Form.NFD).replaceAll("[^\\w]", "_").toLowerCase();
    }

}
