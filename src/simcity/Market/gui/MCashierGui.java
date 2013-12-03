package simcity.Market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.Market.MarketCashierRole;

public class MCashierGui implements Gui{
	
	private MarketCashierRole role = null;
	private boolean isPresent = false;

	int homeposx = 170;
	int homeposy = 100;
    private int xPos = -20, yPos = -20;
    private int xDestination = 170, yDestination = 100;

	public MCashierGui(MarketCashierRole r) {
		this.role = r;
	}
	
	public void updatePosition() {
		if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;		
		if (yPos < yDestination)
	        yPos++;
	    else if (yPos > yDestination)
	        yPos--;
	}

	
	public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos,yPos, 20, 20);	
        g.setColor(Color.BLACK);
        g.drawString("$", xPos + 7, yPos + 15);
	}

	
	public boolean isPresent() {
		return true;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoHome() {
		xDestination = -20;
		yDestination = -20;
	}
	
	public void DoGoToCounter() {
		xDestination = 170;
		yDestination = 100;
	}
}
