import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from '../../storage/localStorage.service';

@Component({
  selector: 'app-logout',
  imports: [],
  templateUrl: './logout.component.html',
})
export class LogoutComponent {
  private route = inject(Router);
  private TokenStorage = inject(TokenStorageService);
  Logout() {
    this.TokenStorage.removeItem('access_token');
    this.route.navigate(['/login']);
  }

  Cancel() {
    this.route.navigate(['/']);
  }
}
