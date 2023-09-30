import { Component, Output, Input, EventEmitter, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn, ValidationErrors } from '@angular/forms';
import { debounceTime, tap, switchMap, finalize, distinctUntilChanged, filter } from 'rxjs/operators';
var axios = require('axios');
var info_api = "1d33eadb5f5e85";
@Component({
  selector: 'app-search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.css']
})
export class SearchFormComponent implements OnInit {
  searchForm: FormGroup<{
    keyword: any,
    distance: any,
    location: any,
    category: any,
    checked_box: any
  }>;
  categories = [
    {cat: 'All', value: 'Default'},
    {cat: 'arts', value: 'Arts & Entertainment'},
    {cat: 'health', value: 'Health & Medical'},
    {cat: 'hotelstravel', value: 'Hotels & Travel'},
    {cat: 'food', value: 'Food'},
    {cat: 'professional', value: 'Professional Services'},
  ];
  minLengthTerm = 2;
  isLoading = false;
  Yelp_auto: any;
  auto_location_status = false;
  @Output() newItemEvent = new EventEmitter<{
    keyword: string, distance: number, location: string,
    category: string, auto_location: boolean, isReset: boolean}>();
  constructor(private formBuilder: FormBuilder, private http: HttpClient) {
  }
  displayWith(value: any) {
    return value;
  }
  onSelected(value: any) {
    this.searchForm.value.keyword = value;
  }
  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      keyword: '',
      distance: 10,
      location: '',
      category: this.categories[0].cat,
      checked_box: false
    })
    this.searchForm.controls['keyword'].valueChanges
      .pipe(
        filter((res:any) => {
          return res !== null && res.length >= this.minLengthTerm
        }),
        distinctUntilChanged(),
        debounceTime(1000),
        tap(() => {
          this.Yelp_auto = [];
          this.isLoading = true;
        }),
        switchMap(value => this.http.get("https://csci571-hw8-backend-367910.wl.r.appspot.com/autocomplete?keyword=" + value)
          .pipe(
            finalize(() => {
              this.isLoading = false
            }),
          )
        )
      )
      .subscribe((data: any) => {
        if (data['terms'] === undefined || data['categories'] === undefined) {
          this.Yelp_auto = [];
        } else {
          this.Yelp_auto = [];
          for (let value of data['terms']) {
            this.Yelp_auto.push(value.text);
          }
          for (let value of data['categories']) {
            this.Yelp_auto.push(value.title);
          }
        }
      });
  }
  reSet() {
    this.searchForm.controls['location'].enable();
    this.auto_location_status = false;
    this.isLoading = false;
    this.newItemEvent.emit({keyword: this.searchForm.value.keyword,
      distance: this.searchForm.value.distance, location: this.searchForm.value.location, category: this.searchForm.value.category,
      auto_location: this.searchForm.value.checked_box, isReset: true});
    this.Yelp_auto = [];
    this.searchForm.reset({
      keyword: '',
      distance: 10,
      location: '',
      category: this.categories[0].cat,
      checked_box: false
    });
  }
  disableLocation() {
    if (!this.auto_location_status) {
      this.searchForm.controls['location'].disable();
      this.searchForm.controls['location'].patchValue("");
      this.auto_location_status = true;
    } else {
      this.searchForm.controls['location'].enable();
      this.auto_location_status = false;
    }
  }
  onSubmit() {
    if (this.searchForm.value.checked_box) {
      var IPInfo = async () => {
        try {
              const response = await axios.get('https://ipinfo.io/', {
                  params: {
                      'token': info_api,
                  }
              })
              return response.data;
        } catch(err) {
            return err;
        }
      };
      IPInfo().then(response => {
        this.newItemEvent.emit({keyword: this.searchForm.value.keyword,
          distance: this.searchForm.value.distance, location: response.loc, category: this.searchForm.value.category,
          auto_location: this.searchForm.value.checked_box, isReset: false});
      });
    } else {
      this.newItemEvent.emit({keyword: this.searchForm.value.keyword,
        distance: this.searchForm.value.distance, location: this.searchForm.value.location, category: this.searchForm.value.category,
        auto_location: this.searchForm.value.checked_box, isReset: false});
      }
    }
}
