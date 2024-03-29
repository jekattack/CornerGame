import '../Components.css';
import ContentControlsHeader from "./ContentControlsHeader";
import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import AchievementPage from "../subpages/AchievementPage";
import ProfilePage from "../subpages/ProfilePage";
import QuestPage from "../subpages/QuestPage";
import ScoreboardPage from "../subpages/ScoreboardPage";
import {ActiveQuest, Quest} from "../../service/models";

interface MenuProps{
    questModeSetter: ((questMode: boolean) => void),
    activeQuestSetter: ((quest: ActiveQuest) => void),
    activeQuestInfoSetter: ((quest: Quest) => void),
    visibility: ((visibility: boolean) => void),
    dirRenderer: React.MutableRefObject<google.maps.DirectionsRenderer>|undefined,
    mapRef: React.MutableRefObject<google.maps.Map|null>;
}

export default function Menu(props: MenuProps){

    const nav = useNavigate();

    const [menuMode, setMenuMode] = useState(true)
    const [profilePage, setProfilePage] = useState(false)
    const [achievementPage, setAchievementPage] = useState(false)
    const [questPage, setQuestPage] = useState(false)
    const [scoreboardPage, setScoreboardPage] = useState(false)

    function toPageMode(pageState: React.Dispatch<React.SetStateAction<boolean>>) {
        setMenuMode(false)
        pageState(true)
    }

    function logout() {
        localStorage.clear()
        return nav("/")
    }

    function togglePage() {
        setProfilePage(false);
        setAchievementPage(false);
        setQuestPage(false);
        setScoreboardPage(false);
    }

    return(
        <div>
            <ContentControlsHeader statusContent={props.visibility} menuMode={menuMode} setMenuMode={setMenuMode} togglePage={togglePage}/>
            {menuMode &&
                <>
                    <h1 onClick={() => toPageMode(setProfilePage)}>Dein Profil</h1>
                    <h1 onClick={() => toPageMode(setAchievementPage)}>Achievements</h1>
                    <h1 onClick={() => toPageMode(setQuestPage)}>Quests</h1>
                    <h1 onClick={() => toPageMode(setScoreboardPage)}>Scoreboard</h1>
                    <h1 onClick={logout}>Logout</h1>
                </>
            }
            {profilePage &&
                <>
                    <ProfilePage/>
                </>
            }
            {achievementPage &&
                <>
                    <AchievementPage/>
                </>
            }
            {questPage &&
                <>
                    <QuestPage
                        questModeSetter={props.questModeSetter}
                        activeQuestSetter={props.activeQuestSetter}
                        activeQuestInfoSetter={props.activeQuestInfoSetter}
                        menuModeSetter={setMenuMode}
                        questPageSetter={setQuestPage}
                        dirRenderer={props.dirRenderer}
                        mapRef={props.mapRef}
                    />
                </>
            }
            {scoreboardPage &&
                <>
                    <ScoreboardPage/>
                </>
            }
        </div>

    )
}