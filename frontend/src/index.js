import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import './index.css';
import RegistrationPage from "./pages/RegistrationPage";
import MainPage from "./pages/MainPage";
import LoginPage from "./pages/LoginPage";
import NavBar from "./components/NavBar";

import reportWebVitals from './reportWebVitals';

const router = createBrowserRouter([
    {
        path: "/",
        element: <NavBar />,
        children: [
            {
                path: "/",
                element: <MainPage />,
            },
            {
                path: "/register",
                element: <RegistrationPage />,
            },
            {
                path: "/login",
                element: <LoginPage />,
            },
        ],
    },
]);


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <RouterProvider router={router} /> {}
    </React.StrictMode>
);


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
