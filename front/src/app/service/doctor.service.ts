import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Utilisateur } from "../Modele";

@Injectable({
    providedIn: 'root'
})
export class DoctorService{
  constructor(private http: HttpClient){}

  public getDoc(): Observable<Utilisateur[]> {
      return this.http.get<Utilisateur[]>('/api/private/doctor/showdoc');
    }

  public getDoctorByCenterId(centerId: number){
      return this.http.get<Utilisateur[]>('/api/private/showdocbycenter/'+centerId)
  }

  deleteDoc(id_user:number){
      return this.http.delete('/api/private/doctor/deletedoc/'+id_user);
  }
  
  confDeleteDoc(id_user:number) {
    let conf = confirm("Etes vous sûr?");
    if (conf) {
      this.deleteDoc(id_user)
        .subscribe(data => {
          this.getDoc();
        }, err => {
          console.log(err);
        })
    }
  }

  public maxId():Observable<number>{
    return this.http.get<number>('/api/public/max');
  }

  public saveDocToServer(doctor: Utilisateur) {
    this.http.post('/api/private/doctor/adddoc' , { id:doctor.id,
      firstName:doctor.firstName,
      lastName:doctor.lastName,
      mail:doctor.mail,
      password:doctor.password,
      role:"Doctor",
      center:doctor.center
    })
      .subscribe(
        () => {
          console.log('Ok');
        },
        (error) => {
          console.log('Erreur ! : ' + error);
        }
      );
  }

  deleteAdmin(id_user:number){
    return this.http.delete('/api/private/doctor/deletedoc/'+id_user);
  }
}