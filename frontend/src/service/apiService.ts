import axios, {AxiosResponse} from "axios";
import {
    Achievement, ActiveQuest, ActiveQuestDTO,
    CGUser,
    CGUserGameDataDTO,
    CGUserPasswordDTO,
    CGUserUpdateDTO,
    Kiosk,
    LoginResponse,
    Quest, QuestEventDTO,
    Visit
} from "./models";

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

export function visit(googlePlacesId: string, lat: number, lng: number){
    return axios.post("/api/visits/add", {
        userLocation: {
            userLocationCoordinates: {
                lat: `${lat}` , lng: `${lng}`
            }},
        place_id: googlePlacesId
    }, {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
}

export function fetchAllQuests(){
    return axios.get("/api/quests", {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<Quest[]>) => response.data)
}

export function fetchUsersAchievements(){
    return axios.get("/api/gamedata/achievements", {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<Achievement[]>) => response.data)
}

export function fetchHighscore(){
    return axios.get("/api/gamedata/highscore", {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<CGUserGameDataDTO[]>) => response.data)
}

export function fetchScore(){
    return axios.get("/api/gamedata/score", {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<CGUserGameDataDTO>) => response.data)
}


export function fetchUser(){
    return axios.get("/api/user", {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<CGUser>) => response.data)
}

export function updateUser(userUpdate: CGUserUpdateDTO){
    return axios.post("/api/user/update", userUpdate, {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<CGUser>) => response.data)
}

export function updatePassword(passwordUpdate: CGUserPasswordDTO){
    return axios.post("/api/user/update/password", passwordUpdate, {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<String>) => response.data);
}

export function getLocationsForQuest(questPlacesIds: string[]){
    return axios.post("/api/kiosk/locations", questPlacesIds, {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<ActiveQuest>) => response.data);
}

export function startQuestRequest(questId: string){
    return axios.post("/api/quests/start", questId, {headers: {"Content-Type": "text/plain", Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<QuestEventDTO>) => response.data)
}

export function fetchActiveQuestsInfo(){
    return axios.get("/api/gamedata/quests/active", {headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<ActiveQuestDTO[]>) => response.data)
}

export function cancelActiveQuest(questId: string){
    return axios.post("/api/quests/cancel", questId, {headers: {"Content-Type": "text/plain", Authorization: `Bearer ${localStorage.getItem('jwt')}`}})
        .then((response: AxiosResponse<QuestEventDTO>) => response.data)
}

// LocalDateTime from java has been converted to String for the request, this creates a js Date from it
export function parseISOString(s: string) {
    const b = s.split(/\D+/);
    let month = Number(b[1]);
    return new Date(Date.UTC(Number(b[0]), --month, Number(b[2]), Number(b[3]), Number(b[4]), Number(b[5]), Number(b[6])));
}