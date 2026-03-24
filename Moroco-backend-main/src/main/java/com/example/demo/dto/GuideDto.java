package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuideDto {
	private Long id;
	private Double tarif;

	@JsonProperty("disponible")
	private Boolean disponibilite;

	private UserDto user;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getTarif() {
		return tarif;
	}
	public void setTarif(Double tarif) {
		this.tarif = tarif;
	}
	public Boolean getDisponibilite() {
		return disponibilite;
	}
	public void setDisponibilite(Boolean disponibilite) {
		this.disponibilite = disponibilite;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
}
