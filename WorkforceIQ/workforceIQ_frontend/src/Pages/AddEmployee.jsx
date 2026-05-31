import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Layout from "../components/Layout";
import { API_BASE } from "../api/config";

function AddEmployee() {

    const navigate = useNavigate();

    const [employee, setEmployee] = useState({
        name: "",
        email: "",
        password: "",
        gender: "",
        salary: "",
        role: "",
        departmentId: "",
        yearsOfExperience: "",
    });

    const [departments, setDepartments] = useState([]);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        axios.get(`${API_BASE}/departments`).then((res) => {
            setDepartments(res.data);
        });
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEmployee({
            ...employee,
            [name]: name === "salary" || name === "departmentId" || name === "yearsOfExperience"
                ? Number(value)
                : value,
        });
    };

    const saveEmployee = async (e) => {
        e.preventDefault();
        setSaving(true);

        try {
            await axios.post(`${API_BASE}/employee`, employee);
            alert("Employee added successfully! Hire date recorded automatically.");
            navigate("/home");
        } catch {
            alert("Failed to add employee. Check all fields and try again.");
        } finally {
            setSaving(false);
        }
    };

    const today = new Date().toLocaleDateString("en-IN", {
        day: "numeric",
        month: "long",
        year: "numeric",
    });

    return (
        <Layout title="Add Employee" subtitle="New hires are automatically tagged with today's date">
            <div className="card add-form-card">
                <form onSubmit={saveEmployee}>
                    <div className="form-grid">
                        <div className="form-group">
                            <label>Full Name</label>
                            <input
                                type="text"
                                name="name"
                                className="form-input"
                                placeholder="John Doe"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                name="email"
                                className="form-input"
                                placeholder="john@company.com"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Password</label>
                            <input
                                type="password"
                                name="password"
                                className="form-input"
                                placeholder="Password"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Gender</label>
                            <select name="gender" className="form-select" onChange={handleChange} required>
                                <option value="">Select Gender</option>
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label>Salary (INR)</label>
                            <input
                                type="number"
                                name="salary"
                                className="form-input"
                                placeholder="50000"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Years of Experience</label>
                            <input
                                type="number"
                                name="yearsOfExperience"
                                className="form-input"
                                placeholder="e.g. 3"
                                min="0"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Role</label>
                            <select name="role" className="form-select" onChange={handleChange} required>
                                <option value="">Select Role</option>
                                <option value="HR">HR</option>
                                <option value="EMPLOYEE">Employee</option>
                            </select>
                        </div>

                        <div className="form-group" style={{ gridColumn: "1 / -1" }}>
                            <label>Department</label>
                            <select
                                name="departmentId"
                                className="form-select"
                                onChange={handleChange}
                                required
                            >
                                <option value="">Select Department</option>
                                {departments.map((dept) => (
                                    <option key={dept.dept_id} value={dept.dept_id}>
                                        {dept.departmentName}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <p className="hire-date-note">
                        Hire date will be automatically set to <strong>{today}</strong>. Experience increases by 1 year on each hire anniversary.
                    </p>

                    <div className="btn-group" style={{ marginTop: "1.25rem" }}>
                        <button type="submit" className="btn btn-primary" disabled={saving}>
                            {saving ? "Saving..." : "Save Employee"}
                        </button>
                        <button type="button" className="btn btn-secondary" onClick={() => navigate("/home")}>
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </Layout>
    );
}

export default AddEmployee;
