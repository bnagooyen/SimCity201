package simcity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class CityCard extends SimCityPanel implements MouseListener {

        public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;

        public CityCard(SimCityGui city) {
                super(city);
                this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
                this.setVisible(true);
                addMouseListener(this);
                setBackground(Color.green);
        }
        
        public CityCard(SimCityGui city, Color c) {
                super(city);
                this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
                this.setVisible(true);
                addMouseListener(this);
                setBackground(c);
        }


        public void mouseClicked(MouseEvent e) {
                
        }

        
        public void mouseEntered(MouseEvent e) {
                
        }

        
        public void mouseExited(MouseEvent e) {
                
        }

        
        public void mousePressed(MouseEvent e) {
                
        }

        
        public void mouseReleased(MouseEvent e) {
                
        }


        

}