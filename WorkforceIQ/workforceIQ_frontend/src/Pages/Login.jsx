import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Layout from "../components/Layout";
import { API_BASE } from "../api/config";
import { saveSession } from "../auth/auth";

function Login() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            const response = await axios.get(
                `${API_BASE}/employee/login`,
                { params: { email, password } }
            );

            if (response.data?.token) {
                saveSession(response.data);
                navigate("/home");
            } else {
                alert("Invalid Email or Password");
            }
        } catch (err) {
            if (err.response?.status === 401) {
                alert("Invalid Email or Password");
            } else {
                alert("Server Error — make sure the backend is running on port 8086");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <Layout>
            <div className="login-card">
                <div className="login-brand">
                    <span>WIQ</span>
                </div>
                <h1>Welcome Back</h1>
                <p className="subtitle">Sign in to WorkforceIQ HR Analytics</p>

                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label>Email</label>
                        <input
                            type="email"
                            className="form-input"
                            placeholder="you@company.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Password</label>
                        <input
                            type="password"
                            className="form-input"
                            placeholder="Enter password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button type="submit" className="btn btn-primary" style={{ width: "100%" }} disabled={loading}>
                        {loading ? "Signing in..." : "Sign In"}
                    </button>
                </form>
            </div>
        </Layout>
    );
}

export default Login;
