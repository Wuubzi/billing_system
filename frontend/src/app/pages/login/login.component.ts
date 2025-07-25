import { NgClass } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';
import { TokenStorageService } from '../../storage/localStorage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [NgClass, ReactiveFormsModule],
  standalone: true,
  templateUrl: './login.component.html',
})
export class LoginComponent {
  private toast = inject(ToastrService);
  private tokenStorage = inject(TokenStorageService);
  private router = inject(Router);
  private authService = inject(AuthService);
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  });
  onSubmit() {
    this.loginForm.markAllAsTouched();

    if (this.loginForm.valid) {
      const formData = this.loginForm.value;
      const loginData = {
        email: formData.email || '',
        password: formData.password || '',
      };
      this.authService.login(loginData).subscribe({
        next: (response) => {
          this.loginForm.reset();
          this.showToast();
          this.tokenStorage.setItem('access_token', response.token);
          this.router.navigate(['/']);
        },
        error: (error) => {
          this.showErrorToast(error.error.message);
        },
      });
    }
  }

  showToast() {
    this.toast.success('Login successful', 'Success');
  }

  showErrorToast(error: string) {
    this.toast.error(error, 'Error');
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
