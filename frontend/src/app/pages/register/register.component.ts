import { NgClass } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { passwordMatchValidator } from '../../validators/passwordMatch.validator';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/register-request.model';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { SessionStorage } from '../../storage/sessionStorage.service';
import { expirationTime } from '../../utils/ExpirationTimeUtil';
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  private toastr = inject(ToastrService);
  private router = inject(Router);
  private sessionStorage = inject(SessionStorage);
  private authService = inject(AuthService);
  registerForm = new FormGroup(
    {
      name: new FormControl('', Validators.required),
      lastName: new FormControl(''),
      email: new FormControl('', [Validators.required, Validators.email]),
      phone: new FormControl('', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(10),
        Validators.pattern('^[0-9]*$'),
      ]),
      address: new FormControl(''),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(24),
        Validators.pattern(
          '^(?=.*[A-Z])(?=.*\\d{2,})(?=.*[!@#$%^&*()_+\\-=\\[\\]{};\':"\\\\|,.<>\\/?]).*$'
        ),
      ]),
      confirmPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(24),
        Validators.pattern(
          '^(?=.*[A-Z])(?=.*\\d{2,})(?=.*[!@#$%^&*()_+\\-=\\[\\]{};\':"\\\\|,.<>\\/?]).*$'
        ),
      ]),
    },
    {
      validators: passwordMatchValidator('password', 'confirmPassword'),
    }
  );

  onSubmit() {
    this.registerForm.markAllAsTouched();
    if (this.registerForm.valid) {
      const formData = this.registerForm.value;
      const registerData: RegisterRequest = {
        name: formData.name || '',
        last_name: formData.lastName || '',
        email: formData.email || '',
        phone_number: formData.phone || '',
        address: formData.address || '',
        password: formData.password || '',
        confirmPassword: formData.confirmPassword || '',
      };
      this.authService.register(registerData).subscribe({
        next: () => {
          this.toastr.success('Registration successful', 'Success');
          this.registerForm.reset();
          sessionStorage.setItem('isAvailaleForVerification', 'true');
          const expiration = expirationTime(10);
          this.sessionStorage.setItem('verify_phone', expiration.toString());
          this.router.navigate(['/verify-phone'], {
            queryParams: {
              identifier: registerData.phone_number,
            },
          });
        },
        error: (error) => this.toastr.error(error.error.message, 'Error'),
      });
    }
  }

  get name() {
    return this.registerForm.get('name');
  }

  get lastName() {
    return this.registerForm.get('lastName');
  }

  get email() {
    return this.registerForm.get('email');
  }

  get phone() {
    return this.registerForm.get('phone');
  }

  get address() {
    return this.registerForm.get('address');
  }

  get password() {
    return this.registerForm.get('password');
  }

  get confirmPassword() {
    return this.registerForm.get('confirmPassword');
  }
}
