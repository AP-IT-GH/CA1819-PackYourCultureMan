import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment';
import { User } from '../_models';

@Injectable()
export class UserService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<User[]>(`http://aspcorepycmapi.azurewebsites.net/Users`);
    }

    getById(id: number) {
        return this.http.get(`http://aspcorepycmapi.azurewebsites.net/Users` + id);
    }

    register(user: User) {
        return this.http.post(`http://aspcorepycmapi.azurewebsites.net/Users/register`, user);
    }

    update(user: User) {
        return this.http.put(`http://aspcorepycmapi.azurewebsites.net/Users` + user.id, user);
    }

    delete(id: number) {
        return this.http.delete(`http://aspcorepycmapi.azurewebsites.net/Users` + id);
    }
}