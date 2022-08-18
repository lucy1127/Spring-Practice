import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { DeleteUserComponent } from './delete-user/delete-user.component';

const routes: Routes = [
  { path: 'users', component: UserListComponent },//查詢
  { path: 'adduser', component: UserFormComponent }, //新增
  { path: 'users/:id',component:EditUserComponent}, //修改
  { path: 'users/:id', component: DeleteUserComponent } //刪除
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }