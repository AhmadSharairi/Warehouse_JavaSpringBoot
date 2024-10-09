import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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


  getAllWarehouses(): Observable<Warehouse[]> {
    return this.http.get<Warehouse[]>(this.apiUrl);
  }

  getWarehouseById(id: number): Observable<Warehouse> {
    return this.http.get<Warehouse>(`${this.apiUrl}/${id}`);
  }


  addWarehouse(warehouse: Warehouse): Observable<Warehouse>
  {
    return this.http.post<Warehouse>(this.apiUrl, warehouse);
  }


  addWarehouseWithItems(warehouse: Warehouse): Observable<Warehouse> {
    return this.http.post<Warehouse>(`${this.apiUrl}/withItems`, warehouse);



}
  getAllWarehouseInfo(): Observable<WarehouseInfo[]> {
    return this.http.get<WarehouseInfo[]>(`${this.apiUrl}/info`);
  }


  getItemsByWarehouseId(warehouseId: number): Observable<Item[]> {
    return this.http.get<string[]>(`${this.apiUrl}/${warehouseId}/items`).pipe(
      map((data: string[]) => {
        return data.map(itemString => {
          const itemParts = itemString.split(', ');
          const itemObj: Item = {
            Name: itemParts[0].split(': ')[1],
            Description: itemParts[1].split(': ')[1],
            Quantity: Number(itemParts[2].split(': ')[1])
            ,
            id: 0
          };
          return itemObj;
        });
      })
    );
  }

  removeWarehouse(warehouseId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${warehouseId}`);
  }

  getTotalItemsCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/items/count`);
  }






}
