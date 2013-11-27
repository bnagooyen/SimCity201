package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityHomelessShelter extends CityComponent {

	public CityHomelessShelter(int x, int y) {
		super(x, y, Color.gray, "Homeless Shelter");
		rectangle = new Rectangle(x, y, 30, 35);
	}

	public CityHomelessShelter(int x, int y, String I) {
		super(x, y, Color.gray, I);
		rectangle = new Rectangle(x, y, 30, 35);
	}

	public void updatePosition() {

	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, 30, 35);
	}
}