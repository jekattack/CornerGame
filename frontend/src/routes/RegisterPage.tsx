import '../components/Components.css';
import Map from "../components/maps/Map";
import React, {FormEvent, useCallback, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {register} from "../service/apiService";
import "../App.css";
import {toast} from "react-toastify";
import {AxiosError} from "axios";

export default function RegisterPage(){

    const nav = useNavigate();
    const mapRef = React.useRef<google.maps.Map|null>(null);

    useEffect(() => {
        if(localStorage.getItem("jwt")){
            nav("/map")
        }
    }, [nav])

    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordAgain, setPasswordAgain] = useState('');

    const apiResponseChecks = useCallback((err: Error | AxiosError) => {
        console.log("bonjour!")
    }, [])

    const createUser = (ev: FormEvent) => {
        ev.preventDefault();
        register(username, email, password, passwordAgain)
            .then(() => nav('/login'))
            .catch((error) => {
                if(error.response) {
                    toast.error(error.response.data.message + ": " + error.response.data.subMessages[0])
                    console.log(error)
                }
            })
        // -> Later directed to double-opt-in
    }


    return (
        <div id={"app-container"}>
            <Map
                mapRef={mapRef}
                inGame={false}
                apiAuthCheck={apiResponseChecks}
            />
            <div className={"wrapper"}>
                <div id={"content-wrapper"}>
                    <img src="/images/CGLogoBild.svg" className={"logo"} alt={"Logo"}/>
                    <h1>Registrierung</h1>

                    <form onSubmit={createUser}>
                        <div className={"form-wrapper"}>
                            <div>
                                <label htmlFor={"username"}>Username</label>
                            </div>
                            <div>
                                <input id={"username"} type={"text"} value={username} onChange={(ev) => setUsername(ev.target.value)} required/>
                            </div>
                            <div>
                                <label htmlFor={"email"}>E-Mail</label>
                            </div>
                            <div>
                                <input id={"email"} type={"text"} value={email} onChange={(ev) => setEmail(ev.target.value)} required/>
                            </div>
                            <div>
                                <label htmlFor={"password"}>Password</label>
                            </div>
                            <div>
                                <input id={"password"} type={"password"} value={password} onChange={(ev) => setPassword(ev.target.value)} required/>
                            </div>
                            <div>
                                <label htmlFor={"password-again"}>Password bestätigen</label>
                            </div>
                            <div>
                                <input id={"password-again"} type={"password"} value={passwordAgain} onChange={(ev) => setPasswordAgain(ev.target.value)} required/>
                            </div>
                            <div>
                                <input type={"submit"} value={"Register"}/>
                            </div>
                        </div>
                    </form>
                    <a href={"/login"}>Schon angemeldet? Klicke hier!</a>
                </div>
            </div>
        </div>
    )
}