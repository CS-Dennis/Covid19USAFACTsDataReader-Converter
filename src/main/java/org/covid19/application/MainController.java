package org.covid19.application;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {

	FileChooser dataSourceFileChooser = new FileChooser();

	@FXML
	private Label statusLabel = new Label();
	
	@FXML
	private Button exportButton = new Button();

	@FXML
	private ChoiceBox<String> dateChoiceBox = new ChoiceBox<String>();

	@FXML
	private DatePicker datePicker = new DatePicker();
	
	@FXML
	private ProgressBar progressBar;

	private String selectedDataPath;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
//		init dateChoiceBox
		this.dateChoiceBox.getItems().addAll("All Dates", "Choose One Date");
		this.dateChoiceBox.setValue("All Dates");

//		init datepicker as today's date
//		this.datePicker.getEditor().setText();
		this.datePicker.setValue(LocalDate.now());
		
		this.progressBar.setProgress(0d);
	}

	public void openDataPage(ActionEvent event) {
		try {
			Desktop.getDesktop()
					.browse(new URI("https://usafacts.org/visualizations/coronavirus-covid-19-spread-map/"));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	public void openDataDownloadLink(ActionEvent event) {
		try {
			Desktop.getDesktop().browse(new URI(
					"https://usafactsstatic.blob.core.windows.net/public/data/covid-19/covid_confirmed_usafacts.csv"));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	public void selectDataFile(ActionEvent event) {
		dataSourceFileChooser.setTitle("Please choose the csv file you have or just downloaded from usafacts.org");
		dataSourceFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
		File selectedDataFile = dataSourceFileChooser.showOpenDialog(Stage.getWindows().get(0));

		if (selectedDataFile != null) {
			this.selectedDataPath = selectedDataFile.getAbsolutePath();
			this.statusLabel.setText("Data selected: " + selectedDataFile.getAbsolutePath());
		}
	}

	public void dateChoiceBoxSelection(ActionEvent event) {
		String selection = this.dateChoiceBox.getValue();

		if (selection.equals("All Dates")) {
			this.datePicker.setDisable(true);
		} else {
			this.datePicker.setDisable(false);
		}
	}

	public void exportData(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);

		alert.setTitle("No Data File Selected.");
		alert.setHeaderText("No Data File Selected.");
		alert.setContentText(
				"No Data File Selected. Please download the date file on usafacts.org and select the data file once downloaded.");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		boolean taskFinished = false;

		if (this.selectedDataPath != null) {
			FileChooser saveFileChooser = new FileChooser();
			saveFileChooser.setTitle("Please name the exported CSV file.");
			saveFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));

			try {
//				check if date choicebox is all dates
				if (this.dateChoiceBox.getValue().equals("All Dates")) {
					File savedFile = saveFileChooser.showSaveDialog(Stage.getWindows().get(0));
					
//						task defined for threading: processing data file and output it in a CSV file 
						Task<Void> task = new Task<Void>() {
							
							@Override
							protected Void call() throws Exception {
								// TODO Auto-generated method stub
								try {
									savedFile.createNewFile();
									
									BufferedWriter bw = new BufferedWriter(new FileWriter(savedFile));
									CovidDataReader covidDateReader = new CovidDataReader(selectedDataPath);
									ArrayList<String> resultList = covidDateReader.readDataAllDates();
									
									for (String line : resultList) {
										bw.write(line);
									}

									bw.close();
									
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								return null;
							}
							
							@Override
							protected void succeeded() {
								// TODO Auto-generated method stub
								super.succeeded();
								
								progressBar.progressProperty().unbind();
								progressBar.setProgress(100);
								
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("The US Covid 19 Data by State Has Been Successfully Exported.");
								alert.setHeaderText("The US Covid 19 Data by State Has Been Successfully Exported.");
								alert.setContentText("The US Covid 19 Data by State Has Been Successfully Exported.");
								alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
								alert.show();
							}
						};
						
						progressBar.progressProperty().bind(task.progressProperty());
						Thread thread1 = new Thread(task);
						thread1.start();

				}
//				if date choicebox is select one date
				else {
					File savedFile = saveFileChooser.showSaveDialog(Stage.getWindows().get(0));
					
					CovidDataReader covidDataReader = new CovidDataReader(this.selectedDataPath);

					DateTimeFormatter dft = DateTimeFormatter.ofPattern("M/d/yyyy");
					String selectedDate = this.datePicker.getValue().format(dft);
					ArrayList<String> resultList = covidDataReader.readDataByDate(selectedDate);
					
					try {
						savedFile.createNewFile();
						
						Task<Void> task = new Task<Void>() {
							
							@Override
							protected Void call() throws Exception {
								// TODO Auto-generated method stub
								resultList.add(0, "id,State,stateFIPS,"+selectedDate);
								BufferedWriter bw = new BufferedWriter(new FileWriter(savedFile));
								for (int i = 0; i < resultList.size(); i++) {
									resultList.set(i, resultList.get(i)+"\n"); 
								}
								
								for (String line : resultList) {
									bw.write(line);
								}
								bw.close();
								return null;
							}
							
							@Override
							protected void succeeded() {
								// TODO Auto-generated method stub
								super.succeeded();
								progressBar.progressProperty().unbind();
								progressBar.setProgress(100);
								
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("The US Covid 19 Data by State Has Been Successfully Exported.");
								alert.setHeaderText("The US Covid 19 Data by State Has Been Successfully Exported.");
								alert.setContentText("The US Covid 19 Data by State Has Been Successfully Exported.");
								alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
								alert.show();
							}
						};
						progressBar.progressProperty().bind(task.progressProperty());
						Thread thread = new Thread(task);
						thread.start();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				alert.show();
			}
//			if selected date doesn't exist
			catch (ArrayIndexOutOfBoundsException e) {
				alert.setTitle("Error");
				alert.setHeaderText("The data file doesn't contain data based on your selected date.");
				alert.setContentText(
						"The data file doesn't contain data based on your selected date. Please choose another date.");
				alert.show();
			}

		} else {

			alert.show();
		}

	}
	
	public void readMe(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Read Me");
		alert.setHeaderText("Covid 19 Confirmed Cases By State Data Reader Ver. 1.0");
		alert.setContentText("This application can convert the Covid 19 confirmed cases by county and state date from USAFACTS.ORG in to an independent CSV file that consolidate all confirmed cases of all counties by each state plus D.C..\n"
				+ "You can convert the data based on all dates that are avaible in the original data file or by a specific date based on your selection.\n"
				+ "The application provides a direct download link to the Covid 19 U.S. confirmed cases by county and state provided by USAFACTS.ORG\n"
				+ "It also provides a link to the USAFACTS.ORG official page for the user to download.\n\n"
				+ "Application Developer: Dennis Ye\n"
				+ "Email: ynyz2003@gmail.com");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
	}
}
