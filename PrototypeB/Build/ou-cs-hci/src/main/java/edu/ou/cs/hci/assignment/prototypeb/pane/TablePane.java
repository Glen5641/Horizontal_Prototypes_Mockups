//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Sun Feb 24 19:56:48 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
// 20190220 [weaver]:	Adapted from swingmvc to fxmvc.
// 20190305 [glenn]:	Prototype B modifications
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypeb.pane;

import java.util.Calendar;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import edu.ou.cs.hci.assignment.prototypeb.Controller;

//******************************************************************************

/**
 * The <CODE>TablePane</CODE> class.
 *
 * @author Chris Weaver
 * @version %I%, %G%
 */
public final class TablePane extends AbstractPane {
	// **********************************************************************
	// Private Class Members
	// **********************************************************************

	private static final String NAME = "Table";
	private static final String HINT = "Table View for Movie Information";
	private static final String absPath = "edu/ou/cs/hci/resources/example/fx/icon/";

	// **********************************************************************
	// Private Class Members (Layout)
	// **********************************************************************

	private static final double W = 32; // Item icon width
	private static final double H = W * 1.5; // Item icon height

	private static final Insets PADDING = new Insets(40.0, 20.0, 40.0, 20.0);

	// **********************************************************************
	// Private Class Members (Effects)
	// **********************************************************************

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// Data
	private List<List<String>> data;
	ObservableList<Record> records;
	private static GridPane infoGrid = new GridPane();
	private static Image infoImg;
	private static Label infoName;
	private Hyperlink documentsLbl;
	private Hyperlink favoritesLbl;
	private Hyperlink redLbl;
	private Hyperlink orangeLbl;
	private Hyperlink yellowLbl;
	private Hyperlink greenLbl;
	private Hyperlink blueLbl;
	private Hyperlink purpleLbl;
	private Hyperlink greyLbl;
	private Hyperlink allTagsLbl;

	// Layout
	private static SplitPane splitPane;
	private StackPane lpane;
	private StackPane rpane;
	private BorderPane base;
	private TableView<Record> table;
	private SelectionModel<Record> smodel;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	public TablePane(Controller controller) {
		super(controller, NAME, HINT);

		setBase(buildPane());
	}

	// **********************************************************************
	// Public Methods (Controller)
	// **********************************************************************

	// The controller calls this method when it adds a view.
	// Set up the nodes in the view with data accessed through the controller.
	public void initialize() {
		smodel.selectedIndexProperty().addListener(this::changeIndex);

		int index = (Integer) controller.get("itemIndex");

		smodel.select(index);
	}

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void terminate() {
		smodel.selectedIndexProperty().removeListener(this::changeIndex);
	}

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void update(String key, Object value) {
		if ("itemIndex".equals(key)) {
			int index = (Integer) value;
			smodel.select(index);

			// Wipe the info grid for repopulation
			infoGrid.getChildren().clear();

			// Grab the image of the selected movie and add to grid
			infoImg = new Image(absPath + records.get(smodel.getSelectedIndex()).getIcon(), W * 5, H * 5, false, false);
			StackPane imGpane = new StackPane(new ImageView(infoImg));
			infoGrid.add(imGpane, 0, 0, 2, 2);

			// Grab name of movie and add it to the info grid
			infoName = new Label(records.get(smodel.getSelectedIndex()).getName());
			infoName.setStyle("-fx-font: 24 arial;");
			infoGrid.add(infoName, 0, 2, 2, 2);

			// Grab created time and add it to the info grid
			infoGrid.add(new Label("Created"), 0, 4);
			infoGrid.add(new Label(records.get(smodel.getSelectedIndex()).getDate()), 1, 4);

			// Grab the modified time which is created or vise versa and add to grid
			infoGrid.add(new Label("Modified"), 0, 5);
			infoGrid.add(new Label(records.get(smodel.getSelectedIndex()).getDate()), 1, 5);

			// Grab the current time and add it to last opened portion of info grid
			Label dimLbl = new Label("Last Opened");
			dimLbl.setPrefWidth(180);
			infoGrid.add(dimLbl, 0, 6);
			Calendar calendar = Calendar.getInstance();
			StringBuilder sb = new StringBuilder("Today, ");
			int hours = (calendar.get(Calendar.HOUR) == 0) ? 12 : calendar.get(Calendar.HOUR);
			int minutes = calendar.get(Calendar.MINUTE);
			sb.append(hours + ":" + minutes);
			if (calendar.get(Calendar.HOUR_OF_DAY) > 12)
				sb.append(" PM");
			else
				sb.append(" AM");
			infoGrid.add(new Label(sb.toString()), 1, 6);

			// Grab the dimensions of the movie poster and add to info grid
			infoGrid.add(new Label("Dimensions"), 0, 7);
			infoGrid.add(new Label(Integer.toString((int) (W * 5)) + " x " + Integer.toString((int) (H * 5))), 1, 7);
		}

	}

	// **********************************************************************
	// Private Methods (Layout)
	// **********************************************************************

	private Pane buildPane() {

		// Populate data from text file
		data = loadFXData("list-movies-plus.txt");

		// Transfer the data into an ObservableList to use as the table model
		records = FXCollections.observableArrayList();

		// Construct records for each datum
		for (List<String> item : data) {
			records.add(new Record(item.get(0), item.get(1), item.get(2), Integer.parseInt(item.get(3)), item.get(4)));
		}

		// Create a column for movie titles
		TableColumn<Record, String> nameColumn = new TableColumn<Record, String>("Title");
		nameColumn.setEditable(false);
		nameColumn.setPrefWidth(250);
		nameColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("name"));
		nameColumn.setCellFactory(new NameCellFactory());

		// Create a column for movie posters
		TableColumn<Record, String> iconColumn = new TableColumn<Record, String>("Poster");
		iconColumn.setEditable(false);
		iconColumn.setPrefWidth(W + 16.0);
		iconColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("icon"));
		iconColumn.setCellFactory(new IconCellFactory());

		// Create a column for dates
		TableColumn<Record, String> dateColumn = new TableColumn<Record, String>("Date Modified");
		dateColumn.setEditable(false);
		dateColumn.setPrefWidth(200);
		dateColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("date"));
		dateColumn.setCellFactory(new dateCellFactory());

		// Create a column for size
		TableColumn<Record, String> sizeColumn = new TableColumn<Record, String>("Size");
		sizeColumn.setEditable(false);
		sizeColumn.setPrefWidth(50);
		sizeColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("Size"));
		sizeColumn.setCellFactory(new sizeCellFactory());

		// Create a column for tags
		TableColumn<Record, String> tagColumn = new TableColumn<Record, String>("Tag");
		tagColumn.setEditable(false);
		tagColumn.setPrefWidth(50);
		tagColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("fav"));
		tagColumn.setCellFactory(new tagCellFactory());

		// Create the table from the columns
		table = new TableView<Record>();
		smodel = table.getSelectionModel();
		table.setEditable(false);
		table.setPlaceholder(new Text("No Data!"));
		table.getColumns().add(nameColumn);
		table.getColumns().add(iconColumn);
		table.getColumns().add(dateColumn);
		table.getColumns().add(sizeColumn);
		table.getColumns().add(tagColumn);

		// Create a split pane to share space between the cover pane and table
		splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.VERTICAL);
		splitPane.setDividerPosition(0, 0.1); // Put divider at 50% T-to-B

		// For cover pane
		Label tlabel = new Label("this space intentionally left blank");
		tlabel.setPadding(PADDING);

		// Add the content panes to the super panes
		splitPane.getItems().add(tlabel);
		splitPane.getItems().add(table);
		lpane = buildAccordion();
		rpane = buildGrid();
		rpane.setPrefWidth(400);

		// Create the FX base for view
		base = new BorderPane(splitPane, null, rpane, null, lpane);

		// Return the base
		return base;
	}

	// **********************************************************************
	// Private Methods (Change Handlers)
	// **********************************************************************

	private void changeIndex(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		int index = (Integer) newValue;

		controller.set("itemIndex", index);

	}

	// **********************************************************************
	// Inner Classes (Record Object)
	// **********************************************************************

	public static final class Record {
		private final SimpleStringProperty name;
		private final SimpleStringProperty icon;
		private final SimpleStringProperty date;
		private final SimpleStringProperty size;
		private final SimpleStringProperty fav;

		public Record(String name, String icon, String date, int size, String fav) {

			// Parse the content from the text file
			int timehour = Integer.parseInt(date.substring(8, 10));
			int hour = (timehour < 12) ? timehour : timehour / 1200;
			hour = (hour == 0) ? 12 : hour;
			String ampm = (timehour < 12) ? "AM" : "PM";
			int min = Integer.parseInt(date.substring(10, 12));
			size = size / 1000;
			StringBuilder sb = new StringBuilder(date.substring(0, 4) + ".");
			sb.append(date.substring(4, 6) + ".");
			sb.append(date.substring(6, 8) + " ");
			sb.append(hour + ":");
			sb.append(min + " ");
			sb.append(ampm);

			this.name = new SimpleStringProperty(name);
			this.icon = new SimpleStringProperty(icon);
			this.date = new SimpleStringProperty(sb.toString());
			this.size = new SimpleStringProperty(String.valueOf(size) + " kb");
			this.fav = new SimpleStringProperty(fav);
		}

		public String getName() {
			return name.get();
		}

		public void setName(String v) {
			this.name.set(v);
		}

		public String getIcon() {
			return icon.get();
		}

		public void setIcon(String v) {
			this.icon.set(v);
		}

		public String getDate() {
			return date.get();
		}

		public void setDate(String v) {
			this.date.set(v);
		}

		public String getSize() {
			return size.get();
		}

		public void setSize(String v) {
			this.size.set(v);
		}

		public String getFav() {
			return fav.get();
		}

		public void setFav(String v) {
			this.fav.set(v);
		}
	}

	// **********************************************************************
	// Inner Classes (Cell Factories)
	// **********************************************************************

	private static final class NameCellFactory
			implements Callback<TableColumn<Record, String>, TableCell<Record, String>> {
		public TableCell<Record, String> call(TableColumn<Record, String> v) {
			return new NameCell();
		}
	}

	private static final class IconCellFactory
			implements Callback<TableColumn<Record, String>, TableCell<Record, String>> {
		public TableCell<Record, String> call(TableColumn<Record, String> v) {
			return new IconCell();
		}
	}

	private static final class dateCellFactory
			implements Callback<TableColumn<Record, String>, TableCell<Record, String>> {
		public TableCell<Record, String> call(TableColumn<Record, String> v) {
			return new dateCell();
		}
	}

	private static final class sizeCellFactory
			implements Callback<TableColumn<Record, String>, TableCell<Record, String>> {
		public TableCell<Record, String> call(TableColumn<Record, String> v) {
			return new sizeCell();
		}
	}

	private static final class tagCellFactory
			implements Callback<TableColumn<Record, String>, TableCell<Record, String>> {
		public TableCell<Record, String> call(TableColumn<Record, String> v) {
			return new tagCell();
		}
	}

	private static final class NameCell extends TableCell<Record, String> {
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty); // Prepare for setup

			if (empty || (item == null)) // Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			setText(item);
			setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
		}
	}

	private static final class IconCell extends TableCell<Record, String> {
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty); // Prepare for setup

			if (empty || (item == null)) // Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			ImageView icon = createFXIcon(item, W, H);

			setText(null);
			setGraphic(icon);
		}
	}

	private static final class dateCell extends TableCell<Record, String> {
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty); // Prepare for setup

			if (empty || (item == null)) // Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			setText(item);
			setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
		}
	}

	private static final class sizeCell extends TableCell<Record, String> {
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty); // Prepare for setup

			if (empty || (item == null)) // Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			setText(item);
			setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
		}
	}

	private static final class tagCell extends TableCell<Record, String> {

		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty); // Prepare for setup

			if (empty || (item == null)) // Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			// Build the absolute path to the swing icons
			StringBuilder imgString = new StringBuilder("edu/ou/cs/hci/resources/example/swing/icon/");
			imgString.append(item + ".png");
			System.out.println(imgString.toString());
			Image iconImg = new Image(imgString.toString(), 15, 15, false, false);
			ImageView icon = new ImageView(iconImg);

			setText(null);
			setGraphic(icon);
		}
	}

	private StackPane buildAccordion() {

		// Size for .png's
		int size = 15;

		// Create new accordion
		Accordion accordion = new Accordion();

		// Grab all images for accordion
		Image allMyFilesImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/files.png", size, size, false,
				false);
		Image dropboxImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/dropbox.png", size, size, false,
				false);
		Image applicationsImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/application.png", size, size,
				false, false);
		Image desktopImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/desktop.png", size, size, false,
				false);
		Image documentsImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/documents.png", size, size, false,
				false);
		Image downloadsImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/download.png", size, size, false,
				false);
		Image favoritesImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/favorite.png", size, size, false,
				false);
		Image redImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/red.png", size, size, false, false);
		Image orangeImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/orange.png", size, size, false, false);
		Image yellowImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/yellow.png", size, size, false, false);
		Image greenImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/green.png", size, size, false, false);
		Image blueImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/blue.png", size, size, false, false);
		Image purpleImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/purple.png", size, size, false, false);
		Image greyImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/black.png", size, size, false, false);
		Image allTagsImg = new Image("edu/ou/cs/hci/resources/example/swing/icon/black.png", size, size, false, false);

		ColorAdjust colorAdjust = new ColorAdjust();
	    colorAdjust.setBrightness(.6);
	    ImageView grey = new ImageView(greyImg);
	    grey.setEffect(colorAdjust);
		
		// Create Favorites and Tags Hyperlinks for accordion
		Hyperlink allMyFilesLbl = new Hyperlink("All My Files");
		Hyperlink dropboxLbl = new Hyperlink("Dropbox");
		Hyperlink applicationsLbl = new Hyperlink("Applications");
		Hyperlink desktopLbl = new Hyperlink("Desktop");
		documentsLbl = new Hyperlink("Documents");
		Hyperlink downloadsLbl = new Hyperlink("Downloads");
		favoritesLbl = new Hyperlink("Favorites");
		redLbl = new Hyperlink("Red");
		orangeLbl = new Hyperlink("Orange");
		yellowLbl = new Hyperlink("Yellow");
		greenLbl = new Hyperlink("Green");
		blueLbl = new Hyperlink("Blue");
		purpleLbl = new Hyperlink("Purple");
		greyLbl = new Hyperlink("Grey");
		allTagsLbl = new Hyperlink("All Tags...");

		// Remove the borders of all links
		allMyFilesLbl.setBorder(Border.EMPTY);
		dropboxLbl.setBorder(Border.EMPTY);
		applicationsLbl.setBorder(Border.EMPTY);
		desktopLbl.setBorder(Border.EMPTY);
		documentsLbl.setBorder(Border.EMPTY);
		downloadsLbl.setBorder(Border.EMPTY);
		favoritesLbl.setBorder(Border.EMPTY);
		redLbl.setBorder(Border.EMPTY);
		orangeLbl.setBorder(Border.EMPTY);
		yellowLbl.setBorder(Border.EMPTY);
		greenLbl.setBorder(Border.EMPTY);
		blueLbl.setBorder(Border.EMPTY);
		purpleLbl.setBorder(Border.EMPTY);
		greyLbl.setBorder(Border.EMPTY);
		allTagsLbl.setBorder(Border.EMPTY);

		// Set all action events
		documentsLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
		favoritesLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
		redLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
		orangeLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
		yellowLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
		greenLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
		blueLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
		purpleLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
		greyLbl.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);

		// Create favorites grid
		GridPane favoritesGrid = new GridPane();
		favoritesGrid.setVgap(4);
		favoritesGrid.setPadding(new Insets(0, 80, 30, 20));
		favoritesGrid.add(new ImageView(allMyFilesImg), 0, 0);
		favoritesGrid.add(allMyFilesLbl, 1, 0);
		favoritesGrid.add(new ImageView(dropboxImg), 0, 1);
		favoritesGrid.add(dropboxLbl, 1, 1);
		favoritesGrid.add(new ImageView(applicationsImg), 0, 2);
		favoritesGrid.add(applicationsLbl, 1, 2);
		favoritesGrid.add(new ImageView(desktopImg), 0, 3);
		favoritesGrid.add(desktopLbl, 1, 3);
		favoritesGrid.add(new ImageView(documentsImg), 0, 4);
		favoritesGrid.add(documentsLbl, 1, 4);
		favoritesGrid.add(new ImageView(downloadsImg), 0, 5);
		favoritesGrid.add(downloadsLbl, 1, 5);
		favoritesGrid.add(new ImageView(favoritesImg), 0, 6);
		favoritesGrid.add(favoritesLbl, 1, 6);

		// Create tags grid
		GridPane tagsGrid = new GridPane();
		tagsGrid.setVgap(4);
		tagsGrid.setPadding(new Insets(0, 80, 30, 20));
		tagsGrid.add(new ImageView(redImg), 0, 0);
		tagsGrid.add(redLbl, 1, 0);
		tagsGrid.add(new ImageView(orangeImg), 0, 1);
		tagsGrid.add(orangeLbl, 1, 1);
		tagsGrid.add(new ImageView(yellowImg), 0, 2);
		tagsGrid.add(yellowLbl, 1, 2);
		tagsGrid.add(new ImageView(greenImg), 0, 3);
		tagsGrid.add(greenLbl, 1, 3);
		tagsGrid.add(new ImageView(blueImg), 0, 4);
		tagsGrid.add(blueLbl, 1, 4);
		tagsGrid.add(new ImageView(purpleImg), 0, 5);
		tagsGrid.add(purpleLbl, 1, 5);
		tagsGrid.add(grey, 0, 6);
		tagsGrid.add(greyLbl, 1, 6);
		tagsGrid.add(new ImageView(allTagsImg), 0, 7);
		tagsGrid.add(allTagsLbl, 1, 7);

		// Append grids to the titledpanes
		TitledPane favoritesPane = new TitledPane("Favorites", favoritesGrid);
		TitledPane devicesPane = new TitledPane("Devices", null);
		TitledPane sharedPane = new TitledPane("Shared", null);
		TitledPane tagsPane = new TitledPane("Tags", tagsGrid);

		devicesPane.setCollapsible(false);
		devicesPane.setAnimated(false);
		sharedPane.setCollapsible(false);
		sharedPane.setAnimated(false);

		// Add the panes to the accordion
		accordion.getPanes().addAll(favoritesPane, devicesPane, sharedPane, tagsPane);
		accordion.setExpandedPane(favoritesPane);
		StackPane pane = new StackPane(accordion);

		// Return the accordion stack pane
		return pane;
	}

	private StackPane buildGrid() {

		// Create a new info grid with padding
		infoGrid = new GridPane();
		infoGrid.setVgap(4);
		infoGrid.setPadding(new Insets(30, 20, 0, 20));

		// If selected index is not selected
		if (smodel.getSelectedIndex() < 0) {

			// Get movie image of selection zero
			infoImg = new Image(absPath + records.get(0).getIcon(), W * 5, H * 5, false, false);
			StackPane imGpane = new StackPane(new ImageView(infoImg));

			// Get name of movie zero
			infoName = new Label(records.get(0).getName());
			infoName.setStyle("-fx-font: 24 arial;");

			// Get time of day
			Label dimLbl = new Label("Last Opened");
			dimLbl.setPrefWidth(180);
			Calendar calendar = Calendar.getInstance();
			StringBuilder sb = new StringBuilder("Today, ");
			int hours = (calendar.get(Calendar.HOUR) == 0) ? 12 : calendar.get(Calendar.HOUR);
			int minutes = calendar.get(Calendar.MINUTE);
			sb.append(hours + ":" + minutes);
			if (calendar.get(Calendar.HOUR_OF_DAY) > 12)
				sb.append(" PM");
			else
				sb.append(" AM");

			// Add all contents to info grid
			infoGrid.add(imGpane, 0, 0, 2, 2);
			infoGrid.add(infoName, 0, 2, 1, 2);
			infoGrid.add(new Label("Created"), 0, 4);
			infoGrid.add(new Label(records.get(0).getDate()), 1, 4);
			infoGrid.add(new Label("Modified"), 0, 5);
			infoGrid.add(new Label(records.get(0).getDate()), 1, 5);
			infoGrid.add(dimLbl, 0, 6);
			infoGrid.add(new Label(sb.toString()), 1, 6);
			infoGrid.add(new Label("Dimensions"), 0, 7);
			infoGrid.add(new Label(Integer.toString((int) (W * 5)) + " x " + Integer.toString((int) (H * 5))), 1, 7);

			// If selected movie is not zero
		} else {

			// Get movie image
			infoImg = new Image(absPath + records.get(smodel.getSelectedIndex()).getIcon(), W * 5, H * 5, false, false);
			StackPane imGpane = new StackPane(new ImageView(infoImg));

			// Get movie name
			infoName = new Label(records.get(smodel.getSelectedIndex()).getName());
			infoName.setStyle("-fx-font: 24 arial;");

			// Get last opened with current time
			Label dimLbl = new Label("Last Opened");
			dimLbl.setPrefWidth(180);
			Calendar calendar = Calendar.getInstance();
			StringBuilder sb = new StringBuilder("Today, ");
			int hours = (calendar.get(Calendar.HOUR) == 0) ? 12 : calendar.get(Calendar.HOUR);
			int minutes = calendar.get(Calendar.MINUTE);
			sb.append(hours + ":" + minutes);
			if (calendar.get(Calendar.HOUR_OF_DAY) > 12)
				sb.append(" PM");
			else
				sb.append(" AM");

			// Add contents to the info grid
			infoGrid.add(imGpane, 0, 0, 2, 2);
			infoGrid.add(infoName, 0, 2, 1, 2);
			infoGrid.add(new Label("Created"), 0, 4);
			infoGrid.add(new Label(records.get(smodel.getSelectedIndex()).getDate()), 1, 4);
			infoGrid.add(new Label("Modified"), 0, 5);
			infoGrid.add(new Label(records.get(smodel.getSelectedIndex()).getDate()), 1, 5);
			infoGrid.add(dimLbl, 0, 6);
			infoGrid.add(new Label(sb.toString()), 1, 6);
			infoGrid.add(new Label("Dimensions"), 0, 7);
			infoGrid.add(new Label(Integer.toString((int) (W * 5)) + " x " + Integer.toString((int) (H * 5))), 1, 7);
		}

		// Create stackpane with the info grid and return
		StackPane pane = new StackPane(infoGrid);
		return pane;
	}

	// Rotate the cycle to put the clicked item at the top
	private void handleMouseClicked(MouseEvent e) {

		// If documents, show all records
		if (e.getSource() == documentsLbl) {
			table.setItems(records);
		}

		// If favorites, show green tags
		if (e.getSource() == favoritesLbl) {
			ObservableList<Record> favoriteRecords;
			favoriteRecords = FXCollections.observableArrayList();
			for (int i = 0; i < records.size(); ++i) {
				if (records.get(i).getFav().equalsIgnoreCase("green")) {
					favoriteRecords.add(records.get(i));
				}
			}
			table.setItems(favoriteRecords);
		}

		// If red tag, change selected movie to red
		if (e.getSource() == redLbl) {
			records.get(smodel.getSelectedIndex()).setFav("red");
			for (int i = 0; i < 5; i++)
				table.setItems(records);
		}

		// If orange tag, change selected movie to orange
		if (e.getSource() == orangeLbl) {
			records.get(smodel.getSelectedIndex()).setFav("orange");
			for (int i = 0; i < 5; i++)
				table.setItems(records);
		}

		// If yellow tag, change selected movie to yellow
		if (e.getSource() == yellowLbl) {
			records.get(smodel.getSelectedIndex()).setFav("yellow");
			for (int i = 0; i < 5; i++)
				table.setItems(records);
		}

		// If green tag, change selected movie to green
		if (e.getSource() == greenLbl) {
			records.get(smodel.getSelectedIndex()).setFav("green");
			for (int i = 0; i < 5; i++)
				table.setItems(records);
		}

		// If blue tag, change selected movie to blue
		if (e.getSource() == blueLbl) {
			records.get(smodel.getSelectedIndex()).setFav("blue");
			for (int i = 0; i < 5; i++)
				table.setItems(records);
		}

		// If purple tag, change selected movie to purple
		if (e.getSource() == purpleLbl) {
			records.get(smodel.getSelectedIndex()).setFav("purple");
			for (int i = 0; i < 5; i++)
				table.setItems(records);
		}

		// If grey tag, change selected movie to grey
		if (e.getSource() == greyLbl) {
			records.get(smodel.getSelectedIndex()).setFav("black");
			for (int i = 0; i < 5; i++)
				table.setItems(records);
		}
	}
}

//******************************************************************************
