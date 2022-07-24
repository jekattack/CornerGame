import '../components/Components.css';
import Map from "../components/maps/Map";
import React, {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {login} from "../service/apiService";

export default function LoginPage(){

    const nav = useNavigate();

    useEffect(() => {
        if(localStorage.getItem("jwt")){
            nav("/map")
        }
    }, [nav])

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const createUser = (ev: FormEvent) => {
        ev.preventDefault();
        login(username, password)
            .then(response => localStorage.setItem('jwt', response.token))
            .then(() => nav('/map'))
    }

    return (
        <div id={"app-container"}>
            <Map />
            <div id={"content-wrapper"}>
                <img src="/images/CGLogoBild.svg" className={"logo"} alt={"Logo"}/>
                <h1>Login</h1>

                <form onSubmit={createUser}>
                    <div className={"form-wrapper"}>
                        <div>
                            <label htmlFor={"username"}>Username</label>
                        </div>
                        <div>
                            <input id={"username"} type={"text"} value={username} onChange={(ev) => setUsername(ev.target.value)}/>
                        </div>
                        <div>
                            <label htmlFor={"password"}>Password</label>
                        </div>
                        <div>
                            <input id={"password"} type={"password"} value={password} onChange={(ev) => setPassword(ev.target.value)}/>
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