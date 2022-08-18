import { Component, OnInit } from '@angular/core';
import { UserService } from '../user-service.service';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {

  id: number;
  user: User = new User();

  constructor(private userService: UserService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.userService.getUserById(this.id).subscribe(data => {
      this.user = data;
    }, error => console.log(error));
  }

  onSubmit() {
    this.userService.updateUser(this.id,this.user).subscribe(data => this.goToUserList());
  }  
  
  goToUserList(){
    this.router.navigate(['/users']);
  }

}
