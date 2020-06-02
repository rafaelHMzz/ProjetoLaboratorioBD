CREATE DATABASE Ex08a
GO
USE Ex08a

CREATE TABLE times(
cod INT PRIMARY KEY,
time_nome VARCHAR(20),
sigla VARCHAR(3)
)

CREATE TABLE jogos(
time_a INT,
time_b INT,
gols_a INT,
gols_b INT,
data_hora DATETIME
FOREIGN KEY(time_a) REFERENCES times(cod),
FOREIGN KEY(time_b) REFERENCES times(cod),
CONSTRAINT PK_Jogos PRIMARY KEY(time_a, time_b)
)

CREATE TABLE campeonato(
time_cod INT,
jogos INT,
vitorias INT,
empates INT,
derrotas INT,
gols_p INT,
gols_c INT
FOREIGN KEY(time_cod) REFERENCES times(cod)
PRIMARY KEY(time_cod)
)

GO
CREATE TRIGGER t_inserecamp ON times
FOR INSERT
AS
BEGIN
	DECLARE @cod_time INT

	SET @cod_time = (SELECT cod FROM inserted)

	INSERT INTO campeonato (time_cod) VALUES (@cod_time)
END

INSERT INTO times VALUES
(1, 'Barcelona', 'BAR')

INSERT INTO times VALUES
(2, 'Celta de Vigo', 'CEL')

INSERT INTO times VALUES
(3, 'Málaga', 'MAL')

INSERT INTO times VALUES
(4, 'Real Madrid', 'RMA')

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(1, 2, CONVERT(DATETIME, '22/04/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(1, 3, CONVERT(DATETIME, '29/04/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(1, 4, CONVERT(DATETIME, '06/05/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(2, 1, CONVERT(DATETIME, '25/04/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(2, 3, CONVERT(DATETIME, '02/04/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(2, 4, CONVERT(DATETIME, '09/05/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(3, 1, CONVERT(DATETIME, '12/05/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(3, 2, CONVERT(DATETIME, '15/05/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(3, 4, CONVERT(DATETIME, '18/05/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(4, 1, CONVERT(DATETIME, '23/05/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(4, 2, CONVERT(DATETIME, '27/05/2013 15:00', 103))

INSERT INTO jogos (time_a, time_b, data_hora) VALUES
(4, 3, CONVERT(DATETIME, '31/05/2013 15:00', 103))

GO
CREATE TRIGGER t_atualizacamp ON jogos
FOR UPDATE
AS
BEGIN
	DECLARE @gols_a INT,
			@gols_b INT,
			@time_a INT,
			@time_b INT

	SET @gols_a = (SELECT gols_a FROM INSERTED)
	SET @gols_b = (SELECT gols_b FROM INSERTED)
	SET @time_a = (SELECT time_a FROM INSERTED)
	SET @time_b = (SELECT time_b FROM INSERTED)

	IF((SELECT jogos FROM campeonato WHERE time_cod = @time_a) IS NULL)
		BEGIN
			UPDATE campeonato
			SET jogos = 0, vitorias = 0, empates = 0, derrotas = 0, gols_p = 0, gols_c = 0
			WHERE time_cod = @time_a
		END

	IF((SELECT jogos FROM campeonato WHERE time_cod = @time_b) IS NULL)
		BEGIN
			UPDATE campeonato
			SET jogos = 0, vitorias = 0, empates = 0, derrotas = 0, gols_p = 0, gols_c = 0
			WHERE time_cod = @time_b
		END

	IF(@gols_a > @gols_b)
		BEGIN
			UPDATE campeonato
			SET jogos = jogos + 1, vitorias = vitorias + 1, gols_p = gols_p + @gols_a, gols_c = gols_c + @gols_b
			WHERE time_cod = @time_a

			UPDATE campeonato
			SET jogos = jogos + 1, derrotas = derrotas + 1, gols_p = gols_p + @gols_b, gols_c = gols_c + @gols_a
			WHERE time_cod = @time_b
		END
	ELSE
		BEGIN
			IF(@gols_b > @gols_a)
				BEGIN
					UPDATE campeonato
					SET jogos = jogos + 1, vitorias = vitorias + 1, gols_p = gols_p + @gols_a, gols_c = gols_c + @gols_b
					WHERE time_cod = @time_b

					UPDATE campeonato
					SET jogos = jogos + 1, derrotas = derrotas + 1, gols_p = gols_p + @gols_b, gols_c = gols_c + @gols_a
					WHERE time_cod = @time_a
				END
			ELSE
				BEGIN
					IF(@gols_a = @gols_b)
						BEGIN
							UPDATE campeonato
							SET jogos = jogos + 1, empates = empates + 1, gols_p = gols_p + @gols_a, gols_c = gols_c + @gols_b
							WHERE time_cod = @time_a

							UPDATE campeonato
							SET jogos = jogos + 1, empates = empates + 1, gols_p = gols_p + @gols_b, gols_c = gols_c + @gols_a
							WHERE time_cod = @time_b
						END
				END
		END
END

GO
CREATE FUNCTION fn_campeonato (@time_cod INT)
RETURNS @tabela TABLE (
sigla VARCHAR(03),
jogos INT,
vitorias INT,
empates INT,
derrotas INT,
gols_p INT,
gols_c INT,
pontos INT
)
AS
BEGIN
	DECLARE @sigla VARCHAR(03),
			@jogos INT,
			@vitorias INT,
			@empates INT,
			@derrotas INT,
			@gols_p INT,
			@gols_c INT,
			@pontos INT


	IF((SELECT jogos FROM campeonato WHERE time_cod = @time_cod) IS NULL)
		BEGIN
			SET @sigla = (SELECT time_nome FROM times WHERE cod = @time_cod)
			INSERT INTO @tabela VALUES
			(@sigla, 0, 0, 0, 0, 0, 0, 0)
		END
	ELSE
		BEGIN
			SET @sigla = (SELECT time_nome FROM times WHERE cod = @time_cod)
			SET @jogos = (SELECT jogos FROM campeonato WHERE time_cod = @time_cod)
			SET @vitorias = (SELECT vitorias FROM campeonato WHERE time_cod = @time_cod)
			SET @empates = (SELECT empates FROM campeonato WHERE time_cod = @time_cod)
			SET @derrotas = (SELECT derrotas FROM campeonato WHERE time_cod = @time_cod)
			SET @gols_p = (SELECT gols_p FROM campeonato WHERE time_cod = @time_cod)
			SET @gols_c = (SELECT gols_c FROM campeonato WHERE time_cod = @time_cod)
			SET @pontos = @empates + (@vitorias * 3)

			INSERT INTO @tabela VALUES
			(UPPER(@sigla), @jogos, @vitorias, @empates, @derrotas, @gols_p, @gols_c, @pontos)
		END		
RETURN
END

UPDATE jogos
SET gols_a = 3, gols_b = 1
WHERE time_a = 1 AND time_b = 2

UPDATE jogos
SET gols_a = 4, gols_b = 4
WHERE time_a = 3 AND time_b = 4

UPDATE jogos
SET gols_a = 1, gols_b = 1
WHERE time_a = 1 AND time_b = 3

SELECT * FROM dbo.fn_campeonato(1)
SELECT * FROM dbo.fn_campeonato(2)
SELECT * FROM dbo.fn_campeonato(3)
SELECT * FROM dbo.fn_campeonato(4)

SELECT * FROM times
SELECT * FROM jogos
SELECT * FROM campeonato






