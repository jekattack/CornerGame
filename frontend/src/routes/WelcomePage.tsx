import '../components/Components.css';
import Map from "../components/maps/Map";
import React from "react";
import {useNavigate} from "react-router-dom";

export default function WelcomePage(){

    const nav = useNavigate();



    return (
        <div id={"app-container"}>
            <Map />
            <div className={"wrapper"}>
                <div id={"content-wrapper"}>
                    <img src="/images/CGLogoWort.svg" className={"image-logo"} alt={"Logo"}/>
                    <div>
                        Kiosk-, Späti- und Trinkhallenkultur vom feinsten.
                    </div>
                    <div>
                        Zeig deinem Stammkiosk deine Liebe, entdecke neue Perlen der Stadt und triff dich mit Freund:innen auf ein Getränk am Stromkasten deines Vertrauens.
                    </div>
                    <div>
                        <button onClick={() => nav("/register")}>Jetzt anmelden</button><br/>
                    </div>
                    <div>
                        <button onClick={() => nav("/login")}>Zum Login</button>
                    </div>
                </div>
            </div>
        </div>
    )
}