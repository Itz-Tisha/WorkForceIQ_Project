import { useNavigate, useLocation } from "react-router-dom";
import { clearSession, getCurrentUser, isHr } from "../auth/auth";

function Layout({ children, title, subtitle }) {

    const navigate = useNavigate();
    const location = useLocation();
    const user = getCurrentUser();
    const hrUser = isHr();

    const navItems = [
        { path: "/home", label: "Dashboard" },
        { path: "/department", label: "Departments" },
        ...(hrUser ? [{ path: "/addemployee", label: "Add Employee" }] : []),
        { path: "/ai-analysis", label: "AI Insights" },
        ...(hrUser ? [{ path: "/promotion", label: "Promotions" }] : []),
    ];

    const isLogin = location.pathname === "/";

    const handleLogout = () => {
        clearSession();
        navigate("/");
    };

    if (isLogin) {
        return <div className="page-login">{children}</div>;
    }

    return (
        <div className="app-shell">
            <header className="app-header">
                <div className="brand" onClick={() => navigate("/home")}>
                    <span className="brand-icon">WIQ</span>
                    <div>
                        <span className="brand-name">WorkforceIQ</span>
                        <span className="brand-tag">HR Analytics Platform</span>
                    </div>
                </div>

                <nav className="app-nav">
                    {navItems.map((item) => (
                        <button
                            key={item.path}
                            className={`nav-link ${location.pathname === item.path ? "active" : ""}`}
                            onClick={() => navigate(item.path)}
                        >
                            {item.label}
                        </button>
                    ))}
                </nav>

                <div className="header-user">
                    {user && (
                        <span className="user-badge">
                            {user.name} · {user.role}
                        </span>
                    )}
                    <button type="button" className="btn btn-secondary btn-sm" onClick={handleLogout}>
                        Logout
                    </button>
                </div>
            </header>

            <main className="app-main">
                {(title || subtitle) && (
                    <div className="page-header">
                        {title && <h1>{title}</h1>}
                        {subtitle && <p className="page-subtitle">{subtitle}</p>}
                    </div>
                )}
                {children}
            </main>
        </div>
    );
}

export default Layout;
