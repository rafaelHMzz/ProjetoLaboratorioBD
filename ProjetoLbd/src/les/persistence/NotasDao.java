package les.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import les.entity.Nota;
import les.entity.TabelaNotas;

public class NotasDao implements INotasDao{
	
	Connection c = new GenericDao().getConnection();

	@Override
	public List<Nota> buscaListaNota(String cod_disc, String cod_avaliacao) throws SQLException {
		List<Nota> lista = new ArrayList<Nota>();
		String sql = "SELECT * FROM notas WHERE cod_disciplina = ? AND cod_avaliacao = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cod_disc);
		ps.setString(2, cod_avaliacao);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Nota n = new Nota();
			n.setCod_disc(rs.getString("cod_disciplina"));
			n.setCod_avaliacao(rs.getString("cod_avaliacao"));
			n.setRa_aluno(rs.getInt("ra_aluno"));
			n.setNota(rs.getFloat("nota"));
			lista.add(n);
		}
		rs.close();
		ps.close();
		return lista;
	}
	
	@Override
	public void inserirNota(Nota n) throws SQLException {
		String sql = "{CALL sp_grava_notas(?, ?, ?, ?)}";
		CallableStatement cs = c.prepareCall(sql);
		cs.setInt(1, n.getRa_aluno());
		cs.setString(2, n.getCod_disc());
		cs.setString(3, n.getCod_avaliacao());
		cs.setFloat(4, n.getNota());
		System.out.println(n.getNota());
		cs.execute();
		cs.close();
	}
	
	public List<TabelaNotas> buscaTabelaNotas(String cod_disc) throws SQLException {
		List<TabelaNotas> lista = new ArrayList<TabelaNotas>();
		String sql = "SELECT * FROM fn_tabela_notas(?) ORDER BY nome_aluno";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cod_disc);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			TabelaNotas tn = new TabelaNotas();
			tn.setRa_aluno(rs.getInt("ra_aluno"));
			tn.setNome_aluno(rs.getString("nome_aluno"));
			tn.setNota1(rs.getFloat("nota1"));
			tn.setNota2(rs.getFloat("nota2"));
			tn.setNota3(rs.getFloat("nota3"));
			tn.setNota4(rs.getFloat("nota4"));
			tn.setNota5(rs.getFloat("nota5"));
			tn.setMedia_final(rs.getFloat("media"));
			tn.setSituacao(rs.getString("situacao"));
			lista.add(tn);
		}
		rs.close();
		ps.close();
		return lista;
	}

}
