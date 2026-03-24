package com.example.demo.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User {

    // Champs communs à tous les utilisateurs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom, prenom, numTel, password;
@Column(unique = true, nullable = false) 
    private String email; // ✅ Validation email unique   

    private Boolean verifMail;  // ✅ Validation email

    // Relations
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;           // ManyToOne

    @ManyToOne
    private Adresse adresse;     // ManyToOne

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "users_langue",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "langue_id")
    )
    private Set<Langue> langues; // ManyToMany

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Evenement> evenements;

    // Getters / Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getVerifMail() {
        return verifMail;
    }

    public void setVerifMail(Boolean verifEmail) {
        this.verifMail = verifEmail;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Set<Langue> getLangues() {
        return langues;
    }

    public void setLangues(Set<Langue> langues) {
        this.langues = langues;
    }

    
    public Set<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(Set<Evenement> evenements) {
        this.evenements = evenements;
    }
}
