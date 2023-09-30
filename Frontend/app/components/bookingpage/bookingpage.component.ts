import { Component, OnInit, OnChanges } from '@angular/core';
import { faTrashCan } from '@fortawesome/free-solid-svg-icons'
@Component({
  selector: 'app-bookingpage',
  templateUrl: './bookingpage.component.html',
  styleUrls: ['./bookingpage.component.css']
})
export class BookingpageComponent implements OnInit {

  bookingList: Array<{
    id: string,
    name: string,
    date: {
      year: number,
      month: number,
      day: number
    },
    email: string,
    hour: number,
    minute: number
  }>;
  faTrashCan = faTrashCan;
  constructor() { }

  ngOnInit(): void {
    this.getBookingList();
  }
  getBookingList() {
    var tempList: {
      id: string,
      name: string,
      date: {
        year: number,
        month: number,
        day: number
      },
      email: string,
      hour: number,
      minute: number
    }[] = [];
    Object.keys(localStorage).forEach(function(key: string) {
      var item = localStorage.getItem(key);
      var booking_json = JSON.parse(item || '{}');
      tempList.push({
        id: key,
        name: booking_json.name,
        date: {
          year: booking_json.date.year,
          month: booking_json.date.month,
          day: booking_json.date.day
        },
        email: booking_json.email,
        hour: booking_json.hour,
        minute: booking_json.minute
      });
    });
    this.bookingList = tempList;
    console.log(this.bookingList);
  }
  ngOnChanges() {
    this.getBookingList();
  }
  removeBooking(id: string) {
    localStorage.removeItem(id);
    this.getBookingList();
    alert("Reservation cancelled!")
  }
}
