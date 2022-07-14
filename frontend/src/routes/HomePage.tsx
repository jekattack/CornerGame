import {useNavigate} from "react-router-dom";
import Map from "../components/maps/Map";
import React, {useEffect} from "react";
import useGeolocation from "../service/locationService";

export default function HomePage(){

    const nav = useNavigate();

    const location = useGeolocation();

    return (
        <div id={"app-container"}>
            <Map />
            <div className={"wrapper"}>
                <div className={"content-wrapper"}>
                    <img src="/images/CGLogoWort.svg" className={"logo"} alt={"Logo"}/>
                    <div>
                        Das Spiel kann beginnen!
                    </div>
                    <div>
                        Du befindest dich hier: {useGeolocation().coordinates.lat}, {useGeolocation().coordinates.lng}
                    </div>
                </div>
            </div>
        </div>
    )
}