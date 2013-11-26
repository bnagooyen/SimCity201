package simcity.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;

public class BuildingPanel extends JPanel {
        
        String myName;
        SimCityGui myCity;
        int x;
        int y;
        
        public BuildingPanel( Rectangle2D r, int i, SimCityGui sc ) {
                
                myName = "" + i;
                myCity = sc;
                
                setBackground( Color.LIGHT_GRAY );
                setMinimumSize( new Dimension( 500, 250 ) );
                setMaximumSize( new Dimension( 500, 250 ) );
                setPreferredSize( new Dimension( 500, 250 ) );
                
                JLabel j = new JLabel( myName );
                add( j );
        }
        
        public String getName() {
                return myName;
        }

        public void displayBuildingPanel() {
                myCity.displayBuildingPanel( this );
                
        }
}