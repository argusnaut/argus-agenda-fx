package br.argus.agenda.fx;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import br.argus.agenda.entidades.Contato;
import br.argus.agenda.repositorios.impl.ContatoRepositorio;
import br.argus.agenda.repositorios.interfaces.AgendaRepositorio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController implements Initializable {

	@FXML
	private TableView<Contato> tabelaContatos;
	@FXML
	private Button botaoInserir;
	@FXML
	private Button botaoAlterar;
	@FXML
	private Button botaoExcluir;
	@FXML
	private TextField txfNome;
	@FXML
	private TextField txfIdade;
	@FXML
	private TextField txfTelefone;
	@FXML
	private Button botaoSalvar;
	@FXML
	private Button botaoCancelar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.tabelaContatos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		carregarTabelaContatos();
		
	}

	private void carregarTabelaContatos() {
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		List<Contato> contatos = repositorioContato.selecionar();
		// Teste de vínculo com a tabela
		if (contatos.isEmpty()) {
			Contato contato = new Contato();
			contato.setNome("Eu");
			contato.setIdade(12);
			contato.setTelefone(123456);
			contatos.add(contato);
		}
		ObservableList<Contato> contatosObservableList = FXCollections.observableArrayList(contatos);
		this.tabelaContatos.getItems().setAll(contatosObservableList);
	}
}
