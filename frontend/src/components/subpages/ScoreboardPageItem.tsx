export default function ScoreboardPageItem(props: any){

    const rank: number = props.rank;
    const username: string = props.username;
    const score: number = props.score;

    return (
        <div className={"scoreboard-page-item-wrapper"}>
            <div>{rank}.</div><div>{username}</div><div>{score}</div>
        </div>
    )
}