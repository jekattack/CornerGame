import React, {useEffect, useState} from "react";
import "./Subpage.css";
import {useNavigate} from "react-router-dom";
import {
    fetchAllKiosks,
    fetchProgress,
    fetchScore,
    fetchUser, login,
    updatePassword,
    updateUser
} from "../../service/apiService";
import {CGUser, CGUserGameDataDTO, CGUserUpdateDTO, Kiosk, Visit} from "../../service/models";

export default function ProfilePage(){

    const nav = useNavigate();

    const [userInfo, setUserInfo] = useState<CGUser>();
    const [userProgressKiosks, setUserProgressKiosks] = useState<number>();
    const [userScore, setUserScore] = useState<CGUserGameDataDTO>();
    const [stammkioskId, setStammkioskId] = useState<string>("ChIJO1UA9SqJsUcRXBMA3ct5jS8");
    const [firstname, setFirstname] = useState<string>();
    const [lastname, setLastname] = useState<string>();
    const [phone, setPhone] = useState<string>();
    const [password, setPassword] = useState<string>("");
    const [passwordAgain, setPasswordAgain] = useState<string>("");

    const [selectOptions, setSelectOptions] = useState([<option>…loading…</option>]);

    const [editState, setEditState] = useState<boolean>(true);

    useEffect(() => {
        fetchUser().then(response => {
            setUserInfo(response);
            setFirstname(response.firstname);
            setLastname(response.lastname);
            setPhone(response.phone);
        })
        fetchProgress()
            .then(response => {
                displayUsersProgress(response)
            })
        fetchScore()
            .then(response => setUserScore(response));
        fetchAllKiosks().then(response => {
            setOptionItems(response)
        })
    },[nav])

    function displayUsersProgress(visits: Visit[]){
        let kioskIds = visits.map(visit => visit.googlePlacesId);
        setUserProgressKiosks(new Set(kioskIds).size);
    }

    function setOptionItems(kiosks:Kiosk[]) {
        setSelectOptions(kiosks.map((kiosk:Kiosk) => <option key={Math.random()*100} value={kiosk.place_id}>{kiosk.name}</option>));
    }

    function editFunction(){
        if(!editState){
            const userUpdate: CGUserUpdateDTO = {firstname: firstname, phone: phone, lastname: lastname, stammkioskId: stammkioskId}
            updateUser(userUpdate);
            setEditState(true);
        } else {
            setEditState(false);
        }
    }

    function sendNewPassword(password: string, passwordAgain: string){
        if(password === passwordAgain && userInfo?.username!=null){
            updatePassword({password: password, passwordAgain: passwordAgain})
            login(userInfo?.username, password)
                .then(response => {
                    localStorage.setItem('jwt', response.token);
                })
                .then(() => {
                    const passwordInputs = Array.from(document.getElementsByClassName("password-input") as HTMLCollectionOf<HTMLElement>);
                    passwordInputs.forEach((element) => element.style.borderColor = "green");
                })
        }
    }

    return(
    <>
        <h1>Dein Profil</h1>
        <p>Wirf einen Blick auf deinen Punktestand und bearbeite deine persönlichen Daten.</p>
        
        <div className={"subpage-content"}>
            <h2 id={"username-display"}>{userInfo ? userInfo.username : "…loading…"}</h2>

            <label htmlFor="kiosk-display">Kiosks besucht:</label>
            <div>
                <textarea id={"kiosk-display"}
                          readOnly={true} rows={1}
                          cols={50}
                          value={userProgressKiosks ? userProgressKiosks + "" : "0"}
                />
            </div>

            <label htmlFor="score-display">Dein Punktestand:</label>
            <div>
                <textarea id={"score-display"}
                          readOnly={true} rows={1}
                          cols={50} value={userScore ? userScore.score + "" : "0"}
                />
            </div>

            <label htmlFor="stammkiosk-select">Dein Stammkiosk:</label>
            <div className={"subpage-input-fields"}>
                <select id={"stammkiosk-select"} onChange={ev => setStammkioskId(ev.target.value)} value={stammkioskId}>
                    {selectOptions}
                </select>
            </div>

            <label htmlFor="firstname-display">Dein Vorname:</label>
            <div className={"subpage-input-fields"}>
                <textarea id={"firstname-display"}
                          readOnly={editState} rows={1}
                          cols={50} value={firstname ? firstname : ""}
                          onChange={ev => setFirstname(ev.target.value)}
                />
                <div className={"subpage-input-button"} onClick={editFunction}>{editState ? "ändern" : "speichern"}</div>
            </div>

            <label htmlFor="lastname-display">Dein Nachname:</label>
            <div className={"subpage-input-fields"}>
                <textarea id={"lastname-display"}
                          readOnly={editState} rows={1}
                          cols={50} value={lastname ? lastname : ""}
                          onChange={ev => setLastname(ev.target.value)}
                />
                <div className={"subpage-input-button"} onClick={editFunction}>{editState ? "ändern" : "speichern"}</div>
            </div>

            <label htmlFor="phone-display">Deine Telefonnummer:</label>
            <div className={"subpage-input-fields"}>
                <textarea id={"phone-display"}
                          readOnly={editState} rows={1}
                          cols={50} value={phone ? phone : ""}
                          onChange={ev => setPhone(ev.target.value)}
                />
                <div className={"subpage-input-button"} onClick={editFunction}>{editState ? "ändern" : "speichern"}</div>
            </div>

            <label htmlFor="password-display">Dein Password:</label>
            <div className={"subpage-input-fields"}>
                <div>
                    <input className={"password-input"}
                           type={"password"}
                           defaultValue={"********"}
                           onChange={ev => setPassword(ev.target.value)}/>
                    <input className={"password-input"}
                           type={"password"}
                           defaultValue={"********"}
                           onChange={ev => setPasswordAgain(ev.target.value)}/>
                </div>
                    <div className={"subpage-input-button"} onClick={() => sendNewPassword(password, passwordAgain)}>Neues Passwort speichern</div>
            </div>
        </div>
    </>
)
}

