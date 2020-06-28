package les.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import les.entity.Avaliacao;

public class AvaliacaoDao implements IAvaliacaoDao{

	Connection c = new GenericDao().getConnection();
	
	@Override
	public Avaliacao buscaAvaliacao(String cod) throws SQLException{
		String sql = "SELECT * FROM avaliacao WHERE cod = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cod);
		ResultSet rs = ps.executeQuery();
		Avaliacao a = new Avaliacao();
		while(rs.next()) {
			a.setCod(rs.getString("cod"));
			a.setTipo(rs.getString("tipo"));
		}
		rs.close();
		ps.close();
		return a;
	}

}
