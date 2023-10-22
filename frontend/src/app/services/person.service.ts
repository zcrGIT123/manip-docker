import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

const endpoint = "http://localhost:8080/api/person"

@Injectable({
    providedIn: 'root'
})
export class PersonService {
    constructor(private http: HttpClient){}

    getPersons(lastname: string, firstname: string, birthdate: string, gender: string, registrationdate: string,
        sortby: string, sortdirection: string, page: number, size: number): Observable<any>{
        return this.http.get(endpoint+"/get-paging", {params: {
            "lastname": lastname,
            "firstname": firstname,
            "birthdate": birthdate,
            "gender": gender,
            "registrationdate": registrationdate,
            "sortby": sortby,
            "sortdirection": sortdirection,
            "page": page,
            "size": size
        }});
    }

    addPerson(data: any): Observable<any> {
        return this.http.post(endpoint+"/add", data);
    }
}