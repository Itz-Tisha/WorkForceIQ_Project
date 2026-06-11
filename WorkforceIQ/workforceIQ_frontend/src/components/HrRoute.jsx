import { Navigate } from "react-router-dom";
import { isHr, isSessionValid } from "../auth/auth";

function HrRoute({ children }) {
    if (!isSessionValid()) {
        return <Navigate to="/" replace />;
    }
    if (!isHr()) {
        return <Navigate to="/home" replace />;
    }
    return children;
}

export default HrRoute;
