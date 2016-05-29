/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.model.components;

import java.time.LocalDate;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
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
	private ServerInteractor serverInteractor;
	
	public SearchPopup() {
		serverInteractor = new ServerInteractor();
		addComponents();
	}
		
	private void addComponents() {
		// Add the date-search
		DatePicker datePicker = new DatePicker();
		datePicker.setOnAction(event ->{
			try {
				LocalDate date = datePicker.getValue();
				List<SearchResult> searchResults = serverInteractor.find(date);
				
				// Next, place the results into a table: same table
				// as used by all search results.
			} catch (Exception ex) {
				ex.printStackTrace();
				showFailAlert(ex);
			}
		});
		this.getContent().add(datePicker);
	}

	public void showFailAlert(Exception ex) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setHeaderText("Search failed");
		alert.setContentText(ex.getMessage());
	}
}
