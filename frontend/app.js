const auth = requireAuth(["ADMIN"]);

const empresaTable = document.getElementById("empresaTable");
const usuarioTable = document.getElementById("usuarioTable");
const toast = document.getElementById("toast");

function showToast(message, isError = false) {
  toast.textContent = message;
  toast.style.background = isError ? "#b4232a" : "#0d3b66";
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2200);
}

if (!auth) {
  throw new Error("Auth requerido");
}

async function loadEmpresas() {
  const empresas = await apiRequest("/admin/empresas");
  empresaTable.innerHTML = "";

  empresas.forEach((empresa) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${empresa.idEmpresa}</td>
      <td>${empresa.nome || "-"}</td>
      <td>${empresa.documento || "-"}</td>
      <td>${empresa.ativo ? "Ativo" : "Inativo"}</td>
      <td>
        <button class="btn" data-id="${empresa.idEmpresa}" data-ativo="${empresa.ativo}">
          ${empresa.ativo ? "Desativar" : "Ativar"}
        </button>
      </td>
    `;

    tr.querySelector("button").addEventListener("click", async (event) => {
      const id = event.currentTarget.dataset.id;
      const ativo = event.currentTarget.dataset.ativo !== "true";
      await apiRequest(`/admin/empresas/${id}/status?ativo=${ativo}`, {
        method: "PUT"
      });
      showToast("Status atualizado");
      await loadEmpresas();
    });

    empresaTable.appendChild(tr);
  });
}

async function loadUsuariosByEmpresa(idEmpresa) {
  if (!idEmpresa) {
    usuarioTable.innerHTML = "";
    return;
  }

  const usuarios = await apiRequest(`/usuarios/empresa/${idEmpresa}`);
  usuarioTable.innerHTML = "";

  usuarios.forEach((usuario) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${usuario.id}</td>
      <td>${usuario.nome}</td>
      <td>${usuario.email}</td>
      <td>${usuario.role}</td>
      <td>${usuario.empresa?.idEmpresa ?? "-"}</td>
    `;
    usuarioTable.appendChild(tr);
  });
}

document.getElementById("refreshEmpresas").addEventListener("click", async () => {
  try {
    await loadEmpresas();
  } catch (error) {
    showToast(error.message, true);
  }
});

document.getElementById("refreshUsuarios").addEventListener("click", async () => {
  const idEmpresa = document.getElementById("usuarioEmpresaId").value;
  try {
    await loadUsuariosByEmpresa(idEmpresa);
  } catch (error) {
    showToast(error.message, true);
  }
});

document.getElementById("empresaForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  const nome = document.getElementById("empresaNome").value.trim();
  const documento = document.getElementById("empresaDocumento").value.trim();
  const ativo = document.getElementById("empresaAtivo").value === "true";

  try {
    await apiRequest("/admin/empresas", {
      method: "POST",
      body: JSON.stringify({ nome, documento, ativo })
    });
    showToast("Empresa criada");
    event.target.reset();
    await loadEmpresas();
  } catch (error) {
    showToast(error.message, true);
  }
});

document.getElementById("usuarioForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  const nome = document.getElementById("usuarioNome").value.trim();
  const email = document.getElementById("usuarioEmail").value.trim();
  const senha = document.getElementById("usuarioSenha").value;
  const role = document.getElementById("usuarioRole").value;
  const empresaId = Number(document.getElementById("usuarioEmpresaRef").value);

  try {
    await apiRequest("/usuarios", {
      method: "POST",
      body: JSON.stringify({
        nome,
        email,
        senha,
        role,
        empresa: { id: empresaId }
      })
    });
    showToast("Usuario criado");
    event.target.reset();
    await loadUsuariosByEmpresa(empresaId);
  } catch (error) {
    showToast(error.message, true);
  }
});

document.getElementById("usuarioUpdateForm").addEventListener("submit", async (event) => {
  event.preventDefault();

  const id = Number(document.getElementById("usuarioUpdateId").value);
  const nome = document.getElementById("usuarioUpdateNome").value.trim();
  const email = document.getElementById("usuarioUpdateEmail").value.trim();
  const senha = document.getElementById("usuarioUpdateSenha").value;
  const role = document.getElementById("usuarioUpdateRole").value;
  const empresaId = Number(document.getElementById("usuarioUpdateEmpresa").value);

  try {
    await apiRequest(`/usuarios/${id}`, {
      method: "PUT",
      body: JSON.stringify({
        nome,
        email,
        senha,
        role,
        empresa: { id: empresaId }
      })
    });
    showToast("Usuario atualizado");
    await loadUsuariosByEmpresa(empresaId);
  } catch (error) {
    showToast(error.message, true);
  }
});

(async () => {
  try {
    await loadEmpresas();
  } catch (error) {
    showToast("Falha ao conectar na API", true);
  }
})();
