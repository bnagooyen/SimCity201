package simcity.Market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.Market.MarketCustomerRole;

public class MCustomerGui implements Gui{
	
	private MarketCustomerRole role = null;
	
    private int xPos = -20, yPos = -20;
    private int xDestination = 0, yDestination = 0;
    
    boolean goingToCashier = true;
    
    public MCustomerGui(MarketCustomerRole r) {
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
		if(xPos == xDestination && yPos == yDestination) {
			if(goingToCashier) {
				role.msgAtCashier();
				goingToCashier = false;
			}
		}
	}

	public void draw(Graphics2D g) {
		 g.setColor(Color.CYAN);
	     g.fillRect(xPos,yPos, 20, 20);	
	     g.setColor(Color.BLACK);
	     g.drawString("C", xPos + 5, yPos + 15);		
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void DoGoToCashier(){
		xDestination = 130;
		yDestination = 100;
		goingToCashier = true;
	}
	public void DoGoHome() {
		xDestination = -20;
		yDestination = -20;
	}
}
