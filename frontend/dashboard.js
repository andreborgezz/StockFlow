const auth = requireAuth(["ADMIN", "GERENTE", "FUNCIONARIO"]);

const toast = document.getElementById("toast");
const loadDashboardBtn = document.getElementById("loadDashboard");
const dashboardEmpresaId = document.getElementById("dashboardEmpresaId");
const dashboardVendasTable = document.getElementById("dashboardVendasTable");
const kpiTotal = document.getElementById("kpiTotal");
const kpiCount = document.getElementById("kpiCount");
const kpiTicket = document.getElementById("kpiTicket");

const loadEstoqueBtn = document.getElementById("loadEstoque");
const estoqueEmpresaId = document.getElementById("estoqueEmpresaId");
const estoqueLimite = document.getElementById("estoqueLimite");
const estoqueBaixoTable = document.getElementById("estoqueBaixoTable");

function showToast(message, isError = false) {
  toast.textContent = message;
  toast.style.background = isError ? "#b4232a" : "#0d3b66";
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2200);
}

if (!auth) {
  throw new Error("Auth requerido");
}

function formatCurrency(value) {
  return `R$ ${Number(value || 0).toFixed(2)}`;
}

function formatDate(value) {
  if (!value) {
    return "-";
  }

  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return String(value);
  }

  return date.toLocaleString("pt-BR");
}

function setKpis(resumo) {
  kpiTotal.textContent = formatCurrency(resumo.totalVendido);
  kpiCount.textContent = String(resumo.numeroVendas ?? 0);
  kpiTicket.textContent = formatCurrency(resumo.ticketMedio);
}

async function loadDashboard() {
  const empresaId = Number(dashboardEmpresaId.value);
  if (!empresaId) {
    showToast("Informe a empresa", true);
    return;
  }

  const resumo = await apiRequest(`/vendas/empresa/${empresaId}/resumo`);
  setKpis(resumo);

  const vendas = await apiRequest(`/vendas/empresa/${empresaId}`);

  dashboardVendasTable.innerHTML = "";
  vendas.forEach((venda) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${venda.idVenda ?? "-"}</td>
      <td>${formatDate(venda.data)}</td>
      <td>${formatCurrency(venda.valorTotal)}</td>
    `;
    dashboardVendasTable.appendChild(tr);
  });
}

async function loadEstoqueBaixo() {
  const empresaId = Number(estoqueEmpresaId.value);
  const limite = Number(estoqueLimite.value || 0);

  if (!empresaId) {
    showToast("Informe a empresa", true);
    return;
  }

  const produtos = await apiRequest(
    `/produtos/empresa/${empresaId}/estoque-baixo?limite=${limite}`
  );

  estoqueBaixoTable.innerHTML = "";
  produtos.forEach((produto) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${produto.idProduto}</td>
      <td>${produto.nomeProduto}</td>
      <td>${produto.qtdEstoque ?? 0}</td>
      <td>${produto.categoria?.idCategoria ?? "-"}</td>
    `;
    estoqueBaixoTable.appendChild(tr);
  });
}

loadDashboardBtn.addEventListener("click", async () => {
  try {
    await loadDashboard();
  } catch (error) {
    showToast(error.message, true);
  }
});

loadEstoqueBtn.addEventListener("click", async () => {
  try {
    await loadEstoqueBaixo();
  } catch (error) {
    showToast(error.message, true);
  }
});

applyEmpresaScope(["dashboardEmpresaId", "estoqueEmpresaId"]);
