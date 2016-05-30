/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.components;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javax.swing.JFrame;
import org.openide.windows.WindowManager;
import self.lesfoster.cylindrical_alignment.model.server_interaction.ServerInteractor;
import self.lesfoster.cylindrical_alignment.data_source.SearchResult;

/**
 * Popup to accept the search parameters. Shows form, and allows entry of inputs
 * to search for hits.  Then shows the hits, and allows you to select which
 * one to display.
 *
 * @author Leslie L Foster
 */
public class SearchPopup extends JFrame {
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
				} else if (selectedItemObj == null) {
					// Do nothing.
				} else {
					throw new IllegalStateException("Unexpected table content.  Expected search results.  Found " + selectedItemObj.getClass());
				}
			}
		});		

		// Need to make a new stage and do things the Java FX way.
		Platform.runLater( () -> {
			JFXPanel panel = new JFXPanel();
			final Group world = new Group();
			// Add things to the scene.
/*
			 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
			*/			
			final VBox vbox = new VBox();
			vbox.setSpacing(5);
			vbox.setPadding(new Insets(10,0,0,10));
			vbox.getChildren().addAll(datePicker, speciesDropdown, resultsTable);
			SearchPopup.this.setLayout(new BorderLayout());
			world.getChildren().add(vbox);
			SearchPopup.this.add(panel, BorderLayout.CENTER);
			Scene scene = new Scene(world, this.getWidth(), this.getHeight(), true, SceneAntialiasing.BALANCED);
			panel.setScene(scene);
		});
	}

	private void populateResultsTable(List<SearchResult> searchResults) {
		// Next, place the results into a table: same table
		// as used by all search results.
		ObservableList<SearchResult> resultList = FXCollections.observableArrayList(searchResults);
		resultsTable.setItems(resultList);
	}

	private JFrame getMainWin() {
		JFrame mainFrame = (JFrame) WindowManager.getDefault().getMainWindow();
		return mainFrame;
	}

	public void showFailAlert(Exception ex) {
		ex.printStackTrace();

		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setHeaderText("Search failed");
		alert.setContentText(ex.getMessage());
	}
}
