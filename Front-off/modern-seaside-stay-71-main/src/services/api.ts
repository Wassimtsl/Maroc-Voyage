import type {
  User,
  Guide,
  Evenement,
  Reservation,
  TypeEvenement,
  Langue,
  LoginRequest,
  RegisterRequest,
  AuthResponse,
  EvenementFilters,
  GuideFilters,
  Adresse,
} from '@/types';

// ============= API CONFIGURATION =============

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const token = this.getToken();
    
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
      ...options.headers,
    };

    if (token) {
      (headers as Record<string, string>)['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${this.baseUrl}${endpoint}`, {
      ...options,
      headers,
    });

    if (response.status === 401) {
      localStorage.removeItem('auth_token');
      localStorage.removeItem('user');
      window.location.href = '/login';
      throw new Error('Session expirée');
    }

    if (response.status === 403) {
      throw new Error('Accès non autorisé');
    }

    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: 'Erreur serveur' }));
      throw new Error(error.message || 'Une erreur est survenue');
    }

    if (response.status === 204) {
      return {} as T;
    }

    return response.json();
  }

  // ============= AUTH ENDPOINTS =============

  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response = await this.request<AuthResponse>('/auth/login', {
      method: 'POST',
      body: JSON.stringify(credentials),
    });
    localStorage.setItem('auth_token', response.token);
    localStorage.setItem('user', JSON.stringify(response.user));
    return response;
  }

  async register(data: RegisterRequest): Promise<AuthResponse> {
    const response = await this.request<AuthResponse>('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    });
    localStorage.setItem('auth_token', response.token);
    localStorage.setItem('user', JSON.stringify(response.user));
    return response;
  }

  async logout(): Promise<void> {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
  }

  async getCurrentUser(): Promise<User> {
    return this.request<User>('/auth/me');
  }

  // ============= USER ENDPOINTS =============

  async getUsers(): Promise<User[]> {
    return this.request<User[]>('/users');
  }

  async getUserById(id: number): Promise<User> {
    return this.request<User>(`/users/${id}`);
  }

  async updateUser(id: number, data: Partial<User>): Promise<User> {
    return this.request<User>(`/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async deleteUser(id: number): Promise<void> {
    return this.request<void>(`/users/${id}`, {
      method: 'DELETE',
    });
  }

  async updateUserRole(userId: number, roleId: number): Promise<User> {
    return this.request<User>(`/users/${userId}/role`, {
      method: 'PUT',
      body: JSON.stringify({ roleId }),
    });
  }

  async updateUserLangues(userId: number, langueIds: number[]): Promise<User> {
    return this.request<User>(`/users/${userId}/langues`, {
      method: 'PUT',
      body: JSON.stringify({ langueIds }),
    });
  }

  // ============= GUIDE ENDPOINTS =============

  async getGuides(filters?: GuideFilters): Promise<Guide[]> {
    const params = new URLSearchParams();
    if (filters?.disponible !== undefined) params.append('disponible', String(filters.disponible));
    if (filters?.langue) params.append('langue', String(filters.langue));
    if (filters?.tarifMin) params.append('tarifMin', String(filters.tarifMin));
    if (filters?.tarifMax) params.append('tarifMax', String(filters.tarifMax));
    
    const query = params.toString();
    return this.request<Guide[]>(`/guides${query ? `?${query}` : ''}`);
  }

  async getGuideById(id: number): Promise<Guide> {
    return this.request<Guide>(`/guides/${id}`);
  }

  async updateGuideTarif(id: number, tarif: number): Promise<Guide> {
    return this.request<Guide>(`/guides/${id}/tarif`, {
      method: 'PUT',
      body: JSON.stringify({ tarif }),
    });
  }

  async updateGuideDisponibilite(id: number, disponible: boolean): Promise<Guide> {
    return this.request<Guide>(`/guides/${id}/disponibilite`, {
      method: 'PUT',
      body: JSON.stringify({ disponible }),
    });
  }

  async getGuideEvenements(guideId: number): Promise<Evenement[]> {
    return this.request<Evenement[]>(`/guides/${guideId}/evenements`);
  }

  // ============= EVENEMENT ENDPOINTS =============

  async getEvenements(filters?: EvenementFilters): Promise<Evenement[]> {
    const params = new URLSearchParams();
    if (filters?.ville) params.append('ville', filters.ville);
    if (filters?.typeEvenement) params.append('type', String(filters.typeEvenement));
    if (filters?.dateDebut) params.append('dateDebut', filters.dateDebut);
    if (filters?.dateFin) params.append('dateFin', filters.dateFin);
    if (filters?.search) params.append('search', filters.search);
    
    const query = params.toString();
    return this.request<Evenement[]>(`/evenements${query ? `?${query}` : ''}`);
  }

  async getEvenementById(id: number): Promise<Evenement> {
    return this.request<Evenement>(`/evenements/${id}`);
  }

  async createEvenement(data: Partial<Evenement>): Promise<Evenement> {
    return this.request<Evenement>('/evenements', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async updateEvenement(id: number, data: Partial<Evenement>): Promise<Evenement> {
    return this.request<Evenement>(`/evenements/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async deleteEvenement(id: number): Promise<void> {
    return this.request<void>(`/evenements/${id}`, {
      method: 'DELETE',
    });
  }

  // ============= TYPE EVENEMENT ENDPOINTS =============

  async getTypesEvenement(): Promise<TypeEvenement[]> {
    return this.request<TypeEvenement[]>('/types-evenements');
  }

  async createTypeEvenement(data: Partial<TypeEvenement>): Promise<TypeEvenement> {
    return this.request<TypeEvenement>('/types-evenement', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  // ============= RESERVATION ENDPOINTS =============

  async getReservations(): Promise<Reservation[]> {
    return this.request<Reservation[]>('/reservations');
  }

  async getUserReservations(userId: number): Promise<Reservation[]> {
    return this.request<Reservation[]>(`/reservations/user/${userId}`);
  }

  async createReservation(data: { eventId: number; userId: number; nombrePersonne: number; guideId?: number }): Promise<Reservation> {
    return this.request<Reservation>('/reservations', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async cancelReservation(id: number): Promise<Reservation> {
    return this.request<Reservation>(`/reservations/${id}/cancel`, {
      method: 'PUT',
    });
  }

  // ============= LANGUE ENDPOINTS =============

  async getLangues(): Promise<Langue[]> {
    return this.request<Langue[]>('/langues');
  }

  // ============= ADRESSE ENDPOINTS =============

  async createAdresse(data: Partial<Adresse>): Promise<Adresse> {
    return this.request<Adresse>('/adresses', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async updateAdresse(id: number, data: Partial<Adresse>): Promise<Adresse> {
    return this.request<Adresse>(`/adresses/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }
}

export const api = new ApiClient(API_BASE_URL);
export default api;
