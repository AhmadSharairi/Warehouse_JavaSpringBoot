import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { TokenModel } from '../../shared/models/token-api.model';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl: string = 'http://localhost:8080/';

  private userPayload: any;

  constructor(private http: HttpClient, private router: Router) {
    this.userPayload = this.decodeToken();
  }


  loginPost(loginRequest: any): Observable<any> {

    return this.http.post(this.baseUrl + "authenticate", loginRequest)
  }

  signup(signupRequest: any): Observable<any> {
    return this.http.post(this.baseUrl + "sign-up", signupRequest)
  }


  hello(): Observable<any> {
    return this.http.get(this.baseUrl + 'api/hello', {
      headers: this.createAuthorizationHeader()
    });
  }

  private createAuthorizationHeader(): HttpHeaders | undefined {
    const jwtToken = localStorage.getItem('jwtToken');
    if (jwtToken) {
        return new HttpHeaders().set(
            'Authorization', 'Bearer ' + jwtToken
        );
    } else {
        console.log("JWT token not found in the Local Storage");
        return undefined;
    }
}



  // store Token From BACKEND
  storeToken(tokenValue: string) {
    localStorage.setItem('jwtToken', tokenValue);
  }
  //get Token From BACKEND

  getToken() {
    const tokenHere = localStorage.getItem('jwtToken');
    return localStorage.getItem('jwtToken');
  }

  // store Refresh Token from Backend
  storeRefreshToken(tokenValue: string) {
    localStorage.setItem('refreshToken', tokenValue);
  }

  //Get Refresh Token From Backend
  getRefreshToken() {
    return localStorage.getItem('refreshToken');
  }

  //is Logged In the user or not
  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwtToken'); //if user have token that make the user is loggedIn and have token, each user have diffrent token
  }

  // **********************Token functions************************

  SignOut() {
    localStorage.clear();
    this.router.navigate(['/']);
  }

  // Decode Token role and email from before that must be this command -- > npm i @auth0/angular-jwt
  decodeToken() {
    const jwtHelper = new JwtHelperService();
    const token = this.getToken()!;
    console.log('here is the data user');
    console.log(jwtHelper.decodeToken(token));

    return jwtHelper.decodeToken(token); // return payload data as obj from token
  }

  renewToken(tokenApi: TokenModel) {
    return this.http.post<any>(`${this.baseUrl}refresh`, tokenApi);
  }

  getRoleFromToken() {
    if (this.userPayload) return this.userPayload.role;
  }

  getUserNameFromToken(): string {
    if (this.userPayload) {
      console.log("This is the username By Bayload : ");

      return this.userPayload.username;
    }
    return '';
  }


}

