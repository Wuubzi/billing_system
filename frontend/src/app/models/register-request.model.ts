export class RegisterRequest {
  name: string;
  last_name: string;
  email: string;
  phone_number: string;
  address: string;
  password: string;
  confirmPassword: string;

  constructor(
    name: string,
    last_name: string,
    email: string,
    phone_number: string,
    address: string,
    password: string,
    confirmPassword: string
  ) {
    this.name = name;
    this.last_name = last_name;
    this.email = email;
    this.phone_number = phone_number;
    this.address = address;
    this.password = password;
    this.confirmPassword = confirmPassword;
  }
}
