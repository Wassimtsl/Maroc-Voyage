import React from 'react';
import { Link } from 'react-router-dom';
import { Home, ShieldX } from 'lucide-react';
import { Button } from '@/components/ui/button';

const ForbiddenPage: React.FC = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-sand">
      <div className="text-center px-4">
        <div className="mb-8">
          <ShieldX className="h-24 w-24 mx-auto text-destructive" />
        </div>
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-foreground mb-4">
          Accès refusé
        </h1>
        <p className="text-muted-foreground text-lg mb-8 max-w-md mx-auto">
          Vous n'avez pas les permissions nécessaires pour accéder à cette page.
        </p>
        <Link to="/">
          <Button className="btn-morocco">
            <Home className="h-5 w-5 mr-2" />
            Retour à l'accueil
          </Button>
        </Link>
      </div>
    </div>
  );
};

export default ForbiddenPage;
