const AUTH_KEY = "wiq_auth";
const SEVEN_DAYS_MS = 7 * 24 * 60 * 60 * 1000;

export function saveSession(loginResponse) {
    const expiresAt = loginResponse.expiresAt ?? Date.now() + SEVEN_DAYS_MS;
    localStorage.setItem(
        AUTH_KEY,
        JSON.stringify({
            token: loginResponse.token,
            expiresAt,
            user: loginResponse.user,
        })
    );
}

export function getSession() {
    const raw = localStorage.getItem(AUTH_KEY);
    if (!raw) {
        return null;
    }
    try {
        return JSON.parse(raw);
    } catch {
        clearSession();
        return null;
    }
}

export function isSessionValid() {
    const session = getSession();
    if (!session?.token || !session?.expiresAt) {
        return false;
    }
    return Date.now() < session.expiresAt;
}

export function getToken() {
    const session = getSession();
    return isSessionValid() ? session.token : null;
}

export function getCurrentUser() {
    const session = getSession();
    return isSessionValid() ? session.user : null;
}

export function isHr() {
    const user = getCurrentUser();
    return user?.role === "HR";
}

export function clearSession() {
    localStorage.removeItem(AUTH_KEY);
}
