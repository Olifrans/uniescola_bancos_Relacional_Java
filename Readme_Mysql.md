# Scripts de Banco de Dados (Conceitos Relacionais) -- MySQL
## scripts que demonstram os conceitos de : Joins, Subconsultas, Aliás, Procedures, Functions, Triggers e Funções (Agregação, String, Matemática, Data).




-- =========================================================
-- CRIAÇÃO DE TABELAS E TRIGGERS (MySQL)
-- =========================================================
CREATE TABLE IF NOT EXISTS escolas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cnpj VARCHAR(18) UNIQUE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS professores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(50),
    salario DECIMAL(10,2),
    data_contratacao DATE,
    escola_id INT,
    FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

CREATE TABLE IF NOT EXISTS alunos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    matricula VARCHAR(20) UNIQUE,
    data_nascimento DATE,
    escola_id INT,
    FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

-- TRIGGER: Impede cadastro de aluno com data de nascimento no futuro (Função de Data)
DELIMITER //
CREATE TRIGGER trg_verifica_data_nascimento
BEFORE INSERT ON alunos
FOR EACH ROW
BEGIN
    IF NEW.data_nascimento > CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Data de nascimento não pode ser no futuro!';
    END IF;
END;
//
DELIMITER ;

-- FUNCTION: Calcula idade exata (Funções de Data e Matemática)
DELIMITER //
CREATE FUNCTION fn_calcular_idade(data_nasc DATE) 
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE idade INT;
    SET idade = TIMESTAMPDIFF(YEAR, data_nasc, CURDATE());
    RETURN idade;
END;
//
DELIMITER ;

-- PROCEDURE: Relatório de Escola com Agregações e Joins
DELIMITER //
CREATE PROCEDURE sp_relatorio_escola(IN p_escola_id INT)
BEGIN
    -- Utiliza INNER JOIN, LEFT JOIN, Aliás e Funções de Agregação/String
    SELECT 
        e.nome AS 'Nome da Escola',
        UPPER(e.cnpj) AS 'CNPJ Formatado',
        COUNT(DISTINCT p.id) AS 'Total de Professores',
        COUNT(DISTINCT a.id) AS 'Total de Alunos',
        ROUND(AVG(fn_calcular_idade(a.data_nascimento)), 1) AS 'Média de Idade dos Alunos'
    FROM escolas e
    LEFT JOIN professores p ON e.id = p.escola_id
    LEFT JOIN alunos a ON e.id = a.escola_id
    WHERE e.id = p_escola_id
    GROUP BY e.id, e.nome, e.cnpj;
END;
//
DELIMITER ;

-- CONSULTAS AVANÇADAS (Demonstração de Conceitos SQL)
-- 1. Subconsulta e Alias
SELECT nome AS 'Aluno', matricula AS 'Registro' 
FROM alunos 
WHERE escola_id = (SELECT id FROM escolas WHERE nome LIKE '%Federal%');

-- 2. FULL OUTER JOIN (Simulado no MySQL com UNION)
SELECT e.nome AS escola, p.nome AS professor, a.nome AS aluno
FROM escolas e
LEFT JOIN professores p ON e.id = p.escola_id
LEFT JOIN alunos a ON e.id = a.escola_id
UNION
SELECT e.nome, p.nome, a.nome
FROM escolas e
RIGHT JOIN professores p ON e.id = p.escola_id
RIGHT JOIN alunos a ON e.id = a.escola_id;