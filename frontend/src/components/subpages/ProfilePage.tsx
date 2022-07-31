import React from "react";
import "./Subpage.css";

export default function ProfilePage(){




    return(
    <>
        <h1>Dein Profil</h1>
        <p>Wirf einen Blick auf deinen Punktestand und bearbeite deine persönlichen Daten.</p>
        
        <div className={"subpage-content"}>
            <h2 id={"username-display"}>{"Username"}</h2>

            <label htmlFor="kiosk-display">Kiosks besucht:</label>
            <div>
                <textarea id={"kiosk-display"} key={100*Math.random()} readOnly={true} rows={1} cols={50} value={0}/>
            </div>

            <label htmlFor="score-display">Dein Punktestand:</label>
            <div>
                <textarea id={"score-display"} key={100*Math.random()} readOnly={true} rows={1} cols={50} value={0}/>
            </div>

            <label htmlFor="trueKiosk-select">Dein Stammkiosk:</label>
            <div className={"subpage-input-fields"}>
                <select id={"trueKiosk-select"}>…</select>
            </div>

            <label htmlFor="surname-display">Dein Vorname:</label>
            <div className={"subpage-input-fields"}>
                <textarea id={"surname-display"} key={100*Math.random()} readOnly={true} rows={1} cols={50} value={"Jörg"}/>
                <div className={"subpage-input-button"}>ändern</div>
            </div>

            <label htmlFor="lastname-display">Dein Nachname:</label>
            <div className={"subpage-input-fields"}>
                <textarea id={"lastname-display"} key={100*Math.random()} readOnly={true} rows={1} cols={50} value={"Woltaban"}/>
                <div className={"subpage-input-button"}>ändern</div>
            </div>

            <label htmlFor="phone-display">Deine Telefonnummer:</label>
            <div className={"subpage-input-fields"}>
                <textarea id={"phone-display"} key={100*Math.random()} readOnly={true} rows={1} cols={50} value={"0170/10000000"}/>
                <div className={"subpage-input-button"}>ändern</div>
            </div>

            <label htmlFor="password-display">Dein Password:</label>
            <div className={"subpage-input-fields"}>
                <textarea id={"password-display"} key={100*Math.random()} readOnly={true} rows={1} cols={50} value={"********"}/>
                <div className={"subpage-input-button"}>ändern</div>
            </div>
        </div>
    </>
)
}

