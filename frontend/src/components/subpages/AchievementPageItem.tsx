import React from "react";

export default function AchievementPageItem(props: any){

    const name: string = props.achievementname;
    const description: string = props.achievementdescription;
    const requirements: any = props.requirements;

    return (
        <div className={"achievement-wrapper"}>
            <img src="/images/CGIconAchievement.svg" className={"achievement-icon"} alt={"achievement-icon"}/>
            <h4>{name}</h4>
            <textarea value={description} readOnly={true}></textarea>
        </div>
    )
}