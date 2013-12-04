package simcity.Bank.gui;



import java.awt.*;

import simcity.Bank.BankCustomerRole;
import simcity.gui.Gui;
import simcity.interfaces.BankCustomer;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;

public class BankCustomerGui implements Gui{

	private BankCustomer Role = null;
	private boolean isPresent = true;

	private BankManager manager;
	public Gui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, goToTellerPosition, goToLoanPosition, leaveBank};
	private Command command=Command.noCommand;

	public static final int xTeller = 255;
	public static final int yTeller = 125;
	public static final int xLoan = 175;
	public static final int yLoan = 125;
	
	public static final int customerSize = 20;
	public static final int xOffScreen = 100;
	public static final int yOffScreen = 350;

	public BankCustomerGui(BankCustomer BC, BankManager m) {
		Role = BC;
		xPos = 100;
		yPos = 350;
		xDestination = 100;
		yDestination = 400;
		manager = m;
		BC.setManager(m);
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
			if (command==Command.goToTellerPosition) Role.msgAtTellerPos();
			else if (command==Command.leaveBank) Role.msgAnimationFinishedLeaveBank();
			else if (command==command.goToLoanPosition) Role.msgAtLoanPos();
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
	
    public void goToTeller() {
    	xDestination = xTeller;
        yDestination = yTeller;
        command = Command.goToTellerPosition;
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
