package com.daratus.node.windows;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.http.HttpHost;

import com.daratus.node.APIConnector;
import com.daratus.node.ContextObserver;
import com.daratus.node.NodeCommand;
import com.daratus.node.NodeContext;
import com.daratus.node.NodeState;
import com.daratus.node.console.APICommand;
import com.daratus.node.listeners.ApplicationExitListener;
import com.daratus.node.listeners.LoginListener;
import com.daratus.node.listeners.LogoutListener;
import com.daratus.node.listeners.RegisterListener;
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
    
    private Map<NodeCommand, JButton> buttons = new EnumMap<NodeCommand, JButton>(NodeCommand.class);

    private JLabel nodeStateLabel = new JLabel("n/a");

    private JLabel hostDetailsLabel = new JLabel("n/a");
    
    private JLabel messageLabel = new JLabel(" ");
    
    /**
     * Initiates main states, controls and listeners
     * 
     * @param title
     */
    public NodeWindow(String title, NodeContext context) {
        super(title);
        this.context = context;
        context.setMessenger(new WindowsMessenger(this));
        context.addContextObserver(this);
        
        // WELCOME WINDOW
        Container contentPane = getContentPane();
        Image trayIconImage = null;
        try {
            List<Image> windowIconImages = new Vector<Image>();
            trayIconImage = createImage("logo16x16.png", "tray icon");
            windowIconImages.add(trayIconImage);
            windowIconImages.add(createImage("logo32x32.png", "tray icon"));
            windowIconImages.add(createImage("logo40x40.png", "tray icon"));
            windowIconImages.add(createImage("logo64x64.png", "tray icon"));
            windowIconImages.add(createImage("logo128x128.png", "tray icon"));
            this.setIconImages(windowIconImages);
        } catch (IOException e2) {
            System.out.println("Could not load application icons!");
        }
        
        
        // North panel
        JLabel welcomeLabel = new JLabel("<html><h1>Welcome to Daratus</h1></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(welcomeLabel, BorderLayout.NORTH);
        
        // Center panel
        Box centerBox = Box.createVerticalBox();
            centerBox.add(Box.createHorizontalStrut(500));

            centerBox.add(Box.createVerticalStrut(20));
            Dimension statusItemSize = new Dimension(400, 50); 
            Box statusBox = Box.createHorizontalBox();
            //statusBox.setBorder(BorderFactory.createLineBorder(Color.RED));
            statusBox.setSize(statusItemSize);
            statusBox.setMaximumSize(statusItemSize);
            statusBox.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                Box statusTitlesBox = Box.createVerticalBox();
                //statusTitlesBox.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                statusBox.add(statusTitlesBox);
                
                Box statusValuesBox = Box.createVerticalBox();
                //statusValuesBox.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                statusBox.add(statusValuesBox);
                
                JLabel nodeStateTitle = new JLabel("Node status: ");
                Font defaultTitleFont = nodeStateTitle.getFont();
                Font boldTitleFont = new Font(defaultTitleFont.getFontName(), Font.BOLD, defaultTitleFont.getSize());
                nodeStateTitle.setFont(boldTitleFont);
                nodeStateTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
                statusTitlesBox.add(nodeStateTitle);
                statusValuesBox.add(nodeStateLabel);

                JLabel hostDetailsTitle = new JLabel("API host details: ");
                hostDetailsTitle.setFont(boldTitleFont);
                hostDetailsTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
                statusTitlesBox.add(hostDetailsTitle);
                statusValuesBox.add(hostDetailsLabel);
                
            centerBox.add(statusBox);
            
            // Notice
            centerBox.add(Box.createVerticalStrut(50));
            JLabel noticeLabel = new JLabel("Some terms and conditions that user must agree should be placed here...");
            noticeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerBox.add(noticeLabel);
            centerBox.add(Box.createVerticalStrut(30));

            LoginListener loginListener = new LoginListener(APICommand.NODE_LOGIN_PATH, context);
            RegisterListener registerListener = new RegisterListener(APICommand.NODE_REGISTER_PATH, context);
            LogoutListener logoutListener = new LogoutListener(context);
            StartStopListener startListener = new StartStopListener(context, true);
            StartStopListener stopListener = new StartStopListener(context, false);
            
            // Authentication buttons
            Box mainButtonBox = Box.createHorizontalBox();
            mainButtonBox.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainButtonBox.add(addWelcomeWindowButton(NodeCommand.REGISTER, new JButton("Register"), registerListener));
                mainButtonBox.add(addWelcomeWindowButton(NodeCommand.LOGIN, new JButton("Login"), loginListener));
                mainButtonBox.add(addWelcomeWindowButton(NodeCommand.LOGOUT, new JButton("Logout"), logoutListener));
            centerBox.add(mainButtonBox);    
            centerBox.add(Box.createVerticalStrut(30));

            // Node buttons
            Box nodeButtonBox = Box.createHorizontalBox();
            nodeButtonBox.setAlignmentX(Component.CENTER_ALIGNMENT);
                nodeButtonBox.add(addWelcomeWindowButton(NodeCommand.START, new JButton("Start"), startListener));
                nodeButtonBox.add(addWelcomeWindowButton(NodeCommand.STOP, new JButton("Stop"), stopListener));
            centerBox.add(nodeButtonBox);    
            centerBox.add(Box.createVerticalStrut(30));
            
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerBox.add(messageLabel);
            centerBox.add(Box.createVerticalStrut(30));
            
        contentPane.add(centerBox);

        // Links
        FlowLayout southLayout = new FlowLayout(FlowLayout.LEFT, 20, 10);
        JPanel southPanel = new JPanel(southLayout);
            JLabel daratusLink = new JLabel("<html><a href=\"www.daratus.com\">www.daratus.com</a></html>");
            southPanel.add(daratusLink);
            JLabel statisticsLink = new JLabel("<html><a href=\"www.daratus.com\">www.daratus.com</a></html>");
            southPanel.add(statisticsLink);
            JLabel networkLink = new JLabel("<html><a href=\"www.daratus.com\">www.daratus.com</a></html>");
            southPanel.add(networkLink);
        contentPane.add(southPanel, BorderLayout.SOUTH);
        

        boolean isSystemTraySupported = SystemTray.isSupported();
        ApplicationExitListener exitListener = new ApplicationExitListener(this, isSystemTraySupported);
        this.addWindowListener(exitListener);

        // SYSTEM TRAY MENU
        if (isSystemTraySupported) {

            trayIcon = new TrayIcon(trayIconImage);
            systemTray = SystemTray.getSystemTray();
            popupMenu = new PopupMenu();

            // MENU
            addTrayMenuItem(NodeCommand.REGISTER, "Register", loginListener);
            addTrayMenuItem(NodeCommand.LOGIN, "Login", loginListener);
            addTrayMenuItem(NodeCommand.LOGOUT, "Logout", logoutListener);
            addTrayMenuItem("Node Info");
            popupMenu.addSeparator();

            addTrayMenuItem(NodeCommand.START, "Start", startListener);
            addTrayMenuItem(NodeCommand.STOP, "Stop", stopListener);
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

        } else {
            System.out.println("SystemTray is not supported!");
        }
    }
    
    
    
    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public JLabel getMessageLabel() {
        return messageLabel;
    }

    private JButton addWelcomeWindowButton(NodeCommand menuSlot, JButton button, ActionListener listener){
        button.addActionListener(listener);
        buttons.put(menuSlot, button);
        return button;
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
            /*if (image.getHeight(null) > 16) {
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics g = bufferedImage.createGraphics();
                g.drawImage(image, 0, 0, 16, 16, null);
                image = bufferedImage;
            }*/
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
        APIConnector apiConnector = context.getAPIConnector();
        HttpHost host = apiConnector.getHost();

        Set<NodeCommand> enabledCommands = currentState.getEnabledCommands();
        
        nodeStateLabel.setText("<html><p>" + currentState.getGreeting(context) + "</p></html>");
        hostDetailsLabel.setText("<html><p>" + host.toURI() + "</p></html>");
        
        Set<Entry<NodeCommand, MenuItem>> menuEntrySet = menus.entrySet();
        Iterator<Entry<NodeCommand, MenuItem>> menuIterator =  menuEntrySet.iterator();
        while (menuIterator.hasNext()) {
            Entry<NodeCommand, MenuItem> entry = menuIterator.next();
            NodeCommand nodeCommand = entry.getKey();
            MenuItem menuItem = entry.getValue();
            menuItem.setEnabled(enabledCommands.contains(nodeCommand));
        }
        
        Set<Entry<NodeCommand, JButton>> buttonsEntrySet = buttons.entrySet();
        Iterator<Entry<NodeCommand, JButton>> buttonsIterator =  buttonsEntrySet.iterator();
        while (buttonsIterator.hasNext()) {
            Entry<NodeCommand, JButton> entry = buttonsIterator.next();
            NodeCommand nodeCommand = entry.getKey();
            JButton button = entry.getValue();
            button.setEnabled(enabledCommands.contains(nodeCommand));
        }
    }

}
