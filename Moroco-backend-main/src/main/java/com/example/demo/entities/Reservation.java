package com.example.demo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer nombrePersonne;
    private LocalDateTime dateReservation;
    private LocalDateTime dateAnnulation;

    // Relations
    private String statut; // ManyToOne (TABLE)
    
    @ManyToOne
    private Evenement evenement;       // ManyToOne
    
    @ManyToOne
    private User user;                 // ManyToOne

    @ManyToOne
    private Guide guide;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNombrePersonne() {
		return nombrePersonne;
	}
	public void setNombrePersonne(Integer nombrePersonne) {
		this.nombrePersonne = nombrePersonne;
	}
	public LocalDateTime getDateReservation() {
		return dateReservation;
	}
	public void setDateReservation(LocalDateTime dateReservation) {
		this.dateReservation = dateReservation;
	}
	public LocalDateTime getDateAnnulation() {
		return dateAnnulation;
	}
	public void setDateAnnulation(LocalDateTime dateAnnulation) {
		this.dateAnnulation = dateAnnulation;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public Evenement getEvenement() {
		return evenement;
	}
	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Guide getGuide() {
		return guide;
	}
	public void setGuide(Guide guide) {
		this.guide = guide;
	}
}