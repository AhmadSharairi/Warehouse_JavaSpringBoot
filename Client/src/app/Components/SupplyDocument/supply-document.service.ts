import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { CreatedSupplyDocument } from '../../shared/models/CreatedSupplyDocument';
import { SupplyDocumentInfo } from '../../shared/models/SupplyDocumentInfo';


@Injectable({
  providedIn: 'root'
})
export class SupplyDocumentService {

  private apiUrl = 'http://localhost:8080/api/supplydocuments';

  constructor(private http: HttpClient) {}


  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }


  createSupplyDocument(supplyDocument: CreatedSupplyDocument): Observable<CreatedSupplyDocument> {
    const headers = this.getHeaders();
    return this.http.post<CreatedSupplyDocument>(`${this.apiUrl}/create`, supplyDocument, { headers });
  }


  updateSupplyDocument(supplyId: number, supplyDocument: CreatedSupplyDocument): Observable<CreatedSupplyDocument> {
    const headers = this.getHeaders();
    return this.http.put<CreatedSupplyDocument>(`${this.apiUrl}/update/${supplyId}`, supplyDocument, { headers });
  }

  changeSupplyDocumentStatus(supplyId: number, status: string) {
    const body = { status }; 
  
    return this.http.put(`${this.apiUrl}/status/${supplyId}`, body).pipe(
      catchError((error) => {
        console.error('Error updating supply status:', error);
        return throwError(error);
      })
    );
  }
  
  deleteSupplyDocument(supplyId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.apiUrl}/${supplyId}`, { headers });
  }



  getAllSupplyDocuments(): Observable<SupplyDocumentInfo[]> {
    const headers = this.getHeaders();
    return this.http.get<SupplyDocumentInfo[]>(`${this.apiUrl}/all`, { headers });
  }

  getSupplyDocumentsByCreated(createdBy: string): Observable<SupplyDocumentInfo[]> {
    return this.http.get<SupplyDocumentInfo[]>(`${this.apiUrl}/createdBy/${createdBy}`);
  }

}
