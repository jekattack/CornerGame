import {CGGeolocation} from "../../service/models";

export function options(location: CGGeolocation) {
    return {
        center: location.loaded && location.error.code===0 ? location.coordinates : center,
            mapId: "fe949f204787b654",
        disableDefaultUI: true
    } as google.maps.MapOptions
}


export const containerStyle = {
    width: '100%',
    height: '100vh'
};

// Center on Umut
export const center = {
    lat: 53.5627359,
    lng: 9.9607925
};

let locationRetainer: CGGeolocation;
locationRetainer = {
    loaded: false,
    coordinates: {
        lat: 53.5627359,
        lng: 9.9607925
    }, error: {
        code: 0,
        message: "",
    }
};

//Position is only centered once on current position
export function centerOnceOnPositionWhenLoaded(location: CGGeolocation) {
    if (!locationRetainer.loaded && location.loaded && location.error.code === 0){
        locationRetainer = location;
        return locationRetainer.coordinates;
    }
}
