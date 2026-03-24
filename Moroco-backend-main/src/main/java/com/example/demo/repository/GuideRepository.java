package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.GuideDto;
import com.example.demo.entities.Guide;

public interface GuideRepository extends JpaRepository<Guide, Long>  {


}
