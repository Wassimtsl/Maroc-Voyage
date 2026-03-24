package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Tarif;

public interface TarifRepository extends JpaRepository<Tarif, Long> {}
