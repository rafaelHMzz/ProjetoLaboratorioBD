package les.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import les.entity.Disciplina;

public class MateriasDao implements IMateriasDao{
	
	private GenericDao gd = new GenericDao();
	private Connection c = gd.getConnection();
	
	@Override
	public List<Disciplina> buscaListaMateria() throws SQLException{
		List<Disciplina> lista = new ArrayList<Disciplina>();
		String sql = "SELECT * FROM disciplina";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Disciplina m = new Disciplina();
			m.setCod(rs.getString("cod"));
			m.setNome(rs.getString("nome"));
			m.setSigla(rs.getString("sigla"));
			m.setTurno(rs.getString("turno"));
			m.setN_aulas(rs.getInt("num_aulas"));
			lista.add(m);
		}
		rs.close();
		ps.close();
		return lista;
	}
	
	public Disciplina buscaDisciplina(String cod) throws SQLException {
		Disciplina d = new Disciplina();
		String sql = "SELECT * FROM disciplina WHERE cod = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cod);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			d.setCod(rs.getString("cod"));
			d.setNome(rs.getString("nome"));
			d.setSigla(rs.getString("sigla"));
			d.setTurno(rs.getString("turno"));
			d.setN_aulas(rs.getInt("num_aulas"));
		}
		rs.close();
		ps.close();
		return d;
	}
	
	public String excluirDisciplina(Disciplina d) {
		String sql = "DELETE FROM disciplina WHERE cod = ?";
		String msg = "";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, d.getCod());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			msg = e.getMessage();
		}
		return msg;
	}
	
	public String atualizarDisciplina(Disciplina d) {
		String sql = "UPDATE disciplina SET cod = ?, nome = ?, sigla = ?, turno = ?, num_aulas = ?";
		String msg = "";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, d.getCod());
			ps.setString(2, d.getNome());
			ps.setString(3, d.getSigla());
			ps.setString(4, d.getTurno());
			ps.setInt(5, d.getN_aulas());
			ps.execute();
			ps.close();
		} catch(SQLException e) {
			msg = e.getMessage();
		}
		return msg;
	}

}
