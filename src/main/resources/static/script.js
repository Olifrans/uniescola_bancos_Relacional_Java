const API_URL = 'http://localhost:8080/api';

// Cache de dados para busca local
let cacheEscolas = [];
let cacheProfessores = [];
let cacheAlunos = [];

// ==========================================
// 1. NAVEGAÇÃO E UI
// ==========================================
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => {
        document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
        item.classList.add('active');
        
        const target = item.getAttribute('data-target');
        document.querySelectorAll('.content-section').forEach(sec => sec.classList.remove('active'));
        document.getElementById(target).classList.add('active');
        
        const titles = {
            'dashboard': 'Dashboard',
            'escolas': 'Gestão de Escolas',
            'professores': 'Gestão de Professores',
            'alunos': 'Gestão de Alunos'
        };
        document.getElementById('page-title').innerText = titles[target];
    });
});

// ==========================================
// 2. FUNÇÕES DE API (FETCH)
// ==========================================
async function fetchData(endpoint) {
    try {
        const response = await fetch(`${API_URL}/${endpoint}`);
        if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
        return await response.json();
    } catch (error) {
        console.error(`Erro ao buscar ${endpoint}:`, error);
        return [];
    }
}

async function postData(endpoint, data, method = 'POST') {
    try {
        const response = await fetch(`${API_URL}/${endpoint}`, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) {
            const errData = await response.json();
            if (errData.messages) {
                const erros = Object.values(errData.messages).join('\n- ');
                throw new Error(`Erros de validação:\n- ${erros}`);
            }
            throw new Error(errData.message || `Erro HTTP: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        alert(`⚠️ Atenção:\n\n${error.message}`);
        return null;
    }
}

// ==========================================
// 3. RENDERIZAÇÃO DE DADOS
// ==========================================
async function loadDashboard() {
    const escolas = await fetchData('escolas');
    const professores = await fetchData('professores');
    const alunos = await fetchData('alunos');

    document.getElementById('count-escolas').innerText = escolas.length;
    document.getElementById('count-professores').innerText = professores.length;
    document.getElementById('count-alunos').innerText = alunos.length;
}

async function loadEscolas() {
    cacheEscolas = await fetchData('escolas');
    renderEscolas(cacheEscolas);
    updateSchoolDropdowns(cacheEscolas);
}

function renderEscolas(data) {
    const tbody = document.getElementById('tbody-escolas');
    tbody.innerHTML = '';

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-state">Nenhuma escola cadastrada.</td></tr>';
        return;
    }

    data.forEach(esc => {
        tbody.innerHTML += `
            <tr>
                <td>${esc.id}</td>
                <td><strong>${esc.nome}</strong></td>
                <td>${esc.cnpj || '-'}</td>
                <td>${esc.endereco || '-'}</td>
                <td>
                    <button class="btn-edit" onclick="editEscola(${esc.id})">
                        <i class="ph ph-pencil"></i> Editar
                    </button>
                    <button class="btn-delete" onclick="deleteItem('escolas', ${esc.id})">
                        <i class="ph ph-trash"></i> Excluir
                    </button>
                </td>
            </tr>
        `;
    });
}

function updateSchoolDropdowns(escolas) {
    const selects = [document.getElementById('prof-escola'), document.getElementById('alu-escola')];
    selects.forEach(select => {
        select.innerHTML = '<option value="">Selecione a Escola *</option>';
        escolas.forEach(esc => {
            select.innerHTML += `<option value="${esc.id}">${esc.nome}</option>`;
        });
    });
}

async function loadProfessores() {
    cacheProfessores = await fetchData('professores');
    renderProfessores(cacheProfessores);
}

function renderProfessores(data) {
    const escolas = cacheEscolas;
    const tbody = document.getElementById('tbody-professores');
    tbody.innerHTML = '';

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="empty-state">Nenhum professor cadastrado.</td></tr>';
        return;
    }

    data.forEach(prof => {
        const escolaId = prof.escola ? prof.escola.id : null;
        const escolaEncontrada = escolas.find(e => e.id === escolaId);
        const escolaNome = escolaEncontrada ? escolaEncontrada.nome : 'Não vinculada';
        
        tbody.innerHTML += `
            <tr>
                <td>${prof.id}</td>
                <td><strong>${prof.nome}</strong><br><small style="color:var(--text-light)">${prof.email || '-'}</small></td>
                <td>${prof.especialidade || '-'}</td>
                <td>${escolaNome}</td>
                <td>R$ ${prof.salario ? Number(prof.salario).toFixed(2) : '0.00'}</td>
                <td>
                    <button class="btn-edit" onclick="editProfessor(${prof.id})">
                        <i class="ph ph-pencil"></i> Editar
                    </button>
                    <button class="btn-delete" onclick="deleteItem('professores', ${prof.id})">
                        <i class="ph ph-trash"></i> Excluir
                    </button>
                </td>
            </tr>
        `;
    });
}

async function loadAlunos() {
    cacheAlunos = await fetchData('alunos');
    renderAlunos(cacheAlunos);
}

function renderAlunos(data) {
    const escolas = cacheEscolas;
    const tbody = document.getElementById('tbody-alunos');
    tbody.innerHTML = '';

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="empty-state">Nenhum aluno cadastrado.</td></tr>';
        return;
    }

    data.forEach(alu => {
        const escolaId = alu.escola ? alu.escola.id : null;
        const escolaEncontrada = escolas.find(e => e.id === escolaId);
        const escolaNome = escolaEncontrada ? escolaEncontrada.nome : 'Não vinculada';
        
        const dataFormatada = alu.dataNascimento ? new Date(alu.dataNascimento).toLocaleDateString('pt-BR') : '-';

        tbody.innerHTML += `
            <tr>
                <td>${alu.id}</td>
                <td><strong>${alu.nome}</strong></td>
                <td>${alu.matricula || '-'}</td>
                <td>${escolaNome}</td>
                <td>${dataFormatada}</td>
                <td>
                    <button class="btn-edit" onclick="editAluno(${alu.id})">
                        <i class="ph ph-pencil"></i> Editar
                    </button>
                    <button class="btn-delete" onclick="deleteItem('alunos', ${alu.id})">
                        <i class="ph ph-trash"></i> Excluir
                    </button>
                </td>
            </tr>
        `;
    });
}

// ==========================================
// 4. BUSCA LOCAL
// ==========================================
document.getElementById('search-escolas').addEventListener('input', (e) => {
    const termo = e.target.value.toLowerCase();
    const filtradas = cacheEscolas.filter(esc => esc.nome.toLowerCase().includes(termo));
    renderEscolas(filtradas);
});

document.getElementById('search-professores').addEventListener('input', (e) => {
    const termo = e.target.value.toLowerCase();
    const filtrados = cacheProfessores.filter(prof => prof.nome.toLowerCase().includes(termo));
    renderProfessores(filtrados);
});

document.getElementById('search-alunos').addEventListener('input', (e) => {
    const termo = e.target.value.toLowerCase();
    const filtrados = cacheAlunos.filter(alu => alu.nome.toLowerCase().includes(termo));
    renderAlunos(filtrados);
});

// ==========================================
// 5. MANIPULAÇÃO DE FORMULÁRIOS (CREATE/UPDATE)
// ==========================================
document.getElementById('form-escola').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('esc-id').value;
    const data = {
        nome: document.getElementById('esc-nome').value.trim(),
        cnpj: document.getElementById('esc-cnpj').value.trim(),
        endereco: document.getElementById('esc-endereco').value.trim()
    };
    
    const endpoint = id ? `escolas/${id}` : 'escolas';
    const method = id ? 'PUT' : 'POST';
    
    if (await postData(endpoint, data, method)) {
        e.target.reset();
        document.getElementById('esc-id').value = '';
        document.getElementById('form-escola-title').innerText = 'Nova Escola';
        document.getElementById('btn-cancel-escola').style.display = 'none';
        await refreshAll();
        alert(`✅ Escola ${id ? 'atualizada' : 'salva'} com sucesso!`);
    }
});

document.getElementById('form-professor').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('prof-id').value;
    const escolaIdStr = document.getElementById('prof-escola').value;
    
    if (!escolaIdStr) {
        alert("⚠️ Por favor, selecione uma Escola no campo correspondente.");
        return;
    }

    const data = {
        nome: document.getElementById('prof-nome').value.trim(),
        email: document.getElementById('prof-email').value.trim(),
        especialidade: document.getElementById('prof-especialidade').value.trim(),
        salario: parseFloat(document.getElementById('prof-salario').value) || 0.0,
        dataContratacao: document.getElementById('prof-data').value || null,
        escolaId: parseInt(escolaIdStr)
    };
    
    const endpoint = id ? `professores/${id}` : 'professores';
    const method = id ? 'PUT' : 'POST';
    
    if (await postData(endpoint, data, method)) {
        e.target.reset();
        document.getElementById('prof-id').value = '';
        document.getElementById('form-professor-title').innerText = 'Novo Professor';
        document.getElementById('btn-cancel-professor').style.display = 'none';
        await refreshAll();
        alert(`✅ Professor ${id ? 'atualizado' : 'salvo'} com sucesso!`);
    }
});

document.getElementById('form-aluno').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('alu-id').value;
    const escolaIdStr = document.getElementById('alu-escola').value;
    
    if (!escolaIdStr) {
        alert("⚠️ Por favor, selecione uma Escola no campo correspondente.");
        return;
    }

    const data = {
        nome: document.getElementById('alu-nome').value.trim(),
        matricula: document.getElementById('alu-matricula').value.trim(),
        email: document.getElementById('alu-email').value.trim(),
        dataNascimento: document.getElementById('alu-data').value,
        escolaId: parseInt(escolaIdStr)
    };
    
    const endpoint = id ? `alunos/${id}` : 'alunos';
    const method = id ? 'PUT' : 'POST';
    
    if (await postData(endpoint, data, method)) {
        e.target.reset();
        document.getElementById('alu-id').value = '';
        document.getElementById('form-aluno-title').innerText = 'Novo Aluno';
        document.getElementById('btn-cancel-aluno').style.display = 'none';
        await refreshAll();
        alert(`✅ Aluno ${id ? 'atualizado' : 'salvo'} com sucesso!`);
    }
});

// ==========================================
// 6. FUNÇÕES DE EDIÇÃO
// ==========================================
function editEscola(id) {
    const escola = cacheEscolas.find(e => e.id === id);
    if (!escola) return;

    document.getElementById('esc-id').value = escola.id;
    document.getElementById('esc-nome').value = escola.nome;
    document.getElementById('esc-cnpj').value = escola.cnpj || '';
    document.getElementById('esc-endereco').value = escola.endereco || '';
    document.getElementById('form-escola-title').innerText = 'Editar Escola';
    document.getElementById('btn-cancel-escola').style.display = 'inline-flex';
    
    document.getElementById('escolas').scrollIntoView({ behavior: 'smooth' });
}

function editProfessor(id) {
    const prof = cacheProfessores.find(p => p.id === id);
    if (!prof) return;

    document.getElementById('prof-id').value = prof.id;
    document.getElementById('prof-nome').value = prof.nome;
    document.getElementById('prof-email').value = prof.email || '';
    document.getElementById('prof-especialidade').value = prof.especialidade || '';
    document.getElementById('prof-salario').value = prof.salario || '';
    document.getElementById('prof-data').value = prof.dataContratacao || '';
    document.getElementById('prof-escola').value = prof.escola ? prof.escola.id : '';
    document.getElementById('form-professor-title').innerText = 'Editar Professor';
    document.getElementById('btn-cancel-professor').style.display = 'inline-flex';
    
    document.getElementById('professores').scrollIntoView({ behavior: 'smooth' });
}

function editAluno(id) {
    const alu = cacheAlunos.find(a => a.id === id);
    if (!alu) return;

    document.getElementById('alu-id').value = alu.id;
    document.getElementById('alu-nome').value = alu.nome;
    document.getElementById('alu-matricula').value = alu.matricula || '';
    document.getElementById('alu-email').value = alu.email || '';
    document.getElementById('alu-data').value = alu.dataNascimento || '';
    document.getElementById('alu-escola').value = alu.escola ? alu.escola.id : '';
    document.getElementById('form-aluno-title').innerText = 'Editar Aluno';
    document.getElementById('btn-cancel-aluno').style.display = 'inline-flex';
    
    document.getElementById('alunos').scrollIntoView({ behavior: 'smooth' });
}

// Botões de cancelar edição
document.getElementById('btn-cancel-escola').addEventListener('click', () => {
    document.getElementById('form-escola').reset();
    document.getElementById('esc-id').value = '';
    document.getElementById('form-escola-title').innerText = 'Nova Escola';
    document.getElementById('btn-cancel-escola').style.display = 'none';
});

document.getElementById('btn-cancel-professor').addEventListener('click', () => {
    document.getElementById('form-professor').reset();
    document.getElementById('prof-id').value = '';
    document.getElementById('form-professor-title').innerText = 'Novo Professor';
    document.getElementById('btn-cancel-professor').style.display = 'none';
});

document.getElementById('btn-cancel-aluno').addEventListener('click', () => {
    document.getElementById('form-aluno').reset();
    document.getElementById('alu-id').value = '';
    document.getElementById('form-aluno-title').innerText = 'Novo Aluno';
    document.getElementById('btn-cancel-aluno').style.display = 'none';
});

// ==========================================
// 7. FUNÇÕES AUXILIARES
// ==========================================
async function deleteItem(endpoint, id) {
    if (!confirm('Tem certeza que deseja excluir este registro? Esta ação não pode ser desfeita.')) return;
    
    try {
        const response = await fetch(`${API_URL}/${endpoint}/${id}`, { method: 'DELETE' });
        if (response.ok) {
            alert('✅ Excluído com sucesso!');
            await refreshAll();
        } else {
            const err = await response.json();
            alert(`❌ Erro ao excluir: ${err.message || 'Verifique se há dependências vinculadas a este registro.'}`);
        }
    } catch (error) {
        alert('❌ Erro de conexão com a API ao tentar excluir.');
    }
}

async function refreshAll() {
    await loadEscolas();
    await loadProfessores();
    await loadAlunos();
    await loadDashboard();
}

// ==========================================
// 8. INICIALIZAÇÃO
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    refreshAll();
});