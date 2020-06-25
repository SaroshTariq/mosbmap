import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ClientService } from './client.service';

@Injectable({
  providedIn: 'root'
})
export class RolesService {
  baseUrl: string = environment.rolesservice_baseUrl;
  baseEndpoint: string = environment.rolesservice_baseEndpoint;

  addRole(object){
    return this.clientService.postRequest(this.baseUrl+this.baseEndpoint, object);
  }

  updateRole(id, object){
    return this.clientService.putRequest(this.baseUrl+this.baseEndpoint+id+"/", object);
  }

  getRoles(){
    return this.clientService.getRequest(this.baseUrl+this.baseEndpoint);
  }

  getRole(id){
    return this.clientService.getRequest(this.baseUrl+this.baseEndpoint+id+"/");
  }

  deleteRole(id){
    return this.clientService.deleteRequest(this.baseUrl+this.baseEndpoint+id+"/");
  }
  constructor(private clientService: ClientService) { }
}
