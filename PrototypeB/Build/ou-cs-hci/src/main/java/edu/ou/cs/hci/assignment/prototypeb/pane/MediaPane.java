//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Sun Feb 24 18:17:00 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
// 20190220 [weaver]:	Adapted from swingmvc to fxmvc.
// 20190305 [glenn]:	Prototype B modifications.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypeb.pane;

//import java.lang.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.util.Duration;

import java.text.DecimalFormat;

import edu.ou.cs.hci.assignment.prototypeb.Controller;

//******************************************************************************

/**
 * The <CODE>MediaPane</CODE> class.
 *
 * @author Chris Weaver
 * @version %I%, %G%
 */
public final class MediaPane extends AbstractPane {
	// **********************************************************************
	// Private Class Members
	// **********************************************************************

	private static final String NAME = "Media";
	private static final String HINT = "Basic Player for Movie Clips";

	// **********************************************************************
	// Private Class Members (Data)
	// **********************************************************************

	private static final String URL = "http://ampliation.org/hci/HPSS.mp4";

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// Layout
	private BorderPane base;
	private MediaView mediaView;
	private Button playButton;
	private Button stopButton;
	private Label duration;
	private double volval;

	// Support
	private MediaPlayer mediaPlayer;
	private GridPane grid;
	private Slider timeSlider;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	public MediaPane(Controller controller) {
		super(controller, NAME, HINT);

		setBase(buildPane());
	}

	// **********************************************************************
	// Public Methods (Controller)
	// **********************************************************************

	// The controller calls this method when it adds a view.
	// Set up the nodes in the view with data accessed through the controller.
	public void initialize() {
		// Keep the media view the same width as the entire scene
		mediaView.fitWidthProperty().bind(mediaView.getScene().widthProperty());

		Media media = new Media(URL);

		media.setOnError(new Runnable() {
			public void run() {
				System.out.println(media.getError());
			}
		});

		mediaPlayer = new MediaPlayer(media);

		mediaPlayer.setAutoPlay(false);

		mediaPlayer.setOnError(new Runnable() {
			public void run() {
				System.out.println(mediaPlayer.getError());
			}
		});

		mediaView.setMediaPlayer(mediaPlayer);
	}

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void terminate() {
		mediaView.setMediaPlayer(null);
		mediaPlayer.dispose();

		playButton.setOnAction(null);
		stopButton.setOnAction(null);
	}

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void update(String key, Object value) {
	}

	// **********************************************************************
	// Private Methods (Layout)
	// **********************************************************************

	private Pane buildPane() {
		base = new BorderPane();

		base.setId("media-pane"); // See #media-pane in View.css

		base.setCenter(createScreen());
		base.setBottom(createPlayer());

		return base;
	}

	// Create a node with the media view.
	private Pane createScreen() {
		mediaView = new MediaView();

		mediaView.setOnError(new MediaErrorHandler());

		return new StackPane(mediaView);
	}

	// Create a node with various playback controls.
	private Pane createPlayer() {
		
		FlowPane pane = new FlowPane(8.0, 8.0);
		grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(0, 30, 20, 30));
		pane.setAlignment(Pos.CENTER);
		
		//Create all buttons and sliders
		Image play = new Image("edu/ou/cs/hci/resources/example/swing/icon/play.png", 30, 30, false, false);
		Image stop = new Image("edu/ou/cs/hci/resources/example/swing/icon/pause.png", 30, 30, false, false);
		Image vol = new Image("edu/ou/cs/hci/resources/example/swing/icon/speaker.png", 30, 30, false, false);
		Image mute = new Image("edu/ou/cs/hci/resources/example/swing/icon/mute.png", 30, 30, false, false);
		Image gear = new Image("edu/ou/cs/hci/resources/example/swing/icon/gear.png", 30, 30, false, false);
		timeSlider = new Slider();
		Slider volume = new Slider();
		Label speed = new Label("Speed: ");
		Hyperlink speedlink = new Hyperlink("X0.5");
		Hyperlink reglink = new Hyperlink("X1");
		ImageView playButton = new ImageView(play);
		ImageView stopButton = new ImageView(stop);
		ImageView volbtn = new ImageView(vol);
		ImageView mutebtn = new ImageView(mute);
		ImageView gearbtn = new ImageView(gear);
		duration = new Label("0.00 / 0.00");
		
		//Change text color to white
		speed.setStyle("-fx-text-fill: white");
		speedlink.setStyle("-fx-text-fill: white");
		reglink.setStyle("-fx-text-fill: white");
		duration.setStyle("-fx-text-fill: white");
		
		//Set some buttons invisible
		stopButton.setVisible(false);
		mutebtn.setVisible(false);
		speedlink.setVisible(false);
		reglink.setVisible(false);
		speed.setVisible(false);
		
		//Set slider and text width
		volume.setMinWidth(200);
		timeSlider.setMinWidth(1100);
		timeSlider.setMaxWidth(Double.MAX_VALUE);
		duration.setPrefWidth(300);
		
		//Add the contents to the grid pane
		grid.add(timeSlider, 0, 0, 20, 1);
		grid.add(playButton, 1, 1, 1, 1);
		grid.add(stopButton, 1, 1, 1, 1);
		grid.add(volbtn, 2, 1);
		grid.add(mutebtn, 2, 1);
		grid.add(volume, 3, 1, 8, 1);
		grid.add(duration, 11, 1);
		grid.add(speed, 12, 1);
		grid.add(speedlink, 13, 1);
		grid.add(reglink, 14, 1);
		grid.add(gearbtn, 20, 1);
		
		//In-line event handler for volume button, makes mute appear and sets volume to zero
		volbtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				volbtn.setVisible(false);
				mutebtn.setVisible(true);
				volval = volume.getValue();
				volume.setValue(0);
				event.consume();
			}
		});
		
		//In-line event handler for mute button. Makes volume appear and sets volume to cached value
		mutebtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				volbtn.setVisible(true);
				mutebtn.setVisible(false);
				volume.setValue(volval);
				event.consume();
			}
		});
		
		//In-line event handler for volume slider. Makes volume appear.
		volume.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				volbtn.setVisible(true);
				mutebtn.setVisible(false);
				event.consume();
			}
		});
		
		//In-line event handler for half speed hyperlink, video plays at half speed
		speedlink.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mediaPlayer.setRate(.5);
				event.consume();
			}
		});
		
		//In-line event handler for regular speed hyperlink, video plays at regular speed
		reglink.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mediaPlayer.setRate(1);
				event.consume();
			}
		});
		
		//In-line event handler for gear button, pops up speed preferences
		gearbtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!speedlink.isVisible()) {
					speedlink.setVisible(true);
					reglink.setVisible(true);
					speed.setVisible(true);
				} else {
					speedlink.setVisible(false);
					reglink.setVisible(false);
					speed.setVisible(false);
				}
			}
		});
		
		//In-line event handler for play button, shows pause button and plays media player
		playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				playButton.setVisible(false);
				stopButton.setVisible(true);
				mediaPlayer.play();
				mediaPlayer.currentTimeProperty().addListener((observableValue, oldDuration, newDuration) -> {
					DecimalFormat df = new DecimalFormat("#.00");
					duration.setText(df.format(newDuration.toSeconds()) + " / "
							+ df.format(mediaPlayer.getMedia().getDuration().toSeconds()));
					timeSlider.setValue(newDuration.toSeconds());
					System.out.println(newDuration.toSeconds());
					timeSlider.setMin(0);
					timeSlider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
				});
				event.consume();
			}
		});

		//In-line event handler for pause button, stops video and shows play button
		stopButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				playButton.setVisible(true);
				stopButton.setVisible(false);
				mediaPlayer.stop();
				event.consume();
			}
		});
		
		//In-line event handler for time slider, creates sync for video vs. slider
		timeSlider.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
				event.consume();
			}
		});

		//Add the grid to the flow pane and return
		pane.getChildren().add(grid);
		return pane;
	}

	// **********************************************************************
	// Inner Classes (Event Handlers)
	// **********************************************************************

	private final class MediaErrorHandler implements EventHandler<MediaErrorEvent> {
		public void handle(MediaErrorEvent e) {
			System.err.println(e);
		}
	}

}

//******************************************************************************
