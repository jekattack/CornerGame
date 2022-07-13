import axios, {AxiosResponse} from "axios";
import {Kiosk} from "./models";

export function fetchAllKiosks() {
    return (
        axios.get("/api/kiosk")
            .then((response: AxiosResponse<Kiosk[]>) => response.data)
    )
}