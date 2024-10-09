import { Component, OnInit } from '@angular/core';
import { UserService } from '../user/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Role } from '../../shared/models/Role';

@Component({
  selector: 'app-update-user',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css'],
})
export class UpdateUserComponent implements OnInit {
  updateUserForm!: FormGroup;
  userId!: number;
  roles: Role[] = [];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.userUpdateForm();
    this.loadUserDetails();
    // this.getAllRoles();

    // Subscribe to changes in roleName
    this.updateUserForm.get('roleName')?.valueChanges.subscribe(value => {
      console.log('Current roleName:', value);
    });
  }

  userUpdateForm() {
    this.userId = +this.route.snapshot.paramMap.get('id')!;
    this.updateUserForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      fullName: ['', Validators.required],
      isActive: [false],
      roleId: ['', Validators.required]
    });
  }

  loadUserDetails() {
    this.userService.getUserById(this.userId).subscribe((user) => {
      this.updateUserForm.patchValue({
        email: user.email,
        fullName: user.fullName,
        roleId: user.roleId,
        isActive: user.isActive,
      });
    });
  }




  onSubmit() {
    if (this.updateUserForm.valid) {
      const userData = { ...this.updateUserForm.value, id: this.userId }

      this.userService.updateUser(this.userId, userData).subscribe(() => {
        alert('User updated successfully');
        this.router.navigate(['/users']);
        console.log('Updated Value is ', userData);
      });
    }
  }

  // getAllRoles() {
  //   this.userService.getRoles().subscribe(
  //     (data: Role[]) => {
  //       this.roles = data;
  //       console.log('the roles ::: ', this.roles);
  //     },
  //     (err: Error) => {
  //       console.log('Error fetching data:', err);
  //     }
  //   );
  // }
}
