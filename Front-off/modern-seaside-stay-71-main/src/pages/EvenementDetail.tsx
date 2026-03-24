import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Calendar, MapPin, ArrowLeft, Users, Tag, Loader2, UserX } from 'lucide-react';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';
import Layout from '@/components/layout/Layout';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import GlassCard from '@/components/ui/GlassCard';
import { useToast } from '@/hooks/use-toast';
import { useAuth } from '@/contexts/AuthContext';
import api from '@/services/api';
import type { Evenement } from '@/types';

const EvenementDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { toast } = useToast();
  const { user } = useAuth();

  const [event, setEvent] = useState<Evenement | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [nombrePersonne, setNombrePersonne] = useState(1);
  const [sansGuide, setSansGuide] = useState(false);
  const [reserving, setReserving] = useState(false);

  useEffect(() => {
    const fetchEvent = async () => {
      if (!id) return;
      try {
        const data = await api.getEvenementById(Number(id));
        setEvent(data);
      } catch (err) {
        setError('Événement introuvable.');
      } finally {
        setLoading(false);
      }
    };
    fetchEvent();
  }, [id]);

  const formatDate = (dateStr: string) => {
    try {
      return format(new Date(dateStr), "d MMMM yyyy", { locale: fr });
    } catch {
      return dateStr;
    }
  };

  const handleReserver = async () => {
    if (!user) {
      toast({
        variant: 'destructive',
        title: 'Connexion requise',
        description: 'Veuillez vous connecter pour réserver.',
      });
      navigate('/login');
      return;
    }
    if (!event) return;

    if (sansGuide) {
      setReserving(true);
      try {
        await api.createReservation({
          eventId: event.id,
          userId: user.id,
          nombrePersonne,
        });
        toast({
          title: 'Réservation confirmée !',
          description: `Votre réservation pour "${event.titreEvent}" a été enregistrée.`,
        });
        navigate('/dashboard');
      } catch (err: any) {
        toast({
          variant: 'destructive',
          title: 'Erreur',
          description: err?.message || 'Impossible de créer la réservation.',
        });
      } finally {
        setReserving(false);
      }
    } else {
      navigate('/guides', {
        state: {
          fromEvent: true,
          eventId: event.id,
          eventTitle: event.titreEvent,
          userId: user.id,
          nombrePersonne,
        },
      });
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="min-h-screen flex items-center justify-center">
          <Loader2 className="h-10 w-10 animate-spin text-primary" />
        </div>
      </Layout>
    );
  }

  if (error || !event) {
    return (
      <Layout>
        <div className="min-h-screen flex flex-col items-center justify-center gap-4">
          <p className="text-xl text-muted-foreground">{error || 'Événement introuvable.'}</p>
          <Link to="/evenements">
            <Button variant="outline">
              <ArrowLeft className="mr-2 h-4 w-4" />
              Retour aux événements
            </Button>
          </Link>
        </div>
      </Layout>
    );
  }

  const unitPrice = event.tarif
    ? (event.tarif.promotion && event.tarif.promotion > 0 ? event.tarif.promotion : event.tarif.prix ?? 0)
    : 0;
  const totalPrice = event.tarif ? unitPrice * nombrePersonne : null;

  return (
    <Layout>
      <div className="min-h-screen">
        {/* Hero image */}
        <div className="relative h-64 md:h-96 overflow-hidden">
          <img
            src={event.image || 'https://images.unsplash.com/photo-1539020140153-e479b8c22e70?w=1200'}
            alt={event.titreEvent}
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-background via-background/40 to-transparent" />
          <div className="absolute top-4 left-4">
            <Link to="/evenements">
              <Button variant="outline" size="sm" className="bg-background/70 backdrop-blur-sm">
                <ArrowLeft className="mr-2 h-4 w-4" />
                Retour
              </Button>
            </Link>
          </div>
        </div>

        {/* Main content */}
        <div className="container py-10 max-w-5xl">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Left – details */}
            <div className="lg:col-span-2 space-y-6">
              {/* Types */}
              {event.typesEvenement && event.typesEvenement.length > 0 && (
                <div className="flex gap-2 flex-wrap">
                  {event.typesEvenement.map((te) => (
                    <Badge key={te.idTypeEvenement} className="bg-primary/20 text-primary">
                      {te.libelleType}
                    </Badge>
                  ))}
                </div>
              )}

              <h1 className="font-serif text-3xl md:text-4xl font-bold">{event.titreEvent}</h1>

              <p className="text-muted-foreground leading-relaxed text-base">{event.description}</p>

              {/* Meta infos */}
              <GlassCard className="p-5 space-y-4">
                {/* Date */}
                <div className="flex items-center gap-3">
                  <Calendar className="h-5 w-5 text-primary flex-shrink-0" />
                  <div>
                    <p className="text-sm text-muted-foreground">Dates</p>
                    <p className="font-medium">
                      {formatDate(event.dateDebut)} → {formatDate(event.dateFin)}
                    </p>
                  </div>
                </div>

                {/* Location */}
                {event.adresse && (
                  <div className="flex items-center gap-3">
                    <MapPin className="h-5 w-5 text-primary flex-shrink-0" />
                    <div>
                      <p className="text-sm text-muted-foreground">Lieu</p>
                      <p className="font-medium">
                        {[event.adresse.adresse, event.adresse.ville, event.adresse.pays]
                          .filter(Boolean)
                          .join(', ')}
                      </p>
                    </div>
                  </div>
                )}

                {/* Tarif */}
                {event.tarif && (
                  <div className="flex items-center gap-3">
                    <Tag className="h-5 w-5 text-primary flex-shrink-0" />
                    <div>
                      <p className="text-sm text-muted-foreground">Tarif par personne</p>
                      <p className="font-medium">
                        {event.tarif.promotion ? (
                          <>
                            <span className="line-through text-muted-foreground mr-2">
                              {event.tarif.prix}€
                            </span>
                            <span className="text-primary font-bold">{event.tarif.promotion}€</span>
                          </>
                        ) : (
                          <span className="text-primary font-bold">{event.tarif.prix}€</span>
                        )}
                      </p>
                    </div>
                  </div>
                )}
              </GlassCard>
            </div>

            {/* Right – booking widget */}
            <div className="lg:col-span-1">
              <GlassCard className="p-6 space-y-5 sticky top-24">
                <h2 className="font-serif text-xl font-semibold">Réserver</h2>

                <div className="space-y-2">
                  <Label htmlFor="nb-personnes">
                    <Users className="inline h-4 w-4 mr-1" />
                    Nombre de personnes
                  </Label>
                  <Input
                    id="nb-personnes"
                    type="number"
                    min={1}
                    max={100}
                    value={nombrePersonne}
                    onChange={(e) => setNombrePersonne(Math.max(1, Number(e.target.value)))}
                  />
                </div>

                {totalPrice !== null && (
                  <div className="bg-primary/10 rounded-xl p-4 text-center">
                    <p className="text-sm text-muted-foreground mb-1">Total estimé</p>
                    <p className="text-2xl font-bold text-primary">{totalPrice}€</p>
                  </div>
                )}

                {/* Option sans guide */}
                <div className="flex items-center gap-3 p-3 rounded-xl border border-border bg-muted/30">
                  <Checkbox
                    id="sans-guide"
                    checked={sansGuide}
                    onCheckedChange={(checked) => setSansGuide(!!checked)}
                  />
                  <Label htmlFor="sans-guide" className="flex items-center gap-2 cursor-pointer">
                    <UserX className="h-4 w-4 text-muted-foreground" />
                    Sans guide
                  </Label>
                </div>

                <Button
                  className="w-full btn-morocco"
                  onClick={handleReserver}
                  disabled={reserving}
                >
                  {reserving ? (
                    <>
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                      Réservation en cours…
                    </>
                  ) : sansGuide ? (
                    'Confirmer'
                  ) : (
                    'Choisir un guide'
                  )}
                </Button>

                {!user && (
                  <p className="text-xs text-muted-foreground text-center">
                    Vous devez être{' '}
                    <Link to="/login" className="text-primary underline">connecté</Link>
                    {' '}pour réserver.
                  </p>
                )}
              </GlassCard>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default EvenementDetail;
