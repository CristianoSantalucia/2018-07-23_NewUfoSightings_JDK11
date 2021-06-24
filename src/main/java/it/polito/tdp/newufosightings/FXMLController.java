package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController
{
	private Model model;

	@FXML private ResourceBundle resources;

	@FXML private URL location;

	@FXML private TextArea txtResult;

	@FXML private TextField txtAnno;

	@FXML private Button btnSelezionaAnno;

	@FXML private ComboBox<String> cmbBoxForma;

	@FXML private Button btnCreaGrafo;

	@FXML private TextField txtT1;

	@FXML private TextField txtAlfa;

	@FXML private Button btnSimula;

	@FXML void doSelezionaAnno(ActionEvent event)
	{
		//controlli input 
		Integer year;
		try
		{
			year = Integer.parseInt(this.txtAnno.getText());
			if(year < 1910 || year > 2014)
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe)
		{
			this.txtResult.appendText("\n\nErrore, inserire un numero corretto");
			return;
		} 
		
		//cliccabili
		this.cmbBoxForma.setDisable(false);
		this.btnCreaGrafo.setDisable(false);
		this.cmbBoxForma.getItems().clear();
		this.cmbBoxForma.getItems().addAll(this.model.getShapes(year));
	}
	
	@FXML void doCreaGrafo(ActionEvent event)
	{
		Integer year;
		try
		{
			year = Integer.parseInt(this.txtAnno.getText());
			if(year < 1910 || year > 2014)
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe)
		{
			this.txtResult.appendText("\n\nErrore, inserire un numero corretto");
			return;
		} 
		
		String shape = this.cmbBoxForma.getValue(); 
		if (shape == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		} 

		//resetto testo
		this.txtResult.clear();
    	this.txtResult.appendText("Crea grafo...\n");

    	//creo grafo
    	this.model.creaGrafo(shape, year);
    	txtResult.appendText(String.format("\nGRAFO CREATO CON:\n#Vertici: %d\n#Archi: %d",
				this.model.getNumVertici(),
				this.model.getNumArchi()));

		//cliccabili
		this.btnSimula.setDisable(false);
		this.txtAlfa.setDisable(false);
		this.txtT1.setDisable(false);
		
		//stampa 
		this.txtResult.appendText("\n\nSTAMPA ARCHI:\n" + this.model.stampaArchi());
	}

	@FXML void doSimula(ActionEvent event)
	{

	}

	@FXML void initialize()
	{
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSelezionaAnno != null
				: "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert cmbBoxForma != null
				: "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnCreaGrafo != null
				: "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

	}

	public void setModel(Model model)
	{
		this.model = model;
	}
}
