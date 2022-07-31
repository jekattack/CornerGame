import React, {useEffect, useState} from "react";
import "./Subpage.css";
import QuestPageItem from "./QuestPageItem";
import {fetchAllQuests} from "../../service/apiService";
import {Quest} from "../../service/models";
import {useNavigate} from "react-router-dom";

export default function QuestPage(){

    const [loadedQuests, setLoadedQuests] = useState<Quest[]>();

    const nav = useNavigate();

    useEffect(() => {
        fetchAllQuests().then(response => setLoadedQuests(response))
    }, [nav])

    function drawQuestItems(){
        return loadedQuests?.map(quest => <QuestPageItem key={Math.random()*100} questname={quest.name} questdescription={quest.description} kioskcount={quest.kioskGooglePlacesIds.length} durationMinutes={quest.durationInMinutes}/>)
    }

    return(
        <>
            <h1>Quests</h1>
            <p>Nimm an Quests teil und kassiere Extrapunkte. Wähle aus den aktuellen Routen und klicke auf Teilnehmen. Sammle jetzt in der vorgegeben Zeit alle Kioske und kassiere ne Menge Extrapunkte!</p>
            {loadedQuests ? drawQuestItems() : <div>…loading…</div>}
        </>
    )
}