import React, {useEffect, useState} from "react";
import {Achievement} from "../../service/models";
import {useNavigate} from "react-router-dom";
import {fetchAllAchievements} from "../../service/apiService";
import AchievementPageItem from "./AchievementPageItem";

export default function AchievementPage(){

    const [loadedAchievements, setLoadedAchievements] = useState<Achievement[]>();

    const nav = useNavigate();

    useEffect(() => {
        fetchAllAchievements().then(response => setLoadedAchievements(response))
    }, [nav])

    function drawAchievementItems(){
        return loadedAchievements?.map(achievement => <AchievementPageItem key={Math.random()*100} achievementname={achievement.name} achievementdescription={achievement.description} requirements={achievement.requirements}/>)
    }

    return(
        <>
            <h1>Achievements</h1>
            <p>Schau dir an, was du schon alles geschafft hast: Hier findest du alle Achievements, die du bisher freigeschaltet hast.</p>
            <div className={"achievement-grid"}>
                {loadedAchievements ? drawAchievementItems() : <div>…loading…</div>}
            </div>
        </>
    )
}