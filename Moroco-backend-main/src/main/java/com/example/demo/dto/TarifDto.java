package com.example.demo.dto;

public class TarifDto {
	private Long Id;
	private Double prix;
	private Double promotion;
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public Double getPromotion() {
		return promotion;
	}
	public void setPromotion(Double promotion) {
		this.promotion = promotion;
	}
	public Double getPrix() {
		return prix;
	}
	public void setPrix(Double prix) {
		this.prix = prix;
	}
}
