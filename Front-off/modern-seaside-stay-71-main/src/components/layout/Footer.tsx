import React from 'react';
import { Link } from 'react-router-dom';
import { MapPin, Mail, Phone, Instagram, Facebook, Twitter } from 'lucide-react';

const Footer: React.FC = () => {
  return (
    <footer className="bg-earth text-primary-foreground">
      <div className="container py-16">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-12">
          {/* Brand */}
          <div className="space-y-4">
            <Link to="/" className="flex items-center gap-2">
              <MapPin className="h-8 w-8 text-ochre" />
              <span className="font-serif text-2xl font-bold">
                Morocco<span className="text-ochre">Voyage</span>
              </span>
            </Link>
            <p className="text-primary-foreground/70 leading-relaxed">
              Découvrez le Maroc authentique avec nos guides locaux passionnés. 
              Des expériences uniques du désert aux montagnes de l'Atlas.
            </p>
            <div className="flex gap-4">
              <a href="#" className="p-2 rounded-full bg-primary-foreground/10 hover:bg-ochre transition-colors">
                <Instagram className="w-5 h-5" />
              </a>
              <a href="#" className="p-2 rounded-full bg-primary-foreground/10 hover:bg-ochre transition-colors">
                <Facebook className="w-5 h-5" />
              </a>
              <a href="#" className="p-2 rounded-full bg-primary-foreground/10 hover:bg-ochre transition-colors">
                <Twitter className="w-5 h-5" />
              </a>
            </div>
          </div>

          {/* Navigation */}
          <div>
            <h4 className="font-serif text-lg font-semibold mb-4">Explorer</h4>
            <ul className="space-y-3">
              <li>
                <Link to="/evenements" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  Expériences
                </Link>
              </li>
              <li>
                <Link to="/guides" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  Nos Guides
                </Link>
              </li>
              <li>
                <Link to="/destinations" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  Destinations
                </Link>
              </li>
              <li>
                <Link to="/about" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  À propos
                </Link>
              </li>
            </ul>
          </div>

          {/* Destinations */}
          <div>
            <h4 className="font-serif text-lg font-semibold mb-4">Destinations</h4>
            <ul className="space-y-3">
              <li>
                <a href="#" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  Marrakech
                </a>
              </li>
              <li>
                <a href="#" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  Chefchaouen
                </a>
              </li>
              <li>
                <a href="#" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  Fès
                </a>
              </li>
              <li>
                <a href="#" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  Désert du Sahara
                </a>
              </li>
              <li>
                <a href="#" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  Atlas
                </a>
              </li>
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h4 className="font-serif text-lg font-semibold mb-4">Contact</h4>
            <ul className="space-y-4">
              <li className="flex items-center gap-3">
                <MapPin className="w-5 h-5 text-ochre flex-shrink-0" />
                <span className="text-primary-foreground/70">
                  123 Medina, Marrakech, Maroc
                </span>
              </li>
              <li className="flex items-center gap-3">
                <Mail className="w-5 h-5 text-ochre flex-shrink-0" />
                <a href="mailto:contact@moroccovoyage.com" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  contact@moroccovoyage.com
                </a>
              </li>
              <li className="flex items-center gap-3">
                <Phone className="w-5 h-5 text-ochre flex-shrink-0" />
                <a href="tel:+212600000000" className="text-primary-foreground/70 hover:text-ochre transition-colors">
                  +212 6 00 00 00 00
                </a>
              </li>
            </ul>
          </div>
        </div>

        <div className="border-t border-primary-foreground/10 mt-12 pt-8 flex flex-col md:flex-row justify-between items-center gap-4">
          <p className="text-primary-foreground/50 text-sm">
            © 2024 MoroccoVoyage. Tous droits réservés.
          </p>
          <div className="flex gap-6 text-sm">
            <a href="#" className="text-primary-foreground/50 hover:text-ochre transition-colors">
              Mentions légales
            </a>
            <a href="#" className="text-primary-foreground/50 hover:text-ochre transition-colors">
              Politique de confidentialité
            </a>
            <a href="#" className="text-primary-foreground/50 hover:text-ochre transition-colors">
              CGV
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
