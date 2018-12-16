package com.synechrone.message;

import lombok.Data;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;

@Data
public class TrayIconDemo implements ActionListener {

    private String message_url;
    private TrayIcon trayIcon;

    public static void main(String[] args) throws AWTException, MalformedURLException {
        if (SystemTray.isSupported()) {
            TrayIconDemo td = new TrayIconDemo();
            td.showMessage("The amazing needed arcticle!",
                    " Click here -> https://webengage.com/blog/web-push-notification-guide/",
                    "https://webengage.com/blog/web-push-notification-guide/");
        } else {
            System.err.println("System tray not supported!");
        }
    }

    TrayIconDemo() throws AWTException, MalformedURLException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        //Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icon.png"));

        //popupmenu
        PopupMenu trayPopupMenu = new PopupMenu();
        //2nd menuitem of popupmenu
        MenuItem close = new MenuItem("Exit");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //tray.remove(trayIcon);
                //trayIcon = null;
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);

        trayIcon = new TrayIcon(image, "Tray Demo", trayPopupMenu);
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray message demo");
        tray.add(trayIcon);
    }

    public void showMessage(String title, String message, String url) {
        message_url = url;
        trayIcon.displayMessage(title, message, MessageType.INFO); //MessageType.NONE
        trayIcon.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        // display the text file in the default app.
        try {
            Desktop.getDesktop().browse(new URI(message_url));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
