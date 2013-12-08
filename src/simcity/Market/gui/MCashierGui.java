package simcity.Market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.Market.MarketCashierRole;

public class MCashierGui implements Gui{
	
	private MarketCashierRole role = null;
	private boolean isPresent = false;

	private int xPos = -45, yPos = 0;
    private int xDestination = 170, yDestination = 100;
    
    State state;
    
    enum State { goingToWork, leavingWork, atPosition, gone}


	public MCashierGui(MarketCashierRole r) {
		this.role = r;
		state = State.goingToWork;
	}
	
	public void updatePosition() {
		if(state == State.goingToWork) {
			if(xPos < 170) {
				if (xPos < xDestination)
		            xPos++;	
				if(yPos < 30) {
			        if (yPos < yDestination)
				        yPos++;
				}
			}
			else{
				if (xPos < xDestination)
		            xPos++;	
				if (yPos < yDestination)
			        yPos++;
			    else if (yPos > yDestination)
			        yPos--;
				if(yPos == yDestination && xPos == xDestination)
					state = State.atPosition;
			}
		}
		else if(state == State.leavingWork) {
			if(xPos > 150) {
				if(yPos > 30) {
			        if (yPos > yDestination)
				        yPos--;
				}
				else {
					if (xPos > xDestination)
			            xPos--;	
				}
			}
			else{
				if (xPos > xDestination)
		            xPos--;	
				if (yPos < yDestination)
			        yPos++;
			    else if (yPos > yDestination)
			        yPos--;
			}
			if(yPos == yDestination && xPos == xDestination)
				state = State.gone;
		}
		else{
			if (xPos < xDestination)
	            xPos++;
	        else if (xPos > xDestination)
	            xPos--;		
			if (yPos < yDestination)
		        yPos++;
		    else if (yPos > yDestination)
		        yPos--;
		}
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
		state = State.leavingWork;
		xDestination = -45;
		yDestination = 0;
	}
	
	public void DoGoToCounter() {
		state = State.goingToWork;
		xDestination = 170;
		yDestination = 100;
	}
}
