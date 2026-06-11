package com.example.WorkforceIQ.util;

import java.util.List;

public final class Roles {

    public static final String HR = "HR";

    public static final List<String> ALL = List.of(
            "HR",
            "EMPLOYEE",
            "MANAGER",
            "TEAM_LEAD",
            "SENIOR_EMPLOYEE",
            "JUNIOR_EMPLOYEE",
            "INTERN",
            "CONTRACTOR",
            "DIRECTOR",
            "VP",
            "CEO",
            "ADMIN"
    );

    private Roles() {
    }
}
