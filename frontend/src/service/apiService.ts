import axios, {AxiosResponse} from "axios";
import {CGGeolocation, Kiosk, LoginResponse, Visit} from "./models";

export function fetchAllKiosks() {
    return (
        axios.get("/api/kiosk", {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
            .then((response: AxiosResponse<Kiosk[]>) => response.data)
    )
}

export function register(username: string, email: string, password: string, passwordAgain: string) {
    return (
        axios.post("/api/user/register", {username: username, email: email, password: password, passwordAgain: passwordAgain})
    )
}

export function login(username: string, password: string){
    return axios.post("/api/login", {username: username, password: password}, {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<LoginResponse>) => response.data);
}

export function fetchProgress(){
    return axios.get("/api/visits/progress", {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<Visit[]>) => response.data)
}

export function visit(googlePlacesId: string, position: CGGeolocation){
    return axios.post("/api/visits/add", {
        userLocation: {
            userLocationCoordinate: {
                lat: position.coordinates.lat, lng: position.coordinates.lng
            }},
        googlePlacesId: googlePlacesId
    }, {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
}