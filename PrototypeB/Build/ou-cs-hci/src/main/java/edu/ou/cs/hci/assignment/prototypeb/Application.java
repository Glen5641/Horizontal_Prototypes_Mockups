//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Mon Feb 25 13:36:46 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
// 20190220 [weaver]:	Adapted from swingmvc to fxmvc.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypeb;

//import java.lang.*;
import javafx.animation.*;
//import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

//******************************************************************************

/**
 * The <CODE>Application</CODE> class.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class Application extends javafx.application.Application
{
	//**********************************************************************
	// Main
	//**********************************************************************

	public static void	main(String[] args)
	{
		javafx.application.Application.launch(args);
	}

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Master of the program, manager of the data, mediator of all updates
	private Controller			controller;

	// Where the data lives; only one place.
	private Model				model;

	//**********************************************************************
	// Override Methods (Application)
	//**********************************************************************

	public void	init()
	{
		controller = new Controller();
		model = new Model(controller);

		controller.setModel(model);
	}

	public void	start(Stage stage)
	{
		Text		text = new Text("PrototypeB");
		StackPane	root = new StackPane();

		root.getChildren().add(text);

		Scene		scene = new Scene(root, 250, 80);

		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();

		// Animation: scale text to fill stage over 1.0 seconds
		Duration			sd = new Duration(500);
		ScaleTransition	st = new ScaleTransition(sd, text);

		st.setFromX(0.01);
		st.setFromY(0.01);
		st.setToX(3.5);
		st.setToY(3.5);

		st.play();

		// Animation: Hide stage after 1.5 seconds
		Timeline	timeline = new Timeline();
		Duration	duration = new Duration(750);
		KeyFrame	endframe = new KeyFrame(duration, new EndSplash(stage));

		timeline.getKeyFrames().add(endframe);
		timeline.play();
	}

	public void	stop()
		throws Exception
	{
	}

	//**********************************************************************
	// Override Methods (EventHandler<ActionEvent>)
	//**********************************************************************

	public final class EndSplash
		implements EventHandler<ActionEvent>
	{
		private final Stage	stage;

		public EndSplash(Stage stage)
		{
			this.stage = stage;
		}

		public void	handle(ActionEvent e)
		{
			View		view1 = new View(controller, "View 1",  40,  40);

			controller.addView(view1);

			stage.hide();
		}
	}
}

//******************************************************************************
