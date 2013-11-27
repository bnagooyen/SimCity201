package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityApartment extends CityComponent {

	public CityApartment(int x, int y) {
		super(x, y, Color.cyan, "Apartment 1");
		rectangle = new Rectangle(x, y, 30, 35);
	}

	public CityApartment(int x, int y, String I) {
		super(x, y, Color.cyan, I);
		rectangle = new Rectangle(x, y, 30, 35);
	}

	public void updatePosition() {

	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, 30, 35);
	}
}