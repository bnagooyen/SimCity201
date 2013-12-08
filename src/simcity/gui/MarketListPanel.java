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

public class MarketListPanel extends BuildingListPanel {
	JTextField setInventory = new JTextField();
	JButton setVal = new JButton("Set!");
	JCheckBox downCB = new JCheckBox("Down?");
	String building;
	SimCityGui gui;
	
	public MarketListPanel(SimCityGui marketGui, String txt) {
		super(marketGui, txt);
		
		// TODO Auto-generated constructor stub
		building = txt;
		
		downCB.addActionListener(this);
		downCB.setForeground(Color.white);
		top.add(downCB, BorderLayout.EAST);
		
		
		JPanel inventoryPanel = new JPanel();
		inventoryPanel.setBackground(Color.darkGray);
		JLabel lbl = new JLabel("Set Inventory Values: ");
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		
		if (arg0.getSource() == setVal) {
			
			String userInput2 = (setInventory).getText().trim();
	    	int inventoryVal=Integer.parseInt(userInput2);
	    	
	    	System.out.println("Market's inventory: "+inventoryVal);
	    	
	    	if(building.equals("Market 1")){
	    		((MarketPlace)(city.simcityPanel.directory.get("Market 1"))).ib.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Market 1's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, building, "Changing Market 1's inventory");
	    	}
	    	else if(building.equals("Market 2")){
	    		((MarketPlace)(city.simcityPanel.directory.get("Market 2"))).ib.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Market 2's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, building, "Changing Market 2's inventory");
	    	}
	    	else if(building.equals("Market 3")){
	    		((MarketPlace)(city.simcityPanel.directory.get("Market 3"))).ib.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Market 3's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, building, "Changing Market 3's inventory");
	    	}
	    	else if(building.equals("Market 4")){
	    		((MarketPlace)(city.simcityPanel.directory.get("Market 4"))).ib.msgSetInventory(inventoryVal);
	    		System.out.println("Changing Market 4's inventory to "+inventoryVal +" for each item");
	    		AlertLog.getInstance().logInfo(AlertTag.Gui, building, "Changing Market 4's inventory");
	    	}
	    	
		}
		if(arg0.getSource()==downCB){ 
        	if(downCB.isSelected()){
        		city.simcityPanel.directory.get(building).down = true;
        		AlertLog.getInstance().logInfo(AlertTag.Gui, building, "set to down");
        	}
        	else{
        		city.simcityPanel.directory.get(building).down = false;
        		AlertLog.getInstance().logInfo(AlertTag.Gui, building, "down checkbox unchecked");
        	}
        }		
	}
	

}
