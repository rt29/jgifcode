package com.jgifcode.gif.applet;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

public class ResizeGifApplet extends JApplet{

	private static String SELECT_FILE_COMMAND = "Select_file";
	 //Called when this applet is loaded into the browser.
    public void init() {
        //Execute a job on the event-dispatching thread; creating this applet's GUI.
        setSize(500, 150);
    	try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) { 
            System.err.println("createGUI didn't complete successfully");
        }
        
    }
    
    private void createGUI() {
        //Create and set up the content pane.
        ResizeGifPanel newContentPane = new ResizeGifPanel();
        newContentPane.setOpaque(true); 
        setContentPane(newContentPane);        
    }

}
