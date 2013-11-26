package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityMarket extends CityComponent {

	public CityMarket(int x, int y) {
		super(x, y, Color.orange, "Market 1");
		rectangle = new Rectangle(x, y, 30, 35);
	}

	public CityMarket(int x, int y, String I) {
		super(x, y, Color.orange, I);
		rectangle = new Rectangle(x, y, 30, 35);
	}

	public void updatePosition() {

	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, 30, 35);
	}
}
