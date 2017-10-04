package com.daratus.node.windows;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.daratus.node.ContextObserver;
import com.daratus.node.NodeCommand;
import com.daratus.node.NodeContext;
import com.daratus.node.NodeState;
import com.daratus.node.console.APICommand;
import com.daratus.node.listeners.ApplicationExitListener;
import com.daratus.node.listeners.LoginListener;
import com.daratus.node.listeners.LogoutListener;
import com.daratus.node.listeners.StartStopListener;
import com.daratus.node.listeners.TrayMouseListener;

/**
 * Main application window type
 * 
 * @author Zilvinas Vaira
 *
 */
public class NodeWindow extends JFrame implements ContextObserver{

    private static final long serialVersionUID = 1001L;
    
    private NodeContext context;

    private PopupMenu popupMenu;

    private TrayIcon trayIcon;

    private SystemTray systemTray;

    private Map<NodeCommand, MenuItem> menus = new EnumMap<NodeCommand, MenuItem>(NodeCommand.class);

    /**
     * Initiates main states, controls and listeners
     * 
     * @param title
     */
    public NodeWindow(String title, NodeContext context) {
        super(title);
        this.context = context;
        context.addContextObserver(this);

        ApplicationExitListener exitListener = new ApplicationExitListener(this);
        this.addWindowListener(exitListener);

        Container contentPane = getContentPane();
        JLabel welcomeLabel = new JLabel("<html><h1>Welcome to Daratus</h1></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(welcomeLabel, BorderLayout.NORTH);
        
        Box centerBox = Box.createVerticalBox();
        //centerBox.setBorder(BorderFactory.createLineBorder(Color.red, 1));
            centerBox.add(Box.createHorizontalStrut(500));

            centerBox.add(Box.createVerticalStrut(50));
            JLabel noticeLabel = new JLabel("Welcome to Daratus");
            noticeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerBox.add(noticeLabel);
            centerBox.add(Box.createVerticalStrut(30));
            
            JButton currentButton = new JButton("Start");
            currentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerBox.add(currentButton);
            centerBox.add(Box.createVerticalStrut(30));
            
        contentPane.add(centerBox);

        FlowLayout southLayout = new FlowLayout(FlowLayout.LEFT, 20, 10);
        JPanel southPanel = new JPanel(southLayout);
            JLabel daratusLink = new JLabel("<html><a href=\"www.daratus.com\">www.daratus.com</a></html>");
            southPanel.add(daratusLink);
            JLabel statisticsLink = new JLabel("<html><a href=\"www.daratus.com\">www.daratus.com</a></html>");
            southPanel.add(statisticsLink);
            JLabel networkLink = new JLabel("<html><a href=\"www.daratus.com\">www.daratus.com</a></html>");
            southPanel.add(networkLink);
        contentPane.add(southPanel, BorderLayout.SOUTH);
        
        if (SystemTray.isSupported()) {
            Image iconImage;
            try {
                iconImage = createImage("logo.png", "tray icon");
                trayIcon = new TrayIcon(iconImage);
                systemTray = SystemTray.getSystemTray();
                popupMenu = new PopupMenu();

                // MENU
                addTrayMenuItem(NodeCommand.LOGIN, "Login", new LoginListener(APICommand.NODE_PATH, context));
                addTrayMenuItem(NodeCommand.LOGOUT, "Logout", new LogoutListener(context));
                addTrayMenuItem("Node Info");
                popupMenu.addSeparator();

                addTrayMenuItem(NodeCommand.START, "Start", new StartStopListener(context, true));
                addTrayMenuItem(NodeCommand.STOP, "Stop", new StartStopListener(context, false));
                popupMenu.addSeparator();

                MenuItem exitItem = addTrayMenuItem("Exit");
                exitItem.addActionListener(exitListener);

                trayIcon.setPopupMenu(popupMenu);
                trayIcon.addMouseListener(new TrayMouseListener(this));
                try {
                    systemTray.add(trayIcon);
                } catch (AWTException e) {
                    System.out.println("TrayIcon could not be added!");
                }

            } catch (IOException e1) {
                System.out.println("TrayIcon image could not loaded!");
            }
        } else {
            System.out.println("SystemTray is not supported!");

        }
    }

    /**
     * Add tray menu item and store it on a particular menu slot (disabled by
     * default)
     * 
     * @param menuSlot
     *            Menu slot constant
     * @param title
     * @return
     */
    private MenuItem addTrayMenuItem(NodeCommand menuSlot, String title, ActionListener listener) {
        MenuItem menuItem = addTrayMenuItem(title);
        menuItem.setEnabled(false);
        menuItem.addActionListener(listener);
        menus.put(menuSlot, menuItem);
        return menuItem;
    }

    /**
     * Add tray menu item without storing it on a particular menu slot (enabled
     * by default)
     * 
     * @param title
     * @return
     */
    private MenuItem addTrayMenuItem(String title) {
        return popupMenu.add(new MenuItem(title));
    }

    /**
     * Retrieves a particular menu based on a provided slot constant
     * 
     * @param menuSlot
     * @return
     */
    public MenuItem getTrayMenuItem(int menuSlot) {
        return menus.get(menuSlot);
    }

    /**
     * Loads image object for tray icon
     * 
     * @param path
     * @param description
     * @return
     * @throws IOException 
     */
    private Image createImage(String path, String description) throws IOException {
        
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream(path);
        ImageIcon imageIcon = null;
        Image image = null;

        if(imageStream != null){
            imageIcon = new ImageIcon(ImageIO.read(imageStream));
        }else{
            URL imageURL = new File(path).toURI().toURL();
            if (imageURL == null) {
                System.err.println("Resource not found: " + path + "!");
            } else {
                imageIcon = new ImageIcon(imageURL, description);
            }
        }
        
        if(imageIcon != null){
            image = imageIcon.getImage();
            if (image.getHeight(null) > 16) {
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics g = bufferedImage.createGraphics();
                g.drawImage(image, 0, 0, 16, 16, null);
                image = bufferedImage;
            }
        }
        
        return image;
    }

    /**
     * Closes the application
     */
    public void exit() {
        context.exit();
        dispose();
        System.exit(0);
    }

    @Override
    public void notify(NodeContext context) {
        NodeState currentState = context.getCurrentState();
        Set<NodeCommand> enabledCommands = currentState.getEnabledCommands();
        Set<Entry<NodeCommand, MenuItem>> entrySet = menus.entrySet();
        Iterator<Entry<NodeCommand, MenuItem>> iterator =  entrySet.iterator();
        while (iterator.hasNext()) {
            Entry<NodeCommand, MenuItem> entry = iterator.next();
            NodeCommand nodeCommand = entry.getKey();
            MenuItem menuItem = entry.getValue();
            menuItem.setEnabled(enabledCommands.contains(nodeCommand));
        }
    }

}
