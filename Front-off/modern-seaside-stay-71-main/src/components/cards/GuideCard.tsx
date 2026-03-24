import React from 'react';
import { Link } from 'react-router-dom';
import { Star, Languages, DollarSign } from 'lucide-react';
import type { Guide } from '@/types';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { cn } from '@/lib/utils';

interface GuideCardProps {
  guide: Guide;
  className?: string;
  onSelect?: (guide: Guide) => void;
}

const GuideCard: React.FC<GuideCardProps> = ({ guide, className, onSelect }) => {
  return (
    <div
      className={cn(
        'group glass-card rounded-2xl overflow-hidden hover-lift text-center',
        className
      )}
    >
      <div className="p-6 space-y-4">
        {/* Avatar */}
        <div className="relative mx-auto w-24 h-24">
          <div className="w-full h-full rounded-full bg-gradient-morocco p-1">
            <div className="w-full h-full rounded-full bg-card flex items-center justify-center overflow-hidden">
              {guide.user?.prenom ? (
                <span className="text-3xl font-serif font-bold text-primary">
                  {guide.user.prenom[0]}{guide.user.nom?.[0] || ''}
                </span>
              ) : (
                <span className="text-3xl font-serif font-bold text-primary">G</span>
              )}
            </div>
          </div>
          {/* Disponibilité indicator */}
          <div
            className={cn(
              'absolute bottom-1 right-1 w-5 h-5 rounded-full border-2 border-card',
              guide.disponible ? 'bg-secondary' : 'bg-muted-foreground'
            )}
          />
        </div>

        {/* Name */}
        <div>
          <h3 className="font-serif text-xl font-semibold text-foreground">
            {guide.user ? `${guide.user.prenom} ${guide.user.nom}` : 'Guide'}
          </h3>
          <p className="text-sm text-muted-foreground">
            {guide.disponible ? 'Disponible' : 'Non disponible'}
          </p>
        </div>

        {/* Rating placeholder */}
        <div className="flex items-center justify-center gap-1">
          {[1, 2, 3, 4, 5].map((star) => (
            <Star
              key={star}
              className={cn(
                'w-4 h-4',
                star <= 4 ? 'text-ochre fill-ochre' : 'text-muted'
              )}
            />
          ))}
          <span className="text-sm text-muted-foreground ml-1">(24 avis)</span>
        </div>

        {/* Languages */}
        {guide.user?.langues && guide.user.langues.length > 0 && (
          <div className="flex flex-wrap justify-center gap-2">
            {guide.user.langues.slice(0, 3).map((langue) => (
              <Badge
                key={langue.id}
                variant="secondary"
                className="bg-secondary/20 text-secondary-foreground"
              >
                <Languages className="w-3 h-3 mr-1" />
                {langue.nomLangue}
              </Badge>
            ))}
          </div>
        )}

        {/* Tarif */}
        <div className="flex items-center justify-center gap-2 text-lg font-semibold">
          <DollarSign className="w-5 h-5 text-primary" />
          <span className="text-foreground">{guide.tarif}€</span>
          <span className="text-sm font-normal text-muted-foreground">/jour</span>
        </div>

        {/* Action */}
        {onSelect ? (
          <Button
            className="w-full btn-morocco"
            onClick={() => onSelect(guide)}
            disabled={!guide.disponible}
          >
            {guide.disponible ? 'Choisir ce guide' : 'Non disponible'}
          </Button>
        ) : (
          <Link to={`/guides/${guide.id}`}>
            <Button className="w-full btn-morocco-outline">
              Voir le profil
            </Button>
          </Link>
        )}
      </div>
    </div>
  );
};

export default GuideCard;
