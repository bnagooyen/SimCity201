package simcity.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import simcity.gui.SimCityPanel.MarketPlace;
import simcity.gui.SimCityPanel.RestaurantPlace;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;

public class RestaurantListPanel extends BuildingListPanel {
	JTextField setInventory = new JTextField();
	JButton setVal = new JButton("Set!");
	JCheckBox downCB = new JCheckBox("Down?");
	String name;
	
	public RestaurantListPanel(SimCityGui restaurantGui, String txt) {
		super(restaurantGui, txt);
		name = txt;
		downCB.addActionListener(this);
		downCB.setForeground(Color.white);
		top.add(downCB, BorderLayout.EAST);
		
		
		// TODO Auto-generated constructor stub
		JPanel inventoryPanel = new JPanel();
		inventoryPanel.setBackground(Color.darkGray);
		JLabel lbl = new JLabel("Set Kitchen Values: ");
		lbl.setForeground(Color.white);
		inventoryPanel.setLayout(new BorderLayout());
		inventoryPanel.add(lbl, BorderLayout.NORTH);
		
		setInventory.setPreferredSize(new Dimension(80,30));
		inventoryPanel.add(setInventory, BorderLayout.WEST);
		
		setVal.addActionListener(this);
		inventoryPanel.add(setVal, BorderLayout.EAST);
		valueSetPanel.add(inventoryPanel, BorderLayout.CENTER);
		validate();
		
	}

	@Override
	public void addPerson(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == downCB){ 
        	if(downCB.isSelected()){
        		city.simcityPanel.directory.get(name).down = true;
        		AlertLog.getInstance().logInfo(AlertTag.Gui, name, "set to down");
        	}
        	else{
        		city.simcityPanel.directory.get(name).down = false;
        		AlertLog.getInstance().logInfo(AlertTag.Gui, name, "down checkbox unchecked");
        	}
        }
		
		if(e.getSource() == setInventory){

			String userInput2 = (setInventory).getText().trim();
	    	int inventoryVal=Integer.parseInt(userInput2);
	    	
	    	System.out.println("Restaurant's inventory: "+inventoryVal);
	    	
	    	if(name.equals("Restaurant 1")){
	    		((RestaurantPlace)(city.simcityPanel.directory.get("Restaurant 1"))).cook.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Restaurant 1's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, name, "Changing inventory");
	    	}
	    	else if(name.equals("Restaurant 2")){
	    		((RestaurantPlace)(city.simcityPanel.directory.get("Restaurant 2"))).cook.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Restaurant 2's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, name, "Changing inventory");
	    	}
	    	else if(name.equals("Restaurant 3")){
	    		((RestaurantPlace)(city.simcityPanel.directory.get("Restaurant 3"))).cook.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Restaurant 3's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, name, "Changing inventory");
	    	}
	    	else if(name.equals("Restaurant 4")){
	    		((RestaurantPlace)(city.simcityPanel.directory.get("Restaurant 4"))).cook.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Restaurant 4's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, name, "Changing inventory");
	    	}
	    	else if(name.equals("Restaurant 5")){
	    		((RestaurantPlace)(city.simcityPanel.directory.get("Restaurant 5"))).cook.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Restaurant 5's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, name, "Changing inventory");
	    	}
	    	else if(name.equals("Restaurant 6")){
	    		((RestaurantPlace)(city.simcityPanel.directory.get("Restaurant 6"))).cook.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Restaurant 6's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, name, "Changing inventory");
	    	}
	    	
		}
		
	}
	
}
