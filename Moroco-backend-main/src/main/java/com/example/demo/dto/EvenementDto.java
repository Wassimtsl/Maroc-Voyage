package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EvenementDto {
    // Champs de base
    private Long id;
    private String titreEvent;
    private String description;
    private String image;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    // Objets imbriqués (réponse GET)
    private AdresseDto adresse;
    private TarifDto tarif;
    private List<TypeEvenementDto> typesEvenement;

    // Champs d'entrée pour création/modification (POST/PUT)
    private Long typeEvenementId;
    private Long tarifId;
    private Long adresseId;
    private Long userId;
    private Long guideId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitreEvent() { return titreEvent; }
    public void setTitreEvent(String titreEvent) { this.titreEvent = titreEvent; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }

    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }

    public AdresseDto getAdresse() { return adresse; }
    public void setAdresse(AdresseDto adresse) { this.adresse = adresse; }

    public TarifDto getTarif() { return tarif; }
    public void setTarif(TarifDto tarif) { this.tarif = tarif; }

    public List<TypeEvenementDto> getTypesEvenement() { return typesEvenement; }
    public void setTypesEvenement(List<TypeEvenementDto> typesEvenement) { this.typesEvenement = typesEvenement; }

    public Long getTypeEvenementId() { return typeEvenementId; }
    public void setTypeEvenementId(Long typeEvenementId) { this.typeEvenementId = typeEvenementId; }

    public Long getTarifId() { return tarifId; }
    public void setTarifId(Long tarifId) { this.tarifId = tarifId; }

    public Long getAdresseId() { return adresseId; }
    public void setAdresseId(Long adresseId) { this.adresseId = adresseId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getGuideId() { return guideId; }
    public void setGuideId(Long guideId) { this.guideId = guideId; }
}
