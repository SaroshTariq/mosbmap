import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ClientService } from './client.service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  baseUrl: string = environment.usersservice_baseUrl;
  baseEndpoint: string = environment.usersservice_baseEndpoint;

  addUser(object){
    return this.clientService.postRequest(this.baseUrl+this.baseEndpoint, object);
  }

  updateUser(id, object){
    return this.clientService.putRequest(this.baseUrl+this.baseEndpoint+id+"/", object);
  }

  getUsers(queryString){
    return this.clientService.getRequest(this.baseUrl+this.baseEndpoint+queryString);
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
