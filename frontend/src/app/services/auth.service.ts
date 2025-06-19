import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../interfaces/auth-response.interface';
import { RegisterRequest } from '../models/register-request.model';
import { TokenStorageService } from '../storage/localStorage.service';
import { verifyPhone } from '../models/verifyPhone-request.model';
import { VerifyResetCode } from '../models/verifyResetCode-request.model';
import { ChangePassword } from '../models/changePassword-request.model';
import { Response } from '../interfaces/response.interface';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8000/api/v1';
  private http = inject(HttpClient);
  private tokenStorage = inject(TokenStorageService);
  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, data);
  }

  register(data: RegisterRequest): Observable<Response> {
    return this.http.post<Response>(`${this.apiUrl}/auth/register`, data);
  }

  verifyPhone(data: verifyPhone): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/auth/verify-phone`,
      data
    );
  }

  forgotPassword(email: string): Observable<Response> {
    return this.http.post<Response>(`${this.apiUrl}/auth/forgot-password`, {
      email,
    });
  }

  changePassword(data: ChangePassword): Observable<Response> {
    return this.http.put<Response>(`${this.apiUrl}/auth/change-password`, data);
  }

  verifyResetCode(data: VerifyResetCode): Observable<Response> {
    return this.http.post<Response>(
      `${this.apiUrl}/auth/verify-reset-code`,
      data
    );
  }

  isLoggedIn(): boolean {
    return !!this.tokenStorage.getItem('access_token');
  }

  logout() {
    this.tokenStorage.removeItem('access_token');
  }
}
