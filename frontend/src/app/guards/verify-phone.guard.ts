import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionStorage } from '../storage/sessionStorage.service';

export const verifyPhoneGuard: CanActivateFn = (route, state) => {
  const sessionStorage = inject(SessionStorage);
  const router = inject(Router);
  const IsAvailable = sessionStorage.getItem('verify_phone');
  if (IsAvailable) {
    if (Number(IsAvailable) < Date.now()) {
      sessionStorage.removeItem('verify_phone');
      router.navigate(['/']);
      return false;
    }
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};
