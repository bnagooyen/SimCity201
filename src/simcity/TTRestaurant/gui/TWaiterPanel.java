package simcity.TTRestaurant.gui;

import simcity.TTRestaurant.TWaiterRole;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the waiters
 */
public class TWaiterPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JTextField addMessage = new JTextField (10); 
    private JButton addWaiter = new JButton("Add");
    private TRestaurantPanel restPanel;
    private String type;

    
    public TWaiterPanel(TRestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Dimension messagePanel = getPreferredSize(); 
        Dimension messageSize = new Dimension(messagePanel.width + 20,
                (int) (messagePanel.height));
        addMessage.setPreferredSize(messageSize);
        addMessage.setMinimumSize(messageSize);
        addMessage.setMaximumSize(messageSize);
        add(addMessage);
        addWaiter.addActionListener(this);
        add(addWaiter);
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addWaiter) {
        	if (!addMessage.getText().equals("")) {
        		addPerson(addMessage.getText());
        	}
        }
        else {

        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
            }
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name);
            validate();
            
            
        }
    }
}
