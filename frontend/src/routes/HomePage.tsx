import Map from "../components/maps/Map";
import React, {useState} from "react";
import Menu from "../components/controls/Menu";

export default function HomePage(){

    const [isVisible, setIsVisible] = useState(false);

    return (
        <div id={"app-container"}>
            <Map />
            {isVisible && <div id={"content-wrapper"}>
                <Menu visibility={setIsVisible}/>
            </div>}
            {!isVisible && <div id={"content-wrapper"} onClick={() => {setIsVisible(true)}}>
                <div className={"content-controls-header"}>
                    <img src="/images/CGLogoBild.svg" className={"logo"} alt={"Logo"}/>
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M3 12H21" stroke="#111111" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        <path d="M3 6H21" stroke="#111111" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        <path d="M3 18H21" stroke="#111111" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                </div>
            </div>}
        </div>
    )
}