package com.example.demo.entities;



import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "typeevenement_")
public class TypeEvenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_typeevenement")
    private Long id;

    @Column(name = "libelle_type", nullable = false, unique = true)
    private String libelleType;
    
    @ManyToMany(mappedBy = "typeEvenements")
    private List<Evenement> evenements;

    // ---- Getters & Setters ----
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleType() {
        return libelleType;
    }

    public void setLibelleType(String libelleType) {
        this.libelleType = libelleType;
    }

    // ---- toString() ----
    @Override
    public String toString() {
        return "TypeEvenement{" +
                "id=" + id +
                ", libelleType='" + libelleType + '\'' +
                '}';
    }
}

