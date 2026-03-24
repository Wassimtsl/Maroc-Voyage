package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.JoinColumn;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Evenement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titreEvent, description, image;
    private LocalDateTime dateDebut, dateFin;

    // Relations
    @ManyToMany
    @JoinTable(
        name = "evenement_typeevenement",
        joinColumns = @JoinColumn(name = "evenement_id"),
        inverseJoinColumns = @JoinColumn(name = "typeevenement_id")
    )
    private List<TypeEvenement> typeEvenements;
    
    


    
    @ManyToOne
    private Tarif tarif;       
    @ManyToOne
    private Adresse adresse;  

    @ManyToMany
    @JoinTable(
        name = "evenement_user",
        joinColumns = @JoinColumn(name = "evenement_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;
    
    @ManyToOne
    private Guide guide;
    
    
	public List<TypeEvenement> getTypeEvenement() {
		return typeEvenements;
	}
	public void setTypeEvenement(List<TypeEvenement> typeEvenements) {
		this.typeEvenements = typeEvenements;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public Tarif getTarif() {
		return tarif;
	}
	public void setTarif(Tarif tarif) {
		this.tarif = tarif;
	}
	public Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	public LocalDateTime getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(LocalDateTime dateDebut) {
		this.dateDebut = dateDebut;
	}
	public LocalDateTime getDateFin() {
		return dateFin;
	}
	public void setDateFin(LocalDateTime dateFin) {
		this.dateFin = dateFin;
	}
	public String getTitreEvent() {
		return titreEvent;
	}
	public void setTitreEvent(String titreEvent) {
		this.titreEvent = titreEvent;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Guide getGuide() {
		return guide;
	}
	public void setGuide(Guide guide) {
		this.guide = guide;
	}        
    
    
  // ManyToOne (créateur)
}
