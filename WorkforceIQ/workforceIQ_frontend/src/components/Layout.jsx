import { useNavigate, useLocation } from "react-router-dom";

function Layout({ children, title, subtitle }) {

    const navigate = useNavigate();
    const location = useLocation();

    const navItems = [
        { path: "/home", label: "Dashboard" },
        { path: "/department", label: "Departments" },
        { path: "/addemployee", label: "Add Employee" },
        { path: "/ai-analysis", label: "AI Insights" },
		
    ];

    const isLogin = location.pathname === "/";

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
