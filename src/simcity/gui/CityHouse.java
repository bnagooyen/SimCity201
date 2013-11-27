package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityHouse extends CityComponent {

	public CityHouse(int x, int y) {
		super(x, y, Color.blue, "House 1");
		rectangle = new Rectangle(x, y, 30, 35);
	}

	public CityHouse(int x, int y, String I) {
		super(x, y, Color.blue, I);
		rectangle = new Rectangle(x, y, 30, 35);
	}

	public void updatePosition() {

	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, 30, 35);
	}
}