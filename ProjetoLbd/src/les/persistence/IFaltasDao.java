package les.persistence;

import java.sql.SQLException;
import java.util.List;

import les.entity.Falta;
import les.entity.TabelaFaltas;

public interface IFaltasDao {
	public List<Falta> buscaListaFaltas(String cod_disc, int dia) throws SQLException;

	public void inserirFalta(Falta f) throws SQLException;
	
	public List<TabelaFaltas> buscaTabelaFaltas(String cod_disc) throws SQLException;
}
