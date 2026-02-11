const auth = requireAuth(["ADMIN", "GERENTE", "FUNCIONARIO"]);

const toast = document.getElementById("toast");
const vendaForm = document.getElementById("vendaForm");
const vendaEmpresaId = document.getElementById("vendaEmpresaId");
const vendaProduto = document.getElementById("vendaProduto");
const vendaQuantidade = document.getElementById("vendaQuantidade");
const itensTable = document.getElementById("itensTable");
const vendaTotal = document.getElementById("vendaTotal");
const vendasTable = document.getElementById("vendasTable");
const vendaEmpresaFilter = document.getElementById("vendaEmpresaFilter");

const clearItensBtn = document.getElementById("clearItens");
const carregarProdutosBtn = document.getElementById("carregarProdutos");
const addItemBtn = document.getElementById("addItem");
const refreshVendasBtn = document.getElementById("refreshVendas");

let itens = [];

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

function renderItens() {
  itensTable.innerHTML = "";

  itens.forEach((item) => {
    const subtotal = item.preco * item.quantidade;
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${item.idProduto}</td>
      <td>${item.nomeProduto}</td>
      <td>${formatCurrency(item.preco)}</td>
      <td>${item.quantidade}</td>
      <td>${formatCurrency(subtotal)}</td>
      <td>
        <button class="btn" type="button" data-id="${item.idProduto}">Remover</button>
      </td>
    `;

    tr.querySelector("button").addEventListener("click", (event) => {
      const id = Number(event.currentTarget.dataset.id);
      itens = itens.filter((current) => current.idProduto !== id);
      renderItens();
    });

    itensTable.appendChild(tr);
  });

  const total = itens.reduce((acc, item) => acc + item.preco * item.quantidade, 0);
  vendaTotal.textContent = formatCurrency(total);
}

function resetItens() {
  itens = [];
  vendaQuantidade.value = 1;
  renderItens();
}

async function loadProdutosByEmpresa(idEmpresa) {
  vendaProduto.innerHTML = "<option value=\"\">Selecione um produto</option>";

  if (!idEmpresa) {
    return;
  }

  const produtos = await apiRequest(`/produtos/empresa/${idEmpresa}`);

  produtos.forEach((produto) => {
    const option = document.createElement("option");
    option.value = produto.idProduto;
    option.dataset.preco = produto.preco ?? 0;
    option.dataset.nome = produto.nomeProduto || "-";
    option.textContent = `${produto.nomeProduto} (Estoque: ${produto.qtdEstoque ?? 0})`;
    vendaProduto.appendChild(option);
  });
}

async function loadVendasByEmpresa(idEmpresa) {
  if (!idEmpresa) {
    vendasTable.innerHTML = "";
    return;
  }

  const vendas = await apiRequest(`/vendas/empresa/${idEmpresa}`);
  vendasTable.innerHTML = "";

  vendas.forEach((venda) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${venda.idVenda ?? "-"}</td>
      <td>${formatDate(venda.data)}</td>
      <td>${formatCurrency(venda.valorTotal)}</td>
      <td>${venda.empresa?.idEmpresa ?? idEmpresa}</td>
    `;
    vendasTable.appendChild(tr);
  });
}

function addItemFromForm() {
  const empresaId = Number(vendaEmpresaId.value);
  const produtoId = Number(vendaProduto.value);
  const quantidade = Number(vendaQuantidade.value);

  if (!empresaId) {
    showToast("Informe a empresa", true);
    return;
  }

  if (!produtoId) {
    showToast("Selecione um produto", true);
    return;
  }

  if (!quantidade || quantidade < 1) {
    showToast("Quantidade invalida", true);
    return;
  }

  const option = vendaProduto.selectedOptions[0];
  const preco = Number(option.dataset.preco || 0);
  const nomeProduto = option.dataset.nome || "-";

  const existente = itens.find((item) => item.idProduto === produtoId);
  if (existente) {
    existente.quantidade += quantidade;
  } else {
    itens.push({
      idProduto: produtoId,
      nomeProduto,
      preco,
      quantidade
    });
  }

  renderItens();
}

carregarProdutosBtn.addEventListener("click", async () => {
  try {
    await loadProdutosByEmpresa(vendaEmpresaId.value);
    showToast("Produtos carregados");
  } catch (error) {
    showToast(error.message, true);
  }
});

addItemBtn.addEventListener("click", () => {
  addItemFromForm();
});

clearItensBtn.addEventListener("click", () => {
  resetItens();
});

refreshVendasBtn.addEventListener("click", async () => {
  try {
    await loadVendasByEmpresa(vendaEmpresaFilter.value);
  } catch (error) {
    showToast(error.message, true);
  }
});

vendaEmpresaId.addEventListener("change", async () => {
  resetItens();
  try {
    await loadProdutosByEmpresa(vendaEmpresaId.value);
  } catch (error) {
    showToast(error.message, true);
  }
});

vendaForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  const empresaId = Number(vendaEmpresaId.value);
  if (!empresaId) {
    showToast("Informe a empresa", true);
    return;
  }

  if (itens.length === 0) {
    showToast("Adicione itens", true);
    return;
  }

  const payload = itens.map((item) => ({
    produto: { idProduto: item.idProduto, empresa: { id: empresaId } },
    quantidade: item.quantidade
  }));

  try {
    await apiRequest(`/vendas/empresa/${empresaId}`, {
      method: "POST",
      body: JSON.stringify(payload)
    });

    showToast("Venda registrada");
    resetItens();

    if (!vendaEmpresaFilter.value) {
      vendaEmpresaFilter.value = empresaId;
    }

    await loadVendasByEmpresa(vendaEmpresaFilter.value || empresaId);
  } catch (error) {
    showToast(error.message, true);
  }
});

applyEmpresaScope(["vendaEmpresaId", "vendaEmpresaFilter"]);
