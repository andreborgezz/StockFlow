const toast = document.getElementById("toast");
const form = document.getElementById("loginForm");

const existingAuth = getAuth();
if (existingAuth && existingAuth.token) {
  window.location.href = "dashboard.html";
}

function showToast(message, isError = false) {
  toast.textContent = message;
  toast.style.background = isError ? "#b4232a" : "#0d3b66";
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2200);
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  const email = document.getElementById("loginEmail").value.trim();
  const senha = document.getElementById("loginSenha").value;

  try {
    const response = await apiRequest("/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, senha }),
      skipAuth: true
    });

    setAuth(response);
    showToast("Login ok");

    const role = String(response.role || "").toUpperCase();
    if (role === "ADMIN") {
      window.location.href = "admin.html";
      return;
    }

    window.location.href = "dashboard.html";
  } catch (error) {
    showToast(error.message || "Falha no login", true);
  }
});
