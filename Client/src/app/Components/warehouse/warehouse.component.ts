import {  Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { WarehouseService } from './warehouse.service';
import { WarehouseInfo } from '../../shared/models/WarehouseInfo';
import { Router } from '@angular/router';

@Component({
  selector: 'app-warehouse',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './warehouse.component.html',
  styleUrls: ['./warehouse.component.css'],
})
export class WarehouseComponent implements OnInit {
  loading: boolean = true;
  warehouseCount: number = 0;
  warehouses: WarehouseInfo[] = [];

  constructor(
    private warehouseService: WarehouseService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit() {
    this.getAllWarehouses();
  }

  getAllWarehouses(): void {
    this.warehouseService.getAllWarehouseInfo().subscribe(
      (data: WarehouseInfo[]) => {
        console.log('API response:', data);
        if (data) {
          this.warehouses = data;
          this.warehouseCount = data.length;
        } else {
          this.warehouses = [];
        }
        this.loading = false;
      },
      (error) => {
        console.error('Error fetching warehouse data:', error);
        // this.toastr.error('Error fetching warehouse data. Please try again.');
        this.loading = false;
      }
    );
  }

  removeWarehouse(warehouseId: number)
   {
    this.warehouseService.removeWarehouse(warehouseId).subscribe(response => {
      this.warehouses = this.warehouses.filter(warehouse => warehouse.id !== warehouseId);
      this.warehouseCount = this.warehouses.length;
    });
  }


  checkItems(warehouseId: number) {
    this.router.navigate(['/warehouses', warehouseId]);
  }

  addWarehouse() {
    this.router.navigate(['/add-warehouse']);
  }


  exportWarehouses() {
    this.warehouseService.exportWarehouses().subscribe(response => {
      const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'warehouses.xlsx';
      a.click();
      window.URL.revokeObjectURL(url);
      this.toastr.success('Warehouses exported successfully!');
    }, error => {
      console.error('Error exporting warehouses:', error);
      this.toastr.error('Failed to export warehouses. Please try again.');
    });
  }




}
