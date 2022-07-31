import React from "react";

export default function QuestPageItem(props: any){

    const questname: string = props.questname;
    const questdescription: string = props.questdescription;
    const kioskcount: number = props.kioskcount;
    const durationMinutes: number = props.durationMinutes;

    return(
        <div className={"subpage-content"}>
            <h2>{questname}</h2>

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

            <div className={"subpage-input-button-bold"}>Zeig mir mehr!</div>
        </div>
    )
}