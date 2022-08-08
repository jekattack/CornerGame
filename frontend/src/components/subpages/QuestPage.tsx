import React, {useEffect, useState} from "react";
import "./Subpage.css";
import QuestPageItem from "./QuestPageItem";
import {fetchAllQuests} from "../../service/apiService";
import {ActiveQuest, Quest} from "../../service/models";
import {useNavigate} from "react-router-dom";

interface QuestProps{
    questModeSetter: ((questMode: boolean) => void);
    activeQuestSetter: ((quest: ActiveQuest) => void);
    activeQuestInfoSetter: ((quest: Quest) => void);
    menuModeSetter: ((menuMode: boolean) => void);
    questPageSetter: ((questPage: boolean) => void);
}

export default function QuestPage(props: QuestProps){


    const [loadedQuests, setLoadedQuests] = useState<Quest[]>();

    const nav = useNavigate();

    useEffect(() => {
        fetchAllQuests().then(response => setLoadedQuests(response))
    }, [nav])

    function drawQuestItems(){
        return loadedQuests?.map(quest =>
            <QuestPageItem
                key={quest.id}
                questModeSetter={props.questModeSetter}
                menuModeSetter={props.menuModeSetter}
                questPageSetter={props.questPageSetter}
                activeQuestSetter={props.activeQuestSetter}
                activeQuestInfoSetter={props.activeQuestInfoSetter}
                quest={quest}
                questname={quest.name}
                questdescription={quest.description}
                kioskcount={quest.kioskGooglePlacesIds.length}
                durationMinutes={quest.durationInMinutes}/>)
    }

    return(
        <>
            <h1>Quests</h1>
            <p>Nimm an Quests teil und kassiere Extrapunkte. Wähle aus den aktuellen Routen und klicke auf Teilnehmen. Sammle jetzt in der vorgegeben Zeit alle Kioske und kassiere ne Menge Extrapunkte!</p>
            {loadedQuests ? drawQuestItems() : <div>…loading…</div>}
        </>
    )
}