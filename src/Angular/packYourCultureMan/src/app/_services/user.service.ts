import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment';
import { User } from '../_models';

@Injectable()
export class UserService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<User[]>(`https://pacyourculturemanapi.azurewebsites.net/users`);
    }

    getById(id: number) {
        return this.http.get(`https://pacyourculturemanapi.azurewebsites.net/users/` + id);
    }

    register(user: User) {
        return this.http.post(`https://pacyourculturemanapi.azurewebsites.net/users/register`, user);
    }

    update(user: User) {
        return this.http.put(`https://pacyourculturemanapi.azurewebsites.net/users/` + user.id, user);
    }

    delete(id: number) {
        return this.http.delete(`https://pacyourculturemanapi.azurewebsites.net/users/` + id);
    }
}