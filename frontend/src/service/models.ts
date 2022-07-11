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