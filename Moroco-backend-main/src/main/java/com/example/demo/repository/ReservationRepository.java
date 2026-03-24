package com.example.demo.repository; 

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.ReservationDto;
import com.example.demo.entities.Reservation;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByUserId(Long id);

	List<Reservation> findByEvenementId(Long id);

	List<Reservation> findByStatut(String statut);
	
}
