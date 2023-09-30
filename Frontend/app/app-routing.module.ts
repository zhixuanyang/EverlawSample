import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router'
import { SearchpageComponent } from './components/searchpage/searchpage.component';
import { BookingpageComponent } from './components/bookingpage/bookingpage.component';
import { SearchFormComponent } from './components/search-form/search-form.component';
import { ResultTableComponent } from './components/result-table/result-table.component';
const routes: Routes = [
  {path: '',   redirectTo: '/search', pathMatch: 'full' },
  {
    path: 'search',
    children: [
      {path: '', component: SearchpageComponent},
      {path: 'search-form', component: SearchFormComponent},
      {path: 'result-table', component: ResultTableComponent}
  ]},
  {path: 'bookings', component: BookingpageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
