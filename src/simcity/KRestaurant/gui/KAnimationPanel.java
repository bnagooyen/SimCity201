package simcity.KRestaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class KAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 400;
    private final int WINDOWY = 350;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private final int framex = 0;
    private final int framey = 0;
    
    //table dim
    private final int tablex1 = 200;
    private final int tabley1 = 200;
    private final int tablew = 50;
    private final int tableh = 50;
   
    private final int tablex2 = 350;
    private final int tabley2 = 200;
    
    private final int tablex3 = 500;
    private final int tabley3 = 200;
    
    private final int tablex4 = 650;
    private final int tabley4 = 200;
   
    private final int grillx = 750;
    private final int grilly = 0;
    private final int grillw = 30;
    private final int grillh = 100;
    
    private final int platex = 650;
    private final int platey = 0;
    private final int platew = 30;
    private final int plateh = 100;
    
    private final int fridgex = 675;
    private final int fridgey = 100;
    private final int fridgew = 75;
    private final int fridgeh = 30;
    private final int timerDelay = 10;

    private List<KGui> guis = Collections.synchronizedList(new ArrayList<KGui>());

    public KAnimationPanel() {
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
        
        //make waiting area and waiter area
        g2.setColor(Color.BLACK);
        g2.drawLine(35, 35, 35, 300);
        g2.drawLine(190, 30, 295, 30);
        g2.drawLine(190, 0, 190, 30);
        g2.drawLine(295, 0, 295, 30);
        
        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(tablex1, tabley1, tablew, tableh);//200 and 250 need to be table params

        //g2.setColor(Color.ORANGE);
        g2.fillRect(tablex2, tabley2, tablew, tableh);
        g2.fillRect(tablex3, tabley3, tablew, tableh);
        g2.fillRect(tablex4, tabley4, tablew, tableh);

        // grill
        g2.setColor(Color.black);
        g2.fillRect(grillx, grilly, grillw, grillh);
        
        //plating area
        g2.setColor(Color.gray);
        g2.fillRect(platex, platey, platew, plateh);

        // fridge
        g2.setColor(Color.BLUE);
        g2.fillRect(fridgex, fridgey, fridgew, fridgeh);
        
        for(KGui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        synchronized(guis) {
	        for(KGui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }

    public void addGui(KCookGui gui) {
    	guis.add(gui);
    }
    public void addGui(KCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(KWaiterGui gui) {
        guis.add(gui);
    }
    public void addGui(KFoodGui gui) {
    	guis.add(gui);
    }

    public void addGui(KMovingFoodGui gui) {
    	guis.add(gui);
    }
    
    public void addFoodOnTable(int table, String food) {
    	
    }
    public void foodArrived(int table) {
    	
    }
}
