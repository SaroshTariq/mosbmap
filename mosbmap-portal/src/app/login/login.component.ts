import { Component, OnInit } from '@angular/core';
import { UsersService } from '../services/users.service';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { AnimationItem } from 'lottie-web';
import { AnimationOptions } from 'ngx-lottie';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  userForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  options: AnimationOptions = {
    path: '/assets/animations/loading-animation.json',
  };

  animate = false;
  errorMessage="";

  constructor(private formBuilder: FormBuilder, private router: Router, private usersService: UsersService,
    private authService: AuthService, private notifyService: NotificationService) { }

  ngOnInit() {
    this.userForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit() {
    this.animate = true;
    try {
      this.usersService.authenticateUser(this.userForm.value).subscribe(res => {
        this.animate = false;

        if (401==res['status']) {
          this.notifyService.error('Invalid email or password.', "Login Failed!");
        } 
        else if(400==res['status']){

        }else if(202==res['status']){
          this.notifyService.success('Successfully logged in.', 'Login Success!');
          this.authService.login(res['data']);
          this.router.navigate(['/panel']);
        }
      },
      err => {
        this.animate = false;
        this.notifyService.error("Internal error.", "Error!");
      });
    } catch (error) {
      this.animate = false;
      this.notifyService.error("Internal error.", "Login Failed!");
    }



  }
}
