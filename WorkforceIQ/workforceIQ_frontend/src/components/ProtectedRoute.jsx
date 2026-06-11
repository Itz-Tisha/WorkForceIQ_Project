import { Navigate } from "react-router-dom";
import { isSessionValid } from "../auth/auth";

function ProtectedRoute({ children }) {
    if (!isSessionValid()) {
        return <Navigate to="/" replace />;
    }
    return children;
}

export default ProtectedRoute;
