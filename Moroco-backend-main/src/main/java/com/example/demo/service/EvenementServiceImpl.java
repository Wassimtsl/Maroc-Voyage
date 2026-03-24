package com.example.demo.service;

import com.example.demo.dto.AdresseDto;
import com.example.demo.dto.EvenementDto;
import com.example.demo.dto.TarifDto;
import com.example.demo.dto.TypeEvenementDto;
import com.example.demo.entities.Evenement;
import com.example.demo.entities.TypeEvenement;
import com.example.demo.repository.EvenementRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class EvenementServiceImpl implements EvenementService {

    private final EvenementRepository evenementRepository;
    private final ModelMapper modelMapper;

    public EvenementServiceImpl(EvenementRepository evenementRepository, ModelMapper modelMapper) {
        this.evenementRepository = evenementRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Evenement convertDtoToEntity(EvenementDto dto) {
        return modelMapper.map(dto, Evenement.class);
    }

    @Override
    public EvenementDto convertEntityToDto(Evenement e) {
        EvenementDto dto = new EvenementDto();
        dto.setId(e.getId());
        dto.setTitreEvent(e.getTitreEvent());
        dto.setDescription(e.getDescription());
        dto.setImage(e.getImage());
        dto.setDateDebut(e.getDateDebut());
        dto.setDateFin(e.getDateFin());

        // Adresse
        if (e.getAdresse() != null) {
            AdresseDto a = new AdresseDto();
            a.setId(e.getAdresse().getId());
            a.setAdresse(e.getAdresse().getAdresse());
            a.setVille(e.getAdresse().getVille());
            a.setPays(e.getAdresse().getPays());
            a.setCodePostal(e.getAdresse().getCodePostal());
            dto.setAdresse(a);
        }

        // Tarif
        if (e.getTarif() != null) {
            TarifDto t = new TarifDto();
            t.setId(e.getTarif().getId());
            t.setPrix(e.getTarif().getPrix());
            t.setPromotion(e.getTarif().getPromotion());
            dto.setTarif(t);
        }

        // Types
        if (e.getTypeEvenement() != null) {
            List<TypeEvenementDto> types = e.getTypeEvenement().stream()
                .map(te -> new TypeEvenementDto(te.getId(), te.getLibelleType()))
                .collect(Collectors.toList());
            dto.setTypesEvenement(types);
        }

        // Guide id
        if (e.getGuide() != null) {
            dto.setGuideId(e.getGuide().getId());
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public EvenementDto findById(Long id) {
        Evenement entity = evenementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evenement id " + id + " introuvable"));
        return convertEntityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementDto> findAll() {
        return evenementRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvenementDto save(EvenementDto dto) {
        Evenement toSave = convertDtoToEntity(dto);
        Evenement saved = evenementRepository.save(toSave);
        return convertEntityToDto(saved);
    }

    @Override
    public EvenementDto update(Long id, EvenementDto dto) {
        Evenement existing = evenementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evenement id " + id + " introuvable"));
        Long keepId = existing.getId();
        modelMapper.map(dto, existing);
        existing.setId(keepId);
        Evenement updated = evenementRepository.save(existing);
        return convertEntityToDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!evenementRepository.existsById(id)) {
            throw new NoSuchElementException("Evenement id " + id + " introuvable");
        }
        evenementRepository.deleteById(id);
    }

    @Override
    public List<EvenementDto> findByTypeEvenementId(Long id) {
        return evenementRepository.findByTypeEvenementsId(id).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvenementDto> findByUserId(Long id) {
        return evenementRepository.findByUsersId(id).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementDto> findByDateDebutBetween(LocalDateTime debut, LocalDateTime fin) {
        return evenementRepository.findByDateDebutBetween(debut, fin).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
}
