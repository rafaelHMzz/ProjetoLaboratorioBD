package les.persistence;

import java.sql.SQLException;

import les.entity.Avaliacao;

public interface IAvaliacaoDao {
	
	public Avaliacao buscaAvaliacao(String cod) throws SQLException;
}
