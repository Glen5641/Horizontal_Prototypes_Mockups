//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Thur Feb  14 11:04:30 2019 by Clayton Glenn
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
// 20190214 [glenn]:	Modified file.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.hci.assignment.prototypea;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import edu.ou.cs.hci.resources.Resources;

//******************************************************************************

/**
 * The <CODE>View</CODE> class.
 *
 * @author Chris Weaver
 * @editor Clayton Glenn
 * @version %I%, %G%
 */
public final class View implements ActionListener {
	// **********************************************************************
	// Private Members
	// **********************************************************************

	// Master of the program, manager of the data, mediator of all updates
	private final Controller controller;

	// The checkbox widgets
	private JCheckBox checkBox1;
	private JCheckBox checkBox2;
	private JCheckBox checkBox3;
	private JCheckBox checkBox4;
	private JCheckBox checkBox5;
	private JCheckBox checkBox6;

	// The ComboBox Widget
	private JComboBox<String> comboBox;

	// The Radiobutton Widgets
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JRadioButton radioButton3;
	private JRadioButton radioButton4;
	private JRadioButton radioButton5;
	private JRadioButton radioButton6;
	private JRadioButton radioButton7;

	// The Slider Widget
	private JSlider slider;

	// The TextField Widgets
	private JTextField textField1;
	private JTextField textField2;

	// The Label Widgets
	private JLabel heightLabel;
	private JLabel widthLabel;

	// The Button Widgets
	private JButton jbOK;
	private JButton jbCancel;

	// The Panels in the menu
	private JPanel generalPanel;
	private JPanel viewPanel;
	private JPanel editPanel;
	private JPanel savePanel;
	private JPanel spellingPanel;
	private JPanel ribbonPanel;
	private JPanel autoCorrectPanel;
	private JPanel compatabilityPanel;
	private JPanel advancedPanel;
	private JPanel feedbackPanel;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	// Construct the UI.
	public View(Controller controller) {

		// Set the controller
		this.controller = controller;

		// Create the content panes
		JPanel buttonPanel = createButtons();
		JPanel menuPanel = createMenu();
		JPanel optionsavePanel = createOptionsavePanel();

		// Create Frame's top panel
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(800, 850));
		panel.setLayout(new BorderLayout());

		// Set the components of the panel
		panel.add(menuPanel, BorderLayout.NORTH);
		panel.add(optionsavePanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		// Create a frame to hold everything and Set dimensions
		JFrame frame = new JFrame("");
		frame.setBounds(50 + (int) (50 * Math.random()), 50 + (int) (50 * Math.random()), 200, 200);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Exit when the user clicks the frame's close button
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.removeView(View.this);
			}
		});
	}

	// **********************************************************************
	// Public Methods (Controller)
	// **********************************************************************

	// Populate the UI with data, accessing it through the controller.
	public void initialize() {

		// Set certain elements disabled
		slider.setEnabled(false);
		textField1.setEnabled(false);
		textField2.setEnabled(false);
		widthLabel.setEnabled(false);
		heightLabel.setEnabled(false);

		// Set certain elements selected
		radioButton1.setSelected(true);
		radioButton3.setSelected(true);

		// Add action listeners to the global widgets
		checkBox5.addActionListener(this);
		comboBox.addActionListener(this);
		jbOK.addActionListener(this);
		jbCancel.addActionListener(this);
		radioButton3.addActionListener(this);
		radioButton4.addActionListener(this);
		radioButton5.addActionListener(this);
		radioButton6.addActionListener(this);
		radioButton7.addActionListener(this);
	}

	public void terminate() {

		// Terminate all action listeners and clean up
		checkBox5.removeActionListener(this);
		comboBox.removeActionListener(this);
		jbOK.removeActionListener(this);
		jbCancel.removeActionListener(this);
		radioButton3.removeActionListener(this);
		radioButton4.removeActionListener(this);
		radioButton5.removeActionListener(this);
		radioButton6.removeActionListener(this);
		radioButton7.removeActionListener(this);
	}

	// **********************************************************************
	// Override Methods (ActionListener)
	// **********************************************************************

	// Send changes to the controller from widgets that trigger actions.
	public void actionPerformed(ActionEvent e) {

		// Exit program if ok or cancel is selected
		if (e.getSource() == jbOK || e.getSource() == jbCancel) {
			controller.removeView(View.this);
		}

		// If "Save AutoRecover info every:" is selected, enable the slider
		if (checkBox5.isSelected()) {
			slider.setEnabled(true);
		} else { // set disabled else
			slider.setEnabled(false);
		}

		// If "custom size" is selected enable the custom options
		if (radioButton7.isSelected()) {
			textField1.setEnabled(true);
			textField2.setEnabled(true);
			widthLabel.setEnabled(true);
			heightLabel.setEnabled(true);
		} else { // Set disabled else
			textField1.setEnabled(false);
			textField2.setEnabled(false);
			widthLabel.setEnabled(false);
			heightLabel.setEnabled(false);
		}
	}

	// **********************************************************************
	// Create menu panel for frame
	// **********************************************************************
	private JPanel createMenu() { 

		// Create Menu Panel
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 13, 0));
		panel.setBorder(BorderFactory.createTitledBorder(null, "PowerPoint Preferences", TitledBorder.CENTER,
				TitledBorder.ABOVE_TOP));
		panel.setBackground(Color.lightGray);

		// Create the cover image label
		JLabel img, imgLabel;

		////////////////////////////////////////////////////////////////////////////////////////////////////
		//I could not pull the correct PNG images for the menu, so these are all empty.png
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Set General Panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("General", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		generalPanel = new JPanel(new BorderLayout());
		generalPanel.setBackground(Color.lightGray);
		generalPanel.add(img, BorderLayout.NORTH);
		generalPanel.add(imgLabel, BorderLayout.SOUTH);

		// Set view Panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("View", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		viewPanel = new JPanel(new BorderLayout());
		viewPanel.setBackground(Color.lightGray);
		viewPanel.add(img, BorderLayout.NORTH);
		viewPanel.add(imgLabel, BorderLayout.SOUTH);

		//Set Edit Panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("Edit", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		editPanel = new JPanel(new BorderLayout());
		editPanel.setBackground(Color.lightGray);
		editPanel.add(img, BorderLayout.NORTH);
		editPanel.add(imgLabel, BorderLayout.SOUTH);

		//Set Save Panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("Save", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		savePanel = new JPanel(new BorderLayout());
		savePanel.setBackground(Color.lightGray);
		savePanel.add(img, BorderLayout.NORTH);
		savePanel.add(imgLabel, BorderLayout.SOUTH);

		//Set Spelling Panel
		spellingPanel = new JPanel(new BorderLayout());
		spellingPanel.setBackground(Color.lightGray);
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("Spelling", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		spellingPanel.add(img, BorderLayout.NORTH);
		spellingPanel.add(imgLabel, BorderLayout.SOUTH);

		//Set Ribbon Panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("Ribbon", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		ribbonPanel = new JPanel(new BorderLayout());
		ribbonPanel.setBackground(Color.lightGray);
		ribbonPanel.add(img, BorderLayout.NORTH);
		ribbonPanel.add(imgLabel, BorderLayout.SOUTH);

		//Set autoCorrect Panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("AutoCorrect", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		autoCorrectPanel = new JPanel(new BorderLayout());
		autoCorrectPanel.setBackground(Color.lightGray);
		autoCorrectPanel.add(img, BorderLayout.NORTH);
		autoCorrectPanel.add(imgLabel, BorderLayout.SOUTH);

		//Set compatibility panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("Compatibility", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		compatabilityPanel = new JPanel(new BorderLayout());
		compatabilityPanel.setBackground(Color.lightGray);
		compatabilityPanel.add(img, BorderLayout.NORTH);
		compatabilityPanel.add(imgLabel, BorderLayout.SOUTH);

		//Set advanced panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("Advanced", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		advancedPanel = new JPanel(new BorderLayout());
		advancedPanel.setBackground(Color.lightGray);
		advancedPanel.add(img, BorderLayout.NORTH);
		advancedPanel.add(imgLabel, BorderLayout.SOUTH);

		//Set feedback panel
		img = new JLabel(Resources.getImage("example/swing/icon/empty.png"), JLabel.CENTER);
		imgLabel = new JLabel("Feedback", JLabel.CENTER);
		imgLabel.setFont(new Font(imgLabel.getFont().getName(), Font.BOLD, 10));
		feedbackPanel = new JPanel(new BorderLayout());
		feedbackPanel.setBackground(Color.lightGray);
		feedbackPanel.add(img, BorderLayout.NORTH);
		feedbackPanel.add(imgLabel, BorderLayout.SOUTH);

		//Add the components to the super panel
		panel.add(generalPanel);
		panel.add(viewPanel);
		panel.add(editPanel);
		panel.add(savePanel);
		panel.add(spellingPanel);
		panel.add(ribbonPanel);
		panel.add(autoCorrectPanel);
		panel.add(compatabilityPanel);
		panel.add(advancedPanel);
		panel.add(feedbackPanel);

		//Return super panel
		return panel;
	}

	// Create a pane with three buttons for the gallery.
	private JPanel createButtons() {

		//Create super panel
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 8));

		//Create OK button
		jbOK = new JButton("OK");
		jbOK.setFont(new Font(jbOK.getFont().getName(), Font.BOLD, 10));
		jbOK.setPreferredSize(new Dimension(90, 30));
		jbOK.addActionListener(this);

		//Create cancel button
		jbCancel = new JButton("Cancel");
		jbCancel.setFont(new Font(jbCancel.getFont().getName(), Font.BOLD, 10));
		jbCancel.setPreferredSize(new Dimension(90, 30));
		jbCancel.addActionListener(this);

		//Add components to super panel
		panel.add(jbCancel);
		panel.add(jbOK);

		//Return super panel
		return panel;
	}

	// Create the panel with all contents
	private JPanel createOptionsavePanel() {

		//Create super panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1, 0, 0));

		//Create the content panels
		JPanel panel1 = createSaveOptionsavePanel();
		JPanel saveSlidesavePanel = createSaveSlidesavePanel();

		//Add content panels to super panel
		panel.add(panel1);
		panel.add(saveSlidesavePanel);

		//Return super panel
		return panel;
	}
	
	//Create the panel with all save options
	private JPanel createSaveOptionsavePanel() {

		// Create super panel and component panels
		JPanel panel = new JPanel(new GridLayout(3, 1, 1, 1));
		JPanel panel1 = new JPanel(new GridLayout(4, 1, 1, 1));
		JPanel panel2 = new JPanel(new GridLayout(3, 1, 1, 1));
		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panel4 = new JPanel(new GridLayout(3, 1, 1, 1));
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 30));

		//Create Labels for content
		JLabel label1 = new JLabel("Save panel");
		label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 10));
		JLabel label2 = new JLabel("Save PowerPoint files as: ");
		label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, 10));
		JLabel label3 = new JLabel("When saving a presentation that has updates from other authors:");
		label3.setFont(new Font(label3.getFont().getName(), Font.BOLD, 10));
		
		//Create seperator to seperate content
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setBackground(Color.black);
		
		//Create all checkboxes for this content panel
		checkBox1 = new JCheckBox("Save preview picture with new documents");
		checkBox1.setFont(new Font(checkBox1.getFont().getName(), Font.BOLD, 10));
		checkBox2 = new JCheckBox("Prompt for document properties");
		checkBox2.setFont(new Font(checkBox2.getFont().getName(), Font.BOLD, 10));
		checkBox3 = new JCheckBox("Include full text search information");
		checkBox3.setFont(new Font(checkBox3.getFont().getName(), Font.BOLD, 10));
		checkBox4 = new JCheckBox("Warn before saving in a format where presentation elements are removed");
		checkBox4.setFont(new Font(checkBox4.getFont().getName(), Font.BOLD, 10));
		checkBox5 = new JCheckBox("Save AutoRecover info every:");
		checkBox5.setFont(new Font(checkBox5.getFont().getName(), Font.BOLD, 10));
		checkBox6 = new JCheckBox("Warn about and review changes");
		checkBox6.setFont(new Font(checkBox6.getFont().getName(), Font.BOLD, 10));
		
		//Create a bounded range model for the slider's constraints
		DefaultBoundedRangeModel model = new DefaultBoundedRangeModel(0, 0, 0, 60);
		
		//Create the slider
		slider = new JSlider(model);
		slider.setOrientation(SwingConstants.HORIZONTAL);
		slider.setMajorTickSpacing(15);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setFont(new Font(slider.getFont().getName(), Font.PLAIN, 5));
		
		//Create the combo box and add the String to the combo box
		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font(comboBox.getFont().getName(), Font.BOLD, 10));
		comboBox.addItem("PowerPoint Presentation (.pptx)");
		comboBox.setBackground(Color.white);

		//Add components to panel 1
		panel1.add(label1);
		panel1.add(sep);
		panel1.add(checkBox1);
		panel1.add(checkBox2);
		
		//Add components to panel 3
		panel3.add(checkBox5);
		panel3.add(slider);
		
		//Add components to panel 2
		panel2.add(checkBox3);
		panel2.add(checkBox4);
		panel2.add(panel3);
		
		//Add components to panel 5
		panel5.add(label2);
		panel5.add(comboBox);
		
		//Add components to panel 4
		panel4.add(panel5);
		panel4.add(label3);
		panel4.add(checkBox6);
		
		//Add panels to super panel
		panel.add(panel1);
		panel.add(panel2);
		panel.add(panel4);

		//Return super panel
		return panel;
	}

	//Create the panel with the slide save options
	private JPanel createSaveSlidesavePanel() {
		
		//Create super panel
		JPanel panel = new JPanel(new GridLayout(2, 1, 0, 0));

		//Create content panels
		JPanel panel1 = new JPanel(new GridLayout(7, 1, 3, 3));
		JPanel panel2 = new JPanel(new GridLayout(1, 4, 3, 3));
		JPanel panel3 = new JPanel(new GridLayout(1, 2, 3, 3));
		JPanel panel4 = new JPanel(new GridLayout(4, 1, 3, 3));
		JPanel panel5 = new JPanel(new GridLayout(1, 2, 3, 3));
		JPanel panel6 = new JPanel(new GridLayout(1, 2, 3, 3));
		JPanel panel7 = new JPanel(new GridLayout(4, 1, 3, 3));
		panel1.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30));

		// Create labels for the content
		JLabel label1 = new JLabel("Save slides as graphics files");
		label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 10));
		label1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JLabel label2 = new JLabel("Size of graphics files");
		label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, 10));
		label2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		widthLabel = new JLabel("Width:");
		widthLabel.setFont(new Font(widthLabel.getFont().getName(), Font.BOLD, 10));
		heightLabel = new JLabel("Height:");
		heightLabel.setFont(new Font(heightLabel.getFont().getName(), Font.BOLD, 10));

		// Create Separators
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setBackground(Color.black);
		JSeparator sep1 = new JSeparator(SwingConstants.HORIZONTAL);
		sep1.setBackground(Color.black);

		//Create radiobuttons
		radioButton1 = new JRadioButton("Save current slide only");
		radioButton1.setFont(new Font(radioButton1.getFont().getName(), Font.BOLD, 10));
		radioButton2 = new JRadioButton("Save every slide (series of graphics files)");
		radioButton2.setFont(new Font(radioButton2.getFont().getName(), Font.BOLD, 10));
		radioButton3 = new JRadioButton("1080 x 810");
		radioButton3.setFont(new Font(radioButton3.getFont().getName(), Font.BOLD, 10));
		radioButton4 = new JRadioButton("720 x 640");
		radioButton4.setFont(new Font(radioButton4.getFont().getName(), Font.BOLD, 10));
		radioButton5 = new JRadioButton("480 x 360");
		radioButton5.setFont(new Font(radioButton5.getFont().getName(), Font.BOLD, 10));
		radioButton6 = new JRadioButton("320 x 240");
		radioButton6.setFont(new Font(radioButton6.getFont().getName(), Font.BOLD, 10));
		radioButton7 = new JRadioButton("Custom Size");
		radioButton7.setFont(new Font(radioButton7.getFont().getName(), Font.BOLD, 10));

		//Create textfields
		textField1 = new JTextField("720");
		textField1.setFont(new Font(textField1.getFont().getName(), Font.BOLD, 10));
		textField2 = new JTextField("640");
		textField2.setFont(new Font(textField2.getFont().getName(), Font.BOLD, 10));

		//Create first buttongroup
		ButtonGroup bgroup1 = new ButtonGroup();
		bgroup1.add(radioButton1);
		bgroup1.add(radioButton2);

		//Create second buttongroup
		ButtonGroup bgroup2 = new ButtonGroup();
		bgroup2.add(radioButton3);
		bgroup2.add(radioButton4);
		bgroup2.add(radioButton5);
		bgroup2.add(radioButton6);
		bgroup2.add(radioButton7);		
		
		//Add components to panel 3
		panel3.add(radioButton3);
		panel3.add(radioButton7);
		
		//Add components to panel 1
		panel1.add(label1);
		panel1.add(sep);
		panel1.add(radioButton1);
		panel1.add(radioButton2);
		panel1.add(label2);
		panel1.add(sep1);
		panel1.add(panel3);
		
		//Add components to panel 7
		panel7.add(radioButton3);
		panel7.add(radioButton4);
		panel7.add(radioButton5);
		panel7.add(radioButton6);
		
		//Add components to panel 5
		panel5.add(widthLabel);
		panel5.add(textField1);
		
		//Add components to panel 6
		panel6.add(heightLabel);
		panel6.add(textField2);
		
		//Add components to panel 4
		panel4.add(radioButton7);
		panel4.add(panel5);
		panel4.add(panel6);
		
		//Add components to panel 2
		panel2.add(panel7);
		panel2.add(panel4);
		panel2.add(new JPanel());
		panel2.add(new JPanel());
		
		//Add the panels to the super panel
		panel.add(panel1);
		panel.add(panel2);

		//Return the super panel
		return panel;
	}
}

//******************************************************************************
