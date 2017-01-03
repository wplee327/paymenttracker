package application;

import java.net.URL;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import application.Database.Clients;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Pair;

public class FXMLController implements Initializable {
	@FXML
	private TableView clientTableView;
	@FXML
	private TableColumn lastTableColumn;
	@FXML
	private TableColumn firstTableColumn;
	@FXML
	private Label monthLabel;
	@FXML
	private Label yearLabel;
	@FXML
	private MenuItem closeMenuItem;
	@FXML
	private MenuItem aboutMenuItem;
	@FXML
	private Button addclientButton;
	@FXML
	private Button removeclientButton;
	@FXML
	private Button prevmonthButton;
	@FXML
	private Button nextmonthButton;
	@FXML
	private Button prevyearButton;
	@FXML
	private Button nextyearButton;
	@FXML
	private Button unpaidButton;
	@FXML
	private Button pendingButton;
	@FXML
	private Button paidButton;
	@FXML
	private GridPane calendarGridPane;
	private ObservableList<Node> calendarButtons;
	private int edittingIndex = -1;
	private List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December");
	private List<String> statuses = Arrays.asList("Unpaid", "Pending", "Paid");
	private Database trackerDatabase = new Database("paymenttracker.db");

	public FXMLController() {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		calendarButtons = calendarGridPane.getChildren();
		displayCurrentCalendar();
		populateTableView();
		closeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
		aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});
		addclientButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Dialog<Pair<String, String>> addclientDialog = new Dialog<>();
				addclientDialog.setTitle("Add Client");
				addclientDialog.setHeaderText("Add a client");
				addclientDialog.setGraphic(new ImageView(
						FXMLController.class.getClassLoader().getResource("res/1481607218_user.png").toString()));
				ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
				addclientDialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
				GridPane dialogGridPane = new GridPane();
				dialogGridPane.setHgap(10);
				dialogGridPane.setVgap(10);
				dialogGridPane.setPadding(new Insets(20, 150, 10, 10));
				TextField lastTextField = new TextField();
				lastTextField.setPromptText("Last Name");
				TextField firstTextField = new TextField();
				firstTextField.setPromptText("First Name");
				dialogGridPane.add(new Label("Last Name:"), 0, 0);
				dialogGridPane.add(lastTextField, 1, 0);
				dialogGridPane.add(new Label("First Name:"), 0, 1);
				dialogGridPane.add(firstTextField, 1, 1);
				Node addButton = addclientDialog.getDialogPane().lookupButton(addButtonType);
				addButton.setDisable(true);
				BooleanBinding lastBinding = Bindings.createBooleanBinding(() -> {
					return lastTextField.getText().trim().isEmpty();
				}, lastTextField.textProperty());
				BooleanBinding firstBinding = Bindings.createBooleanBinding(() -> {
					return firstTextField.getText().trim().isEmpty();
				}, firstTextField.textProperty());
				addButton.disableProperty().bind(lastBinding.or(firstBinding));
				addclientDialog.getDialogPane().setContent(dialogGridPane);
				Platform.runLater(() -> lastTextField.requestFocus());
				addclientDialog.setResultConverter(dialogButton -> {
					if (dialogButton == addButtonType) {
						return new Pair<>(lastTextField.getText(), firstTextField.getText());
					}
					return null;
				});
				Optional<Pair<String, String>> result = addclientDialog.showAndWait();
				if (result.isPresent()) {
					trackerDatabase.addClient(result.get().getKey(), result.get().getValue());
				}
				populateTableView();
			}
		});
		removeclientButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert removeclientAlert = new Alert(AlertType.CONFIRMATION);
				Clients selectedClient = (Clients) clientTableView.getSelectionModel().getSelectedItem();
				String selectedLastName = selectedClient.getLastName();
				String selectedFirstName = selectedClient.getFirstName();
				removeclientAlert.setTitle("Remove Client");
				removeclientAlert.setHeaderText("Remove a client");
				removeclientAlert.setContentText(
						"Are you sure you want to remove " + selectedFirstName + " " + selectedLastName + "?");
				Optional<ButtonType> result = removeclientAlert.showAndWait();
				if (result.get() == ButtonType.OK) {
					int id = trackerDatabase.getID(selectedLastName, selectedFirstName);
					trackerDatabase.removeClient(id);
				} else {

				}
				populateTableView();
			}
		});
		prevmonthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int month = months.indexOf(monthLabel.getText());
				int year = Integer.parseInt(yearLabel.getText());
				if (edittingIndex != -1) {
					Button tempButton = (Button) calendarButtons.get(7 + edittingIndex);
					tempButton.setTextFill(Color.web("#ffffff"));
					edittingIndex = -1;
				}
				if (month == GregorianCalendar.JANUARY) {
					displayNewCalendar(GregorianCalendar.DECEMBER, year - 1);
				} else {
					displayNewCalendar(month - 1, year);
				}
			}
		});
		nextmonthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int month = months.indexOf(monthLabel.getText());
				int year = Integer.parseInt(yearLabel.getText());
				if (edittingIndex != -1) {
					Button tempButton = (Button) calendarButtons.get(7 + edittingIndex);
					tempButton.setTextFill(Color.web("#ffffff"));
					edittingIndex = -1;
				}
				if (month == GregorianCalendar.DECEMBER) {
					displayNewCalendar(GregorianCalendar.JANUARY, year + 1);
				} else {
					displayNewCalendar(month + 1, year);
				}
			}
		});
		prevyearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int month = months.indexOf(monthLabel.getText());
				int year = Integer.parseInt(yearLabel.getText());
				if (edittingIndex != -1) {
					Button tempButton = (Button) calendarButtons.get(7 + edittingIndex);
					tempButton.setTextFill(Color.web("#ffffff"));
					edittingIndex = -1;
				}
				displayNewCalendar(month, year - 1);
			}
		});
		nextyearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int month = months.indexOf(monthLabel.getText());
				int year = Integer.parseInt(yearLabel.getText());
				if (edittingIndex != -1) {
					Button tempButton = (Button) calendarButtons.get(7 + edittingIndex);
					tempButton.setTextFill(Color.web("#ffffff"));
					edittingIndex = -1;
				}
				displayNewCalendar(month, year + 1);
			}
		});
		unpaidButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changePaymentStatus(0);
			}
		});
		pendingButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changePaymentStatus(1);
			}
		});
		paidButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changePaymentStatus(2);
			}
		});
	}

	public void displayCurrentCalendar() {
		GregorianCalendar realCal = new GregorianCalendar();
		int realMonth = realCal.get(GregorianCalendar.MONTH);
		int realYear = realCal.get(GregorianCalendar.YEAR);
		displayNewCalendar(realMonth, realYear);
	}

	public void displayNewCalendar(int month, int year) {
		GregorianCalendar newCal = new GregorianCalendar(year, month, 1);
		int numDays = newCal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		int monthStart = newCal.get(GregorianCalendar.DAY_OF_WEEK) - 1;
		int date;
		if (monthStart > 0) {
			if (month == GregorianCalendar.JANUARY) {
				newCal = new GregorianCalendar(year - 1, GregorianCalendar.DECEMBER, 1);
			} else {
				newCal = new GregorianCalendar(year, month - 1, 1);
			}
			date = newCal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			for (int idx = 7 + (monthStart - 1); idx > 6; idx--) {
				Button currentButton = (Button) calendarButtons.get(idx);
				currentButton.setText(Integer.toString(date));
				currentButton.setTextFill(Color.web("#212121"));
				date--;
			}
		}
		date = 1;
		for (int idx = (7 + monthStart); idx < (monthStart + numDays + 7); idx++) {
			Button currentButton = (Button) calendarButtons.get(idx);
			currentButton.setText(Integer.toString(date));
			currentButton.setTextFill(Color.web("#FFFFFF"));
			date++;
		}
		date = 1;
		for (int idx = (7 + monthStart + numDays); idx < 49; idx++) {
			Button currentButton = (Button) calendarButtons.get(idx);
			currentButton.setText(Integer.toString(date));
			currentButton.setTextFill(Color.web("#212121"));
			date++;
		}
		monthLabel.setText(months.get(month));
		yearLabel.setText(Integer.toString(year));
		for (int idx = 0; idx < 42; idx++) {
			Button currentButton = (Button) calendarButtons.get(7 + idx);
			currentButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int currentIndex = Integer.parseInt(currentButton.getText());
					if (!currentButton.getTextFill().equals(Color.web("#212121"))) {
						if (edittingIndex == -1) {
							edittingIndex = currentIndex;
							currentButton.setTextFill(Color.web("ff0000"));
						} else if (edittingIndex == currentIndex) {
							edittingIndex = -1;
							currentButton.setTextFill(Color.web("ffffff"));
						}
					}
				}
			});
		}
	}

	public void populateTableView() {
		List<Clients> clientsList = trackerDatabase.getAllClients();
		lastTableColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("lastName"));
		firstTableColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("firstName"));
		clientTableView.getItems().setAll(clientsList);
	}

	public void changePaymentStatus(int status) {
		if (edittingIndex != -1) {
			System.out.println(edittingIndex + " : " + status);
		}
	}
}
