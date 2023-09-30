import { Component, Input, OnInit, OnChanges } from '@angular/core';
import { NgbActiveModal, NgbDateStruct, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { faCalendar, faClock } from '@fortawesome/free-regular-svg-icons'

@Component({
  selector: 'app-detail-modal',
  templateUrl: './detail-modal.component.html',
  styleUrls: ['./detail-modal.component.css']
})
export class DetailModalComponent implements OnInit {
  @Input() business_id: string;
  @Input() business_name: string;
  reservationForm: FormGroup;
  submitted = false;
  displayMonths = 1;
	showWeekNumbers = false;
	outsideDays = 'visible';
  faCalendar = faCalendar;
  faClock = faClock;
  minPickerDate: NgbDateStruct;
  hour_list = [10, 11, 12, 13, 14, 15, 16, 17];
  min_list = [0, 15, 30, 45];
  constructor(public activeModal: NgbActiveModal, private formBuilder: FormBuilder) { }
  ngOnInit() {
    this.reservationForm = this.formBuilder.group({
      date: ['', Validators.required],
      hour: ['', Validators.required],
      email: ['', [Validators.required, Validators.email,Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]],
      minute: ['', [Validators.required]]
    });
    this.minPickerDate = {
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1,
      day: new Date().getDate()
    };
  }
  ngOnChanges() {
    this.reservationForm = this.formBuilder.group({
      date: ['', Validators.required],
      hour: ['', Validators.required],
      email: ['', [Validators.required, Validators.email,Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]],
      minute: ['', [Validators.required]]
    });
    this.minPickerDate = {
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1,
      day: new Date().getDate()
    };
  }
  get f() { return this.reservationForm.controls; }
  onSubmit() {
    this.submitted = true;
    if (this.reservationForm.invalid) {
        return;
    }
    localStorage.setItem(this.business_id, JSON.stringify({
      name: this.business_name,
      date: this.reservationForm.value.date,
      email: this.reservationForm.value.email,
      hour: this.reservationForm.value.hour,
      minute: this.reservationForm.value.minute
    }));
    alert("Reservation created!")
    this.activeModal.close();
  }
}
