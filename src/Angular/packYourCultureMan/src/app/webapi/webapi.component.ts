import { Component, OnInit } from '@angular/core';
import {Http, Response } from "@angular/http";
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/map';

@Component({
  selector: 'app-webapi',
  templateUrl: './webapi.component.html',
  styleUrls: ['./webapi.component.scss']
})
export class WebapiComponent {
 //declaratie
 data: any = {};
 id:String;
 name:String;
 lore:String;
 showSpinner:boolean;
 getInfo:boolean;

 constructor(private http: HttpClient) {
 }

 //invoervensters
GetId(event:any){
  this.id = event.target.value;
  console.log("Invoervenster id " + this.id);
}
GetName(event:any){
  this.name = event.target.value;
  console.log("Invoervenster name " + this.name);
}

GetLore(event:any){
  this.lore = event.target.value;
  console.log("Invoervenster lore " + this.lore);
}

postDataToAPI(){
  this.http.post(`http://localhost:4201/api/v1/heroes`, {
    Name :this.name,
    Lore : this.lore
})
      .subscribe(
        res => {
          console.log(res);
        },
        err => {
          console.log("Error occured");
        }
      );
}


putDataToAPI(){
  this.http.put(`http://localhost:4201/api/v1/heroes`, {
    Id : this.id,
    Name :this.name,
    Lore : this.lore
})
      .subscribe(
        res => {
          console.log(res);
        },
        err => {
          console.log("Error occured");
        }
      );
}
deleteDataFromAPI(){
  this.http.delete(`http://localhost:4201/api/v1/heroes/${this.id}`)
      .subscribe(
        res => {
          console.log(res);
        },
        err => {
          console.log("Error occured");
        }
      );
}
 
 
getDataFromAPI(){
    return this.http.get(`http://localhost:4201/api/v1/heroes?page=0&length=1&sort=name&dir=asc&name=${this.name}`)
    .map(res => res)
   }


   getHero(){
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
