package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.util.Set;

public class UserDto {
    
    private Long id; 
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit faire entre 2 et 50 caractères")
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50)
    private String prenom;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    private String email;
    
    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Numéro de téléphone invalide")
    private String numTel;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères")
    private String password;

    @JsonProperty("verifEmail")
    private Boolean verifMail;
    
    // ✅ Champs GUIDE intégrés (null si non-guide)
    @Min(value = 0, message = "Le tarif ne peut pas être négatif")
    private Double tarifGuide;
    private Boolean disponibilite;
    private RoleDto role;
    private AdresseDto adresse;
    private Set<LangueDto> langues;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getVerifMail() {
		return verifMail;
	}
	public void setVerifEmail(Boolean verifEmail) {
		this.verifMail = verifEmail;
	}
	public Boolean getDisponibilite() {
		return disponibilite;
	}
	public void setDisponibilite(Boolean disponibilite) {
		this.disponibilite = disponibilite;
	}
	public RoleDto getRole() {
		return role;
	}
	public void setRole(RoleDto role) {
		this.role = role;
	}
	public AdresseDto getAdresse() {
		return adresse;
	}
	public void setAdresse(AdresseDto adresse) {
		this.adresse = adresse;
	}
	public Set<LangueDto> getLangues() {
		return langues;
	}
	public void setLangues(Set<LangueDto> langues) {
		this.langues = langues;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNom() {
	 return this.nom ;
	
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
	public Double getTarifGuide() {
		return tarifGuide;
	}
	public void setTarifGuide(Double tarifGuide) {
		this.tarifGuide = tarifGuide;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
	
}
