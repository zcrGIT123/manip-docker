import { Component, OnInit } from '@angular/core';
import { PersonService } from '../services/person.service';
import { FormBuilder, FormGroup, Validators, FormControl, AbstractControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-person-list',
  templateUrl: './person-list.component.html',
  styleUrls: ['./person-list.component.scss']
})
export class PersonListComponent implements OnInit {

  constructor(private personService: PersonService, private fb: FormBuilder, private modalService: NgbModal) {
    this.addform = this.fb.group({
      lastname: ['', Validators.required],
      firstname: ['', Validators.required],
      birthdate: ['', Validators.required],
      gender: ['male', Validators.required],
    });
  }

  addform: FormGroup = new FormGroup({
    firstname: new FormControl(''),
    lastname: new FormControl(''),
    birthdate: new FormControl(''),
    gender: new FormControl('male')
  });
  addspin: boolean = false;
  adderror = null;

  selected: any = null;

  tfirstname: string = "";
  tlastname: string = "";
  tbirthdate: string = "";
  tgender: string = "male";

  page: number=0;
  currentPage: number = 1;
  size: number=10;
  sortDirection: string = 'DESC'
  sortby: string = "registrationdate";
  pages: number[] = [0,1];
  totalelements: number = 0;

  firstname: string = "";
  lastname: string = "";
  birthdate: string = "";
  gender: string = "";
  registrationdate: string = "";

  persons: any[] = [];
  isLoading: boolean = true;


  ngOnInit(): void {
    this.getPersons();
  }

  get f(): { [key: string]: AbstractControl } {
    return this.addform.controls;
  }

  change(){
    this.getPersons();
  }

  setPage(i: number, event: Event) {
    event.preventDefault();
    if(i>=0 && i<this.pages.length){
      this.page = i;
      this.currentPage = this.page+1;
      this.getPersons();
    }
  }

  changePage(){
    if(this.currentPage>0){
      this.page = this.currentPage - 1;
      this.getPersons();
    }
  }

  setSize(i: number) {
    this.size = i;
    this.getPersons();
  }

  setSort(sortby: string, sortDirection: string) {
    this.sortby = sortby;
    this.sortDirection = sortDirection;
    this.getPersons();
  }

  
  getPersons(): void{
    this.isLoading = true;
    this.personService.getPersons(this.lastname, this.firstname, this.birthdate, this.gender, this.registrationdate, this.sortby,
      this.sortDirection, this.page, this.size).subscribe(
      data => {
        this.persons = [...data.content];
        this.pages = new Array(data.totalPages);
        this.totalelements = data.numberOfElements;
        this.isLoading = false;
      },
      error => {
        this.isLoading = false
      }
    );
  }
  
  filter() {
    this.page = 0;
    this.currentPage = 1;
    this.getPersons();
  }

  openModal(content: any){
    this.adderror = null;
    this.modalService.open(content, {centered: true});
  }

  addPerson(){
    this.addspin = true;
    this.adderror = null;
    this.personService.addPerson(this.addform.value).subscribe(
      data => {
        this.modalService.dismissAll();
        this.addform.reset();
        this.filter();
        this.adderror = null;
        this.addspin = false;
      },
      error => {
        this.addspin = false;
        this.adderror = error.error.message;
      }
    )
  }

  // addPerson2(){
  //   this.addspin = true;
  //   let data = {
  //     "firstname": this.tfirstname,
  //     "lastname": this.tlastname,
  //     "birthdate": this.tbirthdate,
  //     "gender": this.tgender
  //   }
  //   this.personService.addPerson(data).subscribe(
  //     data => {
  //       this.filter();
  //       this.addspin = false;
  //     },
  //     error => {
  //       this.addspin = false;
  //       this.adderror = error.error.message;
  //     }
  //   )
  // }

  select(person:any){
    this.selected != person ? this.selected = person : this.selected = null;
  }
}
