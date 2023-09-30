import { Component, OnInit, Input, OnChanges, EventEmitter, Output } from '@angular/core';
import {faTwitter, faFacebook} from '@fortawesome/free-brands-svg-icons'
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DetailModalComponent } from '../detail-modal/detail-modal.component';
var axios = require('axios');
@Component({
  selector: 'app-business-detail',
  templateUrl: './business-detail.component.html',
  styleUrls: ['./business-detail.component.css']
})
export class BusinessDetailComponent implements OnInit {
  @Output() hideDetail = new EventEmitter<boolean>();
  @Input() yelp_id: string;
  business_detail: {
    title: string,
    lat: string,
    lng: string,
    phone: string,
    is_open: boolean,
    photos: Array<string>,
    price: string,
    url: string,
    category: string,
    address: string,
    is_undefined: boolean
  }
  reviews_array: Array<{
    name: string,
    rating: number,
    text: string,
    time: string}
  >
  faTwitter = faTwitter;
  faFacebook = faFacebook;
  faArrowLeft = faArrowLeft;
  displayBusinessDetails: boolean = false;
  center: google.maps.LatLngLiteral;
  marker:{position: {
    lat: number,
    lng: number
  }, label: any};
  zoom = 14;
  options: google.maps.MapOptions = {
    mapTypeId: 'roadmap',
    zoomControl: true,
    scrollwheel: true,
    disableDoubleClickZoom: false
  };

  constructor(private modalService: NgbModal) { }
  ngOnInit(): void {
  }

  backToResult() {
    this.hideDetail.emit(true);
  }

  open() {
    const modalRef = this.modalService.open(DetailModalComponent);
    modalRef.componentInstance.business_id = this.yelp_id;
    modalRef.componentInstance.business_name = this.business_detail.title;
  };
  checkBooked(id: string) {
    if (localStorage.getItem(id) === null) {
      return false;
    }
    return true;
  }
  cancelRes(id: string) {
    localStorage.removeItem(id);
  }
  ngOnChanges() {
    var queryBusinessDetail = async () => {
      try {
            const response = await axios.get('https://csci571-hw8-backend-367910.wl.r.appspot.com/searchdetail', {
                params: {
                    'id': this.yelp_id,
                }
            })
            return response.data;
      } catch(err) {
          return err;
      }
    };
    var queryReview = async () => {
      try {
            const response = await axios.get('https://csci571-hw8-backend-367910.wl.r.appspot.com/business_review', {
                params: {
                    'id': this.yelp_id,
                }
            })
            return response.data;
      } catch(err) {
          return err;
      }
    };
    queryBusinessDetail().then(response => {
        var photos_array = [];
        if (response !== undefined && response.photos !== undefined) {
          for (let i = 0; i < response.photos.length; i++) {
            photos_array.push(response.photos[i]);
          }
        }
        var cate = "N/A";
        if (response !== undefined && response.categories !== undefined && response.categories.length > 0) {
          cate = "";
          for (let i = 0; i < response.categories.length; i++) {
            if (i === response.categories.length - 1) {
              cate += response.categories[i].title;
            } else {
              cate += response.categories[i].title + " | ";
            }
          }
        }
        var add = "N/A";
        if (response !== undefined && response.location !== undefined && response.location.display_address !== undefined && response.location.display_address.length > 0) {
          add = "";
          for (let i = 0; i < response.location.display_address.length; i++) {
            if (i !== 0) {
              add += response.location.display_address[i];
            } else {
              add += response.location.display_address[i] + ", ";
            }
          }
        }
        var business_name = "N/A";
        if (response !== undefined && response.name !== undefined) {
          business_name = response.name;
        }
        var business_price = "N/A";
        if (response !== undefined && response.price !== undefined) {
          business_price = response.price;
        }
        var business_phone = "N/A";
        if (response !== undefined && response.display_phone !== undefined && response.display_phone.length > 0) {
          business_phone = response.display_phone;
        }
        var is_open_now = false;
        var is_open_undefined = false;
        if (response !== undefined && response.hours != undefined && response.hours.length > 0 && response.hours[0].is_open_now !== undefined) {
          is_open_now = response.hours[0].is_open_now;
          is_open_undefined = true;
        }
        this.business_detail = {
          title: business_name,
          price: business_price,
          url: response.url,
          lat: response.coordinates.latitude,
          lng: response.coordinates.longitude,
          phone: business_phone,
          is_open: is_open_now,
          photos: photos_array,
          category: cate,
          address: add,
          is_undefined: is_open_undefined
        };
        this.center = {
          lat: parseFloat(this.business_detail.lat),
          lng: parseFloat(this.business_detail.lng)
        }
        this.marker = {
          position: {
            lat: parseFloat(this.business_detail.lat),
            lng: parseFloat(this.business_detail.lng),
          },
          label: {
            color: "red",
          },
        }
        queryReview().then(reviews => {
          var tempArray = [];
          for (let i = 0; i < reviews.reviews.length; i++) {
            var display_time = reviews.reviews[i].time_created.split(" ");
            tempArray.push({
              name: reviews.reviews[i].user.name,
              rating: reviews.reviews[i].rating,
              text: reviews.reviews[i].text,
              time: display_time[0]
            })
          }
          this.reviews_array = tempArray;
          this.displayBusinessDetails = true;
        })
    });
  }
}
