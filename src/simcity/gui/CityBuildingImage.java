package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CityBuildingImage extends CityComponent {
	// g2.drawImage(restaurant, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth+parkingGap, null);
	public CityBuildingImage(int x, int y, BufferedImage i) {
		super(x, y, i);
//		rectangle = new Rectangle(x, y, 30, 35);
	}

	public CityBuildingImage(int x, int y, BufferedImage i, String I) {
		super(x, y, i, I);
	}

	public void updatePosition() {

	}
//
//	public void paint(Graphics g) {
//	
//	}
}
