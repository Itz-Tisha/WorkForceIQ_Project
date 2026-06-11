import { EMPLOYEE_ROLES } from "../constants/roles";

function RoleSelect({ name = "role", value, onChange, required = true, placeholder = "Select Role" }) {
    return (
        <select
            name={name}
            className="form-select"
            value={value}
            onChange={onChange}
            required={required}
        >
            <option value="">{placeholder}</option>
            {EMPLOYEE_ROLES.map((role) => (
                <option key={role.value} value={role.value}>
                    {role.label}
                </option>
            ))}
        </select>
    );
}

export default RoleSelect;
