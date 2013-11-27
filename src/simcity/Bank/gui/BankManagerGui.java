package simcity.Bank.gui;



import java.awt.*;

import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;

public class BankManagerGui implements Gui{

	private BankManager Role = null;
	private boolean isPresent = true;
	public boolean isHungry = false;
	
	public BankGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, goToCorner, goToManagerPosition, goToTellerPosition, goToLoanPosition, leaveBank};
	private Command command=Command.noCommand;

	public static final int xCorner = 100;
	public static final int yCorner = 80;
	public static final int xTeller = 255;
	public static final int yTeller = 60;
	public static final int xLoan = 195;
	public static final int yLoan = 60;
	public static final int xManager = 215;
	public static final int yManager = 15;
	
	public static final int customerSize = 20;
	public static final int xOffScreen = 80;
	public static final int yOffScreen = 400;

	public BankManagerGui(BankManager Bman, BankGui gui) {
		Role = Bman;
		xPos = 80;
		yPos = 400;
		xDestination = 80;
		yDestination = 400;
		this.gui = gui;
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
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.goToCorner) Role.msgAnimationFinishedGoToCorner();
			else if (command==Command.leaveBank) Role.msgAnimationFinishedLeaveBank();
			else if (command==command.goToManagerPosition) Role.msgAtManagerPos();
			else if (command==command.goToTellerPosition) Role.msgAtTellerPos();
			else if (command==command.goToLoanPosition) Role.msgAtLoanPos();
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, customerSize, customerSize);
	}

	public boolean isPresent() {
		return true;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
    public void goToCorner() {
    	xDestination = xCorner;
        yDestination = yCorner;
        command = Command.goToCorner;
    }
    
    public void goToTellerPos() {
    	xDestination = xTeller;
        yDestination = yTeller;
        command = Command.goToTellerPosition;
    }
    
    public void goToManagerPos() {
    	xDestination = xManager;
        yDestination = yManager;
        command = Command.goToManagerPosition;
    }
    
    public void goToLoanPos() {
    	xDestination = xLoan;
        yDestination = yLoan;
        command = Command.goToLoanPosition;
    }
	
	public void DoExitBank() {
		xDestination = xOffScreen;
		yDestination = yOffScreen;
		command = Command.leaveBank;
	}
}
