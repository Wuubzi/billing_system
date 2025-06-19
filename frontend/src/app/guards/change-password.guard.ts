import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionStorage } from '../storage/sessionStorage.service';
import { isExpired } from '../utils/ExpirationTimeUtil';

export const changePasswordGuard: CanActivateFn = (route, state) => {
  const sessionStorage = inject(SessionStorage);
  const router = inject(Router);
  const IsAvailable = sessionStorage.getItem('change_password');
  if (IsAvailable) {
    if (isExpired(Number(IsAvailable))) {
      sessionStorage.removeItem('change_password');
      router.navigate(['/']);
      return false;
    }
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};
