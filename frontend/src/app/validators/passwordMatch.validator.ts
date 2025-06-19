import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function passwordMatchValidator(
  passwordKey: string,
  confirmPasswordKey: string,
): ValidatorFn {
  return (form: AbstractControl): ValidationErrors | null => {
    const password = form.get(passwordKey)?.value;
    const confirmPassword = form.get(confirmPasswordKey)?.value;

    if (password !== confirmPassword) {
      form.get(confirmPasswordKey)?.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }

    if (form.get(confirmPasswordKey)?.hasError('passwordMismatch')) {
      form.get(confirmPasswordKey)?.setErrors(null);
    }

    return null;
  };
}
