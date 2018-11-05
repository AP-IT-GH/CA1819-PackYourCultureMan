import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment';
import { User } from '../_models';

@Injectable()
export class UserService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<User[]>(`http://localhost:56898/users`);
    }

    getById(id: number) {
        return this.http.get(`http://localhost:56898/users` + id);
    }

    register(user: User) {
        return this.http.post(`http://localhost:56898/users/register`, user);
    }

    update(user: User) {
        return this.http.put(`http://localhost:56898/users` + user.id, user);
    }

    delete(id: number) {
        return this.http.delete(`http://localhost:56898/users` + id);
    }
}