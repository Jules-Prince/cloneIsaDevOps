import {Injectable} from '@nestjs/common';

import {PaymentDto} from './dto/payment.dto';
import {PaymentRejectedException} from './exceptions/payment-rejected-exception';

@Injectable()
export class AppService {

  private static readonly magicKey : string = '896983'; // ASCII code for 'YES'

  private transactions : Array<PaymentDto>;

  constructor() {
    this.transactions = [];
  }

  findAll(): PaymentDto[] {
    return this.transactions;
  }

  pay(paymentDto: PaymentDto): PaymentDto {
    if (paymentDto.creditCard.includes(AppService.magicKey) && /^\d+$/.test(paymentDto.creditCard)) {
        this.transactions.push(paymentDto);
        console.log("Bank withdrawal made in the amount of "+paymentDto.amount+" with credit card :"+paymentDto.creditCard);
        return paymentDto;
      } else {
      console.log("Echec of bank withdrawal made in the amount of "+paymentDto.amount+" with credit card :"+paymentDto.creditCard);
      throw new PaymentRejectedException(paymentDto.amount);
      }
    }

}
