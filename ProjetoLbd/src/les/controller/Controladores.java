package les.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import les.entity.AlunoDisc;
import les.entity.Avaliacao;
import les.entity.Disciplina;
import les.entity.Falta;
import les.entity.Nota;
import les.entity.TabelaFaltas;
import les.entity.TabelaNotas;
import les.persistence.AlunoDiscDao;
import les.persistence.AvaliacaoDao;
import les.persistence.FaltasDao;
import les.persistence.GenericDao;
import les.persistence.MateriasDao;
import les.persistence.NotasDao;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class Controladores {
	
	@Autowired
	ServletContext context;
	
	@RequestMapping("/")
	public ModelAndView getTelaPrincipal() throws SQLException {
		MateriasDao md = new MateriasDao();
		List<Disciplina> lista = md.buscaListaMateria();
		ModelAndView mv = new ModelAndView("Principal");
		mv.addObject("MateriaLista", lista);
		return mv;
	}
	
	@RequestMapping("/menuInserirFaltas")
	public String getTelaFaltas() {
		return "Falta1";
	}
	
	@RequestMapping("/menuAlterarDisciplina")
	public String teste() {
		return "disciplina1";
	}
	
	@RequestMapping("/tabelaFaltas1")
	public String getTabelaFaltas1() {
		return "TabelaFaltas1";
	}
	
	
	@RequestMapping("/tabelaNotas1")
	public String getTabelaNotas1() {
		return "TabelaNotas1";
	}
	
	@RequestMapping("/tabelaFaltas2")
	public ModelAndView getTabelaFaltas2(@RequestParam(value="disciplina") String disc) throws SQLException {
		ModelAndView mv = new ModelAndView("TabelaFaltas2");
		FaltasDao fd = new FaltasDao();
		List<TabelaFaltas> lista = fd.buscaTabelaFaltas(disc);
		mv.addObject("listaTabelaFaltas", lista);
		mv.addObject("disciplina", disc);
		return mv;
	}
	
	@RequestMapping("/geraRelatorioFaltas")
	public void geraRelatorioFaltas(HttpServletResponse response, @ModelAttribute(value="disciplina") String disc) throws JRException, IOException, SQLException {
		Connection c = new GenericDao().getConnection();
		InputStream jasperStream = context.getResourceAsStream("WEB-INF/report/Faltas_Relatorio.jasper");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("DISCIPLINA", disc);
		
		JasperReport relatorio = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(relatorio, param, c);
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		c.close();
	}
	
	@RequestMapping("/tabelaNotas2")
	public ModelAndView getTabelaNotas2(@RequestParam(value="disciplina") String disc) throws SQLException, FileNotFoundException {
		ModelAndView mv = null;
		mv = new ModelAndView("TabelaNotas2");
		NotasDao nd = new NotasDao();
		List<TabelaNotas> lista = nd.buscaTabelaNotas(disc);
		mv.addObject("listaTabelaNotas", lista);
		mv.addObject("disciplina", disc);
		return mv;
	}
	@RequestMapping("/geraRelatorioNotas")
	public void geraRelatorioNotas(HttpServletResponse response, @ModelAttribute(value="disciplina") String disc) throws JRException, IOException, SQLException {
		Connection c = new GenericDao().getConnection();
		InputStream jasperStream = context.getResourceAsStream("WEB-INF/report/Notas_Relatorio.jasper");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("DISCIPLINA", disc);
		
		JasperReport relatorio = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(relatorio, param, c);
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		c.close();
	}
	
	@PostMapping("/faltas2")
	public ModelAndView getFaltas(@RequestParam(value="disciplina") String disc, @RequestParam(value="dia") int dia) throws SQLException {
		FaltasDao fd = new FaltasDao();
		List<Falta> lista = fd.buscaListaFaltas(disc, dia);
		if(lista.isEmpty() == true) {
			AlunoDiscDao add = new AlunoDiscDao();
			List<AlunoDisc> lista2 = add.buscaListaAlunoDisc(disc);
			for(AlunoDisc ad : lista2) {
				Falta f = new Falta();
				f.setRa_aluno(ad.getRa_aluno());
				f.setCod_disciplina(ad.getCod_disc());
				f.setDia(dia);
				lista.add(f);
			}
		}
		FaltaWrapper fw = new FaltaWrapper();
		fw.setListaFalta(lista);
		ModelAndView mv = new ModelAndView("Falta2");
		mv.addObject("FaltaLista", fw);
		return mv;
	}
	
	@PostMapping("/notas3")
	public ModelAndView mostraNotas(@ModelAttribute(value="nota") Nota n) throws SQLException {
		NotasDao nd = new NotasDao();
		List<Nota> lista = nd.buscaListaNota(n.getCod_disc(), n.getCod_avaliacao());
		if(lista.isEmpty() == true) {
			AlunoDiscDao add = new AlunoDiscDao();
			List<AlunoDisc> lista2 = add.buscaListaAlunoDisc(n.getCod_disc());
			for(AlunoDisc ad : lista2) {
				Nota n1 = new Nota();
				n1.setRa_aluno(ad.getRa_aluno());
				n1.setCod_disc(ad.getCod_disc());
				n1.setCod_avaliacao(n.getCod_avaliacao());
				lista.add(n1);
			}
		}
		NotaWrapper nw = new NotaWrapper();
		nw.setListaNota(lista);
		ModelAndView mv = new ModelAndView("Notas3");
		mv.addObject("NotaLista", nw);
		return mv;
	}
	
	@PostMapping("/salvarFaltas")
	public String salvaFaltas(@ModelAttribute("FaltaLista") FaltaWrapper fw) throws SQLException {
		List<Falta> lista = fw.getListaFalta();
		FaltasDao fd = new FaltasDao();
		for(Falta f : lista) {
			System.out.println(f.getPresenca());
			fd.inserirFalta(f);
		}
		return "Falta1";
	}
	
	@PostMapping("/salvarNotas")
	public String salvaNotas(@ModelAttribute("NotaLista") NotaWrapper fw) throws SQLException {
		List<Nota> lista = fw.getListaNota();
		NotasDao nd = new NotasDao();
		for(Nota n : lista) {
			nd.inserirNota(n);
		}
		return "Notas1";
	}
	
	@PostMapping("/disciplina")
	public ModelAndView getMateria(@RequestParam(value="disciplina") String disc, @RequestParam(value="cmd") String cmd) throws SQLException {
		MateriasDao md = new MateriasDao();
		ModelAndView mv = null;
		String msg = "";
		if("editar".equals(cmd)) {
			Disciplina d = md.buscaDisciplina(disc);
			mv = new ModelAndView("disciplina2");
			mv.addObject("disciplina", d);
		} else {
			Disciplina d = md.buscaDisciplina(disc);
			msg = md.excluirDisciplina(d);
			mv = new ModelAndView("disciplina1");
			mv.addObject("msg", msg);
		}
		return mv;
	}
	
	@PostMapping("/disciplina2")
	public ModelAndView msgErro(@ModelAttribute("disciplina") Disciplina d) throws SQLException{
		MateriasDao md = new MateriasDao();
		String msg = "";
		msg = md.atualizarDisciplina(d);
		ModelAndView mv = new ModelAndView("disciplina1");
		mv.addObject("msg", msg);
		return mv;
	}
	
	@RequestMapping("/notas1")
	public String telaNotas1() {
		return "Notas1";
	}
	
	@PostMapping("/notas2")
	public ModelAndView telasNotas2(@RequestParam(value="disciplina") String disc) throws SQLException{
		AvaliacaoDao ad = new AvaliacaoDao();
		MateriasDao md = new MateriasDao();
		List<Avaliacao> lista = new ArrayList<Avaliacao>();
		if("4203-010".equals(disc) | "4203-020".equals(disc) | "4208-010".equals(disc) | "4226-004".equals(disc)) {
			Avaliacao a = ad.buscaAvaliacao("P1");
			Avaliacao b = ad.buscaAvaliacao("P2");
			Avaliacao c = ad.buscaAvaliacao("T");
			Avaliacao d = ad.buscaAvaliacao("Exame");
			lista.add(a);
			lista.add(b);
			lista.add(c);
			lista.add(d);
		} else if("4213-003".equals(disc) | "4213-013".equals(disc)) {
			Avaliacao a = ad.buscaAvaliacao("P1");
			Avaliacao b = ad.buscaAvaliacao("P2");
			Avaliacao c = ad.buscaAvaliacao("T");
			Avaliacao d = ad.buscaAvaliacao("Pré Exame");
			Avaliacao e = ad.buscaAvaliacao("Exame");
			lista.add(a);
			lista.add(b);
			lista.add(c);
			lista.add(d);
			lista.add(e);
		} else if("4233-005".equals(disc)) {
			Avaliacao a = ad.buscaAvaliacao("P1");
			Avaliacao b = ad.buscaAvaliacao("P2");
			Avaliacao c = ad.buscaAvaliacao("P3");
			Avaliacao d = ad.buscaAvaliacao("Exame");
			lista.add(a);
			lista.add(b);
			lista.add(c);
			lista.add(d);
		} else {
			Avaliacao a = ad.buscaAvaliacao("Monografia Completa");
			Avaliacao b = ad.buscaAvaliacao("Monografia Resumida");
			Avaliacao c = ad.buscaAvaliacao("Exame");
			lista.add(a);
			lista.add(b);
			lista.add(c);
		}
		ModelAndView mv = new ModelAndView("Notas2");
		Nota n = new Nota();
		Disciplina d = md.buscaDisciplina(disc);
		n.setCod_disc(disc);
		mv.addObject("nota", n);
		mv.addObject("listaAvaliacoes", lista);
		mv.addObject("disciplina", d);
		return mv;
	}
	
	
}
