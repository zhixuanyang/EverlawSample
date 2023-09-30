import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { SearchpageComponent } from './components/searchpage/searchpage.component';
import { BookingpageComponent } from './components/bookingpage/bookingpage.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { SearchFormComponent } from './components/search-form/search-form.component';
import { ResultTableComponent } from './components/result-table/result-table.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BusinessDetailComponent } from './components/business-detail/business-detail.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { MatTabsModule } from '@angular/material/tabs';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GoogleMapsModule } from '@angular/google-maps';
import { DetailModalComponent } from './components/detail-modal/detail-modal.component';
import { NgbDateParserFormatter } from "@ng-bootstrap/ng-bootstrap";
import {NgbDateCustomParserFormatter} from './class/ngb-date-custom-parser-formatter';
import {MatSelectModule} from '@angular/material/select';
import { HttpClientModule } from '@angular/common/http';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@NgModule({
  declarations: [
    AppComponent,
    SearchpageComponent,
    BookingpageComponent,
    NavBarComponent,
    SearchFormComponent,
    ResultTableComponent,
    BusinessDetailComponent,
    DetailModalComponent,
  ],
  imports: [
    BrowserModule,
    NgbModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    MatTabsModule,
    BrowserAnimationsModule,
    GoogleMapsModule,
    MatSelectModule,
    MatAutocompleteModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    HttpClientModule
  ],
  providers: [{provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter}],
  bootstrap: [AppComponent]
})
export class AppModule { }
