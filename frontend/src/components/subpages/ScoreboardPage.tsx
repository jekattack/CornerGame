import React, {useEffect, useState} from "react";
import {CGUserGameDataDTO, Quest} from "../../service/models";
import {useNavigate} from "react-router-dom";
import {fetchHighscore} from "../../service/apiService";
import ScoreboardPageItem from "./ScoreboardPageItem";

export default function ScoreboardPage(){
    const [loadedHighscore, setHighscore] = useState<CGUserGameDataDTO[]>();

    const nav = useNavigate();

    useEffect(() => {
        fetchHighscore().then(response => setHighscore(response))
    }, [nav])

    function drawScoreboardItems(){
        return loadedHighscore?.map(score => <ScoreboardPageItem key={Math.random()*100} rank={loadedHighscore?.indexOf(score)} username={score.username} score={score.score}/>)
    }

    return(
        <>
            <h1>Scoreboard</h1>
            {loadedHighscore ? drawScoreboardItems() : "…loading…"}
        </>
    )
}