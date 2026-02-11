const auth = requireAuth(["ADMIN", "GERENTE", "FUNCIONARIO"]);

const categoriaTable = document.getElementById("categoriaTable");
const produtoTable = document.getElementById("produtoTable");
const toast = document.getElementById("toast");
const categoriaSubmit = document.getElementById("categoriaSubmit");
const produtoSubmit = document.getElementById("produtoSubmit");
const cancelCategoriaEdit = document.getElementById("cancelCategoriaEdit");
const cancelProdutoEdit = document.getElementById("cancelProdutoEdit");

let categoriaEditId = null;
let produtoEditId = null;

function showToast(message, isError = false) {
  toast.textContent = message;
  toast.style.background = isError ? "#b4232a" : "#0d3b66";
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2200);
}

if (!auth) {
  throw new Error("Auth requerido");
}

async function loadCategorias() {
  const categorias = await apiRequest("/categorias");
  categoriaTable.innerHTML = "";

  categorias.forEach((categoria) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${categoria.idCategoria}</td>
      <td>${categoria.nomeCategoria}</td>
      <td>
        <div class="table-actions">
          <button class="btn outline" type="button">Editar</button>
          <button class="btn" type="button">Excluir</button>
        </div>
      </td>
    `;

    const [editBtn, deleteBtn] = tr.querySelectorAll("button");
    editBtn.addEventListener("click", () => {
      setCategoriaEditMode(categoria);
    });

    deleteBtn.addEventListener("click", async () => {
      try {
        await apiRequest(`/categorias/${categoria.idCategoria}`, { method: "DELETE" });
        showToast("Categoria excluida");
        if (categoriaEditId === categoria.idCategoria) {
          resetCategoriaForm();
        }
        await loadCategorias();
      } catch (error) {
        showToast(error.message, true);
      }
    });

    categoriaTable.appendChild(tr);
  });
}

async function loadProdutosByEmpresa(idEmpresa) {
  if (!idEmpresa) {
    produtoTable.innerHTML = "";
    return;
  }

  const produtos = await apiRequest(`/produtos/empresa/${idEmpresa}`);
  produtoTable.innerHTML = "";

  produtos.forEach((produto) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${produto.idProduto}</td>
      <td>${produto.nomeProduto}</td>
      <td>R$ ${Number(produto.preco || 0).toFixed(2)}</td>
      <td>${produto.qtdEstoque ?? 0}</td>
      <td>${produto.categoria?.idCategoria ?? "-"}</td>
      <td>${produto.empresa?.idEmpresa ?? "-"}</td>
      <td>
        <div class="table-actions">
          <button class="btn outline" type="button">Editar</button>
          <button class="btn" type="button">Excluir</button>
        </div>
      </td>
    `;

    const [editBtn, deleteBtn] = tr.querySelectorAll("button");
    editBtn.addEventListener("click", () => {
      setProdutoEditMode(produto);
    });

    deleteBtn.addEventListener("click", async () => {
      try {
        await apiRequest(`/produtos/${produto.idProduto}`, { method: "DELETE" });
        showToast("Produto excluido");
        if (produtoEditId === produto.idProduto) {
          resetProdutoForm();
        }
        await loadProdutosByEmpresa(idEmpresa);
      } catch (error) {
        showToast(error.message, true);
      }
    });

    produtoTable.appendChild(tr);
  });
}

function setCategoriaEditMode(categoria) {
  categoriaEditId = categoria.idCategoria;
  document.getElementById("categoriaNome").value = categoria.nomeCategoria || "";
  categoriaSubmit.textContent = "Salvar categoria";
  cancelCategoriaEdit.hidden = false;
}

function resetCategoriaForm() {
  categoriaEditId = null;
  document.getElementById("categoriaNome").value = "";
  categoriaSubmit.textContent = "Criar categoria";
  cancelCategoriaEdit.hidden = true;
}

function setProdutoEditMode(produto) {
  produtoEditId = produto.idProduto;
  document.getElementById("produtoNome").value = produto.nomeProduto || "";
  document.getElementById("produtoPreco").value = produto.preco ?? 0;
  document.getElementById("produtoQtd").value = produto.qtdEstoque ?? 0;
  document.getElementById("produtoEmpresaId").value = produto.empresa?.idEmpresa ?? "";
  document.getElementById("produtoCategoriaId").value = produto.categoria?.idCategoria ?? "";
  produtoSubmit.textContent = "Salvar produto";
  cancelProdutoEdit.hidden = false;
}

function resetProdutoForm() {
  produtoEditId = null;
  document.getElementById("produtoForm").reset();
  produtoSubmit.textContent = "Criar produto";
  cancelProdutoEdit.hidden = true;
}

document.getElementById("refreshCategorias").addEventListener("click", async () => {
  try {
    await loadCategorias();
  } catch (error) {
    showToast(error.message, true);
  }
});

document.getElementById("refreshProdutos").addEventListener("click", async () => {
  const idEmpresa = document.getElementById("produtoEmpresaFilter").value;
  try {
    await loadProdutosByEmpresa(idEmpresa);
  } catch (error) {
    showToast(error.message, true);
  }
});

document.getElementById("categoriaForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  const nomeCategoria = document.getElementById("categoriaNome").value.trim();

  try {
    if (categoriaEditId) {
      await apiRequest(`/categorias/${categoriaEditId}`, {
        method: "PUT",
        body: JSON.stringify({ nomeCategoria })
      });
      showToast("Categoria atualizada");
      resetCategoriaForm();
    } else {
      await apiRequest("/categorias", {
        method: "POST",
        body: JSON.stringify({ nomeCategoria })
      });
      showToast("Categoria criada");
      event.target.reset();
    }
    await loadCategorias();
  } catch (error) {
    showToast(error.message, true);
  }
});

document.getElementById("produtoForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  const nomeProduto = document.getElementById("produtoNome").value.trim();
  const preco = Number(document.getElementById("produtoPreco").value);
  const qtdEstoque = Number(document.getElementById("produtoQtd").value);
  const empresaId = Number(document.getElementById("produtoEmpresaId").value);
  const categoriaId = Number(document.getElementById("produtoCategoriaId").value);

  try {
    const payload = {
      nomeProduto,
      preco,
      qtdEstoque,
      empresa: { id: empresaId },
      categoria: { idCategoria: categoriaId }
    };

    if (produtoEditId) {
      await apiRequest(`/produtos/${produtoEditId}`, {
        method: "PUT",
        body: JSON.stringify(payload)
      });
      showToast("Produto atualizado");
      resetProdutoForm();
    } else {
      await apiRequest("/produtos", {
        method: "POST",
        body: JSON.stringify(payload)
      });
      showToast("Produto criado");
      event.target.reset();
    }
    await loadProdutosByEmpresa(empresaId);
  } catch (error) {
    showToast(error.message, true);
  }
});

cancelCategoriaEdit.addEventListener("click", () => {
  resetCategoriaForm();
});

cancelProdutoEdit.addEventListener("click", () => {
  resetProdutoForm();
});

(async () => {
  applyEmpresaScope(["produtoEmpresaFilter", "produtoEmpresaId"]);
  try {
    await loadCategorias();
  } catch (error) {
    showToast("Falha ao conectar na API", true);
  }
})();
