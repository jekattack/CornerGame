import React, {useCallback, useEffect, useState} from 'react';
import {GoogleMap, useJsApiLoader} from "@react-google-maps/api";
import { containerStyle, options, centerOnceOnPositionWhenLoaded } from "./mapSettings";
import {fetchAllKiosks, fetchProgress, visit} from "../../service/apiService";
import {ActiveQuest, Kiosk} from "../../service/models";
import '../Components.css';
import './Map.css';
import useGeolocation from "../../service/locationService";
import {toast} from "react-toastify";
import {AxiosError} from "axios";

interface MapProps{
    activeQuest?: ActiveQuest;
    dirRenderer?: ((renderer: React.MutableRefObject<google.maps.DirectionsRenderer> | undefined) => void);
    mapRef: React.MutableRefObject<google.maps.Map|null>;
    inGame: boolean;
    apiAuthCheck: (err: (Error | AxiosError)) => void;
}

export default function Map(props: MapProps){

    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: process.env.REACT_APP_GOOGLE_API_KEY!
    })

    const mapRef = props.mapRef;
    const directionsServiceRef = React.useRef<google.maps.DirectionsService|null>(null);
    const directionsRendererRef = React.useRef<google.maps.DirectionsRenderer|null>(null);
    const markersRef = React.useRef<google.maps.Marker[]|null>(null);

    const onUnmount = (): void => {
        mapRef.current = null;
    }
    const location = useGeolocation();

    //When Map is loaded
    const onLoad = (map: google.maps.Map): void => {
        mapRef.current = map;

        const visitedIdsSet: Set<String> = new Set();
        if(props.inGame){
            fetchProgress()
                .then(response => response.map(response => response.googlePlacesId))
                .then(response => response.map(item => visitedIdsSet.add(item)))
                .catch(err => props.apiAuthCheck(err))

            fetchAllKiosks()
                .then((response) => setMarkers(map, response, visitedIdsSet))
                .then(() =>  enableDirections(map))
                .catch(err => props.apiAuthCheck(err))
        }

        function enableDirections(map: google.maps.Map){
            const directionsServices = new google.maps.DirectionsService();

            const directionsRenderer = new google.maps.DirectionsRenderer({
                suppressMarkers: true
            });

            directionsRenderer.setMap(map);

            directionsServiceRef.current = directionsServices;
            directionsRendererRef.current = directionsRenderer;
            if(directionsRenderer && props.dirRenderer){
                props.dirRenderer(directionsRendererRef as React.MutableRefObject<google.maps.DirectionsRenderer>);
            }
        }
    }

    //Setting Marker for current position
    // eslint-disable-next-line
    const [_, setPositionMarker] = useState<google.maps.Marker>()


    const refreshPositionMarker = useCallback((currentLocationCoords: {lat: number, lng: number}) => {setPositionMarker((positionMarker) => {
        positionMarker?.setVisible(false);
        return (
            new google.maps.Marker({
                map: mapRef.current,
                position: currentLocationCoords,
                icon: "/images/CGIconStandort.png",
                title: "Du",
                zIndex: 300,
            })
        )
    })}, [mapRef])

    useEffect(() => {
        if(mapRef.current && location.loaded) {
            refreshPositionMarker(location.coordinates)
        }
    }, [location, mapRef, refreshPositionMarker])

    useEffect(() => {
        if(directionsServiceRef.current!=null){
            calcRoute(directionsServiceRef.current!,
                directionsRendererRef.current!,
                props.activeQuest?.start!,
                props.activeQuest?.waypoints!,
                props.activeQuest?.finish!)
        }
    }, [props.activeQuest])

    //Setting Markers for Kiosks
    function setMarkers(map: google.maps.Map, kiosks: Kiosk[], progress: Set<String>) {
        let infoWindow: google.maps.InfoWindow = new google.maps.InfoWindow();
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

        kiosks.forEach((kiosk) => {
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
                marker.set("place_id", kiosk.place_id)
            } else {
                marker = new google.maps.Marker({
                    position: { lat: kiosk.geometry.location.lat, lng: kiosk.geometry.location.lng },
                    map,
                    icon: image,
                    shape: shape,
                    title: kiosk.name,
                })
                marker.set("place_id", kiosk.place_id)
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

            marker.addListener("click", () => {
                if (infoWindow) {
                    infoWindow.close();
                }
                infoWindow = new google.maps.InfoWindow({
                    content: contentString
                });
                infoWindow.open({
                    anchor: marker,
                    map,
                    shouldFocus: false
                });
                google.maps.event.addListener(infoWindow, "domready", buttonFunctionality);
            })

            //Adding marker to MarkerArray
            kioskMarkers.push(marker);
            markersRef.current = kioskMarkers;
        })
    }

    //Adding Functionality to button
    function buttonFunctionality (){
        return document.getElementById("visit-button")!.onclick=addVisit;
    }

    //Action for Button in InfoWindow
    function addVisit(){
        const visitGooglePlacesId = document.getElementById('kiosk-identifier')!.innerHTML;
        const locationLat: number = +document.getElementById('location-element-lat')!.innerHTML;
        const locationLng: number = +document.getElementById('location-element-lng')!.innerHTML;
        visit(visitGooglePlacesId, locationLat, locationLng)
            .then(() => {
                toast.success("Kiosk besucht! +100 Punkte 🍻")
                const infoWindowElements = document.getElementsByClassName("gm-style-iw-a")
                while(infoWindowElements.length>0 && infoWindowElements[0].parentNode){
                    infoWindowElements[0].parentNode.removeChild(infoWindowElements[0]);
                }
                for(let i = 0; i<markersRef.current!.length; i++){
                    if(markersRef.current && markersRef.current![i].get("place_id")===visitGooglePlacesId){
                        markersRef.current![i].setIcon({
                            url: "/images/CGIconVisited.png",
                        })
                    }
                }
            })
            .catch((error) => {
            if(error.response) {
                toast.error(error.response.data.message + ": " + error.response.data.subMessages[0])
            }
        });
    }

    function calcRoute(
        directionsService: google.maps.DirectionsService,
        directionsRenderer: google.maps.DirectionsRenderer,
        start: google.maps.LatLng,
        waypoints: google.maps.DirectionsWaypoint[],
        finish: google.maps.LatLng
    ){
        directionsService.route({
            origin: start,
            waypoints: waypoints,
            destination: finish,
            provideRouteAlternatives: false,
            travelMode: google.maps.TravelMode.WALKING,
        })
            .then((response) => {
                directionsRenderer.setDirections(response);
            })
    }

    //When map didnt already load
    if(!isLoaded) return <div>Map Loading ...</div>

    return (
        <div className={"wrapper"}>
            <GoogleMap
                mapContainerStyle={containerStyle}
                options={options(location)}
                center={centerOnceOnPositionWhenLoaded(location)}
                zoom={16}
                onLoad={onLoad}
                onUnmount={onUnmount}
            />
            <div id={"location-element-lat"}>{location.coordinates.lat}</div>
            <div id={"location-element-lng"}>{location.coordinates.lng}</div>
        </div>
    );
};

