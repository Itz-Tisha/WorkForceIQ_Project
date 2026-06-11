import axios from "axios";
import { useEffect, useState } from "react";
import Layout from "../components/Layout";
import RoleSelect from "../components/RoleSelect";
import { API_BASE } from "../api/config";

function PromotionPage() {

    const [employees, setEmployees] = useState([]);
    const [selectedEmployee, setSelectedEmployee] = useState(null);

    const [form, setForm] = useState({
        newRole: "",
        newSalary: "",
    });

    useEffect(() => {
        loadEmployees();
    }, []);

    const loadEmployees = async () => {

        const res = await axios.get(`${API_BASE}/promotion/eligible`);

        setEmployees(res.data);
    };

    const openPromotionForm = (emp) => {

        setSelectedEmployee(emp);

        setForm({
            newRole: emp.role,
            newSalary: emp.salary,
        });
    };

    const promoteEmployee = async () => {

        await axios.put(
            `${API_BASE}/promotion/${selectedEmployee.id}`,
            form
        );

        alert("Employee promoted successfully");

        setSelectedEmployee(null);

        loadEmployees();
    };

    return (
        <Layout
            title="Promotion Management"
            subtitle="Employees eligible for promotion"
        >

            <div className="card">

                <table className="table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Department</th>
                            <th>Experience</th>
                            <th>Role</th>
                            <th>Salary</th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody>

                        {employees.map((emp) => (

                            <tr key={emp.id}>

                                <td>{emp.name}</td>

                                <td>
                                    {emp.department?.departmentName}
                                </td>

                                <td>
                                    {emp.yearsOfExperience} years
                                </td>

                                <td>{emp.role}</td>

                                <td>
                                    ₹{emp.salary}
                                </td>

                                <td>

                                    <button
                                        className="btn btn-primary"
                                        onClick={() => openPromotionForm(emp)}
                                    >
                                        Promote
                                    </button>

                                </td>

                            </tr>
                        ))}

                    </tbody>
                </table>

            </div>

            {selectedEmployee && (

                <div className="card" style={{ marginTop: "2rem" }}>

                    <h2>Promotion Form</h2>

                    <div className="form-grid">

                        <div className="form-group">
                            <label>Employee</label>

                            <input
                                className="form-input"
                                value={selectedEmployee.name}
                                disabled
                            />
                        </div>

                        <div className="form-group">
                            <label>Current Role</label>

                            <input
                                className="form-input"
                                value={selectedEmployee.role}
                                disabled
                            />
                        </div>

                        <div className="form-group">
                            <label>Current Salary</label>

                            <input
                                className="form-input"
                                value={selectedEmployee.salary}
                                disabled
                            />
                        </div>

                        <div className="form-group">
                            <label>New Role</label>
                            <RoleSelect
                                name="newRole"
                                value={form.newRole}
                                onChange={(e) =>
                                    setForm({
                                        ...form,
                                        newRole: e.target.value,
                                    })
                                }
                                placeholder="Select new role"
                            />
                        </div>

                        <div className="form-group">
                            <label>New Salary</label>

                            <input
                                type="number"
                                className="form-input"
                                value={form.newSalary}
                                onChange={(e) =>
                                    setForm({
                                        ...form,
                                        newSalary: e.target.value,
                                    })
                                }
                            />
                        </div>

                    </div>

                    <button
                        className="btn btn-primary"
                        onClick={promoteEmployee}
                    >
                        Confirm Promotion
                    </button>

                </div>
            )}

        </Layout>
    );
}

export default PromotionPage;