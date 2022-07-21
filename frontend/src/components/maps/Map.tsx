import React, {useCallback} from 'react';
import {GoogleMap, useJsApiLoader} from "@react-google-maps/api";
import { containerStyle, center, options } from "./mapSettings";
import {fetchAllKiosks, fetchProgress, visit} from "../../service/apiService";
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
    const onUnmount = (): void => {
        mapRef.current = null;
    }
    const location = useGeolocation();

    //When Map is loaded
    const onLoad = (map: google.maps.Map): void => {
        mapRef.current = map;
        setContinuouslyCurrentPosition();

        const visitedIdsSet: Set<String> = new Set();
        fetchProgress()
            .then(response => response.map(response => response.googlePlacesId))
            .then(response => response.map(item => visitedIdsSet.add(item)))

        fetchAllKiosks()
            .then((response) => setMarkers(map, response, visitedIdsSet));
    }

    //Setting Marker for current position
    const setContinuouslyCurrentPosition = useCallback(() => {
        if(mapRef.current != null && location.coordinates != null) {
            setPositionMarker(mapRef.current, location.coordinates)
        }
    }, [location])

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
                '<div id="site-notice">' +
                "</div>" +
                '<h1 id="first-heading" class="first-heading">' + kiosk.name +
                '</h1>' +
                '<div id="body-content">' +
                '<button id="visit-button">' +
                'Jetzt Kiosk besuchen!' +
                '</button>' +
                "</div>" +
                '<div id="kiosk-identifier" style="display: none">' + kiosk.place_id +
                "</div>" +
                "</div>";

            const infoWindow = new google.maps.InfoWindow({
                content: contentString});

            marker.addListener("click", () => {
                infoWindow.open({
                    anchor: marker,
                    map,
                    shouldFocus: false
                });
                google.maps.event.addListener(infoWindow, "domready", buttonFunctionality);
            })

            //Adding marker to MarkerArray
            kioskMarkers.push(marker);
        }
    }

    //Adding Functionality to button
    function buttonFunctionality (){
        document.getElementById("visit-button")!.onclick=addVisit;
    };

    //Action for Button in InfoWindow
    function addVisit(){
        const visitGooglePlacesId = document.getElementById('kiosk-identifier')!.innerHTML;
        const locationLat: number = +document.getElementById('location-element-lat')!.innerHTML;
        const locationLng: number = +document.getElementById('location-element-lng')!.innerHTML;
        visit(visitGooglePlacesId, locationLat, locationLng).then(() => console.log("success!"));
    }

    //When map didnt already load
    if(!isLoaded) return <div>Map Loading ...</div>

    return (
        <div className={"wrapper"}>
            <GoogleMap
                mapContainerStyle={containerStyle}
                options={options as google.maps.MapOptions}
                center={location.loaded && location.error.code===0 ? location.coordinates :center}
                zoom={16}
                onLoad={onLoad}
                onUnmount={onUnmount}
            />
            <div id={"location-element-lat"}>{location.coordinates.lat}</div>
            <div id={"location-element-lng"}>{location.coordinates.lng}</div>
        </div>
    );
};

export default Map;