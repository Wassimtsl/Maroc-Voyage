// ============= TYPES BASED ON SPRING BOOT BACKEND =============

export interface Adresse {
  id: number;
  adresse: string;
  codePostal: string;
  pays: string;
  ville: string;
}

export interface Role {
  id: number;
  libelleRole: 'ROLE_TOURISTE' | 'ROLE_GUIDE' | 'ROLE_ADMIN';
}

export interface Langue {
  id: number;
  nomLangue: string;
}

export interface Tarif {
  id: number;
  prix: number;
  promotion?: number;
}

export interface TypeEvenement {
  idTypeEvenement: number;
  libelleType: string;
}

export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  numTel?: string;
  verifEmail: boolean;
  adresse?: Adresse;
  role: Role;
  langues?: Langue[];
}

export interface Guide {
  id: number;
  tarif: number;
  user?: User;
  evenements?: Evenement[];
  disponible?: boolean;
}

export interface Evenement {
  id: number;
  titreEvent: string;
  description: string;
  image?: string;
  dateDebut: string;
  dateFin: string;
  adresse?: Adresse;
  guide?: Guide;
  guideId?: number;
  tarif?: Tarif;
  typesEvenement?: TypeEvenement[];
  participants?: User[];
}

export interface StatutReservation {
  id: number;
  codeStatut: string;
  libelleStatut: string;
  description?: string;
  annulable: boolean;
  isActif: boolean;
  ordreAffichage: number;
}

export interface Reservation {
  id: number;
  dateReservation: string;
  dateAnnulation?: string;
  nombrePersonne: number;
  statut: string;
  evenement?: Evenement;
  user?: User;
  guide?: Guide;
}

// ============= AUTH TYPES =============

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  nom: string;
  prenom: string;
  email: string;
  password: string;
  numTel?: string;
  adresse?: Partial<Adresse>;
}

export interface AuthResponse {
  token: string;
  user: User;
}

// ============= API RESPONSE TYPES =============

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

// ============= FILTER TYPES =============

export interface EvenementFilters {
  ville?: string;
  typeEvenement?: number;
  dateDebut?: string;
  dateFin?: string;
  search?: string;
}

export interface GuideFilters {
  disponible?: boolean;
  langue?: number;
  tarifMin?: number;
  tarifMax?: number;
}
