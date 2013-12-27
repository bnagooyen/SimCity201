/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

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

	public static final int NUMRESTAURANTS = 6;
//	HashMap<String, CityCard> cards;
	HashMap<String, BuildingPanel> cards;
	SimCityGui city;
	public static final int VIEW_WIDTH = 875, VIEW_HEIGHT = 325;
	CardLayout layout;
	
	public CityView(SimCityGui restaurantGui, HashMap<String, BuildingPanel> myp) {
	
		
		this.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		this.city = restaurantGui;
		cards = new HashMap<String, BuildingPanel>();
		cards = myp;
		

		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}
		

		layout.show(this, "Restaurant 3");
	}
	
//	public boolean addView(CityCard panel, String key) {
//		if (cards.containsKey(key))
//			return false;
//		cards.put(key, panel);
//		this.add(cards.get(key), key);
//		return true;
//	}

	public boolean addView(BuildingPanel panel, String key) {
		if (cards.containsKey(key))
			return false;
		cards.put(key, panel);
		this.add(cards.get(key), key);
		return true;
	}

	
	public void setView(String key) {
		if (cards.containsKey(key)) {
			cards.get(key).setVisible();
			layout.show(this, key);
//			for(int i=0; i<cards.size(); i++) {
//				if(cards.get(i).isVisible) {
//					cards.get(i).setInvisible();
//				}
//			}
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
