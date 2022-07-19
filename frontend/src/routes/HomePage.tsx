import Map from "../components/maps/Map";
import React, {useState} from "react";
import useGeolocation from "../service/locationService";
import ContentControlsHeader from "../components/controls/ContentControlsHeader";

export default function HomePage(){

    const location = useGeolocation();
    const [isVisible, setIsVisible] = useState(true);

    return (
        <div id={"app-container"}>
            <Map />
            {isVisible && <div id={"content-wrapper"}>
                <ContentControlsHeader statusContent={setIsVisible}/>
                <div>
                    Das Spiel kann beginnen!
                </div>
                <div>
                    Du befindest dich hier: {location.coordinates.lat}, {location.coordinates.lng}
                </div>
            </div>}
            {!isVisible && <div id={"content-wrapper"} onClick={() => {setIsVisible(true)}}>
                <img src="/images/CGLogoBild.svg" className={"logo"} alt={"Logo"}/>
            </div>}

        </div>
    )
}