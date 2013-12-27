/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.gui;
import simcity.PersonAgent;
import simcity.gui.BuildingPanel;
import simcity.gui.CityPanel;
import simcity.gui.CityView;
import simcity.gui.BuildingListPanel;
import simcity.gui.trace.AlertLevel;
import simcity.gui.trace.AlertTag;
import simcity.gui.trace.TracePanel;
import simcity.interfaces.Person;

import javax.swing.*;

import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DWaiterRole;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimCityGui extends JFrame implements ActionListener {
	/* The GUI has three frames, the city view frame (gui itself) 
	 * the innercity frame, (in variable innerBuildingFrame within gui)
	 * and the global log (in variable globalLog within gui)
	 */


	CityPanel city; // the panel of all the city features ec homes, businesses, roads, etc.
	BuildingListPanel info; // info panel for inner buildings user views.
	PersonListPanel cityInfo; // info panel of simcity. for adding ppl/tracking/setting scenarios/etc.
	CityView view; //cardlayout impplementation. controls what building's innerCityBuildingFrame is showing. 

	TracePanel tracePanel;
	ControlPanel controlPanel;

	JFrame globalLog = new JFrame("SimCity Log");
	JFrame innerBuildingFrame = new JFrame("InnerBuilding");
	static final int NUMRESTAURANTS = 6;
	static final int NUMBANKS = 2;
	static final int NUMMARKETS =4;
	static final int NUMHOUSES =15;
	static final int NUMAPTS= 12;
	static final int NUMHOMELESSSHELTER = 1;
	public HashMap<String, BuildingPanel> myPanels;


	//the file that creates/holds all "person"s. 
	//agent side of all city people/businesses/directory creation
	public SimCityPanel simcityPanel;

	private JPanel infoPanel;
	private JLabel infoLabel; //part of infoPanel

	private JCheckBox stateCB;//part of infoLabel

	private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */


	public SimCityGui() {
		int WINDOWX = 750;
		int WINDOWY = 750;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(20, 0, 875, 444);

		/*trace panel is the global log itself  */
		tracePanel = new TracePanel();
		tracePanel.setPreferredSize(new Dimension(400, 730));
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();

		//info = new TListPanel(this);
		this.controlPanel = new ControlPanel(tracePanel);

		//globalLog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		globalLog.setLayout(new BorderLayout());
		globalLog.setBounds(900,0,350, 735);
		globalLog.setResizable(true);
		globalLog.add(tracePanel, BorderLayout.CENTER);
		globalLog.add(controlPanel, BorderLayout.EAST);
		globalLog.pack();
		globalLog.setVisible(true);

		myPanels = new HashMap<String, BuildingPanel>();
		myPanels.clear();

		city = new CityPanel(this);



		for(int i=1; i<=NUMRESTAURANTS; i++) {
			String name = "Restaurant "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
			//System.err.println(name);
		}

		for(int i=1; i<=NUMMARKETS; i++) {
			String name = "Market "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
		}

		for(int i=1; i<=NUMBANKS; i++) {
			String name = "Bank "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
		}

		for(int i=1; i<=NUMAPTS; i++) {
			String name = "Apartment "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
		}

		for(int i=1; i<=NUMHOUSES; i++) {
			String name = "House "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
			//System.err.println(name);
		}


		String name = "Homeless Shelter";
		myPanels.put(name, new BuildingPanel(this, name));
		//System.err.println(name);

		view = new CityView(this, myPanels);

		innerBuildingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		innerBuildingFrame.setBounds(20, 430, 875, 444);
		innerBuildingFrame.setResizable(false);
		innerBuildingFrame.add(view);


		innerBuildingFrame.pack();
		innerBuildingFrame.setVisible(true);

		this.setLayout(new BorderLayout());

		cityInfo  = new PersonListPanel(this);

		add(cityInfo, BorderLayout.WEST);

		add(city, BorderLayout.CENTER);

		simcityPanel = new SimCityPanel(this, city);


	}
	private class ControlPanel extends JPanel {
		TracePanel tp;	//Hack so I can easily call showAlertsWithLevel for this demo.

		JToggleButton enableMessagesButton;		//You could (and probably should) substitute a JToggleButton to replace both
		JToggleButton enableErrorButton;		
		JToggleButton enablePersonButton;			
		JToggleButton enableBRestaurantButton;
		JToggleButton enableDRestaurantButton;
		JToggleButton enableDrewRestaurantButton;
		JToggleButton enableKRestaurantButton;
		JToggleButton enableLRestaurantButton;
		JToggleButton enableTRestaurantButton;
		JToggleButton enableMarketButton;
		JToggleButton enableBankButton;
		JToggleButton enableGuiButton;

		public ControlPanel(final TracePanel tracePanel) {
			this.tp = tracePanel;
			enableMessagesButton = new 	JToggleButton("MESSAGE");
			enableErrorButton = new JToggleButton("ERROR");
			enablePersonButton = new JToggleButton("Person");
			enableBRestaurantButton = new JToggleButton("BRestaurant");
			enableDRestaurantButton = new JToggleButton("DRestaurant");
			enableDrewRestaurantButton = new JToggleButton("DrewRestaurant");
			enableKRestaurantButton = new JToggleButton("KRestaurant");
			enableLRestaurantButton = new JToggleButton("LRestaurant");
			enableTRestaurantButton = new JToggleButton("TRestaurant");
			enableMarketButton = new JToggleButton("Market");
			enableBankButton = new JToggleButton("Bank");
			enableGuiButton = new JToggleButton("Gui");

			enableMessagesButton.setSelected(true);
			enableErrorButton.setSelected(true);
			enablePersonButton.setSelected(true);
			enableBRestaurantButton.setSelected(true);
			enableDRestaurantButton.setSelected(true);
			enableDrewRestaurantButton.setSelected(true);
			enableKRestaurantButton.setSelected(true);
			enableLRestaurantButton.setSelected(true);
			enableTRestaurantButton.setSelected(true);
			enableMarketButton.setSelected(true);
			enableBankButton.setSelected(true);
			enableGuiButton.setSelected(true);

			enableMessagesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableMessagesButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
					}
					else{
						tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
					}
				}
			});
			enableErrorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableErrorButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
					}
					else{
						tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
					}
				}
			});

			enablePersonButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enablePersonButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.PERSON);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.PERSON);
					}
				}
			});
			enableBRestaurantButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableBRestaurantButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.BRestaurant);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.BRestaurant);
					}
				}
			});
			enableDRestaurantButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableDRestaurantButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.DRestaurant);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.DRestaurant);
					}
				}
			});
			enableDrewRestaurantButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableDrewRestaurantButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.DrewRestaurant);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.DrewRestaurant);
					}
				}
			});
			enableKRestaurantButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableKRestaurantButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.KRestaurant);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.KRestaurant);
					}
				}
			});
			enableLRestaurantButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableLRestaurantButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.LRestaurant);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.LRestaurant);
					}
				}
			});
			enableKRestaurantButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableKRestaurantButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.KRestaurant);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.KRestaurant);
					}
				}
			});
			enableTRestaurantButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableTRestaurantButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.TRestaurant);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.TRestaurant);
					}
				}
			});
			enableMarketButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableMarketButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.Market);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.Market);
					}
				}
			});
			enableBankButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableBankButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.Bank);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.Bank);
					}
				}
			});
			enableGuiButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean selected = enableGuiButton.getModel().isSelected();
					if(selected) {
						tracePanel.showAlertsWithTag(AlertTag.Gui);
					}
					else{
						tracePanel.hideAlertsWithTag(AlertTag.Gui);
					}
				}
			});
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(enableMessagesButton);
			this.add(enableErrorButton);
			this.add(enablePersonButton);
			this.add(enableBRestaurantButton);
			this.add(enableDRestaurantButton);
			this.add(enableDrewRestaurantButton);
			this.add(enableKRestaurantButton);
			this.add(enableLRestaurantButton);
			this.add(enableTRestaurantButton);
			this.add(enableMarketButton);
			this.add(enableBankButton);
			this.add(enableGuiButton);
			this.setMinimumSize(new Dimension(50, 600));
		}
	}

	public void updateInfoPanel(Object person) {
		stateCB.setVisible(true);
		currentPerson = person;

		if (person instanceof DCustomerRole) {
			DCustomerRole customer = (DCustomerRole) person;
			stateCB.setText("Hungry?");
			//Should checkmark be there? 
			stateCB.setSelected(customer.getGui().isHungry());
			//Is customer hungry? Hack. Should ask customerGui
			stateCB.setEnabled(!customer.getGui().isHungry());
			// Hack. Should ask customerGui
			infoLabel.setText(
					"<html><pre>     Name: " + customer.getName() + " </pre></html>");
		}
		infoPanel.validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == stateCB) {
			if (currentPerson instanceof DCustomerRole) {
				DCustomerRole c = (DCustomerRole) currentPerson;
				//c.getGui().setHungry();
				stateCB.setEnabled(false);
			}
		}


	}

	public void AddPeople(Vector<PersonAgent> people) {
		cityInfo.AddPeople(people);
	}

	/**
	 * Main routine to get gui started
	 */
	public static void main(String[] args) {
		SimCityGui gui = new SimCityGui();
		gui.setTitle("csci201 Restaurant");
		gui.setResizable(false);
		gui.pack();
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
