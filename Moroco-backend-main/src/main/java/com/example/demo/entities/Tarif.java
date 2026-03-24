package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
public class Tarif {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double prix;
	private Double promotion;

//constructor
public Tarif() {};

public Tarif(Double prix,Double promotion) {
	this.prix = prix;
	this.promotion = promotion;
}
//Getter Setter
public Double getPrix() {
	return prix;
}
public void setPrix(Double prix) {
	this.prix = prix;
}

public Double getPromotion() {
	return promotion;
}
public void setPromotion(Double promotion) {
	this.promotion = promotion;
}

public Long getId() {
	return id;
}
public void setId(Long Id) {
	this.id = Id;
}
@Override
public String toString() {
    return "Tarif{" +
            "id=" + id +
            ", prix=" + prix +
            ", promotion=" + promotion +
            '}';

}
}
	


