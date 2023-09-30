import { Component, OnInit, Input, Output, ViewChild, EventEmitter } from '@angular/core';
@Component({
  selector: 'app-searchpage',
  templateUrl: './searchpage.component.html',
  styleUrls: ['./searchpage.component.css']
})
export class SearchpageComponent implements OnInit {
  displayResults: boolean = false;
  displayDetail: boolean = false;
  @Output() query: { keyword: string, distance: number, location: string,
    category: string, auto_location: boolean, isReset: boolean};
  @Output() yelp_id: string;
  constructor() {
  }

  ngOnInit(): void {
  }
  createResult_table(query_result: {
    keyword: string, distance: number, location: string,
    category: string, auto_location: boolean, isReset: boolean}) {
      this.query = query_result;
      if (this.query.isReset) {
        this.displayResults = false;
        this.displayDetail = false;
      } else {
        this.displayResults = true;
      }
  }
  createBusinessDetail(id: string) {
      this.yelp_id = id;
      this.displayResults = false;
      this.displayDetail = true;
  }
  hideBusinessDetail() {
    this.displayResults = true;
    this.displayDetail = false;
  }
}
