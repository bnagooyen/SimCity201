package simcity.housing.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class HouseAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public HouseAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
        int timerLength = 20; 
    	Timer timer = new Timer(timerLength, this);
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        int beginX = 0; 
        int beginY = 0; 
        g2.setColor(getBackground());
        g2.fillRect(beginX, beginY, WINDOWX, WINDOWY );

        //Here is the bed
        int w = 50; 
        int h = 80; 
        g2.setColor(Color.cyan);
        g2.fillRect(0, 0, w, h);
        
        int grillW = 40; 
        int grillH = 50; 
        g2.setColor(Color.RED);
        g2.fillRect(440, 140, grillW, grillH);
        

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(PersonGui gui) {
        guis.add(gui);
    }

}