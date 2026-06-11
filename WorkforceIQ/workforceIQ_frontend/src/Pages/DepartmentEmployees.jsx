import axios from "axios";
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Layout from "../components/Layout";
import { API_BASE } from "../api/config";
import { isHr } from "../auth/auth";

function DepartmentEmployees() {

    const navigate = useNavigate();
    const { id } = useParams();
    const hrUser = isHr();
    const [department, setDepartment] = useState(null);

    useEffect(() => {
        getDepartment();
    }, [id]);

    const getDepartment = async () => {
        const response = await axios.get(`${API_BASE}/department/${id}`);
        setDepartment(response.data);
    };

    const deleteEmployee = async (empId) => {
        if (!confirm("Delete this employee?")) return;
        await axios.delete(`${API_BASE}/employee/${empId}`);
        getDepartment();
    };

    if (!department) {
        return (
            <Layout title="Loading...">
                <p className="loading-text">Loading department...</p>
            </Layout>
        );
    }

    return (
        <Layout
            title={department.departmentName}
            subtitle={`${department.employees?.length || 0} employees in this department`}
        >
            <button className="btn btn-secondary btn-sm" onClick={() => navigate("/department")} style={{ marginBottom: "1.25rem" }}>
                ← Back to Departments
            </button>

            {department.employees?.length === 0 ? (
                <p className="empty-state">No employees in this department yet.</p>
            ) : (
                department.employees.map((emp) => (
                    <div className="list-card" key={emp.id}>
                        <div className="list-card-info">
                            <h3>{emp.name}</h3>
                            <p>{emp.email}</p>
                            <div className="employee-meta">
                                <span>
                                    <span className={`tag tag-${emp.gender?.toLowerCase()}`}>{emp.gender}</span>
                                </span>
                                <span>
                                    <span className={`tag tag-${emp.role?.toLowerCase()}`}>{emp.role}</span>
                                </span>
                                <span>₹{emp.salary?.toLocaleString()}</span>
                                {emp.yearsOfExperience != null && (
                                    <span>{emp.yearsOfExperience} yrs exp</span>
                                )}
                                {emp.hireDate && <span>Hired: {emp.hireDate}</span>}
                            </div>
                        </div>
                        {hrUser && (
                            <div className="btn-group">
                                <button className="btn btn-secondary btn-sm" onClick={() => navigate(`/update/${emp.id}`)}>
                                    Edit
                                </button>
                                <button className="btn btn-danger btn-sm" onClick={() => deleteEmployee(emp.id)}>
                                    Delete
                                </button>
                            </div>
                        )}
                    </div>
                ))
            )}
        </Layout>
    );
}

export default DepartmentEmployees;
