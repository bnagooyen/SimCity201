package simcity.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class BuildingPanel extends JPanel {

	TListPanel info;
	String name;
	public BuildingAnimationPanel panel;
	String type;
	SimCityGui gui;
	public boolean isVisible;
	
	BuildingPanel(SimCityGui restaurantGui, String nm) {
		this.gui=restaurantGui;
		setSize(875, 325);
		info = new TListPanel(restaurantGui, nm);
		
		if(nm.equals("Restaurant 1")) {
			panel = new RestaurantAnimationPanel1(restaurantGui, nm);
			type = "restaurant";
		}
		else if(nm.equals("Restaurant 2")) {
			panel = new RestaurantAnimationPanel2(restaurantGui, nm);
			type = "restaurant";
		}
		else if(nm.equals("Restaurant 3")) {
			panel = new RestaurantAnimationPanel3(restaurantGui, nm);
			type = "restaurant";
		}
		else if(nm.equals("Restaurant 4")) {
			panel = new RestaurantAnimationPanel4(restaurantGui, nm);
			type = "restaurant";
		}
		else if(nm.equals("Restaurant 5")) {
			panel = new RestaurantAnimationPanel5(restaurantGui, nm);
			type = "restaurant";
		}
		else if(nm.equals("Restaurant 6")) {
			panel = new RestaurantAnimationPanel6(restaurantGui, nm);
			type = "restaurant";
		}
		else if(nm.contains("Bank")) {
			panel = new BankAnimationPanel(restaurantGui, nm);
			type = "bank";
		}
		else if(nm.contains("Market")) {
			panel = new MarketAnimationPanel(restaurantGui, nm);
			type = "market";
		}
		else if(nm.contains("House")) {
			panel = new HouseAnimationPanel(restaurantGui, nm);
			type = "house";
		}
		else if(nm.contains("Apartment")) {
			panel = new ApartmentAnimationPanel(restaurantGui, nm);
			type = "apartment";
		}
		else if(nm.contains("Homeless Shelter")) {
			panel = new HomelessAnimationPanel(restaurantGui, nm);
			type = "homelessShelter";
		}
		isVisible = false;
		name = nm;
		setLayout(new BorderLayout());
		add(info, BorderLayout.WEST);
		add(panel, BorderLayout.CENTER);
		
	}
	
	public void setVisible() {
		//info.isVisible=true;
		panel.isVisible=true;
		panel.repaint();
	}
	public void setInvisible() {
		//info.isVisible=false;
		panel.isVisible=false;
	}
}
