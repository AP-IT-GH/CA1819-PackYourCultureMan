import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment';
import { User } from '../_models';

@Injectable()
export class UserService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<User[]>(`http://192.168.1.51:56898/Users`);
    }

    getById(id: number) {
        return this.http.get(`http://192.168.1.51:56898/Users` + id);
    }

    register(user: User) {
        return this.http.post(`http://192.168.1.51:56898/Users/register`, user);
    }

    update(user: User) {
        return this.http.put(`http://192.168.1.51:56898/Users` + user.id, user);
    }

    delete(id: number) {
        return this.http.delete(`http://192.168.1.51:56898/Users` + id);
    }
}