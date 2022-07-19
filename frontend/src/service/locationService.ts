import {useEffect, useState} from "react";
import {CGGeolocation, CGGeolocationError} from "./models";

export default function useGeolocation(){
    const [location, setLocation] = useState<CGGeolocation>({
        loaded: false,
        coordinates: {
            lat: 0,
            lng: 0
        },
        error: {
            code: 0,
            message: ""
        }
    });

    const onSuccess = (location: GeolocationPosition) => {
        setLocation({
            loaded: true,
            coordinates: {
                lat: location.coords.latitude,
                lng: location.coords.longitude
            },
            error: {
                code: 0,
                message: ""
            }
        });
    };

    const onError = (err: CGGeolocationError) => {
        setLocation({
            loaded: true,
            coordinates: {
                lat: 0,
                lng: 0,
            },
            error: err
        });
    }


    useEffect(() => {
        if(!("geolocation" in navigator)) {
            onError({
                code: 2,
                message: "Geolocation not supported"
            });
        }

        navigator.geolocation.getCurrentPosition(onSuccess, onError)

    }, [])
    return location;
}