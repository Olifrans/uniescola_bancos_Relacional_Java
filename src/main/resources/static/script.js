const API_URL = 'http://localhost:8080/api';

// ==========================================
// 1. NAVEGAÇÃO E UI
// ==========================================
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => {
        // Atualiza classe active no menu
        document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
        item.classList.add('active');
        
        // Mostra a seção correta
        const target = item.getAttribute('data-target');
        document.querySelectorAll('.content-section').forEach(sec => sec.classList.remove('active'));
        document.getElementById(target).classList.add('active');
        
        // Atualiza título da página
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

async function postData(endpoint, data) {
    try {
        const response = await fetch(`${API_URL}/${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) {
            // Tenta ler a resposta de erro detalhada do GlobalExceptionHandler do Spring
            const errData = await response.json();
            
            // Se tiver mensagens de validação de campos (ex: @NotBlank, @Email)
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
    const data = await fetchData('escolas');
    const tbody = document.getElementById('tbody-escolas');
    tbody.innerHTML = '';

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-state">Nenhuma escola cadastrada.</td></tr>';
        updateSchoolDropdowns([]); // Limpa os dropdowns também
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
                    <button class="btn-primary" style="padding: 0.4rem 0.8rem; font-size: 0.8rem; background-color: #ef4444;" 
                            onclick="deleteItem('escolas', ${esc.id})">
                        Excluir
                    </button>
                </td>
            </tr>
        `;
    });
    
    // Atualiza dropdowns de escolas nos formulários de Professor e Aluno
    updateSchoolDropdowns(data);
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
    const data = await fetchData('professores');
    const escolas = await fetchData('escolas'); 
    const tbody = document.getElementById('tbody-professores');
    tbody.innerHTML = '';

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-state">Nenhum professor cadastrado.</td></tr>';
        return;
    }

    data.forEach(prof => {
        // Usa encadeamento opcional (?.) para evitar erro se prof.escola for null/undefined
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
            </tr>
        `;
    });
}

async function loadAlunos() {
    const data = await fetchData('alunos');
    const escolas = await fetchData('escolas');
    const tbody = document.getElementById('tbody-alunos');
    tbody.innerHTML = '';

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-state">Nenhum aluno cadastrado.</td></tr>';
        return;
    }

    data.forEach(alu => {
        // Usa encadeamento opcional (?.) para evitar erro se alu.escola for null/undefined
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
            </tr>
        `;
    });
}

// ==========================================
// 4. MANIPULAÇÃO DE FORMULÁRIOS
// ==========================================
document.getElementById('form-escola').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        nome: document.getElementById('esc-nome').value.trim(),
        cnpj: document.getElementById('esc-cnpj').value.trim(),
        endereco: document.getElementById('esc-endereco').value.trim()
    };
    
    if (await postData('escolas', data)) {
        e.target.reset();
        await refreshAll();
        alert('✅ Escola salva com sucesso!');
    }
});

document.getElementById('form-professor').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    // VALIDAÇÃO CRÍTICA: Impede envio de NaN se o usuário não selecionar a escola
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
        escolaId: parseInt(escolaIdStr) // Agora garantimos que é um número inteiro válido
    };
    
    if (await postData('professores', data)) {
        e.target.reset();
        await refreshAll();
        alert('✅ Professor salvo com sucesso!');
    }
});

document.getElementById('form-aluno').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    // VALIDAÇÃO CRÍTICA: Impede envio de NaN se o usuário não selecionar a escola
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
        escolaId: parseInt(escolaIdStr) // Agora garantimos que é um número inteiro válido
    };
    
    if (await postData('alunos', data)) {
        e.target.reset();
        await refreshAll();
        alert('✅ Aluno salvo com sucesso!');
    }
});

// ==========================================
// 5. FUNÇÕES AUXILIARES
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
    await loadEscolas();      // Carrega primeiro para popular os dropdowns
    await loadProfessores();  // Carrega depois que os dropdowns estão prontos
    await loadAlunos();
    await loadDashboard();    // Atualiza os contadores por último
}

// ==========================================
// 6. INICIALIZAÇÃO
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    refreshAll();
});