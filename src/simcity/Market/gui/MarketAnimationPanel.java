package simcity.Market.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MarketAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 400;
    private final int WINDOWY = 350;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private final int framex = 0;
    private final int framey = 0;
    

    private final int timerDelay = 10;

    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

    public MarketAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
    	
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(timerDelay, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(framex,  framey, this.getWidth(), this.getHeight());
        
        
        // counter
        g2.setColor(Color.ORANGE);
        g2.fillRect(150, 50, 20, 100);

        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(400, 25, 100, 20);
        g2.fillRect(400, 100, 100, 20);
        g2.fillRect(400, 175, 100, 20);
        g2.fillRect(400, 250, 100, 20);
        g2.fillRect(600, 100, 50, 100);
        
        g.setColor(Color.BLACK);
        g.drawString("Steak", 435, 40);
        g.drawString("Chicken", 425, 115);
        g.drawString("Salad", 435, 190);
        g.drawString("Pizza", 435, 265);
        g.drawString("Car", 615, 150);
        
//        //make waiting area and waiter area
//        g2.setColor(Color.BLACK);
//        g2.drawLine(35, 35, 35, 300);
//        g2.drawLine(190, 30, 295, 30);
//        g2.drawLine(190, 0, 190, 30);
//        g2.drawLine(295, 0, 295, 30);
//        
//        //Here is the table
//        g2.setColor(Color.ORANGE);
//        g2.fillRect(tablex1, tabley1, tablew, tableh);//200 and 250 need to be table params
//
//        //g2.setColor(Color.ORANGE);
//        g2.fillRect(tablex2, tabley2, tablew, tableh);
//        g2.fillRect(tablex3, tabley3, tablew, tableh);
//        g2.fillRect(tablex4, tabley4, tablew, tableh);
//
//        // grill
//        g2.setColor(Color.black);
//        g2.fillRect(grillx, grilly, grillw, grillh);
//        
//        //plating area
//        g2.setColor(Color.gray);
//        g2.fillRect(platex, platey, platew, plateh);
//
//        // fridge
//        g2.setColor(Color.BLUE);
//        g2.fillRect(fridgex, fridgey, fridgew, fridgeh);
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        synchronized(guis) {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }

    public void addGui(IBGui gui) {
    	guis.add(gui);
    }
    public void addGui(MCustomerGui gui) {
        guis.add(gui);
    }
    public void addGui(MCashierGui gui) {
        guis.add(gui);
    }
    public void addGui(MManagerGui gui) {
        guis.add(gui);
    }
}
