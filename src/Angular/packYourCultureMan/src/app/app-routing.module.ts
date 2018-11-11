import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { LoginComponent } from './login';
import { RegisterComponent } from './register';
import { AuthGuard } from './_guards';
import { WebapiComponent } from './webapi/webapi.component';
const appRoutes: Routes = [

    
    
    {path: 'home', component: HomeComponent},
    {path: '', component: HomeComponent, canActivate: [AuthGuard] },
    {path: 'contact', component: ContactComponent},
    {path: 'webapi', component: WebapiComponent},
    {path: 'login', component: LoginComponent },
    {path: 'register', component: RegisterComponent },
    {path: '**', component: NotFoundComponent},
    
];
@NgModule({
    imports:[RouterModule.forRoot(appRoutes)],
    exports:[RouterModule]
})
export class AppRoutingModule{}
export const routingComponents = [ContactComponent,HomeComponent,NotFoundComponent]