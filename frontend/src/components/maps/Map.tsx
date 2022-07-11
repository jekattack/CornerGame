import React, {useEffect} from 'react';
import { GoogleMap, Marker, InfoWindow, useJsApiLoader} from "@react-google-maps/api";
import { containerStyle, center, options } from "./mapSettings";
import {fetchAllKiosks} from "../../service/apiService";

// Styles
import '../Components.css';
import axios from "axios";

const Map: React.FC = () => {

    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: process.env.REACT_APP_GOOGLE_API_KEY!
    })

    const mapRef = React.useRef<google.maps.Map|null>(null);

    const onLoad = (map: google.maps.Map): void => {
        mapRef.current = map;
        setMarkers(map);
    }

    const onUnmount = (): void => {
        mapRef.current = null;
    }

    useEffect(() => {
        fetchAllKiosks()


    }, [])


    const kiosks: [string, number, number, number][] = [
        ["Kiosk1", 53.5663504, 9.9539504, 4],
        ["Kiosk2", 53.560944, 9.9673876, 5],
        ["Kiosk3", 53.5545769, 9.9575032, 3],
        ["Kiosk4", 53.5584889, 9.9726274, 2],
        ["Kiosk5", 53.5615809, 9.9530724, 1],
    ];

    function setMarkers(map: google.maps.Map) {

        const image = {
            url: "/images/CGLogoBildBG.svg",
            // This marker is 20 pixels wide by 32 pixels high.
            size: new google.maps.Size(20, 32),
            // The origin for this image is (0, 0).
            origin: new google.maps.Point(0, 0),
            // The anchor for this image is the base of the flagpole at (0, 32).
            anchor: new google.maps.Point(0, 32),
        };
        // Shapes define the clickable region of the icon. The type defines an HTML
        // <area> element 'poly' which traces out a polygon as a series of X,Y points.
        // The final coordinate closes the poly by connecting to the first coordinate.
        const shape = {
            coords: [1, 1, 1, 20, 18, 20, 18, 1],
            type: "poly",
        };


        for (let i = 0; i < kiosks.length; i++) {
            const kiosk = kiosks[i];

            new google.maps.Marker({
                position: { lat: kiosk[1], lng: kiosk[2] },
                map,
                icon: image,
                shape: shape,
                title: kiosk[0],
                zIndex: kiosk[3],
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