package les.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import les.entity.Falta;
import les.entity.TabelaFaltas;

public class FaltasDao implements IFaltasDao{
	
	Connection c = new GenericDao().getConnection();
	
	@Override
	public List<Falta> buscaListaFaltas(String cod_disc, int dia) throws SQLException {
		List<Falta> lista = new ArrayList<Falta>();
		String sql = "SELECT ra_aluno, cod_disciplina, dia, presenca FROM faltas WHERE cod_disciplina = ? AND dia = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cod_disc);
		ps.setInt(2, dia);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Falta f = new Falta();
			f.setRa_aluno(rs.getInt("ra_aluno"));
			f.setCod_disciplina(rs.getString("cod_disciplina"));
			f.setDia(rs.getInt("dia"));
			f.setPresenca(rs.getInt("presenca"));
			lista.add(f);
		}
		rs.close();
		ps.close();
		return lista;
	}
	
	@Override
	public void inserirFalta(Falta f) throws SQLException {
		String sql = "{CALL sp_grava_faltas(?, ?, ?, ?)}";
		CallableStatement cs = c.prepareCall(sql);
		cs.setInt(1, f.getRa_aluno());
		cs.setString(2, f.getCod_disciplina());
		cs.setInt(3, f.getDia());
		cs.setInt(4, f.getPresenca());
		cs.execute();
		cs.close();
	}
	
	public List<TabelaFaltas> buscaTabelaFaltas(String cod_disc) throws SQLException {
		List<TabelaFaltas> lista = new ArrayList<TabelaFaltas>();
		String sql = "SELECT * FROM fn_tabela_faltas(?) ORDER BY nome_aluno";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cod_disc);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			TabelaFaltas tf = new TabelaFaltas();
			tf.setRa_aluno(rs.getInt("ra_aluno"));
			tf.setNome_aluno(rs.getString("nome_aluno"));
			tf.setSem1(rs.getString("sem1"));
			tf.setSem2(rs.getString("sem2"));
			tf.setSem3(rs.getString("sem3"));
			tf.setSem4(rs.getString("sem4"));
			tf.setSem5(rs.getString("sem5"));
			tf.setSem6(rs.getString("sem6"));
			tf.setSem7(rs.getString("sem7"));
			tf.setSem8(rs.getString("sem8"));
			tf.setSem9(rs.getString("sem9"));
			tf.setSem10(rs.getString("sem10"));
			tf.setSem11(rs.getString("sem11"));
			tf.setSem12(rs.getString("sem12"));
			tf.setSem13(rs.getString("sem13"));
			tf.setSem14(rs.getString("sem14"));
			tf.setSem15(rs.getString("sem15"));
			tf.setSem16(rs.getString("sem16"));
			tf.setSem17(rs.getString("sem17"));
			tf.setSem18(rs.getString("sem18"));
			tf.setSem19(rs.getString("sem19"));
			tf.setSem20(rs.getString("sem20"));
			tf.setTotal(rs.getInt("total"));
			lista.add(tf);		
		}
		rs.close();
		ps.close();
		return lista;
	}

}
