import React from "react";
import "./App.css";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import WelcomePage from "./routes/WelcomePage";
import RegisterPage from "./routes/RegisterPage";
import LoginPage from "./routes/LoginPage";

export default function App(){

    return(
        <div>
            <BrowserRouter>
                <Routes>
                    <Route path={"/"} element={<WelcomePage />} />
                    <Route path={"/login"} element={<LoginPage />} />
                    <Route path={"/register"} element={<RegisterPage />} />
                </Routes>
            </BrowserRouter>
        </div>
    )
}