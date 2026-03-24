import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { User, Calendar, MapPin, Settings, Edit2, Save, X, Loader2, UserCheck, UserX } from 'lucide-react';
import Layout from '@/components/layout/Layout';
import { useAuth } from '@/contexts/AuthContext';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import GlassCard from '@/components/ui/GlassCard';
import { Badge } from '@/components/ui/badge';
import { useToast } from '@/hooks/use-toast';
import api from '@/services/api';
import type { Reservation } from '@/types';

const UserDashboard: React.FC = () => {
  const { user, updateUser } = useAuth();
  const { toast } = useToast();
  const [isEditing, setIsEditing] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [loadingReservations, setLoadingReservations] = useState(true);
  const [formData, setFormData] = useState({
    nom: user?.nom || '',
    prenom: user?.prenom || '',
    email: user?.email || '',
    numTel: user?.numTel || '',
  });

  useEffect(() => {
    const fetchReservations = async () => {
      if (!user?.id) return;
      try {
        const data = await api.getUserReservations(user.id);
        setReservations(data);
      } catch (err) {
        console.error('Erreur chargement réservations:', err);
      } finally {
        setLoadingReservations(false);
      }
    };
    fetchReservations();
  }, [user?.id]);

  const handleCancelReservation = async (id: number) => {
    try {
      await api.cancelReservation(id);
      setReservations((prev) =>
        prev.map((r) => r.id === id ? { ...r, statut: 'ANNULEE' } : r)
      );
      toast({ title: 'Réservation annulée' });
    } catch {
      toast({ variant: 'destructive', title: 'Erreur', description: 'Impossible d\'annuler la réservation.' });
    }
  };

  const handleSave = async () => {
    setIsSaving(true);
    try {
      if (user) {
        const updated = await api.updateUser(user.id, formData);
        updateUser({ ...user, ...updated });
      }
      toast({
        title: 'Profil mis à jour',
        description: 'Vos informations ont été sauvegardées.',
      });
      setIsEditing(false);
    } catch {
      toast({
        variant: 'destructive',
        title: 'Erreur',
        description: 'Impossible de mettre à jour le profil.',
      });
    } finally {
      setIsSaving(false);
    }
  };

  const getStatusBadge = (statut: string) => {
    const statusConfig: Record<string, { label: string; variant: 'default' | 'secondary' | 'destructive' }> = {
      CONFIRMEE: { label: 'Confirmée', variant: 'default' },
      EN_ATTENTE: { label: 'En attente', variant: 'secondary' },
      ANNULEE: { label: 'Annulée', variant: 'destructive' },
    };
    const config = statusConfig[statut] || { label: statut, variant: 'secondary' };
    return <Badge variant={config.variant}>{config.label}</Badge>;
  };

  return (
    <Layout>
      <div className="pt-24 pb-16 min-h-screen">
        <div className="container">
          {/* Header */}
          <div className="mb-8">
            <h1 className="font-serif text-3xl md:text-4xl font-bold text-foreground">
              Mon espace
            </h1>
            <p className="text-muted-foreground mt-2">
              Gérez votre profil et vos réservations
            </p>
          </div>

          <Tabs defaultValue="profile" className="space-y-8">
            <TabsList className="glass-card p-1">
              <TabsTrigger value="profile" className="flex items-center gap-2">
                <User className="h-4 w-4" />
                Profil
              </TabsTrigger>
              <TabsTrigger value="reservations" className="flex items-center gap-2">
                <Calendar className="h-4 w-4" />
                Réservations
              </TabsTrigger>
              <TabsTrigger value="settings" className="flex items-center gap-2">
                <Settings className="h-4 w-4" />
                Paramètres
              </TabsTrigger>
            </TabsList>

            {/* Profile Tab */}
            <TabsContent value="profile">
              <div className="grid lg:grid-cols-3 gap-8">
                {/* Profile Card */}
                <GlassCard className="lg:col-span-1 text-center">
                  <div className="w-24 h-24 mx-auto rounded-full bg-gradient-morocco p-1 mb-4">
                    <div className="w-full h-full rounded-full bg-card flex items-center justify-center">
                      <span className="text-3xl font-serif font-bold text-primary">
                        {user?.prenom?.[0]}{user?.nom?.[0]}
                      </span>
                    </div>
                  </div>
                  <h2 className="font-serif text-2xl font-semibold">
                    {user?.prenom} {user?.nom}
                  </h2>
                  <p className="text-muted-foreground">{user?.email}</p>
                  <Badge className="mt-4" variant="secondary">
                    {user?.role?.libelleRole === 'ROLE_TOURISTE' ? 'Voyageur' : user?.role?.libelleRole === 'ROLE_GUIDE' ? 'Guide' : user?.role?.libelleRole === 'ROLE_ADMIN' ? 'Admin' : 'Voyageur'}
                  </Badge>
                </GlassCard>

                {/* Edit Form */}
                <GlassCard className="lg:col-span-2">
                  <div className="flex items-center justify-between mb-6">
                    <h3 className="font-serif text-xl font-semibold">
                      Informations personnelles
                    </h3>
                    {!isEditing ? (
                      <Button variant="outline" onClick={() => setIsEditing(true)}>
                        <Edit2 className="h-4 w-4 mr-2" />
                        Modifier
                      </Button>
                    ) : (
                      <div className="flex gap-2">
                        <Button
                          variant="ghost"
                          onClick={() => {
                            setIsEditing(false);
                            setFormData({
                              nom: user?.nom || '',
                              prenom: user?.prenom || '',
                              email: user?.email || '',
                              numTel: user?.numTel || '',
                            });
                          }}
                        >
                          <X className="h-4 w-4 mr-2" />
                          Annuler
                        </Button>
                        <Button onClick={handleSave} disabled={isSaving}>
                          {isSaving ? (
                            <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                          ) : (
                            <Save className="h-4 w-4 mr-2" />
                          )}
                          Sauvegarder
                        </Button>
                      </div>
                    )}
                  </div>

                  <div className="grid md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label>Prénom</Label>
                      {isEditing ? (
                        <Input
                          value={formData.prenom}
                          onChange={(e) => setFormData({ ...formData, prenom: e.target.value })}
                        />
                      ) : (
                        <p className="py-2 text-foreground">{user?.prenom}</p>
                      )}
                    </div>
                    <div className="space-y-2">
                      <Label>Nom</Label>
                      {isEditing ? (
                        <Input
                          value={formData.nom}
                          onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                        />
                      ) : (
                        <p className="py-2 text-foreground">{user?.nom}</p>
                      )}
                    </div>
                    <div className="space-y-2">
                      <Label>Email</Label>
                      {isEditing ? (
                        <Input
                          type="email"
                          value={formData.email}
                          onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                        />
                      ) : (
                        <p className="py-2 text-foreground">{user?.email}</p>
                      )}
                    </div>
                    <div className="space-y-2">
                      <Label>Téléphone</Label>
                      {isEditing ? (
                        <Input
                          type="tel"
                          value={formData.numTel}
                          onChange={(e) => setFormData({ ...formData, numTel: e.target.value })}
                        />
                      ) : (
                        <p className="py-2 text-foreground">{user?.numTel || '-'}</p>
                      )}
                    </div>
                  </div>
                </GlassCard>
              </div>
            </TabsContent>

            {/* Reservations Tab */}
            <TabsContent value="reservations">
              <div className="space-y-6">
                <div className="flex items-center justify-between">
                  <h3 className="font-serif text-xl font-semibold">
                    Mes réservations
                  </h3>
                  <Link to="/evenements">
                    <Button className="btn-morocco">
                      Nouvelle réservation
                    </Button>
                  </Link>
                </div>

                {loadingReservations ? (
                <div className="flex justify-center py-12"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>
              ) : reservations.length > 0 ? (
                  <div className="space-y-4">
                    {reservations.map((reservation) => (
                      <GlassCard key={reservation.id} className="flex flex-col md:flex-row gap-6">
                        {reservation.evenement?.image && (
                          <img
                            src={reservation.evenement.image}
                            alt={reservation.evenement.titreEvent}
                            className="w-full md:w-48 h-32 object-cover rounded-xl"
                          />
                        )}
                        <div className="flex-1 space-y-2">
                          <div className="flex items-start justify-between">
                            <h4 className="font-serif text-lg font-semibold">
                              {reservation.evenement?.titreEvent || 'Événement'}
                            </h4>
                            {getStatusBadge(reservation.statut)}
                          </div>
                          {reservation.evenement?.dateDebut && (
                            <div className="flex items-center gap-2 text-sm text-muted-foreground">
                              <Calendar className="h-4 w-4" />
                              <span>
                                {reservation.evenement.dateDebut} - {reservation.evenement.dateFin}
                              </span>
                            </div>
                          )}
                          {reservation.evenement?.adresse && (
                            <div className="flex items-center gap-2 text-sm text-muted-foreground">
                              <MapPin className="h-4 w-4" />
                              <span>
                                {[reservation.evenement.adresse.ville, reservation.evenement.adresse.pays].filter(Boolean).join(', ')}
                              </span>
                            </div>
                          )}
                          <p className="text-sm text-muted-foreground">
                            {reservation.nombrePersonne} personne{reservation.nombrePersonne > 1 ? 's' : ''}
                          </p>
                          {reservation.guide ? (
                            <div className="flex items-center gap-2 text-sm text-primary font-medium">
                              <UserCheck className="h-4 w-4" />
                              Guide : {reservation.guide.user?.prenom} {reservation.guide.user?.nom}
                            </div>
                          ) : (
                            <div className="flex items-center gap-2 text-sm text-muted-foreground">
                              <UserX className="h-4 w-4" />
                              Sans guide
                            </div>
                          )}
                          {reservation.dateReservation && (
                            <p className="text-xs text-muted-foreground">
                              Réservé le {new Date(reservation.dateReservation).toLocaleDateString('fr-FR')}
                            </p>
                          )}
                        </div>
                        <div className="flex md:flex-col gap-2 md:justify-center">
                          {reservation.evenement?.id && (
                            <Link to={`/evenements/${reservation.evenement.id}`}>
                              <Button variant="outline" size="sm">
                                Voir détails
                              </Button>
                            </Link>
                          )}
                          {reservation.statut !== 'ANNULEE' && (
                            <Button
                              variant="ghost"
                              size="sm"
                              className="text-destructive"
                              onClick={() => handleCancelReservation(reservation.id)}
                            >
                              Annuler
                            </Button>
                          )}
                        </div>
                      </GlassCard>
                    ))}
                  </div>
                ) : (
                  <GlassCard className="text-center py-12">
                    <Calendar className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
                    <h4 className="font-serif text-xl font-semibold mb-2">
                      Aucune réservation
                    </h4>
                    <p className="text-muted-foreground mb-6">
                      Vous n'avez pas encore de réservation
                    </p>
                    <Link to="/evenements">
                      <Button className="btn-morocco">
                        Découvrir les expériences
                      </Button>
                    </Link>
                  </GlassCard>
                )}
              </div>
            </TabsContent>

            {/* Settings Tab */}
            <TabsContent value="settings">
              <GlassCard>
                <h3 className="font-serif text-xl font-semibold mb-6">
                  Paramètres du compte
                </h3>
                <div className="space-y-6">
                  <div className="flex items-center justify-between py-4 border-b">
                    <div>
                      <h4 className="font-medium">Changer le mot de passe</h4>
                      <p className="text-sm text-muted-foreground">
                        Modifiez votre mot de passe de connexion
                      </p>
                    </div>
                    <Button variant="outline">Modifier</Button>
                  </div>
                  <div className="flex items-center justify-between py-4 border-b">
                    <div>
                      <h4 className="font-medium">Notifications email</h4>
                      <p className="text-sm text-muted-foreground">
                        Gérez vos préférences de notification
                      </p>
                    </div>
                    <Button variant="outline">Configurer</Button>
                  </div>
                  <div className="flex items-center justify-between py-4">
                    <div>
                      <h4 className="font-medium text-destructive">Supprimer le compte</h4>
                      <p className="text-sm text-muted-foreground">
                        Cette action est irréversible
                      </p>
                    </div>
                    <Button variant="destructive">Supprimer</Button>
                  </div>
                </div>
              </GlassCard>
            </TabsContent>
          </Tabs>
        </div>
      </div>
    </Layout>
  );
};

export default UserDashboard;
