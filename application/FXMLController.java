package application;

import java.net.URL;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
	private List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December");

	public FXMLController() {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		calendarButtons = calendarGridPane.getChildren();
		displayCurrentCalendar();
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

			}
		});
		removeclientButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});
		prevmonthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int month = months.indexOf(monthLabel.getText());
				int year = Integer.parseInt(yearLabel.getText());
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
				displayNewCalendar(month, year - 1);
			}
		});
		nextyearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int month = months.indexOf(monthLabel.getText());
				int year = Integer.parseInt(yearLabel.getText());
				displayNewCalendar(month, year + 1);
			}
		});
		unpaidButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});
		pendingButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});
		paidButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

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
	}

	public void changePaymentStatus(int status) {

	}
}
