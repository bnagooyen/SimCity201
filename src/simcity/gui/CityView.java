package simcity.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JPanel;

public class CityView extends JPanel implements MouseListener, ActionListener {

	HashMap<String, CityCard> cards;
	SimCityGui city;
	public static final int VIEW_WIDTH = 575, VIEW_HEIGHT = 325;
	CardLayout layout;
	
	public CityView(SimCityGui city) {
		
		this.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		this.city = city;
		
		cards = new HashMap<String, CityCard>();

		cards.put("Restaurant 1", new CityCard(city, Color.red));
		cards.put("Restaurant 2", new CityCard(city, Color.orange));
		cards.put("Restaurant 3", new CityCard(city, Color.yellow));
		cards.put("Restaurant 4", new CityCard(city, Color.green));
		cards.put("Restaurant 5", new CityCard(city, Color.blue));
		cards.put("Restaurant 6", new CityCard(city, Color.magenta));
		cards.put("Market 1", new CityCard(city, Color.pink));
		cards.put("Market 2", new CityCard(city, Color.pink));
		cards.put("Market 3", new CityCard(city, Color.pink));
		cards.put("Market 4", new CityCard(city, Color.pink));
		cards.put("Bank 1", new CityCard(city, Color.lightGray));
		cards.put("Bank 2", new CityCard(city, Color.lightGray));
		cards.put("House 1", new CityCard(city, Color.cyan));
		cards.put("House 2", new CityCard(city, Color.cyan));
		cards.put("House 3", new CityCard(city, Color.cyan));
		cards.put("House 4", new CityCard(city, Color.cyan));
		cards.put("House 5", new CityCard(city, Color.cyan));
		cards.put("House 6", new CityCard(city, Color.cyan));
		cards.put("House 7", new CityCard(city, Color.cyan));
		cards.put("House 8", new CityCard(city, Color.cyan));
		cards.put("House 9", new CityCard(city, Color.cyan));
		cards.put("House 10", new CityCard(city, Color.cyan));
		cards.put("House 11", new CityCard(city, Color.cyan));
		cards.put("House 12", new CityCard(city, Color.cyan));
		cards.put("House 13", new CityCard(city, Color.cyan));
		cards.put("House 14", new CityCard(city, Color.cyan));
		cards.put("House 15", new CityCard(city, Color.cyan));
		cards.put("Apartment 1", new CityCard(city, Color.darkGray));
		cards.put("Apartment 2", new CityCard(city, Color.darkGray));
		cards.put("Apartment 3", new CityCard(city, Color.darkGray));
		cards.put("Apartment 4", new CityCard(city, Color.darkGray));
		cards.put("Apartment 5", new CityCard(city, Color.darkGray));
		cards.put("Apartment 6", new CityCard(city, Color.darkGray));
		cards.put("Apartment 7", new CityCard(city, Color.darkGray));
		cards.put("Apartment 8", new CityCard(city, Color.darkGray));
		cards.put("Apartment 9", new CityCard(city, Color.darkGray));
		cards.put("Apartment 10", new CityCard(city, Color.darkGray));
		cards.put("Apartment 11", new CityCard(city, Color.darkGray));
		cards.put("Apartment 12", new CityCard(city, Color.darkGray));
		cards.put("Apartment 13", new CityCard(city, Color.darkGray));
		cards.put("Apartment 14", new CityCard(city, Color.darkGray));
		cards.put("Apartment 15", new CityCard(city, Color.darkGray));
		
		cards.put("null", new CityCard(city, Color.gray));
		cards.put("Road", new CityCard(city, Color.DARK_GRAY));
		
		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}
		

		layout.show(this, "null");
	}
	
	public boolean addView(CityCard panel, String key) {
		if (cards.containsKey(key))
			return false;
		cards.put(key, panel);
		this.add(cards.get(key), key);
		return true;
	}
	
	public void setView(String key) {
		if (cards.containsKey(key)) {
			layout.show(this, key);
			//city.info.setText(key);
		}
	}

	
	public void actionPerformed(ActionEvent arg0) {
		
	}

	
	public void mouseClicked(MouseEvent e) {
		
	}

	
	public void mouseEntered(MouseEvent e) {
		
	}

	
	public void mouseExited(MouseEvent e) {
		
	}

	
	public void mousePressed(MouseEvent e) {
		
	}

	
	public void mouseReleased(MouseEvent e) {
		
	}

}
