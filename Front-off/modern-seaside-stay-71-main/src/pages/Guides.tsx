import React, { useState, useEffect } from 'react';
import { Search, Filter, X, Languages, DollarSign } from 'lucide-react';
import { useLocation, useNavigate } from 'react-router-dom';
import Layout from '@/components/layout/Layout';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Switch } from '@/components/ui/switch';
import { Label } from '@/components/ui/label';
import GuideCard from '@/components/cards/GuideCard';
import SkeletonCard from '@/components/ui/SkeletonCard';
import GlassCard from '@/components/ui/GlassCard';
import { useToast } from '@/hooks/use-toast';
import api from '@/services/api';
import type { Guide } from '@/types';

// Mock data
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
  {
    id: 4,
    tarif: 100,
    disponible: true,
    user: {
      id: 4,
      nom: 'Bennani',
      prenom: 'Sara',
      email: 'sara@example.com',
      verifEmail: true,
      role: { id: 2, libelleRole: 'ROLE_GUIDE' },
      langues: [
        { id: 1, nomLangue: 'Français' },
        { id: 3, nomLangue: 'Anglais' },
        { id: 5, nomLangue: 'Allemand' },
      ],
    },
  },
  {
    id: 5,
    tarif: 200,
    disponible: true,
    user: {
      id: 5,
      nom: 'Tazi',
      prenom: 'Mohammed',
      email: 'mohammed@example.com',
      verifEmail: true,
      role: { id: 2, libelleRole: 'ROLE_GUIDE' },
      langues: [
        { id: 1, nomLangue: 'Français' },
        { id: 2, nomLangue: 'Arabe' },
        { id: 3, nomLangue: 'Anglais' },
        { id: 6, nomLangue: 'Italien' },
      ],
    },
  },
  {
    id: 6,
    tarif: 130,
    disponible: true,
    user: {
      id: 6,
      nom: 'Alaoui',
      prenom: 'Khadija',
      email: 'khadija@example.com',
      verifEmail: true,
      role: { id: 2, libelleRole: 'ROLE_GUIDE' },
      langues: [
        { id: 1, nomLangue: 'Français' },
        { id: 4, nomLangue: 'Espagnol' },
        { id: 7, nomLangue: 'Portugais' },
      ],
    },
  },
];

const languages = ['Toutes', 'Français', 'Arabe', 'Anglais', 'Espagnol', 'Allemand', 'Italien', 'Portugais'];
const priceRanges = [
  { label: 'Tous les prix', min: 0, max: Infinity },
  { label: '< 100€', min: 0, max: 100 },
  { label: '100€ - 150€', min: 100, max: 150 },
  { label: '150€ - 200€', min: 150, max: 200 },
  { label: '> 200€', min: 200, max: Infinity },
];

const Guides: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { toast } = useToast();
  const eventState = location.state as {
    fromEvent?: boolean;
    eventId?: number;
    eventTitle?: string;
    userId?: number;
    nombrePersonne?: number;
  } | null;

  const [guides, setGuides] = useState<Guide[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedLanguage, setSelectedLanguage] = useState('Toutes');
  const [selectedPriceRange, setSelectedPriceRange] = useState(0);
  const [onlyAvailable, setOnlyAvailable] = useState(false);
  const [showFilters, setShowFilters] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [selecting, setSelecting] = useState(false);

  const handleSelectGuide = async (guide: Guide) => {
    if (!eventState?.eventId || !eventState?.userId) return;
    setSelecting(true);
    try {
      await api.createReservation({
        eventId: eventState.eventId,
        userId: eventState.userId,
        nombrePersonne: eventState.nombrePersonne ?? 1,
        guideId: guide.id,
      });
      toast({
        title: 'Réservation confirmée !',
        description: `Réservation pour "${eventState.eventTitle}" avec ${guide.user?.prenom} ${guide.user?.nom} enregistrée.`,
      });
      navigate('/dashboard');
    } catch (err: any) {
      toast({
        variant: 'destructive',
        title: 'Erreur',
        description: err?.message || 'Impossible de créer la réservation.',
      });
    } finally {
      setSelecting(false);
    }
  };

  useEffect(() => {
    api.getGuides()
      .then((data) => setGuides(data))
      .catch(() => setGuides(mockGuides))
      .finally(() => setIsLoading(false));
  }, []);

  const filteredGuides = guides.filter((guide) => {
    const fullName = `${guide.user?.prenom} ${guide.user?.nom}`.toLowerCase();
    const matchesSearch = fullName.includes(searchQuery.toLowerCase());
    const matchesLanguage = selectedLanguage === 'Toutes' || 
      guide.user?.langues?.some((l) => l.nomLangue === selectedLanguage);
    const priceRange = priceRanges[selectedPriceRange];
    const matchesPrice = guide.tarif >= priceRange.min && guide.tarif < priceRange.max;
    const matchesAvailability = !onlyAvailable || guide.disponible;
    return matchesSearch && matchesLanguage && matchesPrice && matchesAvailability;
  });

  const clearFilters = () => {
    setSearchQuery('');
    setSelectedLanguage('Toutes');
    setSelectedPriceRange(0);
    setOnlyAvailable(false);
  };

  const hasActiveFilters = searchQuery || selectedLanguage !== 'Toutes' || selectedPriceRange !== 0 || onlyAvailable;

  return (
    <Layout>
      {/* Hero */}
      <section className="pt-32 pb-16 bg-gradient-sand">
        <div className="container">
          <div className="text-center max-w-3xl mx-auto">
            <span className="text-primary font-medium uppercase tracking-wider">
              Nos guides
            </span>
            <h1 className="font-serif text-4xl md:text-5xl lg:text-6xl font-bold mt-2 mb-6">
              Des experts locaux <span className="text-gradient-morocco">passionnés</span>
            </h1>
            <p className="text-muted-foreground text-lg">
              Trouvez le guide parfait pour votre aventure marocaine
            </p>
            {eventState?.fromEvent && (
              <div className="mt-6 p-4 rounded-xl bg-primary/10 border border-primary/30 text-primary font-medium">
                🗺️ Sélectionnez un guide pour votre réservation
                {eventState.eventTitle && (
                  <span className="block text-sm font-normal text-muted-foreground mt-1">
                    Événement : {eventState.eventTitle} &bull; {eventState.nombrePersonne} personne{(eventState.nombrePersonne ?? 1) > 1 ? 's' : ''}
                  </span>
                )}
              </div>
            )}          </div>
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
                placeholder="Rechercher un guide..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-12 h-12"
              />
            </div>

            {/* Desktop Filters */}
            <div className="hidden lg:flex items-center gap-4">
              <Select value={selectedLanguage} onValueChange={setSelectedLanguage}>
                <SelectTrigger className="w-[180px] h-12">
                  <Languages className="h-4 w-4 mr-2 text-muted-foreground" />
                  <SelectValue placeholder="Langue" />
                </SelectTrigger>
                <SelectContent>
                  {languages.map((lang) => (
                    <SelectItem key={lang} value={lang}>
                      {lang}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              <Select 
                value={String(selectedPriceRange)} 
                onValueChange={(v) => setSelectedPriceRange(Number(v))}
              >
                <SelectTrigger className="w-[180px] h-12">
                  <DollarSign className="h-4 w-4 mr-2 text-muted-foreground" />
                  <SelectValue placeholder="Prix" />
                </SelectTrigger>
                <SelectContent>
                  {priceRanges.map((range, index) => (
                    <SelectItem key={index} value={String(index)}>
                      {range.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              <div className="flex items-center gap-2">
                <Switch
                  id="available"
                  checked={onlyAvailable}
                  onCheckedChange={setOnlyAvailable}
                />
                <Label htmlFor="available" className="text-sm whitespace-nowrap">
                  Disponibles uniquement
                </Label>
              </div>

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
              <Select value={selectedLanguage} onValueChange={setSelectedLanguage}>
                <SelectTrigger className="w-full h-12">
                  <Languages className="h-4 w-4 mr-2 text-muted-foreground" />
                  <SelectValue placeholder="Langue" />
                </SelectTrigger>
                <SelectContent>
                  {languages.map((lang) => (
                    <SelectItem key={lang} value={lang}>
                      {lang}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              <Select 
                value={String(selectedPriceRange)} 
                onValueChange={(v) => setSelectedPriceRange(Number(v))}
              >
                <SelectTrigger className="w-full h-12">
                  <DollarSign className="h-4 w-4 mr-2 text-muted-foreground" />
                  <SelectValue placeholder="Prix" />
                </SelectTrigger>
                <SelectContent>
                  {priceRanges.map((range, index) => (
                    <SelectItem key={index} value={String(index)}>
                      {range.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              <div className="flex items-center gap-2">
                <Switch
                  id="available-mobile"
                  checked={onlyAvailable}
                  onCheckedChange={setOnlyAvailable}
                />
                <Label htmlFor="available-mobile" className="text-sm">
                  Disponibles uniquement
                </Label>
              </div>

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
              {filteredGuides.length} guide{filteredGuides.length !== 1 ? 's' : ''} trouvé{filteredGuides.length !== 1 ? 's' : ''}
            </p>
          </div>

          {isLoading ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
              {[1, 2, 3, 4, 5, 6].map((i) => (
                <SkeletonCard key={i} variant="guide" />
              ))}
            </div>
          ) : filteredGuides.length > 0 ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
              {filteredGuides.map((guide, index) => (
                <div
                  key={guide.id}
                  className="animate-fade-up opacity-0"
                  style={{ animationDelay: `${(index % 8) * 0.1}s` }}
                >
                  <GuideCard
                    guide={guide}
                    onSelect={eventState?.fromEvent ? handleSelectGuide : undefined}
                  />
                </div>
              ))}
            </div>
          ) : (
            <GlassCard className="py-16 text-center">
              <div className="text-6xl mb-4">🧭</div>
              <h3 className="font-serif text-2xl font-semibold mb-2">
                Aucun guide trouvé
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

export default Guides;
