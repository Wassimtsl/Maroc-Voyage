import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, MapPin, Users, Star, Calendar, ChevronDown } from 'lucide-react';
import Layout from '@/components/layout/Layout';
import { Button } from '@/components/ui/button';
import GlassCard from '@/components/ui/GlassCard';
import EventCard from '@/components/cards/EventCard';
import GuideCard from '@/components/cards/GuideCard';
import type { Evenement, Guide } from '@/types';
import api from '@/services/api';

// Kept as fallback if API is unavailable
const fallbackEvents: Evenement[] = [
  {
    id: 1,
    titreEvent: 'Trek dans les montagnes de l\'Atlas',
    description: 'Une aventure inoubliable à travers les sommets majestueux de l\'Atlas avec des guides berbères expérimentés.',
    image: 'https://images.unsplash.com/photo-1489749798305-4fea3ae63d43?w=600',
    dateDebut: '2024-03-15',
    dateFin: '2024-03-18',
    adresse: { id: 1, adresse: '', codePostal: '', ville: 'Imlil', pays: 'Maroc' },
    tarif: { id: 1, prix: 450, promotion: 399 },
    typesEvenement: [{ idTypeEvenement: 1, libelleType: 'Randonnée' }],
  },
  {
    id: 2,
    titreEvent: 'Nuit sous les étoiles du Sahara',
    description: 'Vivez une expérience magique dans le désert de Merzouga avec dîner traditionnel et musique berbère.',
    image: 'https://images.unsplash.com/photo-1511860810434-a92f84c6f01e?w=600',
    dateDebut: '2024-04-01',
    dateFin: '2024-04-03',
    adresse: { id: 2, adresse: '', codePostal: '', ville: 'Merzouga', pays: 'Maroc' },
    tarif: { id: 2, prix: 299 },
    typesEvenement: [{ idTypeEvenement: 2, libelleType: 'Aventure' }],
  },
  {
    id: 3,
    titreEvent: 'Découverte de la médina de Fès',
    description: 'Explorez la plus ancienne médina du monde avec un guide local passionné par l\'histoire.',
    image: 'https://images.unsplash.com/photo-1548690596-f1722c190938?w=600',
    dateDebut: '2024-03-20',
    dateFin: '2024-03-20',
    adresse: { id: 3, adresse: '', codePostal: '', ville: 'Fès', pays: 'Maroc' },
    tarif: { id: 3, prix: 89 },
    typesEvenement: [{ idTypeEvenement: 3, libelleType: 'Culture' }],
  },
];

const mockGuides: Guide[] = [
  {
    id: 1,
    tarif: 150,
    disponible: true,
    user: {
      id: 1,
      nom: 'Amrani',
      prenom: 'Hassan',
      email: 'hassan@example.com',
      verifEmail: true,
      role: { id: 2, libelleRole: 'ROLE_GUIDE' },
      langues: [
        { id: 1, nomLangue: 'Français' },
        { id: 2, nomLangue: 'Arabe' },
        { id: 3, nomLangue: 'Anglais' },
      ],
    },
  },
  {
    id: 2,
    tarif: 120,
    disponible: true,
    user: {
      id: 2,
      nom: 'Berrada',
      prenom: 'Fatima',
      email: 'fatima@example.com',
      verifEmail: true,
      role: { id: 2, libelleRole: 'ROLE_GUIDE' },
      langues: [
        { id: 1, nomLangue: 'Français' },
        { id: 4, nomLangue: 'Espagnol' },
      ],
    },
  },
  {
    id: 3,
    tarif: 180,
    disponible: false,
    user: {
      id: 3,
      nom: 'El Idrissi',
      prenom: 'Youssef',
      email: 'youssef@example.com',
      verifEmail: true,
      role: { id: 2, libelleRole: 'ROLE_GUIDE' },
      langues: [
        { id: 1, nomLangue: 'Français' },
        { id: 2, nomLangue: 'Arabe' },
      ],
    },
  },
];

const stats = [
  { value: '150+', label: 'Guides certifiés', icon: Users },
  { value: '500+', label: 'Expériences', icon: Calendar },
  { value: '20+', label: 'Destinations', icon: MapPin },
  { value: '4.9', label: 'Note moyenne', icon: Star },
];

const Home: React.FC = () => {
  const [scrollY, setScrollY] = useState(0);
  const [featuredEvents, setFeaturedEvents] = useState<Evenement[]>(fallbackEvents);

  useEffect(() => {
    const handleScroll = () => setScrollY(window.scrollY);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  useEffect(() => {
    api.getEvenements()
      .then((data) => {
        if (data && data.length > 0) setFeaturedEvents(data.slice(0, 3));
      })
      .catch(() => { /* garde les données fallback */ });
  }, []);

  return (
    <Layout>
      {/* Hero Section */}
      <section className="relative min-h-screen flex items-center justify-center overflow-hidden">
        {/* Background Image with Parallax */}
        <div
          className="absolute inset-0 z-0"
          style={{ transform: `translateY(${scrollY * 0.5}px)` }}
        >
          <img
            src="https://images.unsplash.com/photo-1539020140153-e479b8c22e70?w=1920"
            alt="Maroc paysage"
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-b from-earth/40 via-earth/20 to-background" />
        </div>

        {/* Content */}
        <div className="relative z-10 container text-center px-4">
          <div className="max-w-4xl mx-auto">
            <span className="inline-block px-4 py-2 rounded-full bg-primary/20 text-primary-foreground backdrop-blur-sm text-sm font-medium mb-6 animate-fade-up opacity-0 stagger-1">
              ✨ Découvrez le Maroc authentique
            </span>
            
            <h1 className="font-serif text-5xl md:text-7xl lg:text-8xl font-bold text-primary-foreground mb-6 animate-fade-up opacity-0 stagger-2">
              Voyagez autrement
              <span className="block text-ochre">au Maroc</span>
            </h1>
            
            <p className="text-xl md:text-2xl text-primary-foreground/90 mb-10 max-w-2xl mx-auto animate-fade-up opacity-0 stagger-3">
              Des expériences uniques avec des guides locaux passionnés. 
              Du désert du Sahara aux montagnes de l'Atlas.
            </p>
            
            <div className="flex flex-col sm:flex-row gap-4 justify-center animate-fade-up opacity-0 stagger-4">
              <Link to="/evenements">
                <Button size="lg" className="btn-morocco text-lg px-8 py-6">
                  Découvrir les expériences
                  <ArrowRight className="ml-2 h-5 w-5" />
                </Button>
              </Link>
              <Link to="/guides">
                <Button
                  size="lg"
                  variant="outline"
                  className="text-lg px-8 py-6 bg-primary-foreground/10 border-primary-foreground/30 text-primary-foreground hover:bg-primary-foreground/20 backdrop-blur-sm"
                >
                  Trouver un guide
                </Button>
              </Link>
            </div>
          </div>
        </div>

        {/* Scroll indicator */}
        <div className="absolute bottom-10 left-1/2 -translate-x-1/2 animate-bounce">
          <ChevronDown className="h-8 w-8 text-primary-foreground/60" />
        </div>
      </section>

      {/* Stats Section */}
      <section className="py-16 -mt-20 relative z-20">
        <div className="container">
          <div className="glass-card-strong rounded-3xl p-8 md:p-12">
            <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
              {stats.map((stat, index) => (
                <div
                  key={stat.label}
                  className="text-center animate-fade-up opacity-0"
                  style={{ animationDelay: `${(index + 1) * 0.1}s` }}
                >
                  <stat.icon className="h-8 w-8 mx-auto mb-3 text-primary" />
                  <div className="font-serif text-4xl md:text-5xl font-bold text-foreground mb-1">
                    {stat.value}
                  </div>
                  <div className="text-muted-foreground">{stat.label}</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* Featured Events */}
      <section className="section">
        <div className="container">
          <div className="text-center mb-12">
            <span className="text-primary font-medium uppercase tracking-wider">
              Expériences populaires
            </span>
            <h2 className="font-serif text-4xl md:text-5xl font-bold mt-2 mb-4">
              Vivez des moments <span className="text-gradient-morocco">inoubliables</span>
            </h2>
            <p className="text-muted-foreground max-w-2xl mx-auto">
              Des aventures soigneusement sélectionnées pour vous offrir le meilleur du Maroc
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {featuredEvents.map((event, index) => (
              <div
                key={event.id}
                className="animate-fade-up opacity-0"
                style={{ animationDelay: `${(index + 1) * 0.1}s` }}
              >
                <EventCard event={event} />
              </div>
            ))}
          </div>

          <div className="text-center mt-12">
            <Link to="/evenements">
              <Button variant="outline" size="lg" className="btn-morocco-outline">
                Voir toutes les expériences
                <ArrowRight className="ml-2 h-5 w-5" />
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* About Section */}
      <section className="section bg-sand/30">
        <div className="container">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div className="relative">
              <div className="relative rounded-3xl overflow-hidden shadow-2xl">
                <img
                  src="https://images.unsplash.com/photo-1493246507139-91e8fad9978e?w=800"
                  alt="Guide marocain"
                  className="w-full aspect-[4/5] object-cover"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-earth/50 to-transparent" />
              </div>
              {/* Floating card */}
              <GlassCard className="absolute -bottom-8 -right-8 max-w-xs hidden md:block">
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 rounded-full bg-secondary flex items-center justify-center">
                    <Star className="h-6 w-6 text-secondary-foreground" />
                  </div>
                  <div>
                    <div className="font-serif text-2xl font-bold">4.9/5</div>
                    <div className="text-sm text-muted-foreground">+2000 avis</div>
                  </div>
                </div>
              </GlassCard>
            </div>

            <div className="space-y-6">
              <span className="text-primary font-medium uppercase tracking-wider">
                Notre philosophie
              </span>
              <h2 className="font-serif text-4xl md:text-5xl font-bold">
                Des guides locaux pour un <span className="text-gradient-morocco">voyage authentique</span>
              </h2>
              <p className="text-muted-foreground text-lg leading-relaxed">
                Nous croyons que les meilleures expériences de voyage viennent de connexions humaines authentiques. 
                Nos guides sont des locaux passionnés qui partagent leur culture, leurs traditions et leurs secrets 
                bien gardés avec vous.
              </p>
              <ul className="space-y-4">
                {[
                  'Guides certifiés et passionnés',
                  'Expériences personnalisées',
                  'Support 24/7 pendant votre voyage',
                  'Réservation flexible et sécurisée',
                ].map((item) => (
                  <li key={item} className="flex items-center gap-3">
                    <div className="w-6 h-6 rounded-full bg-secondary flex items-center justify-center flex-shrink-0">
                      <svg className="w-4 h-4 text-secondary-foreground" fill="currentColor" viewBox="0 0 20 20">
                        <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                      </svg>
                    </div>
                    <span className="text-foreground">{item}</span>
                  </li>
                ))}
              </ul>
              <Link to="/about">
                <Button className="btn-morocco mt-4">
                  En savoir plus
                  <ArrowRight className="ml-2 h-5 w-5" />
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Guides Section */}
      <section className="section">
        <div className="container">
          <div className="text-center mb-12">
            <span className="text-primary font-medium uppercase tracking-wider">
              Nos guides
            </span>
            <h2 className="font-serif text-4xl md:text-5xl font-bold mt-2 mb-4">
              Rencontrez nos <span className="text-gradient-morocco">experts locaux</span>
            </h2>
            <p className="text-muted-foreground max-w-2xl mx-auto">
              Des passionnés qui vous feront découvrir le Maroc comme vous ne l'avez jamais vu
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {mockGuides.map((guide, index) => (
              <div
                key={guide.id}
                className="animate-fade-up opacity-0"
                style={{ animationDelay: `${(index + 1) * 0.1}s` }}
              >
                <GuideCard guide={guide} />
              </div>
            ))}
          </div>

          <div className="text-center mt-12">
            <Link to="/guides">
              <Button variant="outline" size="lg" className="btn-morocco-outline">
                Voir tous les guides
                <ArrowRight className="ml-2 h-5 w-5" />
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="section relative overflow-hidden">
        <div className="absolute inset-0">
          <img
            src="https://images.unsplash.com/photo-1518636693090-8407756e4954?w=1920"
            alt="Maroc désert"
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-earth/80" />
        </div>
        <div className="container relative z-10">
          <div className="max-w-3xl mx-auto text-center text-primary-foreground">
            <h2 className="font-serif text-4xl md:text-5xl font-bold mb-6">
              Prêt pour l'aventure ?
            </h2>
            <p className="text-xl text-primary-foreground/80 mb-10">
              Rejoignez des milliers de voyageurs qui ont découvert le Maroc authentique avec nous.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link to="/register">
                <Button size="lg" className="bg-ochre hover:bg-ochre/90 text-earth text-lg px-8 py-6">
                  Créer un compte gratuit
                </Button>
              </Link>
              <Link to="/contact">
                <Button
                  size="lg"
                  variant="outline"
                  className="border-primary-foreground/30 text-primary-foreground hover:bg-primary-foreground/10 text-lg px-8 py-6"
                >
                  Nous contacter
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </section>
    </Layout>
  );
};

export default Home;
