import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Layout from "../components/Layout";
import { API_BASE } from "../api/config";

function Department() {

    const [departments, setDepartments] = useState([]);
    const [name, setName] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        getDepartments();
    }, []);

    const getDepartments = async () => {
        const response = await axios.get(`${API_BASE}/departments`);
        setDepartments(response.data);
    };

    const addDepartment = async (e) => {
        e.preventDefault();
        if (!name.trim()) return;

        await axios.post(`${API_BASE}/department`, { departmentName: name });
        setName("");
        getDepartments();
    };

    const deleteDepartment = async (id) => {
        if (!confirm("Delete this department?")) return;
        await axios.delete(`${API_BASE}/department/${id}`);
        getDepartments();
    };

    const updateDepartment = async (id) => {
        const newName = prompt("Enter new department name");
        if (!newName?.trim()) return;

        await axios.put(`${API_BASE}/department/${id}`, { departmentName: newName });
        getDepartments();
    };

    return (
        <Layout title="Departments" subtitle="Manage organizational departments">
            <div className="card" style={{ marginBottom: "1.5rem" }}>
                <form onSubmit={addDepartment} style={{ display: "flex", gap: "0.75rem", flexWrap: "wrap" }}>
                    <input
                        type="text"
                        className="form-input"
                        placeholder="New department name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        style={{ flex: 1, minWidth: "200px" }}
                    />
                    <button type="submit" className="btn btn-primary">Add Department</button>
                </form>
            </div>

            {departments.length === 0 ? (
                <p className="empty-state">No departments yet. Create one above.</p>
            ) : (
                departments.map((dept) => (
                    <div className="list-card" key={dept.dept_id}>
                        <div className="list-card-info">
                            <h3>{dept.departmentName}</h3>
                            <p>Department ID: {dept.dept_id}</p>
                        </div>
                        <div className="btn-group">
                            <button
                                className="btn btn-primary btn-sm"
                                onClick={() => navigate(`/department/${dept.dept_id}`)}
                            >
                                View Employees
                            </button>
                            <button className="btn btn-secondary btn-sm" onClick={() => updateDepartment(dept.dept_id)}>
                                Rename
                            </button>
                            <button className="btn btn-danger btn-sm" onClick={() => deleteDepartment(dept.dept_id)}>
                                Delete
                            </button>
                        </div>
                    </div>
                ))
            )}
        </Layout>
    );
}

export default Department;
