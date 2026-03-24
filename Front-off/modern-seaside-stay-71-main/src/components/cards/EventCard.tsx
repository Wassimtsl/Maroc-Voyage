import React from 'react';
import { Link } from 'react-router-dom';
import { Calendar, MapPin, Users } from 'lucide-react';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';
import type { Evenement } from '@/types';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { cn } from '@/lib/utils';

interface EventCardProps {
  event: Evenement;
  className?: string;
}

const EventCard: React.FC<EventCardProps> = ({ event, className }) => {
  const formatDate = (dateString: string) => {
    return format(new Date(dateString), 'd MMM yyyy', { locale: fr });
  };

  return (
    <div
      className={cn(
        'group glass-card rounded-2xl overflow-hidden hover-lift',
        className
      )}
    >
      {/* Image */}
      <div className="relative h-48 overflow-hidden">
        <img
          src={event.image || 'https://images.unsplash.com/photo-1539020140153-e479b8c22e70?w=600'}
          alt={event.titreEvent}
          className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-earth/60 to-transparent" />
        
        {/* Type badge */}
        {event.typesEvenement && event.typesEvenement.length > 0 && (
          <div className="absolute top-4 left-4">
            <Badge className="bg-primary/90 text-primary-foreground backdrop-blur-sm">
              {event.typesEvenement[0].libelleType}
            </Badge>
          </div>
        )}

        {/* Price */}
        {event.tarif && (
          <div className="absolute bottom-4 right-4">
            <div className="glass-card px-3 py-1 rounded-full">
              <span className="font-semibold text-foreground">
                {event.tarif.promotion ? (
                  <>
                    <span className="line-through text-muted-foreground text-sm mr-2">
                      {event.tarif.prix}€
                    </span>
                    {event.tarif.promotion}€
                  </>
                ) : (
                  `${event.tarif.prix}€`
                )}
              </span>
            </div>
          </div>
        )}
      </div>

      {/* Content */}
      <div className="p-6 space-y-4">
        <h3 className="font-serif text-xl font-semibold text-foreground line-clamp-2 group-hover:text-primary transition-colors">
          {event.titreEvent}
        </h3>

        <p className="text-muted-foreground text-sm line-clamp-2">
          {event.description}
        </p>

        <div className="space-y-2 text-sm">
          {/* Date */}
          <div className="flex items-center gap-2 text-muted-foreground">
            <Calendar className="w-4 h-4 text-primary" />
            <span>
              {formatDate(event.dateDebut)} - {formatDate(event.dateFin)}
            </span>
          </div>

          {/* Location */}
          {event.adresse && (
            <div className="flex items-center gap-2 text-muted-foreground">
              <MapPin className="w-4 h-4 text-primary" />
              <span>{event.adresse.ville}, {event.adresse.pays}</span>
            </div>
          )}

          {/* Participants */}
          {event.participants && (
            <div className="flex items-center gap-2 text-muted-foreground">
              <Users className="w-4 h-4 text-primary" />
              <span>{event.participants.length} participants</span>
            </div>
          )}
        </div>

        <Link to={`/evenements/${event.id}`}>
          <Button className="w-full btn-morocco mt-4">
            Voir les détails
          </Button>
        </Link>
      </div>
    </div>
  );
};

export default EventCard;
