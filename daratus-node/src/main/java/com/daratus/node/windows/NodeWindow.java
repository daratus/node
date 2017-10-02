package com.daratus.node.windows;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.daratus.node.NodeContext;
import com.daratus.node.listeners.ApplicationExitListener;
import com.daratus.node.listeners.LoginListener;
import com.daratus.node.listeners.LogoutListener;
import com.daratus.node.listeners.StartStopListener;
import com.daratus.node.listeners.TrayMouseListener;

/**
 * Main application window type
 * 
 * @author Zilvinas Vaira
 * @copyright Copyright (c) 2016, RevRevDev LLC
 *
 */
public class NodeWindow extends JFrame {

    private static final long serialVersionUID = 1001L;

    private PopupMenu popupMenu;

    private TrayIcon trayIcon;

    private SystemTray systemTray;

    private Map<MenuSlot, MenuItem> menus = new EnumMap<MenuSlot, MenuItem>(MenuSlot.class);

    /**
     * Initiates main states, controls and listeners
     * 
     * @param title
     */
    public NodeWindow(String title, NodeContext context) {
        super(title);

        ApplicationExitListener exitListener = new ApplicationExitListener(this);
        this.addWindowListener(exitListener);

        if (SystemTray.isSupported()) {
            Image iconImage;
            try {
                iconImage = createImage("images/tray-d.png", "tray icon");
                trayIcon = new TrayIcon(iconImage);
                systemTray = SystemTray.getSystemTray();
                popupMenu = new PopupMenu();

                // MENU
                addTrayMenuItem(MenuSlot.LOGIN, "Login", new LoginListener());
                addTrayMenuItem(MenuSlot.LOGOUT, "Logout", new LogoutListener(context));
                addTrayMenuItem("Node Info");
                popupMenu.addSeparator();

                addTrayMenuItem(MenuSlot.START, "Start", new StartStopListener(context, true));
                addTrayMenuItem(MenuSlot.STOP, "Stop", new StartStopListener(context, false));
                popupMenu.addSeparator();

                MenuItem exitItem = addTrayMenuItem("Exit");
                exitItem.addActionListener(exitListener);

                trayIcon.setPopupMenu(popupMenu);
                trayIcon.addMouseListener(new TrayMouseListener());
                try {
                    systemTray.add(trayIcon);
                } catch (AWTException e) {
                    System.out.println("TrayIcon could not be added!");
                }

            } catch (MalformedURLException e1) {
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
    private MenuItem addTrayMenuItem(MenuSlot menuSlot, String title, ActionListener listener) {
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
     * @throws MalformedURLException
     */
    private Image createImage(String path, String description) throws MalformedURLException {
        URL imageURL = new File(path).toURI().toURL();

        // System.out.println(imageURL);
        if (imageURL == null) {
            System.err.println("Resource not found: " + path + "!");
            return null;
        } else {
            ImageIcon imageIcon = new ImageIcon(imageURL, description);
            Image image = imageIcon.getImage();
            if (image.getHeight(null) > 16) {
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics g = bufferedImage.createGraphics();
                g.drawImage(image, 0, 0, 16, 16, null);
                return bufferedImage;
            }
            return image;
        }
    }

    /**
     * Closes the application
     */
    public void exit() {
        this.dispose();
        System.exit(0);
    }

}
