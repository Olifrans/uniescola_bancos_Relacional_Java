# Scripts de Banco de Dados (Conceitos Relacionais) -- MySQL
## scripts que demonstram os conceitos de : Joins, Subconsultas, Aliás, Procedures, Functions, Triggers e Funções (Agregação, String, Matemática, Data).



-- =========================================================
-- CRIAÇÃO DE TABELAS E TRIGGERS (SQL Server)
-- =========================================================
CREATE TABLE escolas (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cnpj VARCHAR(18) UNIQUE,
    data_criacao DATETIME DEFAULT GETDATE()
);

CREATE TABLE professores (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(50),
    salario DECIMAL(10,2),
    data_contratacao DATE,
    escola_id INT FOREIGN KEY REFERENCES escolas(id)
);

CREATE TABLE alunos (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    matricula VARCHAR(20) UNIQUE,
    data_nascimento DATE,
    escola_id INT FOREIGN KEY REFERENCES escolas(id)
);
GO

-- TRIGGER: Auditoria de Salários (Funções de String e Data)
CREATE TRIGGER trg_auditoria_salario
ON professores
AFTER UPDATE
AS
BEGIN
    IF UPDATE(salario)
    BEGIN
        PRINT 'Salário alterado em: ' + CONVERT(VARCHAR, GETDATE(), 120);
    END
END;
GO

-- FUNCTION: Formata Nome (Função de String)
CREATE FUNCTION fn_formatar_nome (@nome VARCHAR(100))
RETURNS VARCHAR(100)
AS
BEGIN
    RETURN UPPER(LTRIM(RTRIM(@nome)));
END;
GO

-- PROCEDURE: Subconsultas e Joins Completos
CREATE PROCEDURE sp_dashboard_escolas
AS
BEGIN
    -- FULL OUTER JOIN Nativo do SQL Server
    SELECT 
        dbo.fn_formatar_nome(e.nome) AS Escola,
        p.nome AS Professor,
        a.nome AS Aluno,
        DATEDIFF(YEAR, a.data_nascimento, GETDATE()) AS IdadeAluno
    FROM escolas e
    FULL OUTER JOIN professores p ON e.id = p.escola_id
    FULL OUTER JOIN alunos a ON e.id = a.escola_id
    WHERE e.id IN (SELECT escola_id FROM alunos GROUP BY escola_id HAVING COUNT(id) > 5);
END;
GO