import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  users: User[];

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit() {
    this.userService.findAll().subscribe(data => {
      this.users = data;
    });
  }

  private getUsers() {
    this.userService.findAll().subscribe(data => {
      this.users = data;
    });
  }

  updateUser(id: number) {
    this.router.navigate(['users',id]);
  }

  deleteUser(id: number) {
    this.userService.deleteUserById(id).subscribe(data => {
      console.log(data);
      this.getUsers();
    })
  }
}