import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Address, Utilisateur } from 'src/app/Modele';
import { Center } from 'src/app/Modele/Center.Model';
import { Location } from '@angular/common';
import { AdminService, CentreService } from 'src/app/service';

@Component({
  selector: 'app-form-edit',
  templateUrl: './form-edit.component.html',
})

export class FormEditComponent {

  @Input()
  callbackFunction: ((newAdmin:Utilisateur) => void) | undefined;
  @Input()
  formType: boolean | undefined;
  @Input()
  centerList: Center[] | undefined;

  form!: FormGroup;
  post: any = '';
  checkPassword: any;
  checkInUseEmail: any;

  public choosenaddress: Address ={
    id: 0,
    zipcode: '',
    street:'',
    city:'',
  }
  public choosencenter: Center =  {
    name: 'rien',
    id: 0,
    timetable: '',
    capacity: 0,
    address:this.choosenaddress
  }
  public newAdmin: Utilisateur = {id:5,firstName:'',lastName:'',mail:'',password:'',role:'Admin',center:this.choosencenter}
  
  public roleList = ["Super administrateur", "Admin", "Médecin", "Patient"]
  
  constructor(private centerService:CentreService, private adminService:AdminService, private _location: Location) { 
    
  }

  

  ngOnInit() {
    this.getcenters();
    this.createForm();
    
  }

  public getcenters(): void{
    this.centerService.getCenters().subscribe(
      (response: Center[]) => {
        this.centerList = response;
        console.log(this.centerList);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  createForm() {
      this.form = new FormGroup({
        lname: new FormControl(''),
        fname: new FormControl(''),
        password: new FormControl(''),
        mail: new FormControl(''),
        role: new FormControl(''),
        centre: new FormControl('')
      });
  }

  backClicked = () => {
    this._location.back();
  }

  onSubmit(post: any) {
    var postData;
    postData = post.value;
    this.newAdmin.firstName=postData.fname;
    this.newAdmin.lastName=postData.lname;
    this.newAdmin.password=postData.password;
    this.newAdmin.mail=postData.mail;
    this.newAdmin.center=postData.centre;
    this.newAdmin.role='Admin';
    console.log('form-edit' + postData.center);
    if(this.callbackFunction ===undefined){
      console.log("callbackFunction du form undefined");
      return
    }
    this.callbackFunction(this.newAdmin);
  }
}
