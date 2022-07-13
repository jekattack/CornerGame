import React from 'react';
import { GoogleMap, useJsApiLoader} from "@react-google-maps/api";
import { containerStyle, center, options } from "./mapSettings";
import {fetchAllKiosks} from "../../service/apiService";
import {Kiosk} from "../../service/models";
import '../Components.css';

const Map: React.FC = () => {

    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: process.env.REACT_APP_GOOGLE_API_KEY!
    })

    const mapRef = React.useRef<google.maps.Map|null>(null);

    const onLoad = (map: google.maps.Map): void => {
        mapRef.current = map;
        fetchAllKiosks()
            .then((response) => setMarkers(map, response));
    }

    const onUnmount = (): void => {
        mapRef.current = null;
    }

    function setMarkers(map: google.maps.Map, kiosks: Kiosk[]) {

        const image = {
            url: "/images/CGLogoBildBGIcon.png",
        };

        const shape = {
            coords: [60,60,60],
            type: "circle",
        };

        for (let i = 0; i < kiosks.length; i++) {
            const kiosk = kiosks[i];
            new google.maps.Marker({
                position: { lat: kiosk.geometry.location.lat, lng: kiosk.geometry.location.lng },
                map,
                icon: image,
                shape: shape,
                title: kiosk.name
            });
        }
    }

    if(!isLoaded) return <div>Map Loading ...</div>

    return (
        <div className={"Wrapper"}>
            <GoogleMap
                mapContainerStyle={containerStyle}
                options={options as google.maps.MapOptions}
                center={center}
                zoom={16}
                onLoad={onLoad}
                onUnmount={onUnmount}
            />
        </div>
    );
};

export default Map;