import { Component, EventEmitter, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Utilisateur } from 'src/app/Modele';
import { AdminService } from 'src/app/service';
import { HttpErrorResponse } from '@angular/common/http';
import { dataSourceType } from 'src/app/component/datagrid';
import { Router } from '@angular/router'

@Component({
  selector: 'app-gestion-admin',
  templateUrl: './gestion-admin.component.html',
  providers: [ AdminService]
})
export class GestionAdminComponent implements OnInit {

  public admins: Utilisateur[] = [];
  public adminSubscription: Subscription = new Subscription;
  public eventemitter: EventEmitter<any> = new EventEmitter();

  constructor(private adminService: AdminService, private router:Router) { }

  ngOnInit() {
    this.adminSubscription = this.adminService.getAdmins().subscribe(
      (admins: Utilisateur[]) => {
        this.admins = admins;
      }
    );
    this.getAdmins();
  }
  
  public getAdmins(): void{
    this.adminService.getAdmins().subscribe(
      (response: Utilisateur[]) => {
        this.admins = response;
        console.log(this.admins);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  changeAdmin = (id: number) => {
    this.router.navigateByUrl('/addadmin/'+id);
  }

  deleteAdmin(id:number){
      //this.adminService.confDeleteAdmin(id);
      this.adminService.confDeleteAdmin(id);
  }
    
}