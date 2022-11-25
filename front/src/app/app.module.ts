import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { Ng2SearchPipeModule } from 'ng2-search-filter'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './login';
import { HomeComponent } from './home';
import { GestionAdminComponent } from './gestion-admin/gestion-admin.component';
import { AdminService } from './admin.service';
import { HttpClientModule } from '@angular/common/http';
import { ConnexionComponent } from './connexion/connexion.component'
import { SuperAdminService } from './super-admin.service';
import { AjoutAdminComponent } from './ajout-admin/ajout-admin.component';
import { AjoutCentreComponent } from './ajout-centre/ajout-centre.component';
import { CentreService } from './centre.service';
import { AddressService } from './address.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    GestionAdminComponent,
    ConnexionComponent,
    AjoutAdminComponent,
    AjoutCentreComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    Ng2SearchPipeModule,
    HttpClientModule
  ],
  providers: [AdminService, SuperAdminService, CentreService, AddressService],
  bootstrap: [AppComponent]
})
export class AppModule { }
