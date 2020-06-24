import { Component, OnInit } from '@angular/core';
import { UsersService } from 'src/app/services/users.service';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users = [];
  searchString = "";
  searchBy = "username";

  constructor(private userService: UsersService, private notifyService: NotificationService) { }

  ngOnInit() {
    this.userService.getUsers().subscribe(res => {
      
      if (401 == res['status']) {
        this.notifyService.error('Please login to access this users.', "Unauthenticated!");

      } else if (200 == res['status']) {
        this.users = res['data'];
      }
    });
  }

}
