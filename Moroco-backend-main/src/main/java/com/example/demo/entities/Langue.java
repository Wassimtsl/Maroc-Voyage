package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Langue {
	private String nomLangue;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany(mappedBy="langues")
	private List<User> users;
	
//constructor
public Langue() {};

public Langue(String nomLangue,String Id) {

	this.nomLangue = nomLangue;
}


//Getter Setter
public String getNomLangue() {
	return nomLangue;
}

public void setNomLangue(String nomLangue) {
	this.nomLangue = nomLangue;
}

public Long getId() { return id; }


public void setId(Long id) { this.id = id; }



//Function 
@Override
public String toString() {
	return "Role{ " + nomLangue + "}";
}
}

