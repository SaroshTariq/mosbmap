import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  setting = {
    timeOut: 10000,
    progressBar: true
  };

  constructor(private toastr: ToastrService) { }

  success(message, title) {
    this.toastr.success(message, title, this.setting);
  }

  error(message, title) {
    this.toastr.error(message, title, this.setting);
  }

  info(message, title) {
    this.toastr.info(message, title, this.setting);
  }

  warning(message, title) {
    this.toastr.warning(message, title, this.setting);
  }

}