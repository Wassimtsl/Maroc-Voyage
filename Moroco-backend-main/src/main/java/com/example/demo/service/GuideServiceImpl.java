package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.GuideDto;
import com.example.demo.dto.LangueDto;
import com.example.demo.dto.RoleDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entities.Guide;
import com.example.demo.repository.GuideRepository;

import jakarta.validation.Valid;

@Service
@Transactional
public class GuideServiceImpl implements GuideService {

	private final GuideRepository guideRepository;

	public GuideServiceImpl(GuideRepository guideRepository) {
		this.guideRepository = guideRepository;
	}

	@Override
	public Guide convertDtoToEntity(GuideDto dto) {
		Guide g = new Guide();
		g.setId(dto.getId());
		g.setTarif(dto.getTarif());
		g.setDisponibilite(dto.getDisponibilite());
		return g;
	}

	@Override
	public GuideDto convertEntityToDto(Guide entity) {
		GuideDto dto = new GuideDto();
		dto.setId(entity.getId());
		dto.setTarif(entity.getTarif());
		dto.setDisponibilite(entity.getDisponibilite());

		if (entity.getUser() != null) {
			com.example.demo.entities.User u = entity.getUser();
			UserDto userDto = new UserDto();
			userDto.setId(u.getId());
			userDto.setNom(u.getNom());
			userDto.setPrenom(u.getPrenom());
			userDto.setEmail(u.getEmail());
			userDto.setNumTel(u.getNumTel());
			userDto.setVerifEmail(u.getVerifMail());

			if (u.getRole() != null) {
				RoleDto roleDto = new RoleDto();
				String nom = u.getRole().getNom();
				roleDto.setLibelleRole(nom != null && !nom.startsWith("ROLE_") ? "ROLE_" + nom : nom);
				userDto.setRole(roleDto);
			}

			if (u.getLangues() != null) {
				java.util.Set<LangueDto> langues = u.getLangues().stream().map(l -> {
					LangueDto ld = new LangueDto();
					ld.setId(l.getId());
					ld.setNomLangue(l.getNomLangue());
					return ld;
				}).collect(java.util.stream.Collectors.toSet());
				userDto.setLangues(langues);
			}

			dto.setUser(userDto);
		}

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GuideDto> findAll() {
		return guideRepository.findAll()
				.stream()
				.map(this::convertEntityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public GuideDto findById(Long id) {
		Guide entity = guideRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Guide id " + id + " introuvable"));
		return convertEntityToDto(entity);
	}

	@Override
	public GuideDto save(@Valid GuideDto guideDto) {
		return this.convertEntityToDto(guideRepository.save(this.convertDtoToEntity(guideDto)));
	}

	@Override
	public GuideDto update(@Valid GuideDto guideDto) {
		return this.convertEntityToDto(guideRepository.save(this.convertDtoToEntity(guideDto)));
	}

	@Override
	public void deleteById(Long id) {
		guideRepository.deleteById(id);
	}
}
