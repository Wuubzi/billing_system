import { Component } from '@angular/core';
import { PinComponent } from '../../components/pin/pin.component';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-verify-phone',
  imports: [PinComponent, CommonModule],
  templateUrl: './verify-phone.component.html',
})
export class VerifyPhoneComponent {}
