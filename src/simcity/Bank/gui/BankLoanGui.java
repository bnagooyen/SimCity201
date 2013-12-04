package simcity.Bank.gui;



import java.awt.*;

import simcity.gui.Gui;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;

public class BankLoanGui implements Gui{

	private BankLoanOfficer Role = null;
	private boolean isPresent = true;

	private BankManager manager;
	public Gui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, goToCorner, goToLoanPosition, leaveBank};
	private Command command=Command.noCommand;

	public static final int xCorner = 100;
	public static final int yCorner = 80;
	public static final int xLoan = 175;
	public static final int yLoan = 80;
	
	public static final int customerSize = 20;
	public static final int xOffScreen = 100;
	public static final int yOffScreen = 350;

	public BankLoanGui(BankLoanOfficer BLO, Gui gui, BankManager m) {
		Role = BLO;
		xPos = 100;
		yPos = 350;
		xDestination = 100;
		yDestination = 400;
		manager = m;
		Role.setManager(m);
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
	
    public void goToCorner() {
    	xDestination = xCorner;
        yDestination = yCorner;
        command = Command.goToCorner;
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
