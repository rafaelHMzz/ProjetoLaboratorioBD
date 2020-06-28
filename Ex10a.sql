CREATE DATABASE ex_10a
GO
USE ex_10a

CREATE TABLE envio(
CPF varchar(20),
NR_LINHA_ARQUIV int,
CD_FILIAL int,
DT_ENVIO datetime,
NR_DDD int,
NR_TELEFONE	varchar(10),
NR_RAMAL varchar(10),
DT_PROCESSAMENT	datetime,
NM_ENDERECO varchar(200),
NR_ENDERECO int,
NM_COMPLEMENTO	varchar(50),
NM_BAIRRO varchar(100),
NR_CEP varchar(10),
NM_CIDADE varchar(100),
NM_UF varchar(2),
)

CREATE TABLE endereço(
CPF varchar(20),
CEP	varchar(10),
PORTA int,
ENDEREÇO varchar(200),
COMPLEMENTO	varchar(100),
BAIRRO	varchar(100),
CIDADE	varchar(100),
UF Varchar(2)
)

create procedure sp_insereenvio
as
declare @cpf as int
declare @cont1 as int
declare @cont2 as int
declare @conttotal as int
set @cpf = 11111
set @cont1 = 1
set @cont2 = 1
set @conttotal = 1
	while @cont1 <= @cont2 and @cont2 < = 100
			begin
				insert into envio (CPF, NR_LINHA_ARQUIV, DT_ENVIO)
				values (cast(@cpf as varchar(20)), @cont1,GETDATE())
				insert into endereço (CPF,PORTA,ENDEREÇO)
				values (@cpf,@conttotal,CAST(@cont2 as varchar(3))+'Rua '+CAST(@conttotal as varchar(5)))
				set @cont1 = @cont1 + 1
				set @conttotal = @conttotal + 1
				if @cont1 > = @cont2
					begin
						set @cont1 = 1
						set @cont2 = @cont2 + 1
						set @cpf = @cpf + 1
					end
	end

exec sp_insereenvio

select * from envio order by CPF,NR_LINHA_ARQUIV asc
select * from endereço order by CPF asc

GO
CREATE PROCEDURE sp_muda_dados
AS
BEGIN
	DECLARE @cpf VARCHAR(20),
			@cont int,
			@cep VARCHAR(10),
			@porta INT,
			@end VARCHAR(200),
			@comp VARCHAR(50),
			@bairro VARCHAR(100),
			@cidade VARCHAR(100),
			@uf VARCHAR(02),
			@linhas INT

			DECLARE cursor_busca CURSOR FOR
				SELECT CPF, CEP, PORTA, ENDEREÇO, COMPLEMENTO, BAIRRO, CIDADE, UF FROM endereço ORDER BY CPF asc
				OPEN cursor_busca
				FETCH NEXT FROM cursor_busca INTO @cpf, @cep, @porta, @end, @comp, @bairro, @cidade, @uf
				WHILE @@FETCH_STATUS = 0
					BEGIN
						SET @linhas = (SELECT COUNT(CPF) FROM envio WHERE CPF = @cpf)
						SET @cont = 1
						WHILE @cont <= @linhas
							BEGIN
								UPDATE envio
								SET NR_CEP = @cep, NR_ENDERECO = @porta, NM_ENDERECO = @end, NM_COMPLEMENTO = @comp, NM_BAIRRO = @bairro, NM_CIDADE = @cidade, NM_UF = @uf
								WHERE CPF = @cpf AND NR_LINHA_ARQUIV = @cont
								SET @cont = @cont + 1
								FETCH NEXT FROM cursor_busca INTO @cpf, @cep, @porta, @end, @comp, @bairro, @cidade, @uf
							END
					END
				CLOSE cursor_busca
				DEALLOCATE cursor_busca
END

EXEC sp_muda_dados