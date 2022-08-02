import Map from "../components/maps/Map";
import React, {useEffect, useState} from "react";
import Menu from "../components/controls/Menu";
import "../App.css";
import {ActiveQuest, ActiveQuestDTO, Quest} from "../service/models";
import {fetchActiveQuestsInfo, startQuestRequest} from "../service/apiService";
import {toast} from "react-toastify";

export default function HomePage(){

    const [isVisible, setIsVisible] = useState(false);
    const [activeQuest, setActiveQuest] = useState<ActiveQuest>();
    const [questMode, setQuestMode] = useState(false);
    const [queststartedMode, setQueststartedMode] = useState(false);
    const [activeQuestInfo, setActiveQuestInfo] = useState<Quest>();
    const [timeRemains, setTimeRemains] = useState<number>();

    useEffect(() => {
        if(timeRemains!=null || timeRemains!=0){
            setTimeout(() => {setTimeRemains(timeRemains!-1)}, 60000)
        } else {
            toast.error("Zeit für den Quest ist abgelaufen ⌛️")
            setQueststartedMode(false)
        }
    }, [timeRemains])

    function getActiveQuests(activeQuestInfo: Quest){
        fetchActiveQuestsInfo()
            .then(response => {
                const questInfo : ActiveQuestDTO | undefined = response.find(questInfo => questInfo.quest==activeQuestInfo)
                if(questInfo?.minutesLeft){
                    setTimeRemains(questInfo.minutesLeft)
                }
            })
    }

    function startQuest(){
        if(activeQuestInfo?.id != null){
            startQuestRequest(activeQuestInfo.id)
                .then(response => {
                    toast.success(response.message)
                    setQueststartedMode(true)
                    getActiveQuests(activeQuestInfo)
                })
                .catch((error) => {
                    if(error.response) {
                        toast.error(error.response.data.message + ": " + error.response.data.subMessages[0])
                    }
                })
        }
    }

    return (
        <div id={"app-container"}>
            <Map activeQuest={activeQuest}/>
            {isVisible && <div id={"content-wrapper"}>
                <Menu
                    questModeSetter={setQuestMode}
                    activeQuestSetter={setActiveQuest}
                    activeQuestInfoSetter={setActiveQuestInfo}
                    visibility={setIsVisible}/>
            </div>}
            {!isVisible &&
                <>
                    <div id={"content-wrapper"} onClick={() => {setIsVisible(true)}}>
                        <div className={"content-controls-header"}>
                            <img src="/images/CGLogoBild.svg" className={"logo"} alt={"Logo"}/>
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M3 12H21" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                <path d="M3 6H21" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                <path d="M3 18H21" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                            </svg>
                        </div>
                    </div>
                    {questMode ??
                        <>
                            {queststartedMode ??
                                <div id={"active-quest-info-wrapper"}>
                                    So lange hast du noch Zeit: {timeRemains} Minuten
                                </div>
                            }
                            {!queststartedMode ??
                                <div id={"active-quest-info-wrapper"} onClick={() => startQuest()}>
                                    Quest starten!
                                </div>
                            }
                        </>
                    }
                </>
            }
        </div>
    )
}