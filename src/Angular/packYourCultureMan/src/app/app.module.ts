import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { AppComponent } from './app.component';
import { UiPageModule } from './ui-page/ui-page.module';
import{AppRoutingModule,routingComponents} from './app-routing.module';
import { ReactiveFormsModule }    from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AlertComponent } from './_directives';
import { AuthGuard } from './_guards';
import {NgxPaginationModule} from 'ngx-pagination';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { AlertService, AuthenticationService, UserService } from './_services';
import { HomeComponent } from './home';
import { LoginComponent } from './login';
import { RegisterComponent } from './register';
import { LoadingSpinnerComponent } from './loading-spinner/loading-spinner.component';
import { WebapiComponent } from './webapi/webapi.component';

@NgModule({
  declarations: [
    AppComponent,
    routingComponents,
    AlertComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent, 
    LoadingSpinnerComponent,
    WebapiComponent, ],
  imports: [
    BrowserModule,
    NgbModule,
    UiPageModule,
    AppRoutingModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    AuthGuard,
    AlertService,
    AuthenticationService,
    UserService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
