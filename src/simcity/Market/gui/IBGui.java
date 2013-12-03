package simcity.Market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.Market.InventoryBoyRole;

public class IBGui implements Gui {
	
	private InventoryBoyRole role = null;
	
	private boolean isPresent;
	private int xPos = -20, yPos = -20;
    private int xDestination = 270, yDestination = 100;
    
    boolean gettingFood = false;
	public IBGui(InventoryBoyRole r) {
		this.role = r;
		DoGoToWaitingPos();
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
		
		if(yPos == yDestination && xPos == xDestination){
			if(gettingFood) {
				role.msgGotFood();
				gettingFood = false;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
        g.fillRect(xPos,yPos, 20, 20);	
        g.setColor(Color.BLACK);
        g.drawString("ib", xPos + 2, yPos + 15);		
	}

	public boolean isPresent() {
		return true;
	}
	
	public void DoGoHome() {
		xDestination = -20;
		yDestination = -20;
	}
	public void DoGoToWaitingPos() {
		xDestination = 250;
		yDestination = 100;
	}
	
	public void DoGoToCashier() {
		gettingFood = true;
		xDestination = 190;
		yDestination = 100;
	}
	
	public void DoGetSteak() {
		gettingFood = true;
		xDestination = 450;
		yDestination = 40;
	}
	
	public void DoGetChicken() {
		gettingFood = true;
		xDestination = 450;
		yDestination = 115;
	}
	
	public void DoGetSalad() {
		gettingFood = true;
		xDestination = 450;
		yDestination = 190;
	}
	
	public void DoGetPizza() {
		gettingFood = true;
		xDestination = 450;
		yDestination = 235;
	}

	public void setPresent(boolean b) {
		isPresent = b;
	}
	
}
