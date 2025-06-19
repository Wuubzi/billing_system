import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { VerifyPhoneComponent } from './pages/verify-phone/verify-phone.component';
import { ChangePasswordComponent } from './pages/change-password/change-password.component';
import { VerifyResetCodeComponent } from './pages/verify-reset-code/verify-reset-code.component';
import { authGuard } from './guards/auth.guard';
import { alreadyAuthGuard } from './guards/already-auth.guard';
import { LogoutComponent } from './pages/logout/logout.component';
import { changePasswordGuard } from './guards/change-password.guard';
import { verifyResetPasswordGuard } from './guards/verify-reset-password.guard';
import { verifyPhoneGuard } from './guards/verify-phone.guard';

export const routes: Routes = [
  {
    path: '',
    title: 'Dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
  {
    path: 'login',
    title: 'Login',
    component: LoginComponent,
    canActivate: [alreadyAuthGuard],
  },
  {
    path: 'register',
    title: 'Register',
    component: RegisterComponent,
    canActivate: [alreadyAuthGuard],
  },
  {
    path: 'forgot-password',
    title: 'Forgot Password',
    component: ForgotPasswordComponent,
    canActivate: [alreadyAuthGuard],
  },
  {
    path: 'change-password',
    title: 'Change Password',
    component: ChangePasswordComponent,
    canActivate: [changePasswordGuard],
  },
  {
    path: 'verify-reset-code',
    title: 'Verify Reset Code',
    component: VerifyResetCodeComponent,
    canActivate: [verifyResetPasswordGuard],
  },
  {
    path: 'verify-phone',
    title: 'Verify Phone',
    component: VerifyPhoneComponent,
    canActivate: [verifyPhoneGuard],
  },
  {
    path: 'logout',
    title: 'Logout',
    component: LogoutComponent,
    canActivate: [authGuard],
  },
  {
    path: '**',
    title: 'Not Found',
    component: NotFoundComponent,
  },
];
