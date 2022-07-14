import Map from "../components/maps/Map";
import React from "react";
import useGeolocation from "../service/locationService";

export default function HomePage(){

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
                        Du befindest dich hier: {location.coordinates.lat}, {location.coordinates.lng}
                    </div>
                </div>
            </div>
        </div>
    )
}