import { Component, inject, Input, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenStorageService } from '../../storage/localStorage.service';
import { SessionStorage } from '../../storage/sessionStorage.service';
import { expirationTime } from '../../utils/ExpirationTimeUtil';

@Component({
  selector: 'app-pin',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgIf, NgFor],
  templateUrl: './pin.component.html',
})
export class PinComponent implements OnInit {
  @Input() context: 'register' | 'reset' = 'register';
  @Input() identifier?: string;
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private route = inject(Router);
  private tokenStorage = inject(TokenStorageService);
  private toast = inject(ToastrService);
  private activatedRoute = inject(ActivatedRoute);
  private sessionStorage = inject(SessionStorage);
  pinForm!: FormGroup;
  pinLength = 6;

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params) => {
      this.identifier = params['identifier'];
    });
    const controlsConfig: Record<string, any> = {};

    for (let i = 0; i < this.pinLength; i++) {
      controlsConfig[`digit${i}`] = [
        '',
        [Validators.required, Validators.pattern('[0-9]')],
      ];
    }

    this.pinForm = this.fb.group(controlsConfig);
  }

  get fullCode(): string {
    return Array.from({ length: this.pinLength })
      .map((_, i) => this.pinForm.get(`digit${i}`)?.value || '')
      .join('');
  }

  onInput(index: number): void {
    const input = document.getElementById(`digit${index}`) as HTMLInputElement;
    if (input.value.length === 1 && index < this.pinLength - 1) {
      const nextInput = document.getElementById(
        `digit${index + 1}`
      ) as HTMLInputElement;
      nextInput?.focus();
    }

    if (this.fullCode.length === this.pinLength) {
      this.sendCodeToBackend(this.fullCode);
    }
  }

  onBackspace(index: number): void {
    if (index > 0 && !this.pinForm.get(`digit${index}`)?.value) {
      const prevInput = document.getElementById(
        `digit${index - 1}`
      ) as HTMLInputElement;
      prevInput.focus();
    }
  }

  sendCodeToBackend(fullcode: string): void {
    this.identifier = this.identifier || '';
    if (this.context == 'register') {
      const requestData = {
        phoneNumber: this.identifier,
        code: fullcode,
      };
      this.authService.verifyPhone(requestData).subscribe({
        next: (response) => {
          this.toast.success(response.message, 'Success');
          this.sessionStorage.removeItem('verify_phone');
          this.tokenStorage.setItem('access_token', response.token);
          this.route.navigate(['/']);
        },
        error: (error) => {
          this.toast.error(error.error.message, 'Error');
        },
      });
    } else {
      const requestData = {
        email: this.identifier,
        code: fullcode,
      };
      this.authService.verifyResetCode(requestData).subscribe({
        next: (response) => {
          this.toast.success(response.message, 'Success');
          const expiration = expirationTime(10);
          this.sessionStorage.removeItem('verify_reset_password');
          this.sessionStorage.setItem('change_password', expiration.toString());
          this.route.navigate(['/change-password'], {
            queryParams: {
              email: this.identifier,
            },
          });
        },
        error: (error) => {
          this.toast.error(error.error.message, 'Error');
        },
      });
    }
  }
}
