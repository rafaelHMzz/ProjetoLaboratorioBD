package les.persistence;

import java.sql.SQLException;
import java.util.List;

import les.entity.Nota;

public interface INotasDao {
	public List<Nota> buscaListaNota(String cod_disc, String cod_avaliacao) throws SQLException;

	public void inserirNota(Nota n) throws SQLException;
}
