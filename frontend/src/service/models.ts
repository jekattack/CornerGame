import exp from "constants";

export interface Kiosk {
    id?: string;

    place_id: string;
    name: string;
    vicinity: string;
    geometry: {
        location: {
            lat: number;
            lng: number;
        }
    };
    isManaged: boolean;
    shopkeeperId: string;
    phone: string;
    shortDescription: string;
    openingHours: {
        monday: {
            openTime: string,
            closeTime: string
        };
        thuesday: {
            openTime: string,
            closeTime: string
        };
        wednesday: {
            openTime: string,
            closeTime: string
        };
        thursday: {
            openTime: string,
            closeTime: string
        };
        friday: {
            openTime: string,
            closeTime: string
        };
        saturday: {
            openTime: string,
            closeTime: string
        };
        sunday: {
            openTime: string,
            closeTime: string
        };
    };
    hasRestroom: boolean;
    hasOutdoors: boolean;
    hasBakery: boolean;
    hasLottery: boolean;
    parcelshop: string;

}

export interface LoginResponse {
    token: string;
}

export interface CGGeolocation {
    loaded: boolean,
    coordinates: {
        lat: number,
        lng: number
    },
    error: CGGeolocationError;
}

export interface CGGeolocationError {
    code: number;
    message: string;
}

export interface Visit {
    id: string,
    googlePlacesId: string,
    issuedAt: Date,
    visitType: string
}

export interface Quest {
    id: string;
    name: string;
    description: string;
    kioskGooglePlacesIds: string[];
    durationInMinutes: number;
    scoreMultiplier: number;
}

export interface Achievement {
    id: string;
    name: string;
    description: string;
    requirements: {
        visitsCreated: number;
        questsStarted: number;
        questsFinished: number;
        kiosksVisited: number;
    };
}

export interface CGUserGameDataDTO {
    username: string;
    score: number;
}

export interface CGUser {
    id: string;
    role: string;
    username: string;
    email: string;
    password: string;
    validated: boolean;
    lastname: string;
    firstname: string;
    phone: string;
    stammkioskId: string;
}

export interface CGUserUpdateDTO {
    firstname?: string;
    lastname?: string;
    phone?: string;
    stammkioskId?: string;
}

export interface CGUserPasswordDTO {
    password: string;
    passwordAgain: string;
}

export interface CGError {
    message: string;
    subMessages: string[];
}

export interface ActiveQuest {
    start: google.maps.LatLng,
    waypoints: google.maps.DirectionsWaypoint[],
    finish: google.maps.LatLng
}

export interface QuestEventDTO {
    message: string;
    quest: Quest;
}

export interface ActiveQuestDTO {
    quest: Quest;
    minutesLeft: number;
}