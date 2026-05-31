import axios from "axios";
import { useEffect, useState } from "react";
import Layout from "../components/Layout";
import { API_BASE } from "../api/config";

function healthColorClass(color) {
    if (color === "GREEN") return "health-green";
    if (color === "YELLOW") return "health-yellow";
    if (color === "RED") return "health-red";
    return "health-grey";
}

function getHeadcount(item) {
    if (item.headcount != null) return item.headcount;
    return (item.maleCount ?? 0) + (item.femaleCount ?? 0);
}

function getGenderRatio(item) {
    if (item.genderRatio) return item.genderRatio;
    const male = item.maleCount ?? 0;
    const female = item.femaleCount ?? 0;
    if (male === 0 && female === 0) return "N/A";
    if (female === 0) return `${male}:0 (no female employees)`;
    if (male === 0) return `0:${female} (no male employees)`;
    return `${(male / female).toFixed(1)}:1 (M:F)`;
}

function getMinSalary(item) {
    if (item.minSalary != null && item.minSalary > 0) return item.minSalary;
    if (getHeadcount(item) === 1 && item.avgSalary) return item.avgSalary;
    return item.minSalary ?? 0;
}

function getMaxSalary(item) {
    if (item.maxSalary != null && item.maxSalary > 0) return item.maxSalary;
    if (getHeadcount(item) === 1 && item.avgSalary) return item.avgSalary;
    return item.maxSalary ?? 0;
}

function AIAnalysis() {

    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getAnalysis();
    }, []);

    const getAnalysis = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`${API_BASE}/ai/departments`);
            setData(response.data);
        } catch {
            setData(null);
        } finally {
            setLoading(false);
        }
    };

    const summary = data?.summary;
    const departments = data?.departments || [];

    return (
        <Layout title="AI Department Insights" subtitle="Department health metrics with Groq executive summary">
            {loading ? (
                <p className="loading-text">Generating AI insights...</p>
            ) : !data ? (
                <p className="empty-state">No departments found or AI service unavailable.</p>
            ) : (
                <>
                    {summary?.aiSummary && (
                        <div className="card ai-section" style={{ marginBottom: "1.5rem" }}>
                            <span className="ai-badge">Groq Executive Summary</span>
                            <div className="ai-content">{summary.aiSummary}</div>
                        </div>
                    )}

                    {summary && (summary.lowestFemaleDepartment || summary.lowestAvgSalaryDepartment) && (
                        <div className="insights-summary-grid" style={{ marginBottom: "1.5rem" }}>
                            {summary.lowestFemaleDepartment && (
                                <div className="card insight-alert insight-female">
                                    <div className="card-title">Lowest Female Representation</div>
                                    <h3>{summary.lowestFemaleDepartment}</h3>
                                    <p>
                                        Only <strong>{summary.lowestFemaleCount}</strong> female employee
                                        {summary.lowestFemaleCount !== 1 ? "s" : ""}
                                    </p>
                                </div>
                            )}
                            {summary.lowestAvgSalaryDepartment && (
                                <div className="card insight-alert insight-salary">
                                    <div className="card-title">Lowest Average Salary</div>
                                    <h3>{summary.lowestAvgSalaryDepartment}</h3>
                                    <p>
                                        Avg salary: <strong>₹{Math.round(summary.lowestAvgSalary).toLocaleString()}</strong>
                                        {" "}(company avg: ₹{Math.round(summary.companyAvgSalary || 0).toLocaleString()})
                                    </p>
                                </div>
                            )}
                        </div>
                    )}

                    {departments.map((item, index) => (
                        <div className="card ai-section" key={index} style={{ marginBottom: "1rem" }}>
                            <div className="dept-analysis-header">
                                <div>
                                    <h3>{item.department}</h3>
                                    <p className="dept-submeta">
                                        Total employees: <strong>{getHeadcount(item)}</strong>
                                        {" · "}Gender ratio: <strong>{getGenderRatio(item)}</strong>
                                    </p>
                                </div>
                                <span className={`tag ${(item.slotsRemaining ?? 0) > 0 ? "tag-employee" : "tag-hr"}`}>
                                    {item.slotsRemaining ?? 0} slots left
                                </span>
                            </div>

                            <div className="salary-stats-grid" style={{ marginTop: "0.75rem" }}>
                                <div className="mini-stat">
                                    <div className="label">Male</div>
                                    <div className="value">{item.maleCount}</div>
                                </div>
                                <div className="mini-stat">
                                    <div className="label">Female</div>
                                    <div className="value">{item.femaleCount}</div>
                                </div>
                                <div className="mini-stat">
                                    <div className="label">Avg Salary</div>
                                    <div className="value">₹{Math.round(item.avgSalary || 0).toLocaleString()}</div>
                                </div>
                                <div className="mini-stat">
                                    <div className="label">Min / Max</div>
                                    <div className="value value-sm">
                                        ₹{Math.round(getMinSalary(item)).toLocaleString()} – ₹{Math.round(getMaxSalary(item)).toLocaleString()}
                                    </div>
                                </div>
                                <div className={`mini-stat ${healthColorClass(item.salaryHealthColor)}`}>
                                    <div className="label">Salary Health</div>
                                    <div className="value">{item.salaryHealthIndex ?? 0}%</div>
                                </div>
                                <div className="mini-stat">
                                    <div className="label">Pay Gap</div>
                                    <div className="value value-sm">
                                        {item.genderPayGapPercent != null
                                            ? `${item.genderPayGapPercent}%`
                                            : "N/A"}
                                    </div>
                                </div>
                            </div>

                            {item.payGapNote && (
                                <p className="pay-gap-note">{item.payGapNote}</p>
                            )}

                            {item.hasPayEquityIssue && (
                                <span className="tag tag-hr" style={{ marginTop: "0.5rem" }}>
                                    Pay equity review recommended
                                </span>
                            )}
                        </div>
                    ))}
                </>
            )}
        </Layout>
    );
}

export default AIAnalysis;
