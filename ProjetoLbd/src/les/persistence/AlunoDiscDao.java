package les.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import les.entity.AlunoDisc;

public class AlunoDiscDao implements IAlunoDiscDao{
	
	Connection c = new GenericDao().getConnection();
	
	@Override
	public List<AlunoDisc> buscaListaAlunoDisc(String cod_disc) throws SQLException {
		List<AlunoDisc> lista = new ArrayList<AlunoDisc>();
		String sql = "SELECT ra_aluno, cod_disciplina FROM aluno_disc WHERE cod_disciplina = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cod_disc);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			AlunoDisc ad = new AlunoDisc();
			ad.setRa_aluno(rs.getInt("ra_aluno"));
			ad.setCod_disc(rs.getString("cod_disciplina"));
			lista.add(ad);
		}
		rs.close();
		ps.close();
		return lista;
	}

}
