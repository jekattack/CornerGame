import '../Components.css';
import ContentControlsHeader from "./ContentControlsHeader";
import React, {useState} from "react";
import {useNavigate} from "react-router-dom";

export default function Menu(props: any){

    const nav = useNavigate();

    const [menuMode, setMenuMode] = useState(true)
    const [profilePage, setProfilePage] = useState(false)
    const [cliquePage, setCliquePage] = useState(false)
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
        setCliquePage(false);
        setQuestPage(false);
        setScoreboardPage(false);
    }

    return(
        <>
            <ContentControlsHeader statusContent={props.visibility} menuMode={menuMode} setMenuMode={setMenuMode} togglePage={togglePage}/>
            {menuMode &&
                <>
                    <h1 onClick={() => toPageMode(setProfilePage)}>Dein Profil</h1>
                    <h1 onClick={() => toPageMode(setCliquePage)}>Deine Clique</h1>
                    <h1 onClick={() => toPageMode(setQuestPage)}>Quests</h1>
                    <h1 onClick={() => toPageMode(setScoreboardPage)}>Scoreboard</h1>
                    <h1 onClick={logout}>Logout</h1>
                </>
            }
            {profilePage &&
                <>
                    <h1>Dein Profil</h1>
                </>
            }
            {cliquePage &&
                <>
                    <h1>Deine Clique</h1>
                </>
            }
            {questPage &&
                <>
                    <h1>Quests</h1>
                </>
            }
            {scoreboardPage &&
                <>
                    <h1>Scoreboard</h1>
                </>
            }
        </>

    )
}