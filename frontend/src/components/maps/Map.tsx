import React, {useCallback} from 'react';
import { GoogleMap, useJsApiLoader } from "@react-google-maps/api";
import { containerStyle, center, options } from "./mapSettings";
import { fetchAllKiosks } from "../../service/apiService";
import { Kiosk } from "../../service/models";
import '../Components.css';
import './Map.css';
import useGeolocation from "../../service/locationService";

const Map: React.FC = () => {

    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: process.env.REACT_APP_GOOGLE_API_KEY!
    })

    const mapRef = React.useRef<google.maps.Map|null>(null);

    const currentLocation = useGeolocation();

    const onLoad = (map: google.maps.Map): void => {
        mapRef.current = map;
        setContinuouslyCurrentPosition()
        fetchAllKiosks()
            .then((response) => setMarkers(map, response));
    }

    const onUnmount = (): void => {
        mapRef.current = null;
    }

    const setContinuouslyCurrentPosition = useCallback(() => {
        if(mapRef.current != null && currentLocation.coordinates != null) {
            setPositionMarker(mapRef.current, currentLocation.coordinates)
        };
    }, [currentLocation])

    function setPositionMarker(map: google.maps.Map, currentLocationCoords: {lat: number, lng: number}){
        new google.maps.Marker({
            position: currentLocationCoords,
            map,
            icon: "/images/CGIconStandort.png",
            title: "Du",
            zIndex: 300
        })
    }

    function setMarkers(map: google.maps.Map, kiosks: Kiosk[]) {
        const image = {
            url: "/images/CGLogoBildBGIcon.png",
        };
        const shape = {
            coords: [60,60,60],
            type: "circle",
        };
        const kioskMarkers = new Array<google.maps.Marker>();
        for (let i = 0; i < kiosks.length; i++) {
            const kiosk = kiosks[i];
            const marker = new google.maps.Marker({
                position: { lat: kiosk.geometry.location.lat, lng: kiosk.geometry.location.lng },
                map,
                icon: image,
                shape: shape,
                title: kiosk.name
            })
            const contentString =
                '<div id="content">' +
                '<div id="siteNotice">' +
                "</div>" +
                '<h1 id="firstHeading" class="firstHeading">' + kiosk.name + '</h1>' +
                '<div id="bodyContent">' +
                "<button>Jetzt Kiosk einchecken!</button>" +
                "</div>" +
                "</div>";
            const infoWindow = new google.maps.InfoWindow({
                content: contentString});
            marker.addListener("click", () => {
                infoWindow.open({
                    anchor: marker,
                    map,
                    shouldFocus: false
                })
            })
            kioskMarkers.push(marker);
        }
    }

    if(!isLoaded) return <div>Map Loading ...</div>

    return (
        <div className={"wrapper"}>
            <GoogleMap
                mapContainerStyle={containerStyle}
                options={options as google.maps.MapOptions}
                center={currentLocation.loaded && currentLocation.error.code===0 ? currentLocation.coordinates :center}
                zoom={16}
                onLoad={onLoad}
                onUnmount={onUnmount}
            />
        </div>
    );
};

export default Map;