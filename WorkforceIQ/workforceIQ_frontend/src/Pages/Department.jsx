import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Layout from "../components/Layout";
import { API_BASE } from "../api/config";
import { isHr } from "../auth/auth";

function Department() {

    const [departments, setDepartments] = useState([]);
    const [name, setName] = useState("");
    const [slots, setSlots] = useState(10);

    const navigate = useNavigate();
    const hrUser = isHr();

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

        await axios.post(`${API_BASE}/department`, {
            departmentName: name,
            slots: Number(slots),
        });
        setName("");
        setSlots(10);
        getDepartments();
    };

    const deleteDepartment = async (id) => {
        if (!confirm("Delete this department?")) return;
        await axios.delete(`${API_BASE}/department/${id}`);
        getDepartments();
    };

    const updateDepartment = async (id, currentSlots) => {
        const newName = prompt("Enter new department name");
        if (!newName?.trim()) return;

        const newSlots = prompt("Enter number of slots", currentSlots);
        if (newSlots === null) return;

        await axios.put(`${API_BASE}/department/${id}`, {
            departmentName: newName,
            slots: Number(newSlots),
        });
        getDepartments();
    };

    return (
        <Layout
            title="Departments"
            subtitle={hrUser
                ? "Manage departments and hiring capacity (slots)"
                : "View departments and employees"}
        >
            {hrUser && (
            <div className="card" style={{ marginBottom: "1.5rem" }}>
                <form onSubmit={addDepartment} className="form-grid" style={{ alignItems: "end" }}>
                    <div className="form-group">
                        <label>Department Name</label>
                        <input
                            type="text"
                            className="form-input"
                            placeholder="e.g. Engineering"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Slots (max employees)</label>
                        <input
                            type="number"
                            className="form-input"
                            min="1"
                            value={slots}
                            onChange={(e) => setSlots(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary">Add Department</button>
                </form>
            </div>
            )}

            {departments.length === 0 ? (
                <p className="empty-state">
                    {hrUser ? "No departments yet. Create one above." : "No departments available."}
                </p>
            ) : (
                departments.map((dept) => (
                    <div className="list-card" key={dept.dept_id}>
                        <div className="list-card-info">
                            <h3>{dept.departmentName}</h3>
                            <p>
                                <span className={`tag ${(dept.slots ?? 0) > 0 ? "tag-employee" : "tag-hr"}`}>
                                    {dept.slots ?? 0} slot{(dept.slots ?? 0) !== 1 ? "s" : ""} remaining
                                </span>
                            </p>
                        </div>
                        <div className="btn-group">
                            <button
                                className="btn btn-primary btn-sm"
                                onClick={() => navigate(`/department/${dept.dept_id}`)}
                            >
                                View Employees
                            </button>
                            {hrUser && (
                                <>
                                    <button className="btn btn-secondary btn-sm" onClick={() => updateDepartment(dept.dept_id, dept.slots)}>
                                        Edit
                                    </button>
                                    <button className="btn btn-danger btn-sm" onClick={() => deleteDepartment(dept.dept_id)}>
                                        Delete
                                    </button>
                                </>
                            )}
                        </div>
                    </div>
                ))
            )}
        </Layout>
    );
}

export default Department;
