import React from "react";
import "./App.css";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import WelcomePage from "./routes/WelcomePage";
import RegisterPage from "./routes/RegisterPage";
import LoginPage from "./routes/LoginPage";
import HomePage from "./routes/HomePage";

export default function App(){



    return(
        <BrowserRouter>
            <Routes>
                <Route path={"/"} element={<WelcomePage />} />
                <Route path={"/login"} element={<LoginPage />} />
                <Route path={"/register"} element={<RegisterPage />} />
                <Route path={"/map"} element={<HomePage />} />
            </Routes>
        </BrowserRouter>
    )
}