import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import { UserInfo } from '../../shared/models/UserInfo';
import { Role } from '../../shared/models/Role';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }


  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserInfo[]> {
    return this.http.get<any>(this.apiUrl  , { headers: this.getHeaders() }).pipe(map((response) => response));
  }

  // getRoles(): Observable<Role[]> {
  //   return this.http.get<Role[]>(this.apiRole).pipe(
  //     map((response) => response),
  //     catchError((error) => {
  //       console.error('Error fetching roles:', error);
  //       return throwError(error);
  //     })
  //   );
  // }


  getUserById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}` ,  { headers: this.getHeaders() });
  }

  addUser(user: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, user ,  { headers: this.getHeaders() }); 
  }

  updateUser(id: number, user: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, user ,  { headers: this.getHeaders() });
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}` ,  { headers: this.getHeaders() });
  }


}
