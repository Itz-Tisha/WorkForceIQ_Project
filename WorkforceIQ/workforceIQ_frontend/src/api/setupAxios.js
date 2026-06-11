import axios from "axios";
import { clearSession, getToken, isSessionValid } from "../auth/auth";

axios.interceptors.request.use((config) => {
    const token = getToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401 && isSessionValid()) {
            clearSession();
            window.location.href = "/";
        }
        return Promise.reject(error);
    }
);
