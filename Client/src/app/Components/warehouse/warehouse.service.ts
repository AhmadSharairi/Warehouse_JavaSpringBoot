import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Warehouse } from '../../shared/models/Warehouse';
import { Item } from '../../shared/models/Item';
import { WarehouseInfo } from '../../shared/models/WarehouseInfo';

@Injectable({
  providedIn: 'root',
})
export class WarehouseService {
  private apiUrl = 'http://localhost:8080/api/warehouses';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getAllWarehouses(): Observable<Warehouse[]> {
    return this.http.get<Warehouse[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  getWarehouseById(id: number): Observable<Warehouse> {
    return this.http.get<Warehouse>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  addWarehouse(warehouse: Warehouse): Observable<Warehouse> {
    return this.http.post<Warehouse>(this.apiUrl, warehouse, { headers: this.getHeaders() });
  }

  addWarehouseWithItems(warehouse: Warehouse): Observable<Warehouse> {
    return this.http.post<Warehouse>(`${this.apiUrl}/withItems`, warehouse, { headers: this.getHeaders() });
  }

  getAllWarehouseInfo(): Observable<WarehouseInfo[]> {
    return this.http.get<WarehouseInfo[]>(`${this.apiUrl}/info`, { headers: this.getHeaders() });
  }

getItemsByWarehouseId(warehouseId: number): Observable<Item[]>
{
    return this.http.get<Item[]>(`${this.apiUrl}/${warehouseId}/items`);
  }
  removeWarehouse(warehouseId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${warehouseId}`, { headers: this.getHeaders() });
  }

  getTotalItemsCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/items/count`, { headers: this.getHeaders() });
  }

   exportWarehouses(): Observable<Blob> {
    return this.http.post(this.apiUrl + '/export', {}, { responseType: 'blob' });
  }
}
