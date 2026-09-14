const API_URL = 'http://localhost:8080/api';

// Navegação por Abas
document.querySelectorAll('.sidebar nav li').forEach(item => {
    item.addEventListener('click', () => {
        document.querySelectorAll('.sidebar nav li').forEach(i => i.classList.remove('active'));
        item.classList.add('active');
        
        const target = item.getAttribute('data-target');
        document.querySelectorAll('.content-section').forEach(sec => sec.classList.remove('active'));
        document.getElementById(target).classList.add('active');
        document.getElementById('page-title').innerText = item.innerText;
    });
});

// Função Genérica para Carregar Dados
async function carregarDados(endpoint, tbodyId, countId) {
    try {
        const response = await fetch(`${API_URL}/${endpoint}`);
        const data = await response.json();
        
        if(countId) document.getElementById(countId).innerText = data.length;
        
        const tbody = document.getElementById(tbodyId);
        if(tbody) {
            tbody.innerHTML = '';
            data.forEach(item => {
                const tr = document.createElement('tr');
                tr.innerHTML = `<td>${item.id}</td><td>${item.nome}</td><td>${item.cnpj || item.matricula || '-'}</td><td>${item.endereco || '-'}</td>`;
                tbody.appendChild(tr);
            });
        }
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
}

// Salvar Escola
document.getElementById('form-escola').addEventListener('submit', async (e) => {
    e.preventDefault();
    const novaEscola = {
        nome: document.getElementById('esc-nome').value,
        cnpj: document.getElementById('esc-cnpj').value,
        endereco: document.getElementById('esc-endereco').value
    };

    await fetch(`${API_URL}/escolas`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(novaEscola)
    });
    
    e.target.reset();
    carregarDados('escolas', 'tbody-escolas', 'count-escolas');
});

// Inicialização
window.onload = () => {
    carregarDados('escolas', 'tbody-escolas', 'count-escolas');
    carregarDados('professores', null, 'count-professores');
    carregarDados('alunos', null, 'count-alunos');
};