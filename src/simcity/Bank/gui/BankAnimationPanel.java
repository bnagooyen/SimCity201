package simcity.Bank.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class BankAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    
    public static final int xCounter = 125;
    public static final int yCounter = 100;
    public static final int CounterSize = 25;
    public static final int CounterLength = 200;
    public static final int xDesk = 200;
    public static final int yDesk = 0;
    public static final int deskSize = 15;
    public static final int deskLength = 50;    
    
    //Constants for the kitchen
    public static final int xKitchen = 175;
    public static final int yKitchen = 225;
    public static final int xCashier = 50;
    public static final int yCashier = 275;
    public static final int roomSize = 50;
    public static final int windowSize = 100;
    public static final int xHome = 300;
    public static final int yHome = 180;
    
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public BankAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(7, this ); 		//adjust speed of animation
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        

        //Bank Counter
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(xCounter, yCounter, CounterLength, CounterSize);
        
        //Manager Desk
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(xDesk, yDesk, deskLength, deskSize);
      

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }

    }

    public void addGui(BankTellerGui gui) {
        guis.add(gui);
    }
    
    public void addGui(BankManagerGui gui) {
        guis.add(gui);
    }
    
    public void addGui(BankLoanGui gui) {
        guis.add(gui);
    }
    
    public void addGui(BankCustomerGui gui) {
        guis.add(gui);
    }
}
