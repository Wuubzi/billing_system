export class ChangePassword {
  email: string;
  password: string;
  confirm_password: string;

  constructor(email: string, password: string, confirm_password: string) {
    this.email = email;
    this.password = password;
    this.confirm_password = confirm_password;
  }
}
