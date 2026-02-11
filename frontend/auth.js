const API_BASE = "http://localhost:8081";
const AUTH_KEY = "stockflow.auth";

function getAuth() {
  try {
    return JSON.parse(localStorage.getItem(AUTH_KEY));
  } catch (error) {
    return null;
  }
}

function setAuth(data) {
  localStorage.setItem(AUTH_KEY, JSON.stringify(data));
}

function clearAuth() {
  localStorage.removeItem(AUTH_KEY);
}

function normalizeRole(role) {
  return String(role || "").trim().toUpperCase();
}

function requireAuth(roles) {
  const auth = getAuth();
  if (!auth || !auth.token) {
    window.location.href = "login.html";
    return null;
  }

  const role = normalizeRole(auth.role);
  if (Array.isArray(roles) && roles.length > 0 && !roles.includes(role)) {
    window.location.href = "dashboard.html";
    return auth;
  }

  return auth;
}

function updateTopbar() {
  const auth = getAuth();
  const apiStatus = document.getElementById("apiStatus");
  if (apiStatus) {
    apiStatus.textContent = `API: ${API_BASE}`;
  }

  const badge = document.getElementById("userBadge");
  if (badge && auth) {
    const role = normalizeRole(auth.role);
    const label = auth.nome || auth.email || "Usuario";
    badge.textContent = `${label} \u2022 ${role}`;
  }

  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      clearAuth();
      window.location.href = "login.html";
    });
  }

  const adminLinks = document.querySelectorAll("[data-admin-link]");
  const isAdmin = auth && normalizeRole(auth.role) === "ADMIN";
  adminLinks.forEach((link) => {
    link.style.display = isAdmin ? "" : "none";
  });
}

function applyEmpresaScope(inputIds) {
  const auth = getAuth();
  if (!auth || normalizeRole(auth.role) === "ADMIN" || !auth.empresaId) {
    return;
  }

  inputIds.forEach((id) => {
    const input = document.getElementById(id);
    if (input) {
      input.value = auth.empresaId;
      input.setAttribute("disabled", "disabled");
    }
  });
}

async function apiRequest(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  if (!options.skipAuth) {
    const auth = getAuth();
    if (auth && auth.token) {
      headers.Authorization = `Bearer ${auth.token}`;
    }
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers
  });

  if (response.status === 401) {
    clearAuth();
    window.location.href = "login.html";
    throw new Error("Nao autorizado");
  }

  if (response.status === 403) {
    throw new Error("Acesso negado");
  }

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `HTTP ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

window.API_BASE = API_BASE;
window.apiRequest = apiRequest;
window.requireAuth = requireAuth;
window.applyEmpresaScope = applyEmpresaScope;
window.setAuth = setAuth;
window.getAuth = getAuth;
window.clearAuth = clearAuth;

document.addEventListener("DOMContentLoaded", updateTopbar);
