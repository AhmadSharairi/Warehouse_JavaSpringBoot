import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { WarehouseService } from '../warehouse/warehouse.service';
import { UserService } from '../user/user.service';
import { AuthService } from '../login/auth.service';
import { SupplyDocumentService } from '../SupplyDocument/supply-document.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  curruntRoleName: string = '';
  userName: string = '';
  warehouseCount: number = 0;
  UserCount: number = 0;
  ItemsCount: number = 0;
  SupplyCount: number =0;

  constructor(
    private warehouseService: WarehouseService,
    private userService: UserService,
    private authService: AuthService,
    private supplyService: SupplyDocumentService,
    private route: Router
  ) {}
  ngOnInit(): void {
    this.curruntRoleName = this.authService.getRoleFromToken();
    this.userName = this.authService.getUserNameFromToken();
    this.getWarehouseCount();
    this.getUserCount();
    this.getTotalItemsCount();
    if(this.curruntRoleName === 'Manager')
    {
       this.getSupplyCount();
    }
    this.getCustomSupplyCount();

  }

  getWarehouseCount() {
    this.warehouseService
      .getAllWarehouses()
      .subscribe(
        (data: any) => (this.warehouseCount = data?.length || 0)
      );
  }

  getSupplyCount() {
    this.supplyService
      .getAllSupplyDocuments()
      .subscribe(
        (data: any) => (this.SupplyCount = data?.length || 0)
      );
  }

  getCustomSupplyCount() {
    this.supplyService
      .getSupplyDocumentsByCreated(this.userName)
      .subscribe(
        (data: any) => (this.SupplyCount = data?.length || 0)
      );
  }



  getUserCount() {
    this.userService.getAllUsers().subscribe((data: any) => {
      this.UserCount = data.length || 0;
      console.log(data);
    });
  }

  getTotalItemsCount(): void {
    this.warehouseService.getTotalItemsCount().subscribe(
      (count: number) => {
        this.ItemsCount = count;
        console.log('Total Items Count:', this.ItemsCount);
      },
      (error) => {
        console.error('Error fetching total items count:', error);
      }
    );
  }

  logout() {
    this.authService.SignOut();
  }
  goWarehousPage() {
    this.route.navigate(['/warehouse']);
  }

  goSubDecomentPage() {
    this.route.navigate(['/supply']).then(() => window.location.reload());
  }
}
