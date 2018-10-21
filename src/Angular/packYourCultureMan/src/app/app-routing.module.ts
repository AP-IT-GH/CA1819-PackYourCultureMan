import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
    {path: '', redirectTo: '/home',pathMatch:'full'},
    {path: 'contact', component: ContactComponent},
    {path: 'home', component: HomeComponent},
    {path: '**', component: NotFoundComponent},
    
];
@NgModule({
    imports:[RouterModule.forRoot(routes)],
    exports:[RouterModule]
})
export class AppRoutingModule{}
export const routingComponents = [ContactComponent,HomeComponent,NotFoundComponent]