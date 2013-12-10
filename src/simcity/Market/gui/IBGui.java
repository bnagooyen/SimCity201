package simcity.Market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.Market.InventoryBoyRole;

public class IBGui implements Gui {
	
private InventoryBoyRole role = null;
	
	private boolean isPresent;
	private int xPos = -20, yPos = 0;
    private int xDestination = 270, yDestination = 100;
    
    private String food = "";
    
    State state;
    
    enum State { goingToWork, leavingWork, gettingFood, gettingMoreFood, gotFood, goingToCashier, waiting, gone}
    
	public IBGui(InventoryBoyRole r) {
		this.role = r;
		state = State.goingToWork;
		DoGoToWaitingPos();
	}
	
	public void updatePosition() {
		// going to work
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
					state = State.waiting;
			}
			
		
		}
		
		// leaving work
		else if(state == State.leavingWork) {
			if(xPos > 150) {
				if(yPos > 30) {
			        if (yPos > yDestination)
				        yPos--;
				}
				if (xPos > xDestination)
			            xPos--;	
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
		
		// getting food
		else if(state == State.gettingFood) {
			if(xPos < 380) {
				if (xPos < xDestination)
		            xPos++;	
				if (yPos < yDestination)
			        yPos++;
			    else if (yPos > yDestination)
			        yPos--;
			}
			else {
				if (xPos < xDestination)
		            xPos++;
				if (yPos < yDestination)
			        yPos++;
			    else if (yPos > yDestination)
			        yPos--;
			}
			if(yPos == yDestination && xPos == xDestination){
				role.msgGotFood();
				state = State.gotFood;	
			}
		}
		// get more than one food in an order
		else if(state == State.gettingMoreFood) {
			if (xPos >380 && yPos != yDestination){
				if(xPos > 380) 
			           xPos--;	
			}
			else if( yPos != yDestination) {
				if (yPos < yDestination)
			        yPos++;
			    else if (yPos > yDestination)
			        yPos--;
			}
			else {
				if (xPos < xDestination)
		            xPos++;
			}
			if(yPos == yDestination && xPos == xDestination){
				role.msgGotFood();
				state = State.gotFood;	
			}
		}
		// going to cashier
		else if(state == State.goingToCashier){
			if(xPos > 380) {
				if (xPos > xDestination)
		            xPos--;	
			}
			else {
				if (xPos > xDestination)
		            xPos--;
				if (yPos < yDestination)
			        yPos++;
			    else if (yPos > yDestination)
			        yPos--;
			}
			if(yPos == yDestination && xPos == xDestination){
				role.msgGotFood();
				state = State.waiting;	
			}
		}
		
		// if going back to waiting pos
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
		g.setColor(Color.GREEN);
        g.fillRect(xPos,yPos, 20, 20);	
        g.setColor(Color.BLACK);
        g.drawString("ib", xPos + 2, yPos + 15);		
	}

	public boolean isPresent() {
		return true;
	}
	
	public void DoGoHome() {
		state = State.leavingWork;
		xDestination = -20;
		yDestination = 00;
	}
	public void DoGoToWaitingPos() {
		xDestination = 270;
		yDestination = 100;
	}
	
	public void DoGoToCashier() {
		state = State.goingToCashier;
		xDestination = 190;
		yDestination = 100;
	}
	
	public void DoGetSteak() {
		food = "Steak";
		if(state == State.gotFood) {
			state = State.gettingMoreFood;
		}
		else {
			state = State.gettingFood;
		}
		xDestination = 450;
		yDestination = 45;
	}
	
	public void DoGetChicken() {
		food = "Chicken";
		if(state == State.gotFood) {
			state = State.gettingMoreFood;
		}
		else {
			state = State.gettingFood;
		}
		xDestination = 450;
		yDestination = 120;
	}
	
	public void DoGetSalad() {
		food = "Salad";
		if(state == State.gotFood) {
			state = State.gettingMoreFood;
		}
		else {
			state = State.gettingFood;
		}
		xDestination = 450;
		yDestination = 195;
	}
	
	public void DoGetPizza() {
		food = "Salad";
		if(state == State.gotFood) {
			state = State.gettingMoreFood;
		}
		else {
			state = State.gettingFood;
		}
		xDestination = 450;
		yDestination = 230;
	}

	public void DoGetCar() {
		food = "Car";
		state = State.gettingFood;
		xDestination = 600;
		yDestination = 235;
	}
	
	public void setPresent(boolean b) {
		isPresent = b;
	}
	
}
