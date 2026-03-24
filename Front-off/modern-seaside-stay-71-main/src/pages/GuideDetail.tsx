import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, Languages, DollarSign, Star, Calendar, MapPin, Loader2 } from 'lucide-react';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';
import Layout from '@/components/layout/Layout';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import GlassCard from '@/components/ui/GlassCard';
import { cn } from '@/lib/utils';
import api from '@/services/api';
import type { Guide, Evenement } from '@/types';

const GuideDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();

  const [guide, setGuide] = useState<Guide | null>(null);
  const [evenements, setEvenements] = useState<Evenement[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    const numId = Number(id);

    Promise.all([
      api.getGuideById(numId),
      api.getEvenements().then((all) =>
        all.filter((e: Evenement) => e.guideId === numId)
      ),
    ])
      .then(([guideData, evts]) => {
        setGuide(guideData);
        setEvenements(evts);
      })
      .catch(() => setError('Guide introuvable.'))
      .finally(() => setLoading(false));
  }, [id]);

  const formatDate = (dateStr: string) => {
    try {
      return format(new Date(dateStr), 'd MMM yyyy', { locale: fr });
    } catch {
      return dateStr;
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

  if (error || !guide) {
    return (
      <Layout>
        <div className="min-h-screen flex flex-col items-center justify-center gap-4">
          <p className="text-xl text-muted-foreground">{error || 'Guide introuvable.'}</p>
          <Link to="/guides">
            <Button variant="outline">
              <ArrowLeft className="mr-2 h-4 w-4" />
              Retour aux guides
            </Button>
          </Link>
        </div>
      </Layout>
    );
  }

  const fullName = guide.user
    ? `${guide.user.prenom} ${guide.user.nom}`
    : `Guide #${guide.id}`;

  const initials = guide.user
    ? `${guide.user.prenom?.[0] ?? ''}${guide.user.nom?.[0] ?? ''}`
    : 'G';

  return (
    <Layout>
      <div className="min-h-screen">
        {/* Hero banner */}
        <div className="relative bg-gradient-sand pt-32 pb-16">
          <div className="absolute top-6 left-6">
            <Link to="/guides">
              <Button variant="outline" size="sm" className="bg-background/70 backdrop-blur-sm">
                <ArrowLeft className="mr-2 h-4 w-4" />
                Retour
              </Button>
            </Link>
          </div>

          <div className="container max-w-4xl">
            <div className="flex flex-col sm:flex-row items-center sm:items-start gap-6">
              {/* Avatar */}
              <div className="relative w-32 h-32 flex-shrink-0">
                <div className="w-full h-full rounded-full bg-gradient-morocco p-1">
                  <div className="w-full h-full rounded-full bg-card flex items-center justify-center">
                    <span className="text-4xl font-serif font-bold text-primary">
                      {initials}
                    </span>
                  </div>
                </div>
                {/* Disponibilité dot */}
                <div
                  className={cn(
                    'absolute bottom-2 right-2 w-6 h-6 rounded-full border-2 border-background',
                    guide.disponible ? 'bg-secondary' : 'bg-muted-foreground'
                  )}
                />
              </div>

              {/* Name + meta */}
              <div className="text-center sm:text-left space-y-2">
                <h1 className="font-serif text-3xl md:text-4xl font-bold">{fullName}</h1>
                <Badge
                  variant="secondary"
                  className={guide.disponible
                    ? 'bg-secondary/20 text-secondary-foreground'
                    : 'bg-muted text-muted-foreground'}
                >
                  {guide.disponible ? 'Disponible' : 'Non disponible'}
                </Badge>

                {/* Stars (static placeholder) */}
                <div className="flex items-center justify-center sm:justify-start gap-1 pt-1">
                  {[1, 2, 3, 4, 5].map((s) => (
                    <Star
                      key={s}
                      className={cn('w-4 h-4', s <= 4 ? 'text-ochre fill-ochre' : 'text-muted')}
                    />
                  ))}
                  <span className="text-sm text-muted-foreground ml-1">(avis clients)</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Content */}
        <div className="container max-w-4xl py-10">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Left – info */}
            <div className="lg:col-span-2 space-y-6">
              {/* Languages */}
              {guide.user?.langues && guide.user.langues.length > 0 && (
                <GlassCard className="p-5 space-y-3">
                  <h2 className="font-serif text-lg font-semibold flex items-center gap-2">
                    <Languages className="h-5 w-5 text-primary" />
                    Langues parlées
                  </h2>
                  <div className="flex flex-wrap gap-2">
                    {guide.user.langues.map((langue) => (
                      <Badge
                        key={langue.id}
                        variant="secondary"
                        className="bg-secondary/20 text-secondary-foreground"
                      >
                        {langue.nomLangue}
                      </Badge>
                    ))}
                  </div>
                </GlassCard>
              )}

              {/* Events led by this guide */}
              {evenements.length > 0 && (
                <div className="space-y-4">
                  <h2 className="font-serif text-xl font-semibold">
                    Événements organisés ({evenements.length})
                  </h2>
                  <div className="grid gap-4">
                    {evenements.map((evt) => (
                      <Link key={evt.id} to={`/evenements/${evt.id}`}>
                        <GlassCard className="p-4 flex gap-4 hover-lift cursor-pointer">
                          {evt.image && (
                            <img
                              src={evt.image}
                              alt={evt.titreEvent}
                              className="w-20 h-20 object-cover rounded-lg flex-shrink-0"
                            />
                          )}
                          <div className="flex-1 min-w-0 space-y-1">
                            <h3 className="font-medium truncate">{evt.titreEvent}</h3>
                            {evt.dateDebut && (
                              <p className="text-sm text-muted-foreground flex items-center gap-1">
                                <Calendar className="h-3 w-3" />
                                {formatDate(evt.dateDebut)}
                              </p>
                            )}
                            {evt.adresse && (
                              <p className="text-sm text-muted-foreground flex items-center gap-1">
                                <MapPin className="h-3 w-3" />
                                {evt.adresse.ville}
                              </p>
                            )}
                            {evt.tarif && (
                              <p className="text-sm font-semibold text-primary">
                                {evt.tarif.prix}€/pers.
                              </p>
                            )}
                          </div>
                        </GlassCard>
                      </Link>
                    ))}
                  </div>
                </div>
              )}

              {evenements.length === 0 && !loading && (
                <GlassCard className="p-6 text-center text-muted-foreground">
                  Aucun événement disponible pour ce guide pour le moment.
                </GlassCard>
              )}
            </div>

            {/* Right – booking panel */}
            <div className="lg:col-span-1">
              <GlassCard className="p-6 space-y-5 sticky top-24">
                <h2 className="font-serif text-xl font-semibold">Contacter le guide</h2>

                <div className="flex items-center gap-2 text-2xl font-bold text-primary">
                  <DollarSign className="h-6 w-6" />
                  {guide.tarif}€
                  <span className="text-sm font-normal text-muted-foreground">/jour</span>
                </div>

                {guide.user?.email && (
                  <p className="text-sm text-muted-foreground break-all">
                    {guide.user.email}
                  </p>
                )}

                <a href={`mailto:${guide.user?.email ?? ''}`}>
                  <Button className="w-full btn-morocco">
                    Envoyer un message
                  </Button>
                </a>

                <Link to="/evenements">
                  <Button variant="outline" className="w-full">
                    Voir tous les événements
                  </Button>
                </Link>
              </GlassCard>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default GuideDetail;
