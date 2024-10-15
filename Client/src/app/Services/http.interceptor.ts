import { Injectable } from '@angular/core';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { NgToastService } from 'ng-angular-popup';
import { Router } from '@angular/router';
import { AuthService } from '../Components/login/auth.service';
import { TokenModel } from '../shared/models/token-api.model';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(
    private auth: AuthService,
    private toast: NgToastService,
    private router: Router
  ) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const myToken = this.auth.getToken();

    if (myToken) {
      request = request.clone({
        setHeaders: { Authorization: `Bearer ${myToken}` },
      });
    }


    return next.handle(request).pipe(
      catchError((err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401) {

            return this.handleUnAuthorizedError(request, next);
          }
        }
        return throwError(() => new Error('Some other error occurred!'));
      })
    );
  }

  private handleUnAuthorizedError(req: HttpRequest<any>, next: HttpHandler) {
    const tokenApiModel = new TokenModel();
    tokenApiModel.jwtToken = this.auth.getToken()!;

    return this.auth.renewToken(tokenApiModel).pipe(
      switchMap((data: TokenModel) => {

        this.auth.storeRefreshToken(data.jwtToken);
        this.auth.storeToken(data.jwtToken);

       
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${data.jwtToken}` },
        });
        return next.handle(req);
      }),
      catchError((err) => {
        this.toast.warning('Token is expired, Please Login Again!', 'Warning', 5000);
        this.router.navigate(['/']); 
        return throwError(() => new Error('Token renewal failed'));
      })
    );
  }
}
