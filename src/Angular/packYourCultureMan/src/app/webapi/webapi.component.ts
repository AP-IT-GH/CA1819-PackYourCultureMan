import { Component, OnInit } from '@angular/core';
import {Http, Response } from "@angular/http";
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/map';

@Component({
  selector: 'app-webapi',
  templateUrl: './webapi.component.html',
  styleUrls: ['./webapi.component.scss']
})
export class WebapiComponent  implements OnInit {
 //declaratie
 data: any = {};
 id:String;
 name:String;
 lore:String;
 showSpinner:boolean;
 getInfo:boolean;

 constructor(private http: HttpClient) {
 }


 ngOnInit() { this. getSights(); }

   getDataFromAPI(){
    // http://aspcorepycmapi.azurewebsites.net/Sights
     return this.http.get(`http://localhost:4201/api/v1/heroes?page=0`)
     .map(res => res)
   }

   getSights(){
    this.showSpinner = true;
    this.getInfo = false;
     this.getDataFromAPI().subscribe(data => {
       console.log("Data werd ingelezen en in array gestoken");
       console.log(data);
       console.log(this.name + " is de ingevoerde naam");
       //data wordt in array gestoken
       this.data = data
       this.showSpinner = false;
       this.getInfo = true;  
     })
   }
 
}
