import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
var axios = require('axios');

@Component({
  selector: 'app-result-table',
  templateUrl: './result-table.component.html',
  styleUrls: ['./result-table.component.css']
})
export class ResultTableComponent implements OnInit {
  @Input() query: {keyword: string, distance: number, location: string,
    category: string, auto_location: boolean};
  displayResults: boolean = false;
  query_result: any;
  no_result: boolean;
  @Output() businessId = new EventEmitter<string>();
  constructor() { }
  ngOnInit(): void {
  }
  ngOnChanges() {
    var querySearch = async () => {
      try {
            const response = await axios.get('https://csci571-hw8-backend-367910.wl.r.appspot.com/searchbusiness', {
                params: {
                    'keyword': this.query.keyword,
                    'distance': this.query.distance,
                    'location': this.query.location,
                    'category': this.query.category,
                    'auto_checked': this.query.auto_location
                }
            })
            return response.data.businesses;
      } catch(err) {
          return err;
      }
    };
    querySearch().then(response => {
        this.displayResults = true;
        this.query_result = response;
        if (this.query_result === undefined || this.query_result.length === 0) {
          this.no_result = false;
        } else {
          this.no_result = true;
        }
    });
  }
  getMiles(meters: number) {
    return (meters * 0.000621371192).toFixed(2);
  }
  produceDetail(index: number) {
    this.businessId.emit(this.query_result[index].id);
  }
}
