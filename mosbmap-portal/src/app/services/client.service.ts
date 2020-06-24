import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  getRequest(url){
    console.log(url);
    return this.http.get(url)
  }

  postRequest(url, object){
    console.log(url);
    console.log(object);
    return this.http.post(url, object);
  }

  putRequest(url: string, object){
    console.log(url);
    console.log(object);
    return this.http.put(url, object);
  }

  deleteRequest(url){
    console.log(url);
    return this.http.delete(url);
  }
  constructor(private http: HttpClient) { }
}
