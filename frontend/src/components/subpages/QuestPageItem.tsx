import React, {useState} from "react";
import {ActiveQuest, Quest} from "../../service/models";
import {getLocationsForQuest} from "../../service/apiService";
import {toast} from "react-toastify";

interface QuestPageItemProps{
    key: string;
    questModeSetter: ((questMode: boolean) => void);
    menuModeSetter: ((menuMode: boolean) => void);
    questPageSetter: ((questPage: boolean) => void);
    activeQuestSetter: ((quest: ActiveQuest) => void),
    activeQuestInfoSetter: ((quest: Quest) => void),
    quest: Quest,
    questname: string,
    questdescription: string,
    kioskcount: number,
    durationMinutes: number,
    dirRenderer: React.MutableRefObject<google.maps.DirectionsRenderer>|undefined,
    mapRef: React.MutableRefObject<google.maps.Map|null>;
}

export default function QuestPageItem(props: QuestPageItemProps){

    const questname: string = props.questname;
    const questdescription: string = props.questdescription;
    const kioskcount: number = props.kioskcount;
    const durationMinutes: number = props.durationMinutes;

    const [expanded, setExpanded] = useState<boolean>(false);


    function getLocationsForQuestAndTriggerDirections(quest: Quest){
        getLocationsForQuest(quest.kioskGooglePlacesIds)
            .then((response: ActiveQuest) => {
                props.dirRenderer?.current?.setMap(props.mapRef.current)
                createDirection(response)
            })
            .then(() => {toast.success("Route wird auf Karte angezeigt. 🏎")})
            .then(() => {
                props.activeQuestInfoSetter(props.quest);
                props.menuModeSetter(false);
                props.questPageSetter(false);
                props.questModeSetter(true);
            })
    }

    function createDirection(activeQuest: ActiveQuest){
        props.activeQuestSetter(activeQuest)
    }

    return(
        <div className={"subpage-content"}>
            <div className={"quest-controls-header"}>
                <h2>{questname}</h2>
                { expanded ?
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" onClick={() => setExpanded(false)}>
                        <path d="M4 14H10V20" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M20 10H14V4" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M14 10L21 3" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M3 21L10 14" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    </svg>
                    :
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" onClick={() => setExpanded(true)}>
                        <path d="M15 3H21V9" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M9 21H3V15" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M21 3L14 10" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M3 21L10 14" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    </svg>
                }
            </div>
            { expanded &&
                <>
                    <div className={"subpage-input-fields"}>
                        <textarea className={"kioskcount-display"} readOnly={true} rows={3} cols={50} value={questdescription ?? "defaultDesc"}/>
                    </div>

                    <label htmlFor="kiosk-display">Anzahl Kioske:</label>
                    <div className={"subpage-input-fields"}>
                    <textarea className={"kioskcount-display"} readOnly={true} rows={1} cols={50} value={kioskcount ?? "defaultCount"}/>
                    </div>

                    <label htmlFor="kiosk-display">Zeitlimit (Minuten):</label>
                    <div className={"subpage-input-fields"}>
                    <textarea className={"duration-minutes-display"} readOnly={true} rows={1} cols={50} value={durationMinutes ?? "defaultDur"}/>
                    </div>
                    <div className={"subpage-input-button-bold"} onClick={() => getLocationsForQuestAndTriggerDirections(props.quest)}>Quest auf Karte anzeigen!</div>
                </>
            }
        </div>
    )
}