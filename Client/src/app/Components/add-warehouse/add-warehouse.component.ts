import { AfterViewInit, Component, OnInit } from '@angular/core';
import { WarehouseInfo } from '../../shared/models/WarehouseInfo';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormArray } from '@angular/forms';
import { WarehouseService } from '../warehouse/warehouse.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { Warehouse } from '../../shared/models/Warehouse';
import { CommonModule } from '@angular/common';
import { AuthService } from '../login/auth.service';

@Component({
  selector: 'app-add-warehouse',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-warehouse.component.html',
  styleUrls: ['./add-warehouse.component.css']
})
export class AddWarehouseComponent implements AfterViewInit, OnInit {
  public warehousForm!: FormGroup;
  loading: boolean = false;
  warehouseCount: number = 0;
  warehouses: WarehouseInfo[] = [];

  constructor(
    private fb: FormBuilder,
    private warehouseService: WarehouseService,
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngAfterViewInit() {
    this.loading = false;
  }

  ngOnInit() {
    this.createCheckoutForm();
  }

  createCheckoutForm() {
    this.warehousForm = this.fb.group({
      name: ['', [Validators.required, Validators.pattern('^[a-zA-Z ]*$')]],
      description: ['', [Validators.required]],
      items: this.fb.array([])
    });
  }

  get items(): FormArray {
    return this.warehousForm.get('items') as FormArray;
  }

  addItem() {
    const itemForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      quantity: [0, [Validators.required, Validators.min(1)]]
    });
    this.items.push(itemForm);
  }

  removeItem(index: number) {
    this.items.removeAt(index);
  }

  isInvalid(controlName: string): boolean {
    const control = this.warehousForm.get(controlName);
    return !!(control?.invalid && (control.dirty || control.touched));
  }

  saveWarehouse() {
    if (this.warehousForm.valid) {
      const newWarehouse: Warehouse = {
        warehouseName: this.warehousForm.value.name,
        warehouseDescription: this.warehousForm.value.description,
        items: this.warehousForm.value.items,
        createdBy: this.authService.getUserNameFromToken(),
      };

      console.log('New Warehouse Data:', newWarehouse);

      this.warehouseService.addWarehouseWithItems(newWarehouse).subscribe({
        next: (createdWarehouse) => {
          this.toastr.success('Warehouse added successfully');
          this.warehousForm.reset();
          this.router.navigate(['/warehouse']).then(() => {
            window.location.reload();
          });
        },
        error: (err: any) => {
          console.error('Error adding warehouse:', err);
          this.toastr.error('Error adding warehouse. Please try again.');
        },
      });
    } else {
      this.toastr.error('Please fill out all required fields.');
    }
  }
}
