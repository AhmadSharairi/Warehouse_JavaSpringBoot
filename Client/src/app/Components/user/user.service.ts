import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import { UserInfo } from '../../shared/models/UserInfo';
import { Role } from '../../shared/models/Role';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';


  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserInfo[]> {
    return this.http.get<any>(this.apiUrl).pipe(map((response) => response));
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
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  addUser(user: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, user);
  }

  updateUser(id: number, user: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, user);
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }


}
