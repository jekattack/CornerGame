import Map from "../components/maps/Map";
import React, {useEffect, useState} from "react";
import Menu from "../components/controls/Menu";
import "../App.css";
import {ActiveQuest, ActiveQuestDTO, Quest} from "../service/models";
import {cancelActiveQuest, fetchActiveQuestsInfo, startQuestRequest} from "../service/apiService";
import {toast} from "react-toastify";
import HomePageQuestControls from "../components/subpages/HomePageQuestControls";

export default function HomePage(){

    const [isVisible, setIsVisible] = useState(false);
    const [activeQuest, setActiveQuest] = useState<ActiveQuest>();
    const [questMode, setQuestMode] = useState(false);
    const [queststartedMode, setQueststartedMode] = useState(false);
    const [activeQuestInfo, setActiveQuestInfo] = useState<Quest>();
    const [timeRemains, setTimeRemains] = useState<number>(0);
    const [dirRenderer, setDirRenderer] = useState<React.MutableRefObject<google.maps.DirectionsRenderer>|undefined>();

    useEffect(() => {
        if(timeRemains !== undefined && timeRemains > 0){
            let timeRemainsRetainer = timeRemains;
            timeRemainsRetainer--;
            setTimeout(() => {setTimeRemains(timeRemainsRetainer)}, 60000)
        } else {
            setQueststartedMode(false)
        }
    }, [timeRemains])

    function getActiveQuests(activeQuestInfo: Quest){
        fetchActiveQuestsInfo()
            .then(response => response.find(questInfo => questInfo.quest.id===activeQuestInfo.id))
            .then(response => {
                if(response !== undefined && response.minutesLeft >= 0){
                    setTimeRemains(response.minutesLeft)
                } else {
                    setQueststartedMode(false);
                }
            })
    }

    function startQuest(){
        if(activeQuestInfo?.id !== undefined){
            startQuestRequest(activeQuestInfo!.id)
                .then(response => {
                    toast.success(response.message)
                    setQueststartedMode(true)
                    getActiveQuests(activeQuestInfo!)
                })
                .catch((error) => {
                    if(error.response) {
                        toast.error(error.response.data.message + ": " + error.response.data.subMessages[0])
                    }
                })
        }
    }

    function cancelQuest(){
        if(activeQuestInfo!==undefined){
            cancelActiveQuest(activeQuestInfo.id)
                .then((response) => {
                    setQuestMode(false);
                    setQueststartedMode(false);
                    setActiveQuest(undefined)
                    setActiveQuestInfo(undefined)
                    toast.success(response.message)
                })
                .catch((error) => {
                    if(error.response) {
                        toast.error(error.response.data.message + ": " + error.response.data.subMessages[0])
                    }
                })
        }
        setQuestMode(false);
        setQueststartedMode(false);
    }

    return (
        <div id={"app-container"}>
            <Map
                activeQuest={activeQuest}
                dirRenderer={setDirRenderer}
            />
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
                </>
            }
            {questMode &&
                <>
                    <HomePageQuestControls
                        dirRenderer={dirRenderer}
                        cancelQuest={cancelQuest}
                        queststartedMode={queststartedMode}
                        timeRemains={timeRemains}
                        startQuest={startQuest}
                        questName={activeQuestInfo?.name ? activeQuestInfo.name : "🛸"}
                    />
                </>
            }

        </div>
    )
}