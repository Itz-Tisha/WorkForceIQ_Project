import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Layout from "../components/Layout";
import RoleSelect from "../components/RoleSelect";
import { API_BASE } from "../api/config";

function UpdateEmployee() {

    const { id } = useParams();
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
    const [hireDate, setHireDate] = useState("");
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        getEmployeeById();
        axios.get(`${API_BASE}/departments`).then((res) => setDepartments(res.data));
    }, [id]);

    const getEmployeeById = async () => {
        const response = await axios.get(`${API_BASE}/employee/${id}`);
        setEmployee({
            name: response.data.name,
            email: response.data.email,
            password: "",
            gender: response.data.gender,
            salary: response.data.salary,
            role: response.data.role,
            departmentId: response.data.department?.dept_id || "",
            yearsOfExperience: response.data.yearsOfExperience ?? "",
        });
        setHireDate(response.data.hireDate || "Not set");
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEmployee({
            ...employee,
            [name]: name === "salary" || name === "departmentId" || name === "yearsOfExperience"
                ? Number(value)
                : value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);

        try {
            await axios.put(`${API_BASE}/employee/${id}`, employee);
            alert("Employee updated successfully");
            navigate("/home");
        } catch {
            alert("Failed to update employee");
        } finally {
            setSaving(false);
        }
    };

    return (
        <Layout title="Update Employee" subtitle="Edit employee details">
            <div className="card add-form-card">
                <form onSubmit={handleSubmit}>
                    <div className="form-grid">
                        <div className="form-group">
                            <label>Full Name</label>
                            <input type="text" name="name" className="form-input" value={employee.name} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label>Email</label>
                            <input type="email" name="email" className="form-input" value={employee.email} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label>New Password (optional)</label>
                            <input
                                type="password"
                                name="password"
                                className="form-input"
                                value={employee.password}
                                onChange={handleChange}
                                placeholder="Leave blank to keep current password"
                            />
                        </div>
                        <div className="form-group">
                            <label>Gender</label>
                            <select name="gender" className="form-select" value={employee.gender} onChange={handleChange} required>
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Salary (INR)</label>
                            <input type="number" name="salary" className="form-input" value={employee.salary} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label>Years of Experience</label>
                            <input
                                type="number"
                                name="yearsOfExperience"
                                className="form-input"
                                value={employee.yearsOfExperience}
                                min="0"
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Role</label>
                            <RoleSelect
                                name="role"
                                value={employee.role}
                                onChange={handleChange}
                                placeholder="Select Role"
                            />
                        </div>
                        <div className="form-group" style={{ gridColumn: "1 / -1" }}>
                            <label>Department</label>
                            <select name="departmentId" className="form-select" value={employee.departmentId} onChange={handleChange} required>
                                <option value="">Select Department</option>
                                {departments.map((dept) => (
                                    <option key={dept.dept_id} value={dept.dept_id}>{dept.departmentName}</option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <p className="hire-date-note">Original hire date: <strong>{hireDate}</strong></p>

                    <div className="btn-group" style={{ marginTop: "1.25rem" }}>
                        <button type="submit" className="btn btn-primary" disabled={saving}>
                            {saving ? "Updating..." : "Update Employee"}
                        </button>
                        <button type="button" className="btn btn-secondary" onClick={() => navigate("/home")}>Cancel</button>
                    </div>
                </form>
            </div>
        </Layout>
    );
}

export default UpdateEmployee;
