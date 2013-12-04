package simcity.Bank.gui;



import java.awt.*;

import simcity.gui.Gui;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;

public class BankTellerGui implements Gui{

	private BankTeller Role = null;
	private boolean isPresent = false;
	public boolean isHungry = false;

	private BankManager manager;
	public Gui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, goToCorner, goToTellerPosition, leaveBank};
	private Command command=Command.noCommand;

	public static final int xCorner = 100;
	public static final int yCorner = 80;
	public static final int xTeller = 255;
	public static final int yTeller = 80;
	
	public static final int customerSize = 20;
	public static final int xOffScreen = 100;
	public static final int yOffScreen = 400;

	public BankTellerGui(BankTeller BT, BankManager m) {
		Role = BT;
		xPos = 50;
		yPos = 430;
		xDestination = 80;
		yDestination = 400;
		manager = m;
		Role.setManager(m);
		//this.gui = gui;
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
			else if (command==command.goToTellerPosition) Role.msgAtTellerPos();
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
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
	
	public void DoExitBank() {
		xDestination = xOffScreen;
		yDestination = yOffScreen;
		command = Command.leaveBank;
	}
}
