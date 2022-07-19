import './ContentControlsHeader.css';
import React from "react";

export default function ContentControlsHeader(props: any){

    const statusContent = props.statusContent;
    const hideContent = () => {
        return (
            statusContent(false)
        )
    }

    return (
        <div className={"content-controls-header"}>
            <img src="/images/CGLogoBild.svg" className={"logo"} alt={"Logo"}/>

            <svg
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
                onClick={hideContent}
            >
                <path d="M18 6L6 18" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                <path d="M6 6L18 18" stroke="#111111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
        </div>
    )
}