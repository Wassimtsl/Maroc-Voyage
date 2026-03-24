package com.example.demo.service;

import com.example.demo.dto.AdresseDto;
import com.example.demo.dto.EvenementDto;
import com.example.demo.dto.GuideDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.TarifDto;
import com.example.demo.dto.TypeEvenementDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entities.Guide;
import com.example.demo.entities.Reservation;
import com.example.demo.repository.EvenementRepository;
import com.example.demo.repository.GuideRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final EvenementRepository evenementRepository;
    private final UserRepository userRepository;
    private final GuideRepository guideRepository;
    private final ModelMapper modelMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  EvenementRepository evenementRepository,
                                  UserRepository userRepository,
                                  GuideRepository guideRepository,
                                  ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.evenementRepository = evenementRepository;
        this.userRepository = userRepository;
        this.guideRepository = guideRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Reservation convertDtoToEntity(ReservationDto dto) {
        return modelMapper.map(dto, Reservation.class);
    }

    @Override
    public ReservationDto convertEntityToDto(Reservation r) {
        ReservationDto dto = new ReservationDto();
        dto.setId(r.getId());
        dto.setNombrePersonne(r.getNombrePersonne());
        dto.setDateReservation(r.getDateReservation());
        dto.setDateAnnulation(r.getDateAnnulation());
        dto.setStatut(r.getStatut());
        if (r.getUser() != null) dto.setUserId(r.getUser().getId());

        if (r.getEvenement() != null) {
            var e = r.getEvenement();
            EvenementDto eDto = new EvenementDto();
            eDto.setId(e.getId());
            eDto.setTitreEvent(e.getTitreEvent());
            eDto.setDescription(e.getDescription());
            eDto.setImage(e.getImage());
            eDto.setDateDebut(e.getDateDebut());
            eDto.setDateFin(e.getDateFin());
            if (e.getAdresse() != null) {
                AdresseDto a = new AdresseDto();
                a.setId(e.getAdresse().getId());
                a.setVille(e.getAdresse().getVille());
                a.setPays(e.getAdresse().getPays());
                a.setAdresse(e.getAdresse().getAdresse());
                a.setCodePostal(e.getAdresse().getCodePostal());
                eDto.setAdresse(a);
            }
            if (e.getTarif() != null) {
                TarifDto t = new TarifDto();
                t.setId(e.getTarif().getId());
                t.setPrix(e.getTarif().getPrix());
                t.setPromotion(e.getTarif().getPromotion());
                eDto.setTarif(t);
            }
            if (e.getTypeEvenement() != null) {
                eDto.setTypesEvenement(e.getTypeEvenement().stream()
                    .map(te -> new TypeEvenementDto(te.getId(), te.getLibelleType()))
                    .collect(Collectors.toList()));
            }
            dto.setEvenement(eDto);
        }
        if (r.getGuide() != null) {
            Guide g = r.getGuide();
            GuideDto gDto = new GuideDto();
            gDto.setId(g.getId());
            gDto.setTarif(g.getTarif());
            gDto.setDisponibilite(g.getDisponibilite());
            if (g.getUser() != null) {
                UserDto uDto = new UserDto();
                uDto.setId(g.getUser().getId());
                uDto.setNom(g.getUser().getNom());
                uDto.setPrenom(g.getUser().getPrenom());
                uDto.setEmail(g.getUser().getEmail());
                gDto.setUser(uDto);
            }
            dto.setGuideId(g.getId());
            dto.setGuide(gDto);
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDto findById(Long id) {
        Reservation entity = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation id " + id + " introuvable"));
        return convertEntityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public ReservationDto save(ReservationDto dto) {
        Reservation entity = new Reservation();
        entity.setNombrePersonne(dto.getNombrePersonne());
        entity.setStatut("EN_ATTENTE");
        entity.setDateReservation(LocalDateTime.now());

        if (dto.getEventId() != null) {
            entity.setEvenement(evenementRepository.findById(dto.getEventId())
                .orElseThrow(() -> new NoSuchElementException("Evenement id " + dto.getEventId() + " introuvable")));
        }
        if (dto.getUserId() != null) {
            entity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User id " + dto.getUserId() + " introuvable")));
        }
        if (dto.getGuideId() != null) {
            entity.setGuide(guideRepository.findById(dto.getGuideId())
                .orElseThrow(() -> new NoSuchElementException("Guide id " + dto.getGuideId() + " introuvable")));
        }

        Reservation saved = reservationRepository.save(entity);
        return convertEntityToDto(saved);
    }

    @Override
    public ReservationDto update(Long id, ReservationDto dto) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation id " + id + " introuvable"));
        Long keepId = existing.getId();
        modelMapper.map(dto, existing);
        existing.setId(keepId);
        return convertEntityToDto(reservationRepository.save(existing));
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id))
            throw new NoSuchElementException("Reservation id " + id + " introuvable");
        reservationRepository.deleteById(id);
    }

    @Override
    public List<ReservationDto> findByUserId(Long id) {
        return reservationRepository.findByUserId(id).stream()
                .map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> findByEvenementId(Long id) {
        return reservationRepository.findByEvenementId(id).stream()
                .map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> findByStatut(String statut) {
        return reservationRepository.findByStatut(statut).stream()
                .map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public ReservationDto confirmer(Long id) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation introuvable"));
        r.setStatut("CONFIRMEE");
        reservationRepository.save(r);
        return convertEntityToDto(r);
    }

    @Override
    public ReservationDto annuler(Long id) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation introuvable"));
        r.setStatut("ANNULEE");
        r.setDateAnnulation(LocalDateTime.now());
        reservationRepository.save(r);
        return convertEntityToDto(r);
    }
}
