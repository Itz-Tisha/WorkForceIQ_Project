import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Layout from "../components/Layout";
import { API_BASE } from "../api/config";

function formatLocalDate(date) {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, "0");
    const d = String(date.getDate()).padStart(2, "0");
    return `${y}-${m}-${d}`;
}

function getDefaultDates() {
    const end = new Date();
    const start = new Date();
    start.setMonth(start.getMonth() - 3);
    return {
        startDate: formatLocalDate(start),
        endDate: formatLocalDate(end),
    };
}

function Home() {

    const navigate = useNavigate();
    const defaults = getDefaultDates();

    const [startDate, setStartDate] = useState(defaults.startDate);
    const [endDate, setEndDate] = useState(defaults.endDate);
    const [hiringStats, setHiringStats] = useState(null);
    const [salaryGap, setSalaryGap] = useState(null);
    const [departments, setDepartments] = useState([]);
    const [loadingHiring, setLoadingHiring] = useState(false);
    const [loadingSalary, setLoadingSalary] = useState(false);
	const [monthlyFired, setMonthlyFired] = useState(0);

    const fetchAnalytics = async (start, end) => {
        setLoadingHiring(true);
        setLoadingSalary(true);

        try {
            const [hiringRes, salaryRes, deptRes,firedRes] = await Promise.all([
                axios.get(`${API_BASE}/analytics/hiring`, {
                    params: { startDate: start, endDate: end },
                }),
                axios.get(`${API_BASE}/analytics/salary-gap`, {
                    params: { startDate: start, endDate: end },
                }),
                axios.get(`${API_BASE}/departments`),
				axios.get(`${API_BASE}/employee/monthly-fired`)
				
            ]);
            setHiringStats(hiringRes.data);
            setSalaryGap(salaryRes.data);
            setDepartments(deptRes.data);
			setMonthlyFired(firedRes.data.totalFired);
        } catch {
            setHiringStats(null);
            setSalaryGap(null);
            setDepartments([]);
        } finally {
            setLoadingHiring(false);
            setLoadingSalary(false);
        }
    };

    useEffect(() => {
        fetchAnalytics(startDate, endDate);
    }, []);

    const handleApplyFilter = () => {
        fetchAnalytics(startDate, endDate);
    };

    const maxDeptCount = hiringStats?.departmentBreakdown?.length
        ? Math.max(...hiringStats.departmentBreakdown.map((d) => d.count))
        : 1;

    const genderStats = salaryGap?.stats?.genderPayGap;

    return (
        <Layout
            title="Dashboard"
            subtitle="Workforce hiring trends and AI-powered salary gap analysis"
        >
            <div className="actions-grid">
                <div className="action-card" onClick={() => navigate("/department")}>
                    <div className="action-icon">🏢</div>
                    <h3>Departments</h3>
                    <p>Manage departments and view employees</p>
                </div>
                <div className="action-card" onClick={() => navigate("/addemployee")}>
                    <div className="action-icon">👤</div>
                    <h3>Add Employee</h3>
                    <p>Register new hires with auto hire date</p>
                </div>
                <div className="action-card" onClick={() => navigate("/ai-analysis")}>
                    <div className="action-icon">🤖</div>
                    <h3>AI Insights</h3>
                    <p>Department-level workforce analysis</p>
                </div>
            </div>
			
            <div className="filter-bar">
                <div className="form-group">
                    <label>Start Date</label>
                    <input
                        type="date"
                        className="form-input"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label>End Date</label>
                    <input
                        type="date"
                        className="form-input"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                    />
                </div>
                <button className="btn btn-primary" onClick={handleApplyFilter}>
                    Apply Filter
                </button>
            </div>
			
			<div className="stat-card stat-danger">
			    <div className="stat-label">Fired This Month</div>
			    <div className="stat-value">{monthlyFired}</div>
			</div>
			
            {!loadingHiring && hiringStats && (
                <div style={{ marginBottom: "1.5rem" }}>
                    <h2 className="section-title">Workforce Gender</h2>
                    <div className="stats-grid">
                        <div className="stat-card">
                            <div className="stat-label">Total Employees</div>
                            <div className="stat-value">{hiringStats.workforceTotal ?? 0}</div>
                        </div>
                        <div className="stat-card stat-male">
                            <div className="stat-label">Male</div>
                            <div className="stat-value">{hiringStats.workforceMale ?? 0}</div>
                        </div>
                        <div className="stat-card stat-female">
                            <div className="stat-label">Female</div>
                            <div className="stat-value">{hiringStats.workforceFemale ?? 0}</div>
                        </div>
                    </div>
                </div>
            )}

            {departments.length > 0 && (
                <div style={{ marginBottom: "1.5rem" }}>
                    <h2 className="section-title">Department Slots</h2>
                    <div className="card">
                        <div className="slots-grid">
                            {departments.map((dept) => (
                                <div
                                    key={dept.dept_id}
                                    className={`slot-card ${(dept.slots ?? 0) === 0 ? "slot-full" : (dept.slots ?? 0) <= 2 ? "slot-low" : ""}`}
                                >
                                    <span className="slot-dept-name">{dept.departmentName}</span>
                                    <span className="slot-count">{dept.slots ?? 0}</span>
                                    <span className="slot-label">empty slot{dept.slots !== 1 ? "s" : ""}</span>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}
			
            <div className="grid-2">
                <div>
                    <h2 className="section-title">Hiring Overview</h2>

                    {loadingHiring ? (
                        <p className="loading-text">Loading hiring data...</p>
                    ) : hiringStats ? (
                        <>
                            <div className="stats-grid">
                                <div className="stat-card highlight">
                                    <div className="stat-label">Total Hired</div>
                                    <div className="stat-value">{hiringStats.totalHired}</div>
                                </div>
                                <div className="stat-card stat-male">
                                    <div className="stat-label">Male Hired</div>
                                    <div className="stat-value">{hiringStats.maleCount ?? 0}</div>
                                </div>
                                <div className="stat-card stat-female">
                                    <div className="stat-label">Female Hired</div>
                                    <div className="stat-value">{hiringStats.femaleCount ?? 0}</div>
                                </div>
                                <div className="stat-card">
                                    <div className="stat-label">Departments</div>
                                    <div className="stat-value">
                                        {hiringStats.departmentBreakdown?.length || 0}
                                    </div>
                                </div>
                            </div>

                            <div className="card">
                                <div className="card-title">Department-wise Hiring</div>
                                {hiringStats.departmentBreakdown?.length > 0 ? (
                                    <div className="dept-list">
                                        {hiringStats.departmentBreakdown.map((dept) => (
                                            <div className="dept-row" key={dept.department}>
                                                <span className="dept-name">{dept.department}</span>
                                                <div className="dept-bar-wrap">
                                                    <div
                                                        className="dept-bar"
                                                        style={{
                                                            width: `${(dept.count / maxDeptCount) * 100}%`,
                                                        }}
                                                    >
                                                        {dept.count > 0 && <span>{dept.count}</span>}
                                                    </div>
                                                </div>
                                                <span className="dept-count">{dept.count}</span>
                                            </div>
                                        ))}
                                    </div>
                                ) : (
                                    <p className="empty-state">
                                        No employees hired in this date range.
                                    </p>
                                )}
                            </div>
                        </>
                    ) : (
                        <p className="empty-state">Unable to load hiring data.</p>
                    )}
                </div>

                <div>
                    <h2 className="section-title">Salary Gap Analysis</h2>

                    {loadingSalary ? (
                        <p className="loading-text">Generating AI salary analysis...</p>
                    ) : salaryGap ? (
                        <div className="card ai-section">
                            <span className="ai-badge">Powered by Groq AI</span>

                            {genderStats && (
                                <div className="salary-stats-grid">
                                    <div className="mini-stat">
                                        <div className="label">Male Avg</div>
                                        <div className="value">
                                            ₹{Math.round(genderStats.maleAverage).toLocaleString()}
                                        </div>
                                    </div>
                                    <div className="mini-stat">
                                        <div className="label">Female Avg</div>
                                        <div className="value">
                                            ₹{Math.round(genderStats.femaleAverage).toLocaleString()}
                                        </div>
                                    </div>
                                    <div className="mini-stat">
                                        <div className="label">Gender Gap</div>
                                        <div className="value">
                                            ₹{Math.round(Math.abs(genderStats.gap)).toLocaleString()}
                                        </div>
                                    </div>
                                </div>
                            )}

                            <div className="ai-content">{salaryGap.analysis}</div>
                        </div>
                    ) : (
                        <p className="empty-state">Unable to load salary analysis.</p>
                    )}
                </div>
            </div>
        </Layout>
    );
}

export default Home;
