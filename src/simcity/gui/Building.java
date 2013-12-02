package simcity.gui;

import java.awt.Color;
import java.awt.Rectangle;

public class Building {
	String name;
	Rectangle rectangle;
	int x, y;
	Color color;
	String ID;
	boolean isActive;
	public Building(int x, int y, Color c, String name) {
		rectangle = new Rectangle(x, y, 30, 35);
		this.x=x;
		this.y=y;
		color = c;
		this.name=name;
	}
	
	public boolean contains(int x, int y) {
		return rectangle.contains(x, y);
	}
}
