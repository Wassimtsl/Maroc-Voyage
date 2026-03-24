package com.example.demo.repository; 

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Adresse;


public interface AdresseRepository extends JpaRepository<Adresse, Long> {
	
}

