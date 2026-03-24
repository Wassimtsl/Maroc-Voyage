package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeEvenementDto {

    @JsonProperty("idTypeEvenement")
    private Long id;
    
    private String libelleType;

    public TypeEvenementDto() {}

    public TypeEvenementDto(Long id, String libelleType) {
        this.id = id;
        this.libelleType = libelleType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelleType() { return libelleType; }
    public void setLibelleType(String libelleType) { this.libelleType = libelleType; }
}
