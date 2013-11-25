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
        
        JFrame frame= new JFrame("Cardlayout");
        JPanel panelContainer= new JPanel();
        JPanel firstPanel=new JPanel();
        JPanel secondPanel= new JPanel();
        JButton firstButton=new JButton("enter restaurant");
        JButton secondButton=new JButton("enter city");
        CardLayout cardLayout= new CardLayout();
        
        
        public CityCard(SimCityGui city) {
        		
                super(city);
                
                panelContainer.setLayout(cardLayout);
                firstPanel.add(firstButton);
                firstPanel.add(secondButton);
                secondPanel.add(firstButton);
                secondPanel.add(secondButton);
                
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