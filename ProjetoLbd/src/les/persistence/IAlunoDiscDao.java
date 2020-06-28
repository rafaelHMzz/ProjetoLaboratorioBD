package les.persistence;

import java.sql.SQLException;
import java.util.List;

import les.entity.AlunoDisc;

public interface IAlunoDiscDao {
	
	public List<AlunoDisc> buscaListaAlunoDisc(String cod_disc) throws SQLException; 

}
