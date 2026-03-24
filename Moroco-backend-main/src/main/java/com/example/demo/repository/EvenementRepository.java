package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Evenement;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    Optional<Evenement> findById(Long id);

    List<Evenement> findByTypeEvenementsId(Long id);
    List<Evenement> findByUsersId(Long id);

   
    List<Evenement> findByDateDebutBetween(LocalDateTime debut, LocalDateTime fin);
}
