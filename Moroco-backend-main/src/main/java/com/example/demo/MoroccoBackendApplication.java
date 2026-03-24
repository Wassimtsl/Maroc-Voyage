package com.example.demo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.*;
import com.example.demo.repository.*;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.demo" })
@EntityScan(basePackages = { "com.example.demo.entities" })
@EnableJpaRepositories(basePackages = { "com.example.demo.repository" })
public class MoroccoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoroccoBackendApplication.class, args);
    }

    @Bean
    public org.modelmapper.ModelMapper modelMapper() {
        org.modelmapper.ModelMapper mapper = new org.modelmapper.ModelMapper();
        // Map Role.nom -> RoleDto.libelleRole with "ROLE_" prefix
        mapper.typeMap(com.example.demo.entities.Role.class, com.example.demo.dto.RoleDto.class)
            .addMappings(m -> m.using(
                ctx -> {
                    String nom = (String) ctx.getSource();
                    if (nom == null) return null;
                    return nom.startsWith("ROLE_") ? nom : "ROLE_" + nom;
                })
                .map(com.example.demo.entities.Role::getNom,
                     com.example.demo.dto.RoleDto::setLibelleRole)
            );
        return mapper;
    }


    /**
     * Seed de données de test
     */
    @Bean
    public org.springframework.boot.CommandLineRunner initData(
            AdresseRepository adresseRepository,
            RoleRepository roleRepository,
            LangueRepository langueRepository,
            UserRepository userRepository,
            TarifRepository tarifRepository,
            GuideRepository guideRepository,
            TypeEvenementRepository typeEvenementRepository,
            EvenementRepository evenementRepository,
            ReservationRepository reservationRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // ── Fix old placeholder images ──────────────────────────────────────
            String[] unsplashImages = {
                "https://images.unsplash.com/photo-1489749798305-4fea3ae63d43?w=600",
                "https://images.unsplash.com/photo-1511860810434-a92f84c6f01e?w=600",
                "https://images.unsplash.com/photo-1548690596-f1722c190938?w=600",
                "https://images.unsplash.com/photo-1502680390469-be75c86b636f?w=600",
                "https://images.unsplash.com/photo-1541518763669-27fef04b14ea?w=600",
                "https://images.unsplash.com/photo-1553522991-71c5a5c4b7cc?w=600",
                "https://images.unsplash.com/photo-1539020140153-e479b8c22e70?w=600"
            };
            java.util.List<com.example.demo.entities.Evenement> toFix = evenementRepository.findAll().stream()
                .filter(ev -> ev.getImage() == null
                    || ev.getImage().isEmpty()
                    || ev.getImage().startsWith("image"))
                .collect(java.util.stream.Collectors.toList());
            for (int idx = 0; idx < toFix.size(); idx++) {
                toFix.get(idx).setImage(unsplashImages[idx % unsplashImages.length]);
                evenementRepository.save(toFix.get(idx));
            }
            if (!toFix.isEmpty()) {
                System.out.println("*** Fixed " + toFix.size() + " event images ***");
            }
            // ────────────────────────────────────────────────────────────────────

            // Pour éviter de reseeder à chaque démarrage
            if (userRepository.count() > 0 || evenementRepository.count() > 0) {
                return;
            }

            Random random = new Random();

            // --------- ROLES ----------
            Role adminRole = new Role();
            adminRole.setNom("ADMIN");

            Role guideRole = new Role();
            guideRole.setNom("GUIDE");

            Role touristeRole = new Role();
            touristeRole.setNom("TOURISTE");


            roleRepository.saveAll(Arrays.asList(adminRole, guideRole, touristeRole));

            // --------- LANGUES ----------
            Langue fr = new Langue();
            fr.setNomLangue("Français");
            Langue en = new Langue();
            en.setNomLangue("Anglais");
            Langue es = new Langue();
            es.setNomLangue("Espagnol");
            Langue ar = new Langue();
            ar.setNomLangue("Arabe");
            Langue de = new Langue();
            de.setNomLangue("Allemand");

            List<Langue> langues = langueRepository.saveAll(Arrays.asList(fr, en, es, ar, de));

            // --------- ADRESSES ----------
            String[] villes = {"Marrakech","Casablanca","Fès","Rabat","Chefchaouen","Essaouira","Agadir","Meknès","Tanger","Ouarzazate"};
            String[] rues = {"Avenue Mohammed V","Rue de la Médina","Boulevard Hassan II","Derb Sidi Bouloukat","Rue des Épices","Avenue des FAR","Rue Souk El Had","Boulevard Moulay Ismail","Rue Bab Doukkala","Avenue du Prince"};
            List<Adresse> adresses = IntStream.range(0, 15)
                    .mapToObj(i -> {
                        Adresse a = new Adresse();
                        a.setAdresse(rues[i % rues.length]);
                        a.setCodePostal(String.valueOf(10000 + i * 1000));
                        a.setPays("Maroc");
                        a.setVille(villes[i % villes.length]);
                        return a;
                    })
                    .map(adresseRepository::save)
                    .collect(Collectors.toList());

            // --------- TARI FS ----------
            double[] prixList = {80,120,150,200,250,180,300,90,160,220,350,75,130,170,280,100,140,190,240,320};
            List<Tarif> tarifs = IntStream.range(0, 20)
                    .mapToObj(i -> {
                        Tarif t = new Tarif();
                        t.setPrix(prixList[i]);
                        t.setPromotion((i % 4 == 0) ? Math.round(prixList[i] * 0.15) : 0.0);
                        return t;
                    })
                    .map(tarifRepository::save)
                    .collect(Collectors.toList());

            // --------- GUIDES ----------
            // guides are linked to GUIDE-role users (indices 3..9 in the users list)
            // We create users first, so the guide section comes AFTER users.
            // But users are created below; we'll link after. Using a post-step:
            List<Guide> guides = IntStream.range(0, 10)
                    .mapToObj(i -> {
                        Guide g = new Guide();
                        g.setTarif(200.0 + i * 15);
                        g.setDisponibilite(i % 3 != 0); // most guides available
                        return g;
                    })
                    .map(guideRepository::save)
                    .collect(Collectors.toList());

            // --------- USERS ----------
            String[] noms = {
                "Amrani","Berrada","El Idrissi","Bennani","Tazi","Alaoui","Chraibi","Fassi",
                "Kettani","Lahlou","Mernissi","Naciri","Ouazzani","Rhali","Sefrioui","Tahiri",
                "Benali","Cherkaoui","Doukkali","El Ouafi","Filali","Guerraoui","Hajji","Iraqi",
                "Jabri","Kabbaj","Lamrani","Mansouri","Naji","Ouhabi","Qadiri","Raissouni",
                "Ziani","Bousfiha","Chaabi","Drissi","El Fassi","Guessous","Hafidi","Idrissi"
            };
            String[] prenoms = {
                "Hassan","Fatima","Youssef","Sara","Mohammed","Khadija","Amine","Nadia",
                "Karim","Leila","Omar","Salma","Rachid","Zineb","Tariq","Houda",
                "Khalid","Rim","Ayoub","Samira","Hamza","Nour","Mehdi","Imane",
                "Adil","Wafa","Bilal","Soukaina","Mourad","Hajar","Samir","Btissam",
                "Walid","Dounia","Othmane","Ghita","Iliass","Malak","Zakaria","Loubna"
            };
            List<User> users = IntStream.range(0, 40)
                    .mapToObj(i -> {
                        User u = new User();
                        u.setNom(noms[i % noms.length]);
                        u.setPrenom(prenoms[i % prenoms.length]);
                        u.setEmail(prenoms[i % prenoms.length].toLowerCase().replace(" ",".") + "." + noms[i % noms.length].toLowerCase().replace(" ","-") + "@gmail.com");
                        u.setNumTel("06" + String.format("%08d", 10000000 + i * 7919));
                        u.setPassword(passwordEncoder.encode("Password@" + (i + 1)));
                        u.setVerifMail(i % 2 == 0);

                        // adresse aléatoire
                        u.setAdresse(adresses.get(random.nextInt(adresses.size())));

                        // rôle : quelques admins, quelques guides, beaucoup de touristes
                        if (i < 3) {
                            u.setRole(adminRole);
                        } else if (i < 10) {
                            u.setRole(guideRole);
                        } else {
                            u.setRole(touristeRole);
                        }

                        // langues (1 à 3 langues)
                        Set<Langue> languesUser = new HashSet<>();
                        int nbLangues = 1 + random.nextInt(3);
                        for (int j = 0; j < nbLangues; j++) {
                            languesUser.add(langues.get(random.nextInt(langues.size())));
                        }
                        u.setLangues(languesUser);

                        return u;
                    })
                    .map(userRepository::save)
                    .collect(Collectors.toList());

            // --------- LINK GUIDES → USERS (guide-role users: indices 3..12) ----------
            // users 3..9 have GUIDE role (7 users); guides list has 10 items
            List<User> guideUsers = users.stream()
                    .filter(u -> u.getRole() != null && "GUIDE".equals(u.getRole().getNom()))
                    .collect(Collectors.toList());
            for (int i = 0; i < guides.size(); i++) {
                if (i < guideUsers.size()) {
                    guides.get(i).setUser(guideUsers.get(i));
                    guideRepository.save(guides.get(i));
                }
            }

            // --------- TYPES D'ÉVÉNEMENT ----------
            TypeEvenement randonnee = new TypeEvenement();
            randonnee.setLibelleType("Randonnée");

            TypeEvenement visite = new TypeEvenement();
            visite.setLibelleType("Visite guidée");

            TypeEvenement desert = new TypeEvenement();
            desert.setLibelleType("Excursion désert");

            TypeEvenement gastronomie = new TypeEvenement();
            gastronomie.setLibelleType("Tour gastronomique");

            List<TypeEvenement> typeEvenements = typeEvenementRepository.saveAll(
                    Arrays.asList(randonnee, visite, desert, gastronomie)
            );

            // --------- ÉVÉNEMENTS ----------
            String[] titres = {
                "Randonnée dans les montagnes de l'Atlas",
                "Visite guidée de la médina de Marrakech",
                "Excursion dans les dunes de Merzouga",
                "Tour gastronomique à Fès",
                "Coucher de soleil sur le désert du Sahara",
                "Découverte du souk de Marrakech",
                "Nuit sous les étoiles à Zagora",
                "Balade en dromadaire à Merzouga",
                "Visite des cascades d'Ouzoud",
                "Surf et détente à Taghazout",
                "Randonnée dans la vallée du Dadès",
                "Exploration des gorges du Todra",
                "Séjour culturel à Chefchaouen",
                "Circuit des kasbahs du Sud marocain",
                "Plongée dans la ville bleue de Chefchaouen",
                "Festival Gnaoua d'Essaouira",
                "Tour à cheval dans la palmeraie de Marrakech",
                "Atelier cuisine marocaine à Rabat",
                "Visite du palais royal de Meknès",
                "Journée spa & hammam traditionnel",
                "Trekking au Jbel Toubkal",
                "Découverte de la poterie de Safi",
                "Promenade en bateau à Essaouira",
                "Soirée musique andalouse à Tétouan",
                "Expédition 4x4 dans les pistes du Sahara"
            };
            String[] descriptions = {
                "Une randonnée inoubliable à travers les sommets enneigés du Haut Atlas, entre villages berbères et panoramas à couper le souffle.",
                "Plongez dans l'histoire millénaire de Marrakech en visitant ses palais, mosquées et ruelles mystérieuses avec un guide expert.",
                "Vivez la magie du désert : dunes dorées, coucher de soleil flamboyant et nuit étoilée au cœur de l'erg Chebbi.",
                "Un voyage culinaire au cœur de la gastronomie marocaine : tajines, pastillas et pâtisseries dans les meilleures tables de Fès.",
                "Contemplez l'horizon infini du Sahara depuis une dune, alors que le soleil peint le ciel en orange et rouge.",
                "Partez à la découverte des souks de Marrakech : épices, cuir, artisanat et mille senteurs dans un dédale coloré.",
                "Dormez sous un ciel parsemé d'étoiles, au milieu des dunes silencieuses, dans un camp berbère authentique.",
                "Une promenade en dromadaire au lever du soleil pour vivre une expérience nomade unique au cœur du désert.",
                "Admirez les cascades spectaculaires d'Ouzoud entourées d'oliviers centenaires et de singes barbares en liberté.",
                "Des vagues parfaites pour débutants et confirmés, suivies d'un coucher de soleil sur l'Atlantique à Taghazout.",
                "Explorez la vallée des Roses, ses villages de terre rouge et ses jardins parfumés au cœur du Haut Atlas.",
                "Découvrez les imposantes gorges du Todra, fissure géologique majestueuse de 300 mètres de hauteur.",
                "Séjournez dans la perle bleue du Maroc : ruelles peintes, artisanat local et ambiance apaisante de Chefchaouen.",
                "Un circuit à travers les plus belles kasbahs du Maroc, témoins de la grandeur des dynasties berbères.",
                "Perdez-vous dans les labyrinthes bleus et blancs de Chefchaouen, la ville la plus photographiée du Maroc.",
                "Vibrez au rythme des tam-tams et des Gnaoua lors du célèbre festival international d'Essaouira.",
                "Une balade équestre entre les dattiers et les jardins luxuriants de la palmeraie de Marrakech.",
                "Apprenez les secrets de la cuisine marocaine : sélection des épices, préparation du couscous et du thé à la menthe.",
                "Découvrez la splendeur du Maroc impérial en visitant les monuments historiques de Meknès.",
                "Ressourcez-vous dans un hammam traditionnel suivi d'un soin argan, rituel de beauté ancestral marocain.",
                "Atteignez le point culminant du Maroc à 4167m : une aventure physique et spirituelle inoubliable.",
                "Rencontrez les artisans potiers de Safi et initiez-vous à cet art millénaire transmis de génération en génération.",
                "Naviguez au large des côtes atlantiques d'Essaouira et découvrez ses forteresses depuis la mer.",
                "Une soirée enchantée bercée par les mélodies andalouses et le parfum du jasmin dans les ruelles de Tétouan.",
                "Partez à l'assaut des pistes sauvages du désert en 4x4, entre oasis, bivouacs et rencontres nomades."
            };
            List<Evenement> evenements = IntStream.range(0, 25)
                    .mapToObj(i -> {
                        Evenement e = new Evenement();
                        e.setTitreEvent(titres[i % titres.length]);
                        e.setDescription(descriptions[i % descriptions.length]);
                        // Images réelles Unsplash
                        String[] images = {
                            "https://images.unsplash.com/photo-1489749798305-4fea3ae63d43?w=600",
                            "https://images.unsplash.com/photo-1511860810434-a92f84c6f01e?w=600",
                            "https://images.unsplash.com/photo-1548690596-f1722c190938?w=600",
                            "https://images.unsplash.com/photo-1502680390469-be75c86b636f?w=600",
                            "https://images.unsplash.com/photo-1541518763669-27fef04b14ea?w=600",
                            "https://images.unsplash.com/photo-1553522991-71c5a5c4b7cc?w=600",
                            "https://images.unsplash.com/photo-1539020140153-e479b8c22e70?w=600"
                        };
                        e.setImage(images[i % images.length]);

                        LocalDateTime start = LocalDateTime.now().plusDays(i);
                        e.setDateDebut(start);
                        e.setDateFin(start.plusHours(4));

                        e.setAdresse(adresses.get(random.nextInt(adresses.size())));
                        e.setTarif(tarifs.get(random.nextInt(tarifs.size())));
                        e.setGuide(guides.get(random.nextInt(guides.size())));

                        // types (1 à 2 types)
                        int nbTypes = 1 + random.nextInt(2);
                        Set<TypeEvenement> typesEvt = new HashSet<>();
                        for (int j = 0; j < nbTypes; j++) {
                            typesEvt.add(typeEvenements.get(random.nextInt(typeEvenements.size())));
                        }
                        e.setTypeEvenement(new ArrayList<>(typesEvt));

                        // participants (3 à 10 users)
                        int nbParticipants = 3 + random.nextInt(8);
                        Set<User> participants = new HashSet<>();
                        for (int j = 0; j < nbParticipants; j++) {
                            participants.add(users.get(random.nextInt(users.size())));
                        }
                        e.setUsers(new ArrayList<>(participants));

                        return e;
                    })
                    .map(evenementRepository::save)
                    .collect(Collectors.toList());

            // --------- RÉSERVATIONS ----------
            List<String> statuts = java.util.Arrays.asList("EN_ATTENTE", "CONFIRMEE", "ANNULEE");

            IntStream.range(0, 60).forEach(i -> {
                Reservation r = new Reservation();
                r.setNombrePersonne(1 + random.nextInt(5));

                LocalDateTime dateRes = LocalDateTime.now().minusDays(random.nextInt(10));
                r.setDateReservation(dateRes);

                String statut = statuts.get(random.nextInt(statuts.size()));
                r.setStatut(statut);

              
                if ("Annulé".equals(statut)) {
                    r.setDateAnnulation(dateRes.plusHours(1 + random.nextInt(48)));
                }

                r.setEvenement(evenements.get(random.nextInt(evenements.size())));
                r.setUser(users.get(random.nextInt(users.size())));

                reservationRepository.save(r);
            });

            System.out.println("********* Données de test insérées *********");
        };
    }
}
