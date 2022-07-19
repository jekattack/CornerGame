import React, {useCallback} from 'react';
import { GoogleMap, useJsApiLoader } from "@react-google-maps/api";
import { containerStyle, center, options } from "./mapSettings";
import {fetchAllKiosks, fetchProgress} from "../../service/apiService";
import {Kiosk} from "../../service/models";
import '../Components.css';
import './Map.css';
import useGeolocation from "../../service/locationService";

const Map: React.FC = () => {

    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: process.env.REACT_APP_GOOGLE_API_KEY!
    })

    const mapRef = React.useRef<google.maps.Map|null>(null);

    const onUnmount = (): void => {
        mapRef.current = null;
    }

    const currentLocation = useGeolocation();

    //When Map is loaded
    const onLoad = (map: google.maps.Map): void => {
        mapRef.current = map;
        setContinuouslyCurrentPosition()

        const visitedIdsSet: Set<String> = new Set();
        fetchProgress()
            .then(response => response.map(response => response.googlePlacesId))
            .then(response => response.map(item => visitedIdsSet.add(item)))

        fetchAllKiosks()
            .then((response) => setMarkers(map, response, visitedIdsSet));
    }

    //Setting Marker for current position
    const setContinuouslyCurrentPosition = useCallback(() => {
        if(mapRef.current != null && currentLocation.coordinates != null) {
            setPositionMarker(mapRef.current, currentLocation.coordinates)
        }
        console.log("Current Marker!")
        console.log(currentLocation.coordinates.lat)
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

    //Setting Markers for Kiosks
    function setMarkers(map: google.maps.Map, kiosks: Kiosk[], progress: Set<String>) {
        const image = {
            url: "/images/CGLogoBildBGIcon.png",
        };
        const imageDone = {
            url: "/images/CGIconVisited.png",
        };
        const shape = {
            coords: [60,60,60],
            type: "circle",
        };
        const kioskMarkers = new Array<google.maps.Marker>();
        for (let i = 0; i < kiosks.length; i++) {
            const kiosk = kiosks[i];
            let marker: google.maps.Marker;

            //Differentiation if Kiosk is already visited

            if(progress.has(kiosk.place_id)){
                marker = new google.maps.Marker({
                    position: { lat: kiosk.geometry.location.lat, lng: kiosk.geometry.location.lng },
                    map,
                    icon: imageDone,
                    shape: shape,
                    title: kiosk.name
                })
            } else {
                marker = new google.maps.Marker({
                    position: { lat: kiosk.geometry.location.lat, lng: kiosk.geometry.location.lng },
                    map,
                    icon: image,
                    shape: shape,
                    title: kiosk.name
                })
            }

            //Content for InfoWindow
            const contentString =
                '<div id="content">' +
                '<div id="siteNotice">' +
                "</div>" +
                '<h1 id="firstHeading" class="firstHeading">' + kiosk.name +
                '</h1>' +
                '<div id="bodyContent">' +
                '<button id="visit-button">' +
                'Jetzt Kiosk besuchen!' +
                '</button>' +
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

            //Adding marker to MarkerArray

            kioskMarkers.push(marker);
        }
    }

    //When map didnt already load

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