import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { UsersService } from 'src/app/services/users.service';
import { RolesService } from 'src/app/services/roles.service';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-new-user-modal',
  templateUrl: './new-user-modal.component.html',
  styleUrls: ['./new-user-modal.component.css']
})
export class NewUserModalComponent implements OnInit {
  @Output() action = new EventEmitter<any>();


  newForm = new FormGroup({
    username: new FormControl(''),
    email: new FormControl(''),
    name: new FormControl(''),
    roleId: new FormControl(''),
    password: new FormControl(''),
  });

  roles = [];



  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder,
    private notifyService: NotificationService,
    private userService: UsersService,
    private rolesService: RolesService) { }

  ngOnInit() {
    this.newForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required]],
      name: ['', [Validators.required]],
      roleId: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
    this.loadRoles();
  }

  loadRoles() {
    this.rolesService.getRoles().subscribe(res => {
      this.roles = res['data'].content;
      console.log(res);
      
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


  save() {
    this.userService.addUser(this.newForm.value).subscribe(res => {
      this.modalRef.hide();
      this.action.emit('saved');
    }, err => {

    });


  }

  cancel() {
    this.modalRef.hide();
    this.action.emit('canceled');
  }
}
