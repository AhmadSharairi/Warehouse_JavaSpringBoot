import { Component, OnInit } from '@angular/core';
import { UserService } from './user.service';
import { ToastrService } from 'ngx-toastr';
import { UserInfo } from '../../shared/models/UserInfo';
import { CommonModule } from '@angular/common';
import { AuthService } from '../login/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
})
export class UserComponent implements OnInit {
  users: UserInfo[] = [];
  emailUser: string = '';
  curruntRoleName: string = '';

  constructor(
    private userService: UserService,
    private toastr: ToastrService,
    private authSerive: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getRoleByToken();
    this.getEmailFromToken();
    this.getAllUsers();
  }

  getRoleByToken() {
    this.curruntRoleName = this.authSerive.getRoleFromToken();
    return this.curruntRoleName;
  }

  getEmailFromToken() {
    this.emailUser = this.authSerive.getEmailFromToken();
    return this.emailUser;
  }

  getAllUsers() {
    this.userService.getAllUsers().subscribe(
      (data: UserInfo[]) => {
        this.users = data;
        console.log(this.users);
      },
      (error: any) => {
        console.error('Error fetching users:', error);
        this.toastr.error('Error fetching users. Please try again.');
      }
    );
  }

  editUser(userId: number) {
    this.router.navigate(['/user', userId, 'update-user']);
  }

  changePassword(userId: number) {
    this.router.navigate(['/user', userId, 'change-password']);
  }

  deleteUser(id: number) {
    const confirmDelete = confirm('Are you sure you want to delete this user?');

    if (confirmDelete) {
      this.userService.deleteUser(id).subscribe(
        () => {
          this.toastr.success('User deleted successfully.');
          this.getAllUsers();
        },
        (error) => {
          console.error('Error deleting user:', error);
          this.toastr.error('Error deleting user. Please try again.');
        }
      );
    }
  }

  toggleUserStatus(user: UserInfo) {
    if (user.email === this.emailUser) {
      this.toastr.error('You cannot disable your own email account');
      return;
    }

    const previousStatus = user.isActive;
    user.isActive = !user.isActive;

    this.userService.updateUser(user.id, user).subscribe(
      () => {
        this.toastr.success(
          `User has been ${user.isActive ? 'enabled' : 'disabled'}`
        );
      },
      (error) => {
        console.error('Error updating user status:', error);
        this.toastr.error('Error updating user status. Please try again.');
        user.isActive = previousStatus;
      }
    );
  }
}
