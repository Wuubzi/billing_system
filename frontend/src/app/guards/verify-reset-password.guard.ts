import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionStorage } from '../storage/sessionStorage.service';

export const verifyResetPasswordGuard: CanActivateFn = (route, state) => {
  const sessionStorage = inject(SessionStorage);
  const router = inject(Router);
  const IsAvailable = sessionStorage.getItem('verify_reset_password');
  if (IsAvailable) {
    if (Number(IsAvailable) < Date.now()) {
      sessionStorage.removeItem('verify_reset_password');
      router.navigate(['/']);
      return false;
    }
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};
