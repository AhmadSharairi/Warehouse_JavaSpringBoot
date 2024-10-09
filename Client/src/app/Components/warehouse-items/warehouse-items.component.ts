import { Component, OnInit } from '@angular/core';
import { WarehouseService } from '../warehouse/warehouse.service';
import { ActivatedRoute } from '@angular/router';
import { Item } from '../../shared/models/Item';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-warehouse-items',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './warehouse-items.component.html',
  styleUrls: ['./warehouse-items.component.scss']
})
export class WarehouseItemsComponent implements OnInit {
  warehouseId: number | undefined;
  items: Item[] = [];
  paginatedItems: Item[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 10;
  errorMessage: string | null = null;

  constructor(private route: ActivatedRoute, private warehouseService: WarehouseService) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.warehouseId = idParam ? +idParam : undefined;
    this.getItems();
  }

  getItems(): void {
    if (this.warehouseId !== undefined) {
      this.warehouseService.getItemsByWarehouseId(this.warehouseId).subscribe(
        (data: Item[]) => {
          console.log('API response:', data);
          this.errorMessage = null; // Reset error message
          if (data && data.length > 0) {
            this.items = data;
            this.updatePaginatedItems();
          } else {
            this.items = [];
            this.paginatedItems = [];
            console.log('No items found for this warehouse.');
          }
        },
        (error) => {
          this.errorMessage = 'Error fetching items warehouse data. Please try again.';
          console.error('Error fetching items warehouse data:', error);
        }
      );
    } else {
      this.errorMessage = 'Warehouse ID is undefined';
      console.error('Warehouse ID is undefined');
    }
  }

  updatePaginatedItems(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    this.paginatedItems = this.items.slice(startIndex, startIndex + this.itemsPerPage);
  }

  nextPage(): void {
    if ((this.currentPage * this.itemsPerPage) < this.items.length) {
      this.currentPage++;
      this.updatePaginatedItems();
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePaginatedItems();
    }
  }

  get totalPages(): number {
    return Math.ceil(this.items.length / this.itemsPerPage);
  }

  showTopItems(): void {
    this.items.sort((a, b) => b.Quantity - a.Quantity);
    this.paginatedItems = this.items.slice(0, 10);
    this.currentPage = 1;
  }

  showLowItems(): void {
    this.items.sort((a, b) => a.Quantity - b.Quantity);
    this.paginatedItems = this.items.slice(0, 10);
    this.currentPage = 1;
  }
}
