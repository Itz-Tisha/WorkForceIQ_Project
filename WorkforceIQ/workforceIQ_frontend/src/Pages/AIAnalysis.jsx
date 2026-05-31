import axios from "axios";
import { useEffect, useState } from "react";
import Layout from "../components/Layout";
import { API_BASE } from "../api/config";

function AIAnalysis() {

    const [analysis, setAnalysis] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getAnalysis();
    }, []);

    const getAnalysis = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`${API_BASE}/ai/departments`);
            setAnalysis(response.data);
        } catch {
            setAnalysis([]);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Layout title="AI Department Insights" subtitle="Groq-powered workforce analysis per department">
            {loading ? (
                <p className="loading-text">Generating AI insights...</p>
            ) : analysis.length === 0 ? (
                <p className="empty-state">No departments found or AI service unavailable.</p>
            ) : (
                analysis.map((item, index) => (
                    <div className="card ai-section" key={index} style={{ marginBottom: "1rem" }}>
                        <h3>{item.department}</h3>
                        <span className="ai-badge">Groq AI</span>
                        <div className="ai-content">{item.analysis}</div>
                    </div>
                ))
            )}
        </Layout>
    );
}

export default AIAnalysis;
