package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ReservationDto {

    private Long id;

    private Long eventId;
    private Long userId;
    private Long statutId;

    @NotNull(message = "Le nombre de personnes est obligatoire")
    @Min(value = 1, message = "Au moins 1 personne requise")
    @Max(value = 100, message = "Maximum 100 personnes")
    private Integer nombrePersonne;

    private LocalDateTime dateReservation;
    private LocalDateTime dateAnnulation;

    // Statut en string direct (EN_ATTENTE, CONFIRMEE, ANNULEE)
    private String statut;

    // Objet evenement complet pour affichage
    private EvenementDto evenement;

    // Guide optionnel
    private Long guideId;
    private GuideDto guide;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getStatutId() { return statutId; }
    public void setStatutId(Long statutId) { this.statutId = statutId; }

    public Integer getNombrePersonne() { return nombrePersonne; }
    public void setNombrePersonne(Integer nombrePersonne) { this.nombrePersonne = nombrePersonne; }

    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }

    public LocalDateTime getDateAnnulation() { return dateAnnulation; }
    public void setDateAnnulation(LocalDateTime dateAnnulation) { this.dateAnnulation = dateAnnulation; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public EvenementDto getEvenement() { return evenement; }
    public void setEvenement(EvenementDto evenement) { this.evenement = evenement; }

    public Long getGuideId() { return guideId; }
    public void setGuideId(Long guideId) { this.guideId = guideId; }

    public GuideDto getGuide() { return guide; }
    public void setGuide(GuideDto guide) { this.guide = guide; }
}