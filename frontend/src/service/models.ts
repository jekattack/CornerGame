export interface Kiosk {
    id?: string;

    googlePlacesId: string;
    name: string;
    kioskAddress: string;
    kioskLocation: {
        location: {
            lat: string;
            lng: string;
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