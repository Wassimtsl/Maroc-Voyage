package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class StatutReservationDto {
    
    private Long id;
    
    @NotBlank(message = "Le code statut est obligatoire")
    @Pattern(regexp = "^[A-Z_]+$", message = "Le code doit être en majuscules avec underscores")
    private String codeStatut;
    
    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 100)
    private String libelleStatut;
    
    @Size(max = 255)
    private String description;
    
    private Integer ordreAffichage;
    
    private Boolean isActif = true;
    
    private Boolean annulable = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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