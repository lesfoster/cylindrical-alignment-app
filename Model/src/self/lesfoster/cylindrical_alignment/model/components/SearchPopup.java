/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.model.components;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Popup;
import self.lesfoster.cylindrical_alignment.model.server_interaction.ServerInteractor;
import self.lesfoster.cylindrical_alignment.data_source.SearchResult;

/**
 * Popup to accept the search parameters. Shows form, and allows entry of inputs
 * to search for hits.  Then shows the hits, and allows you to select which
 * one to display.
 *
 * @author Leslie L Foster
 */
public class SearchPopup extends Popup {
	public static final String ID_COLNAME = "ID";
	public static final String DESCRIPTION_COLNAME = "Description";

	private final ServerInteractor serverInteractor = new ServerInteractor();
	private TableView resultsTable;
	
	public SearchPopup() {
		addComponents();
	}
		
	private void addComponents() {
		// Add a results table.
		resultsTable = new TableView();
		resultsTable.setEditable(false);
        TableColumn descriptionCol = new TableColumn(DESCRIPTION_COLNAME);
        TableColumn idCol = new TableColumn(ID_COLNAME);
		resultsTable.getColumns().addAll(descriptionCol, idCol);
		descriptionCol.setCellValueFactory(
				new PropertyValueFactory<>(DESCRIPTION_COLNAME)
		);
		idCol.setCellValueFactory(
				new PropertyValueFactory<>(ID_COLNAME)
		);
		
		// Add the date-search
		DatePicker datePicker = new DatePicker();
		datePicker.setOnAction(event ->{
			try {
				LocalDate date = datePicker.getValue();
				List<SearchResult> searchResults = serverInteractor.find(date);				
				populateResultsTable(searchResults);
			} catch (Exception ex) {
				showFailAlert(ex);
			}
		});
		ComboBox speciesDropdown = new ComboBox();
		// Todo: fetch this list off the server.
		ObservableList<String> speciesList = FXCollections.observableArrayList(
				Arrays.asList(
						"Arabidopsis thaliana", 
						"Bos taurus", 
						"Caenorhabditis elegans",
						"Canis lupus familiaris",
						"Escherichia coli", 
						"Felis catus",
						"Homo sapiens", 
						"Mus musculus", 
						"Schizosaccharomyces pombe", 
						"Sus scrofa"
				)
		);
		speciesDropdown.setItems(speciesList);
		
		speciesDropdown.setOnAction(event -> {
			try {
				String species = (String)speciesDropdown.getValue();
				List<SearchResult> searchResults = serverInteractor.findBySpecies(species);				
				populateResultsTable(searchResults);
			} catch (Exception ex) {
				showFailAlert(ex);
			}
		});
		
		// Now that we've seen how the table gets populated, let's tell how
		// the table's search results can trigger actions.
		//todo resultsTable.addEventHandler(EventType.ACTIVATED, null);
		resultsTable.setOnMousePressed( event -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
				Object selectedItemObj = resultsTable.getSelectionModel().getSelectedItem();
				if (selectedItemObj instanceof SearchResult) {
					// got the ID.
					String selectedId = ((SearchResult) selectedItemObj).getFetchId();
					serverInteractor.fetch(selectedId);
				} else {
					throw new IllegalStateException("Unexpected table content.  Expected search results.  Found " + selectedItemObj.getClass());
				}
			}
		});		
//		for (Object columnObj: resultsTable.getColumns()) {
//			if (columnObj instanceof TableColumn) {
//				TableColumn tc = (TableColumn)columnObj;
//				tc.
//			}
//		}

		// todo add the things properly.
		this.getContent().add(datePicker);
		this.getContent().add(speciesDropdown);
	}

	private void populateResultsTable(List<SearchResult> searchResults) {
		// Next, place the results into a table: same table
		// as used by all search results.
		ObservableList<SearchResult> resultList = FXCollections.observableArrayList(searchResults);
		resultsTable.setItems(resultList);
	}

	public void showFailAlert(Exception ex) {
		ex.printStackTrace();

		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setHeaderText("Search failed");
		alert.setContentText(ex.getMessage());
	}
}
