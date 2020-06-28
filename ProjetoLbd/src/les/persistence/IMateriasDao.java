package les.persistence;

import java.sql.SQLException;
import java.util.List;

import les.entity.Disciplina;

public interface IMateriasDao {
	public List<Disciplina> buscaListaMateria() throws SQLException;
}
