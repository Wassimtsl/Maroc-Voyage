package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "statut_reservation")
public class StatutReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeStatut;      // EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE
    private String libelleStatut;   // Libellé affiché
    private String description;     // Description détaillée
    private Integer ordreAffichage; // Ordre dans les listes
    private Boolean isActif;        // Statut actif (modifiable)
    private Boolean annulable;      // Peut être annulé
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodeStatut() {
		return codeStatut;
	}
	public void setCodeStatut(String codeStatut) {
		this.codeStatut = codeStatut;
	}
	public String getLibelleStatut() {
		return libelleStatut;
	}
	public void setLibelleStatut(String libelleStatut) {
		this.libelleStatut = libelleStatut;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOrdreAffichage() {
		return ordreAffichage;
	}
	public void setOrdreAffichage(Integer ordreAffichage) {
		this.ordreAffichage = ordreAffichage;
	}
	public Boolean getIsActif() {
		return isActif;
	}
	public void setIsActif(Boolean isActif) {
		this.isActif = isActif;
	}
	public Boolean getAnnulable() {
		return annulable;
	}
	public void setAnnulable(Boolean annulable) {
		this.annulable = annulable;
	}
}
