export class verifyPhone {
  phoneNumber: string;
  code: string;

  constructor(phoneNumber: string, code: string) {
    this.phoneNumber = phoneNumber;
    this.code = code;
  }
}
