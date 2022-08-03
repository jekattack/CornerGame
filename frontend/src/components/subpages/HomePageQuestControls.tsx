import React, {useState} from "react";
import {ActiveQuest, Quest} from "../../service/models";

interface HomePageQuestControlsProps{
    dirRenderer: React.MutableRefObject<google.maps.DirectionsRenderer>|undefined;
    queststartedMode: boolean;
    timeRemains: number;
    startQuest: (() => void);
    cancelQuest: (() => void);
    questName: string;
}

export default function HomePageQuestControls(props: HomePageQuestControlsProps){

    const [collapsed, setCollapsed] = useState<boolean>(false);


    return(
        <div id={"active-quest-info-wrapper"}>
            <div className={"active-quest-info-header"}>

                    <div>
                        Du hast noch {props.timeRemains} Min.
                    </div>
                {!collapsed &&
                    <svg
                        width="24"
                        height="24"
                        viewBox="0 0 24 24"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                        onClick={() => setCollapsed(true)}
                    >
                        <path d="M18 6L6 18" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M6 6L18 18" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    </svg>
                }
                {collapsed &&
                    <svg
                        width="24"
                        height="24"
                        viewBox="0 0 24 24"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                        onClick={() => setCollapsed(false)}
                    >
                        <path d="M18 15L12 9L6 15" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    </svg>

                }
            </div>
            {
                !collapsed &&
                <>
                    {props.queststartedMode &&
                        <>
                            <h4>{props.questName ? props.questName : "🛸"}</h4>
                            <div className={"subpage-input-button-bold"} onClick={() => {
                                props.cancelQuest()
                                if(props.dirRenderer){
                                    props.dirRenderer.current.setMap(null)
                                }
                            }}>
                                Quest abbrechen
                            </div>
                        </>

                    }
                    {!props.queststartedMode &&
                        <>
                            <h4>{props.questName ? props.questName : "🛸"}</h4>
                            <div className={"subpage-input-button-bold"} onClick={() => props.startQuest()}>
                                Quest starten!
                            </div>
                        </>
                    }
                </>
            }
        </div>
    )
}