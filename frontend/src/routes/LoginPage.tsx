import '../components/Components.css';
import Map from "../components/maps/Map";
import React, {FormEvent, useCallback, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {login} from "../service/apiService";
import "../App.css";
import {toast} from "react-toastify";
import axios, {AxiosError} from "axios";

export default function LoginPage(){

    const nav = useNavigate();
    const mapRef = React.useRef<google.maps.Map|null>(null);

    useEffect(() => {
        if(localStorage.getItem("jwt")){
            nav("/map")
        }
    }, [nav])

    const apiResponseChecks = useCallback((err: Error | AxiosError) => {
        console.log("bonjour!")
    }, [nav])

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const createUser = (ev: FormEvent) => {
        ev.preventDefault();
        login(username, password)
            .then(response => localStorage.setItem('jwt', response.token))
            .then(() => nav('/map'))
            .catch((error) => {
                if(error.response) {
                    toast.error(error.response.data.message + ": " + error.response.data.subMessages[0])
                }
            })
    }

    return (
        <div id={"app-container"}>
            <Map
                mapRef={mapRef}
                inGame={false}
                apiAuthCheck={apiResponseChecks}
            />
            <div id={"content-wrapper"}>
                <img src="/images/CGLogoBild.svg" className={"logo"} alt={"Logo"}/>
                <h1>Login</h1>

                <form onSubmit={createUser}>
                    <div className={"form-wrapper"}>
                        <div>
                            <label htmlFor={"username"}>Username</label>
                        </div>
                        <div>
                            <input id={"username"} type={"text"} value={username} onChange={(ev) => setUsername(ev.target.value)} required/>
                        </div>
                        <div>
                            <label htmlFor={"password"}>Password</label>
                        </div>
                        <div>
                            <input id={"password"} type={"password"} value={password} onChange={(ev) => setPassword(ev.target.value)} required/>
                        </div>
                        <div>
                            <input type={"submit"} value={"Los geht's!"}/>
                        </div>
                    </div>
                </form>
                <div>
                    <a href={"/login"}>Passwort vergessen?</a>
                </div>
                <div>
                    <a href={"/register"}>Noch nicht registriert? Klicke hier!</a>
                </div>
            </div>
        </div>
    )
}