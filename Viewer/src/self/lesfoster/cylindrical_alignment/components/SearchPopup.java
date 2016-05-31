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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
	public static final String TABLE_STYLE = "foreground-color: #ffffff; background-color: #000000;";
	public static final String DATE_PICKER_STYLE = "foreground-color: #ffffff; background-color: #000000;";
	public static final String TAXONOMY_SELECTION_STYLE = "foreground-color: #ffffff; background-color: #000000;";
	
	public static final String ID_COLNAME = "FetchId";
	public static final String ID_COLNAME_PRESENTABLE = "Id";
	public static final String DESCRIPTION_COLNAME = "Description";

	private final ServerInteractor serverInteractor = new ServerInteractor();
	private TableView resultsTable;
	
	public SearchPopup() {
		addComponents();
	}
		
	private void addComponents() {
		// Add a results table.
		resultsTable = new TableView();
		resultsTable.setStyle(TABLE_STYLE);
		resultsTable.setEditable(false);
		resultsTable.getSelectionModel().setCellSelectionEnabled(true);
		resultsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		TableColumn descriptionCol = new TableColumn(DESCRIPTION_COLNAME);
        TableColumn idCol = new TableColumn(ID_COLNAME_PRESENTABLE);
		descriptionCol.setCellValueFactory(
				new PropertyValueFactory<>(DESCRIPTION_COLNAME)
		);
		idCol.setCellValueFactory(
				new PropertyValueFactory<>(ID_COLNAME)
		);
		resultsTable.getColumns().addAll(descriptionCol, idCol);
		
		// Add the date-search
		DatePicker datePicker = new DatePicker();
		datePicker.setStyle(DATE_PICKER_STYLE);
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
		speciesDropdown.setStyle(TAXONOMY_SELECTION_STYLE);
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
		resultsTable.setRowFactory(tv -> {
			TableRow<SearchResult> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					SearchResult selectedItemObj = row.getItem();
					if (selectedItemObj instanceof SearchResult) {
						// got the ID.
						String selectedId = ((SearchResult) selectedItemObj).getFetchId();
						serverInteractor.fetch(selectedId);
					} else if (selectedItemObj == null) {
						// Do nothing.
						System.out.println("Somehow selected a null.");
					} else {
						throw new IllegalStateException("Unexpected table content.  Expected search results.  Found " + selectedItemObj.getClass());
					}
				}
			});
			return row;
		});

		// Need to make a new stage and do things the Java FX way.
		Platform.runLater( () -> {
			JFXPanel panel = new JFXPanel();
			final Group world = new Group();
			// Add things to the scene.
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
		resultsTable.getSelectionModel().setCellSelectionEnabled(true);
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
