package simcity.Market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.Market.MarketManagerRole;

public class MManagerGui implements Gui{
	
	private MarketManagerRole role = null;
	int homeposx = 0;
	int homeposy = 20;
    private int xPos = -20, yPos = -20;
    private int xDestination = 0, yDestination = 20;
    
    public MManagerGui(MarketManagerRole r) {
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
		 g.setColor(Color.GRAY);
	     g.fillRect(xPos,yPos, 20, 20);	
	     g.setColor(Color.BLACK);
	     g.drawString("M", xPos + 5, yPos + 15);
	}

	public boolean isPresent() {
		return true;
	}
	
	public void DoGoToWork() {
		xDestination = 0;
		yDestination = 20;
	}
	public void DoGoHome() {
		xDestination = -20;
		yDestination = -20;
	}
	
}
