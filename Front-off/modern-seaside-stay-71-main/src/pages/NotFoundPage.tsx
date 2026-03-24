import React from 'react';
import { Link } from 'react-router-dom';
import { Home, ArrowLeft } from 'lucide-react';
import { Button } from '@/components/ui/button';

const NotFoundPage: React.FC = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-sand">
      <div className="text-center px-4">
        <div className="mb-8">
          <span className="text-9xl font-serif font-bold text-gradient-morocco">404</span>
        </div>
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-foreground mb-4">
          Page introuvable
        </h1>
        <p className="text-muted-foreground text-lg mb-8 max-w-md mx-auto">
          Oups ! On dirait que vous vous êtes perdu dans le désert. 
          Cette page n'existe pas ou a été déplacée.
        </p>
        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <Link to="/">
            <Button className="btn-morocco">
              <Home className="h-5 w-5 mr-2" />
              Retour à l'accueil
            </Button>
          </Link>
          <Button variant="outline" onClick={() => window.history.back()}>
            <ArrowLeft className="h-5 w-5 mr-2" />
            Page précédente
          </Button>
        </div>
      </div>
    </div>
  );
};

export default NotFoundPage;
