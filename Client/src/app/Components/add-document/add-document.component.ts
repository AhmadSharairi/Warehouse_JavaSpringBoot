import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreatedSupplyDocument } from '../../shared/models/CreatedSupplyDocument';
import { SupplyDocumentService } from '../SupplyDocument/supply-document.service';
import { AuthService } from '../login/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { WarehouseService } from '../warehouse/warehouse.service';
import { CommonModule } from '@angular/common';
import { Warehouse } from '../../shared/models/Warehouse';
import { Item } from '../../shared/models/Item';

@Component({
  selector: 'app-add-document',
  templateUrl: './add-document.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  styleUrls: ['./add-document.component.scss'],
})
export class AddDocumentComponent implements AfterViewInit, OnInit {
  public documntForm!: FormGroup;
  loading: boolean = false;
  warehouses: Warehouse[] = [];
  items: Item[] = []; 

  constructor(
    private fb: FormBuilder,
    private documntervice: SupplyDocumentService,
    private warehouseService: WarehouseService,
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngAfterViewInit() {
    this.loading = false;
  }

  ngOnInit() {
    this.createDocumentForm();
    this.fetchWarehouses();
  }

  createDocumentForm() {
    this.documntForm = this.fb.group({
      name: ['', Validators.required],
      subject: ['', Validators.required],
      createdBy: [this.authService.getUserNameFromToken(), Validators.required],
      status: ['Pending', Validators.required],
      warehouseId: [null, Validators.required],
      itemId: [{ value: null, disabled: true }, Validators.required], 
    });
  }

  fetchWarehouses() {
    this.warehouseService.getAllWarehouses().subscribe(
      (warehouses: Warehouse[]) => {
        this.warehouses = warehouses;
      },
      (error) => {
        console.error('Error fetching warehouses:', error);
        this.toastr.error('Could not load warehouses.');
      }
    );
  }

  onWarehouseChange(event: Event) {
    const target = event.target as HTMLSelectElement;
    const warehouseId = +target.value;
    this.items = []; 

    this.documntForm.get('itemId')?.disable(); 

    if (warehouseId) {
      this.warehouseService.getItemsByWarehouseId(warehouseId).subscribe(
        (items: Item[]) => {
          this.items = items;
          this.documntForm.get('itemId')?.enable();
        },
        (error) => {
          console.error('Error fetching items:', error);
          this.toastr.error('Could not load items for the selected warehouse.');
        }
      );
    }
  }

  isInvalid(controlName: string): boolean {
    const control = this.documntForm.get(controlName);
    return !!(control?.invalid && (control.dirty || control.touched));
  }

  savedocument() {
    if (this.documntForm.valid) {
      const newDocument: CreatedSupplyDocument = {
        name: this.documntForm.value.name,
        subject: this.documntForm.value.subject,
        status: this.documntForm.value.status,
        createdBy: this.authService.getUserNameFromToken(),
        warehouseId: this.documntForm.value.warehouseId,
        itemId: this.documntForm.value.itemId,
      };

      this.documntervice.createSupplyDocument(newDocument).subscribe({
        next: (createdDocument) => {
          this.toastr.success('Document added successfully');
          this.documntForm.reset();
          this.router.navigate(['/supply']);
        },
        error: (err) => {
          console.error('Error adding document:', err);
          this.toastr.error('Error adding document. Please try again.');
        },
      });
    } else {
      this.toastr.error('Please fill out all required fields.');
    }
  }


}
