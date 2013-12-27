/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class BuildingAnimationPanel extends JPanel implements ActionListener {

	   public boolean isVisible;
	   private final int WINDOWX = 575;
	   private final int WINDOWY = 325;
	   private Dimension bufferSize;
	   
	   private SimCityGui gui;
	   protected List<Gui> guis = new ArrayList<Gui>();
	   
	   public BuildingAnimationPanel(SimCityGui restaurantGui) {
	    	this.gui=restaurantGui;
	    	this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
	        setVisible(true);
	        
	        bufferSize = this.getSize();
	        
	        Timer timer = new Timer(20, this );
	    	timer.start();
	    	timer.addActionListener(this);
	        
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
			if(isVisible)
			repaint();  //Will have paintComponent called
		}

		public abstract void paintComponent(Graphics g);
		public abstract void addGui(Gui g);
}
