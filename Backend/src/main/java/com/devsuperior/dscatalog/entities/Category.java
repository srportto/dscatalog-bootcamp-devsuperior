package com.devsuperior.dscatalog.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category implements Serializable {
    private static final long serialVerionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;
    private String name;

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
