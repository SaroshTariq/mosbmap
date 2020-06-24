import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }


  login(data) {
    console.log(data);
    localStorage.setItem('sessionId', data.session.id);
    localStorage.setItem('sessionExpiry', data.session.expiry);
    localStorage.setItem('userAuthorities', data.authorities);
  }


  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    if (undefined != localStorage.getItem('sessionId') && 0 != localStorage.getItem('sessionId').length) {
      return true;
    } else {
      return true;
    }

  }
}
