import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ClientService } from './client.service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  baseUrl: string = environment.usersservice_baseUrl;
  baseEndpoint: string = environment.usersservice_baseEndpoint;

  addUser(user){
    return this.clientService.postRequest(this.baseUrl+this.baseEndpoint, user);
  }

  updateUser(id, user){
    return this.clientService.putRequest(this.baseUrl+this.baseEndpoint+id+"/", user);
  }

  getUsers(){
    return this.clientService.getRequest(this.baseUrl+this.baseEndpoint);
  }

  getUser(id){
    return this.clientService.getRequest(this.baseUrl+this.baseEndpoint+id+"/");
  }

  deleteUser(id){
    return this.clientService.deleteRequest(this.baseUrl+this.baseEndpoint+id+"/");
  }

  authenticateUser(user){
    return this.clientService.postRequest(this.baseUrl+this.baseEndpoint+"authenticate", user);
  }

  constructor(private clientService: ClientService) { }
}
