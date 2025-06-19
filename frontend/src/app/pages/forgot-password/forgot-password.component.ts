import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { NgClass } from '@angular/common';
import { SessionStorage } from '../../storage/sessionStorage.service';
import { expirationTime } from '../../utils/ExpirationTimeUtil';
@Component({
  selector: 'app-forgot-password',
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './forgot-password.component.html',
})
export class ForgotPasswordComponent {
  private authService = inject(AuthService);
  private toast = inject(ToastrService);
  private route = inject(Router);
  private sessionStorage = inject(SessionStorage);
  ForgotPasswordForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  onSubmit() {
    this.ForgotPasswordForm.markAllAsTouched();
    if (this.ForgotPasswordForm.valid) {
      const email = this.ForgotPasswordForm.get('email')?.value;
      if (email) {
        this.authService.forgotPassword(email).subscribe({
          next: () => {
            this.toast.success(
              'Check your email for the password reset link.',
              'Success'
            );
            const expiration = expirationTime(10);
            this.sessionStorage.setItem(
              'verify_reset_password',
              expiration.toString()
            );
            this.route.navigate(['/verify-reset-code'], {
              queryParams: {
                identifier: email,
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
  get email() {
    return this.ForgotPasswordForm.get('email');
  }
}
