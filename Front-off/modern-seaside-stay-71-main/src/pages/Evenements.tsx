import React, { useState, useEffect } from 'react';
import { Search, Filter, X, MapPin, Calendar } from 'lucide-react';
import Layout from '@/components/layout/Layout';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import EventCard from '@/components/cards/EventCard';
import SkeletonCard from '@/components/ui/SkeletonCard';
import GlassCard from '@/components/ui/GlassCard';
import type { Evenement } from '@/types';
import api from '@/services/api';

const Evenements: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCity, setSelectedCity] = useState('Toutes');
  const [selectedType, setSelectedType] = useState('Tous');
  const [showFilters, setShowFilters] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [events, setEvents] = useState<Evenement[]>([]);
  const [cities, setCities] = useState<string[]>(['Toutes']);
  const [types, setTypes] = useState<string[]>(['Tous']);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const data = await api.getEvenements();
        setEvents(data);
        // Extraire les villes et types uniques
        const uniqueCities = ['Toutes', ...Array.from(new Set(data.map((e) => e.adresse?.ville).filter(Boolean) as string[]))];
        const uniqueTypes = ['Tous', ...Array.from(new Set(data.flatMap((e) => e.typesEvenement?.map((t) => t.libelleType) ?? [])))];
        setCities(uniqueCities);
        setTypes(uniqueTypes);
      } catch (err) {
        console.error('Erreur chargement événements:', err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchEvents();
  }, []);

  const filteredEvents = events.filter((event) => {
    const matchesSearch = event.titreEvent.toLowerCase().includes(searchQuery.toLowerCase()) ||
      event.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCity = selectedCity === 'Toutes' || event.adresse?.ville === selectedCity;
    const matchesType = selectedType === 'Tous' || 
      event.typesEvenement?.some((t) => t.libelleType === selectedType);
    return matchesSearch && matchesCity && matchesType;
  });

  const clearFilters = () => {
    setSearchQuery('');
    setSelectedCity('Toutes');
    setSelectedType('Tous');
  };

  const hasActiveFilters = searchQuery || selectedCity !== 'Toutes' || selectedType !== 'Tous';

  return (
    <Layout>
      {/* Hero */}
      <section className="pt-32 pb-16 bg-gradient-sand">
        <div className="container">
          <div className="text-center max-w-3xl mx-auto">
            <span className="text-primary font-medium uppercase tracking-wider">
              Nos expériences
            </span>
            <h1 className="font-serif text-4xl md:text-5xl lg:text-6xl font-bold mt-2 mb-6">
              Découvrez des expériences <span className="text-gradient-morocco">uniques</span>
            </h1>
            <p className="text-muted-foreground text-lg">
              Des aventures soigneusement sélectionnées pour vous offrir le meilleur du Maroc
            </p>
          </div>
        </div>
      </section>

      {/* Filters */}
      <section className="py-8 border-b">
        <div className="container">
          <div className="flex flex-col lg:flex-row gap-4 items-center">
            {/* Search */}
            <div className="relative flex-1 w-full">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-muted-foreground" />
              <Input
                placeholder="Rechercher une expérience..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-12 h-12"
              />
            </div>

            {/* Desktop Filters */}
            <div className="hidden lg:flex items-center gap-4">
              <Select value={selectedCity} onValueChange={setSelectedCity}>
                <SelectTrigger className="w-[180px] h-12">
                  <MapPin className="h-4 w-4 mr-2 text-muted-foreground" />
                  <SelectValue placeholder="Ville" />
                </SelectTrigger>
                <SelectContent>
                  {cities.map((city) => (
                    <SelectItem key={city} value={city}>
                      {city}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              <Select value={selectedType} onValueChange={setSelectedType}>
                <SelectTrigger className="w-[180px] h-12">
                  <Calendar className="h-4 w-4 mr-2 text-muted-foreground" />
                  <SelectValue placeholder="Type" />
                </SelectTrigger>
                <SelectContent>
                  {types.map((type) => (
                    <SelectItem key={type} value={type}>
                      {type}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              {hasActiveFilters && (
                <Button variant="ghost" onClick={clearFilters} className="text-muted-foreground">
                  <X className="h-4 w-4 mr-2" />
                  Effacer
                </Button>
              )}
            </div>

            {/* Mobile Filter Toggle */}
            <Button
              variant="outline"
              className="lg:hidden w-full"
              onClick={() => setShowFilters(!showFilters)}
            >
              <Filter className="h-4 w-4 mr-2" />
              Filtres
              {hasActiveFilters && (
                <span className="ml-2 w-5 h-5 rounded-full bg-primary text-primary-foreground text-xs flex items-center justify-center">
                  !
                </span>
              )}
            </Button>
          </div>

          {/* Mobile Filters */}
          {showFilters && (
            <div className="lg:hidden mt-4 space-y-4">
              <Select value={selectedCity} onValueChange={setSelectedCity}>
                <SelectTrigger className="w-full h-12">
                  <MapPin className="h-4 w-4 mr-2 text-muted-foreground" />
                  <SelectValue placeholder="Ville" />
                </SelectTrigger>
                <SelectContent>
                  {cities.map((city) => (
                    <SelectItem key={city} value={city}>
                      {city}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              <Select value={selectedType} onValueChange={setSelectedType}>
                <SelectTrigger className="w-full h-12">
                  <Calendar className="h-4 w-4 mr-2 text-muted-foreground" />
                  <SelectValue placeholder="Type" />
                </SelectTrigger>
                <SelectContent>
                  {types.map((type) => (
                    <SelectItem key={type} value={type}>
                      {type}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              {hasActiveFilters && (
                <Button variant="outline" onClick={clearFilters} className="w-full">
                  <X className="h-4 w-4 mr-2" />
                  Effacer les filtres
                </Button>
              )}
            </div>
          )}
        </div>
      </section>

      {/* Results */}
      <section className="section">
        <div className="container">
          {/* Results count */}
          <div className="mb-8">
            <p className="text-muted-foreground">
              {filteredEvents.length} expérience{filteredEvents.length !== 1 ? 's' : ''} trouvée{filteredEvents.length !== 1 ? 's' : ''}
            </p>
          </div>

          {isLoading ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
              {[1, 2, 3, 4, 5, 6].map((i) => (
                <SkeletonCard key={i} variant="event" />
              ))}
            </div>
          ) : filteredEvents.length > 0 ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
              {filteredEvents.map((event, index) => (
                <div
                  key={event.id}
                  className="animate-fade-up opacity-0"
                  style={{ animationDelay: `${(index % 6) * 0.1}s` }}
                >
                  <EventCard event={event} />
                </div>
              ))}
            </div>
          ) : (
            <GlassCard className="py-16 text-center">
              <div className="text-6xl mb-4">🏜️</div>
              <h3 className="font-serif text-2xl font-semibold mb-2">
                Aucune expérience trouvée
              </h3>
              <p className="text-muted-foreground mb-6">
                Essayez de modifier vos critères de recherche
              </p>
              <Button onClick={clearFilters} className="btn-morocco">
                Réinitialiser les filtres
              </Button>
            </GlassCard>
          )}
        </div>
      </section>
    </Layout>
  );
};

export default Evenements;
