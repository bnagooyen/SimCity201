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

import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;

public class BankListPanel extends BuildingListPanel {
	JTextField setInventory = new JTextField();
	JButton setVal = new JButton("Set!");
	JCheckBox downCB = new JCheckBox("Down?");
	String name;
	
	public BankListPanel(SimCityGui gui, String txt) {
		super(gui, txt);
		name = txt;
		// TODO Auto-generated constructor stub
		JPanel inventoryPanel = new JPanel();
		inventoryPanel.setBackground(Color.darkGray);
		JLabel lbl = new JLabel("Set Vault Value: ");
		lbl.setForeground(Color.white);
		inventoryPanel.setLayout(new BorderLayout());
		inventoryPanel.add(lbl, BorderLayout.NORTH);
		
		setInventory.setPreferredSize(new Dimension(80,30));
		inventoryPanel.add(setInventory, BorderLayout.WEST);
		
		downCB.addActionListener(this);
		downCB.setForeground(Color.white);
		top.add(downCB, BorderLayout.EAST);
		
		setVal.addActionListener(this);
		inventoryPanel.add(setVal, BorderLayout.CENTER);
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
        	System.out.println("Silly Billy, our city has an unlimited amount of money");
        }
        
	}
}
