import { Component, OnInit } from '@angular/core';
import { UsersService } from 'src/app/services/users.service';
import { NotificationService } from 'src/app/services/notification.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { NewUserModalComponent } from './new-user-modal/new-user-modal.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users = [];

  searchString = "";
  searchBy = "username";
  paging = {
    page: "0",
    pageSize: 10,
  };
  pages = [];

  modalRef: BsModalRef;
  constructor(private userService: UsersService, private notifyService: NotificationService,
    private modalService: BsModalService) { }

  ngOnInit() {
    this.loadUsers();
  }
  loadUsers() {

    var queryString = "?"+Object.keys(this.paging).map(key => key + '=' + this.paging[key]).join('&');
    var searchQuery = "";
    if (undefined != this.searchString && 0 < this.searchString.length) {
      searchQuery = "&"+this.searchBy + "=" + this.searchString
    }

    queryString = queryString+searchQuery;

    this.userService.getUsers(queryString).subscribe(res => {

      //data.content is the list
      this.users = res['data'].content;
      this.pages = [];
      for (var i = 0; i < res['data'].totalPages; i++) {
        this.pages.push(i + "");
      }

      //getting page size and current page number from list
      this.paging.pageSize = res['data'].size;
      this.paging.page = res['data'].number;
      
      
    }, err => {
      if (401 == err.status) {
        this.notifyService.error('Please login to access.', "Unauthenticated!");
      } else if (403 == err.status) {
        this.notifyService.error('You are not authorized to access this resource.', "Unauthorized!");
      } else {
        this.notifyService.error("Internal error.", "Error!");
      }
    });

  }

  openCreateModal(){
    let config = {
      backdrop: true,
      ignoreBackdropClick: true,
      class: 'modal-dialog-centered modal-lg  scroll-wrap'
    }
    this.modalRef = this.modalService.show(NewUserModalComponent,  config);
    this.modalRef.content.action.subscribe(res => {
      if(res=='saved'){
        this.loadUsers();
      }
    });
  }

}
