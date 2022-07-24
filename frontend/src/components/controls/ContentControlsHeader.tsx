import './ContentControlsHeader.css';
import React from "react";

export default function ContentControlsHeader(props: any){

    const statusContent = props.statusContent;
    const menuMode = props.menuMode;
    const setMenuMode = props.setMenuMode;
    const togglePage = props.togglePage;

    const hideContent = () => {
        return (
            statusContent(false)
        )
    }

    function returnToMenu(){
        setMenuMode(true)
        togglePage()
    }

    return (
        <div className={"content-controls-header"}>
            <img src="/images/CGLogoBild.svg" className={"logo"} alt={"Logo"}/>

            {menuMode &&
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
            }
            {!menuMode &&
                <svg
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    onClick={returnToMenu}
                >
                    <path d="M15 18L9 12L15 6" stroke="#111111" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            }
        </div>
    )
}