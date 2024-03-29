import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Center } from 'src/app/Modele';

@Injectable({
  providedIn: 'root'
})
export class CentreService {

  constructor(private http:HttpClient) { }

  public getCenters(): Observable<Center[]> {
    return this.http.get<Center[]>('/api/public/showcenter');
  }
  public saveCenterToServer(centre: Center) {
    console.log(centre)
    this.http.post('/api/private/addcenter' , { id:centre.id,
      name:centre.name,
      capacity:centre.capacity,
      timetable:centre.timetable,
      address:centre.address,
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

  public changeInfo(centre: Center){
    this.http.post('/api/private/changecenter', {
      id:centre.id,
      name:centre.name,
      capacity:centre.capacity,
      timetable:centre.timetable,
      address:centre.address,
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

  public maxId():Observable<number>{
    return this.http.get<number>('/api/public/maxcenter');
  }
}
