package br.argus.agenda.fx;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import br.argus.agenda.entidades.Contato;
import br.argus.agenda.repositorios.impl.ContatoRepositorio;
import br.argus.agenda.repositorios.impl.ContatoRepositorioJdbc;
import br.argus.agenda.repositorios.interfaces.AgendaRepositorio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
	private TextField txfEmail;
	@FXML
	private TextField txfTelefone;
	@FXML
	private Button botaoSalvar;
	@FXML
	private Button botaoCancelar;

	private Boolean ehInserir;
	private Contato contatoSelecionado;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.tabelaContatos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		habilitarEdicaoAgenda(false);
		this.tabelaContatos.getSelectionModel().selectedItemProperty()
				.addListener((observador, contatoAntigo, contatoNovo) -> {
					if (contatoNovo != null) {
						txfNome.setText(contatoNovo.getNome());
						txfEmail.setText(contatoNovo.getEmail());
						txfTelefone.setText(contatoNovo.getTelefone());
						this.contatoSelecionado = contatoNovo;
					}
				});
		carregarTabelaContatos();

	}

	public void botaoInserir_Action() {
		this.ehInserir = true;
		this.txfNome.setText("");
		this.txfEmail.setText("");
		this.txfTelefone.setText("");
		habilitarEdicaoAgenda(true);
	}

	public void botaoAlterar_Action() {
		habilitarEdicaoAgenda(true);
		this.ehInserir = false;
		this.txfNome.setText(this.contatoSelecionado.getNome());
		this.txfEmail.setText(this.contatoSelecionado.getEmail());
		this.txfTelefone.setText(this.contatoSelecionado.getTelefone());
	}

	public void botaoExcluir_Action() {
		Alert confirmacao = new Alert(AlertType.CONFIRMATION);
		confirmacao.setTitle("Exclus�o de Contato");
		confirmacao.setHeaderText("Confirma��o para exclus�o do contato.");
		confirmacao.setContentText("Tem certeza de que deseja excluir este contato?");
		Optional<ButtonType> resultadoConfirmacao = confirmacao.showAndWait();
		if (resultadoConfirmacao.isPresent() && resultadoConfirmacao.get() == ButtonType.OK) {
			AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
			repositorioContato.excluir(this.contatoSelecionado);
			carregarTabelaContatos();
			this.tabelaContatos.getSelectionModel().selectFirst();
		}
	}

	public void botaoCancelar_Action() {
		habilitarEdicaoAgenda(false);
		this.tabelaContatos.getSelectionModel().selectFirst();
	}

	public void botaoSalvar_Action() {
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		Contato contato = new Contato();
		contato.setNome(txfNome.getText());
		contato.setEmail(txfEmail.getText());
		contato.setTelefone(txfTelefone.getText());
		if (this.ehInserir) {
			repositorioContato.inserir(contato);
		} else {
			repositorioContato.atualizar(contato);
		}
		habilitarEdicaoAgenda(false);
		carregarTabelaContatos();
		this.tabelaContatos.getSelectionModel().selectFirst();
	}

	private void carregarTabelaContatos() {
		try {
			AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorioJdbc();
			List<Contato> contatos = repositorioContato.selecionar();
			ObservableList<Contato> contatosObservableList = FXCollections.observableArrayList(contatos);
			this.tabelaContatos.getItems().setAll(contatosObservableList);
		} catch (Exception e) {
			Alert mensagemErro = new Alert(AlertType.ERROR);
			mensagemErro.setTitle("Erro!");
			mensagemErro.setHeaderText("Erro no banco de dados.");
			mensagemErro.setContentText("Houve um erro ao obter a lista de contatos: " + e.getMessage());
			mensagemErro.showAndWait();
		}
	}

	private void habilitarEdicaoAgenda(Boolean edicaoEstaHabilitada) {
		this.txfNome.setDisable(!edicaoEstaHabilitada);
		this.txfEmail.setDisable(!edicaoEstaHabilitada);
		this.txfTelefone.setDisable(!edicaoEstaHabilitada);
		this.botaoSalvar.setDisable(!edicaoEstaHabilitada);
		this.botaoCancelar.setDisable(!edicaoEstaHabilitada);
		this.botaoInserir.setDisable(edicaoEstaHabilitada);
		this.botaoAlterar.setDisable(edicaoEstaHabilitada);
		this.botaoExcluir.setDisable(edicaoEstaHabilitada);
		this.tabelaContatos.setDisable(edicaoEstaHabilitada);
	}
}
