package simcity.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MarketListPanel extends BuildingListPanel {
	JTextField setInventory = new JTextField();
	JButton setVal = new JButton("Set!");
	public MarketListPanel(SimCityGui restaurantGui, String txt) {
		super(restaurantGui, txt);
		// TODO Auto-generated constructor stub
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
		
	}

}
