import { Component, OnInit } from '@angular/core';
import {Http, Response } from "@angular/http";
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/map';

@Component({
  selector: 'app-sights',
  templateUrl: './sights.component.html',
  styleUrls: ['./sights.component.scss']
})
export class SightsComponent  implements OnInit {
 //declaratie
 data: any = {};

 showSpinner:boolean;
 getInfo:boolean;

 constructor(private http: HttpClient) {
 }


 ngOnInit() { this. getSights(); }

   getDataFromAPI(){
     return this.http.get(`https://api.myjson.com/bins/iilka`)
     .map(res => res)
   }

   getSights(){
    this.showSpinner = true;
    this.getInfo = false;
     this.getDataFromAPI().subscribe(data => {
       console.log("Data werd ingelezen en in array gestoken");
       console.log(data);
       //data wordt in array gestoken
       this.data = data
       this.getInfo = true;
       this.showSpinner = false;
     })
   }
 
}
