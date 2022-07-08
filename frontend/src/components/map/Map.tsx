import React from 'react';
import { GoogleMap, Marker, InfoWindow, useJsApiLoader} from "@react-google-maps/api";
//Map Settings
import { containerStyle, center, options } from "./mapSettings";

// Styles
import '../Components.css';

export default function Map(){

    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: process.env.REACT_APP_GOOGLE_API_KEY!
    })

    const mapRef = React.useRef<google.maps.Map|null>(null);

    const onLoad = (map: google.maps.Map): void => {
        mapRef.current = map;
        createMarker(    53.5627359, 9.9607925)
    }

    const onUnmount = (): void => {
        mapRef.current = null;
    }

    const createMarker = (lat: number, lng: number) => {
        return new google.maps.Marker({
            position:{
                lat: lat,
                lng: lng
            },
            map: mapRef.current
        })
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

