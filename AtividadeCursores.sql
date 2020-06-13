CREATE DATABASE AtividadeCursores
GO
USE AtividadeCursores

CREATE TABLE curso(
cod INT NOT NULL,
nome VARCHAR(50) NOT NULL,
duracao INT
PRIMARY KEY (cod)
)

INSERT INTO curso VALUES
(0, 'Análise e Desenvolvimento de Sistema', 2880),
(1, 'Logística', 2880),
(2, 'Polímeros', 2880),
(3, 'Comércio Exterior', 2600),
(4, 'Gestão Empresarial', 2600)

CREATE TABLE disciplinas(
cod INT IDENTITY NOT NULL,
nome VARCHAR(50) NOT NULL,
carga_horaria INT NOT NULL
PRIMARY KEY (cod)
)

INSERT INTO disciplinas VALUES
('Algoritmos', 80),
('Administração', 80),
('Laboratório de Hardware', 40),
('Pesquisa Operacional', 80),
('Física I', 80),
('Físico Química', 80),
('Comércio Exterior', 80),
('Fundamentos de Marketing', 80),
('Informática', 40),
('Sistema de Informação', 80)

CREATE TABLE disciplina_curso(
cod_disciplina INT NOT NULL,
cod_curso INT NOT NULL
CONSTRAINT pk_disc_curso PRIMARY KEY (cod_disciplina, cod_curso)
)

INSERT INTO disciplina_curso VALUES
(1, 0),
(2, 0),
(2, 1),
(2, 3),
(2, 4),
(3, 0),
(4, 1),
(5, 2),
(6, 2),
(7, 1),
(7, 3),
(8, 1),
(8, 4),
(9, 1),
(9, 3),
(10, 0),
(10, 4)

GO
CREATE FUNCTION fn_tabela_curso(@cod INT)
RETURNS @tabela TABLE(
cod_disc INT,
nome_disc VARCHAR(50),
ch_disc INT,
nome_curso VARCHAR(50))
AS
BEGIN
	DECLARE @cod_curso INT,
			@cod_disc INT,
			@nome_disc VARCHAR(50),
			@ch_disc INT,
			@nome_curso VARCHAR(50)

	SET @nome_curso = (SELECT nome FROM curso WHERE cod = @cod)

	DECLARE cursor_busca CURSOR FOR
		SELECT cod_disciplina, cod_curso FROM disciplina_curso

	OPEN cursor_busca
	FETCH NEXT FROM cursor_busca INTO @cod_disc, @cod_curso
	WHILE @@FETCH_STATUS = 0
		BEGIN
			IF(@cod_curso = @cod)
				BEGIN
					SET @nome_disc = (SELECT nome FROM disciplinas WHERE cod = @cod_disc)
					SET @ch_disc = (SELECT carga_horaria FROM disciplinas WHERE cod = @cod_disc)
					INSERT INTO @tabela VALUES
					(@cod_disc, @nome_disc, @ch_disc, @nome_curso)
				END
			FETCH NEXT FROM cursor_busca INTO @cod_disc, @cod_curso
		END
	CLOSE cursor_busca
	DEALLOCATE cursor_busca
	RETURN
END

SELECT * FROM dbo.fn_tabela_curso(0)
SELECT * FROM dbo.fn_tabela_curso(1)
SELECT * FROM dbo.fn_tabela_curso(2)
SELECT * FROM dbo.fn_tabela_curso(3)
SELECT * FROM dbo.fn_tabela_curso(4)