import { Component, inject, Input } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { passwordMatchValidator } from '../../validators/passwordMatch.validator';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-change-password',
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './change-password.component.html',
})
export class ChangePasswordComponent {
  @Input() email?: string;
  private authService = inject(AuthService);
  private toast = inject(ToastrService);
  private router = inject(Router);
  private activatedRouter = inject(ActivatedRoute);
  ChangePasswordForm = new FormGroup(
    {
      newPassword: new FormControl('', [
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
      validators: passwordMatchValidator('newPassword', 'confirmPassword'),
    }
  );

  ngOnInit(): void {
    this.activatedRouter.queryParams.subscribe((params) => {
      this.email = params['email'];
    });
  }

  onSubmit() {
    this.ChangePasswordForm.markAllAsTouched();
    if (this.ChangePasswordForm.valid) {
      const ChangePasswordData = {
        email: this.email || '',
        password: this.ChangePasswordForm.get('newPassword')?.value || '',
        confirm_password:
          this.ChangePasswordForm.get('confirmPassword')?.value || '',
      };

      this.authService.changePassword(ChangePasswordData).subscribe({
        next: () => {
          this.toast.success('Password changed successfully', 'Success');
          this.ChangePasswordForm.reset();
          this.router.navigate(['/']);
        },
        error: (error) => {
          this.toast.error(error.error.message, 'Error');
        },
      });
    }
  }

  get newPassword() {
    return this.ChangePasswordForm.get('newPassword');
  }
  get confirmPassword() {
    return this.ChangePasswordForm.get('confirmPassword');
  }
}
