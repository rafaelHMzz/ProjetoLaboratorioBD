package les.entity;

public class Nota {
	private int ra_aluno;
	private String cod_disc;
	private String cod_avaliacao;
	private float nota;
	
	public int getRa_aluno() {
		return ra_aluno;
	}
	public void setRa_aluno(int ra_aluno) {
		this.ra_aluno = ra_aluno;
	}
	public String getCod_disc() {
		return cod_disc;
	}
	public void setCod_disc(String cod_disc) {
		this.cod_disc = cod_disc;
	}
	public String getCod_avaliacao() {
		return cod_avaliacao;
	}
	public void setCod_avaliacao(String cod_avaliacao) {
		this.cod_avaliacao = cod_avaliacao;
	}
	public float getNota() {
		return nota;
	}
	public void setNota(float nota) {
		this.nota = nota;
	}
}
