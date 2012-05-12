package com.jgifcode.gif.applet;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jgifcode.gif.ResizeGifImage;

public class ResizeGifPanel extends JPanel implements ActionListener {

	private static String RESIZE_COMMAND = "Resize";
	private static String SELECT_FILE_COMMAND = "Select_file";

	JTextField srcTextField = null;
	JTextField widthField = null;
	JTextField heightField = null;

	public ResizeGifPanel() {
//		super(new FlowLayout(FlowLayout.LEADING));
		super(new GridLayout(5,3));
		JLabel srcLabel = new JLabel("Select the GIF Animation:");
		add(srcLabel);
		srcTextField = new JTextField("");
		srcTextField.setName("src_file");
//		srcTextField.setColumns(20);
		srcTextField.setEditable(Boolean.TRUE);
		add(srcTextField);

		JButton selectFileButton = new JButton("Select File..");
		selectFileButton.setActionCommand(SELECT_FILE_COMMAND);
		selectFileButton.addActionListener(this);
		add(selectFileButton);
		
		add(new JLabel("New Width:"));
		widthField = new JTextField("");
		widthField.setName("Width");
//		widthField.setColumns(2);
		widthField.setEditable(Boolean.TRUE);
		add(widthField);
		add(new JLabel(" "));
		
		add(new JLabel("New Height:"));
		heightField = new JTextField("");
		heightField.setName("Height");
//		heightField.setColumns(2);
		heightField.setEditable(Boolean.TRUE);
		add(heightField);
		add(new JLabel(" "));
		
		add(new JLabel(" "));
		JButton resizeButton = new JButton("Resize & Save");
		resizeButton.setActionCommand(RESIZE_COMMAND);
		resizeButton.addActionListener(this);
		add(resizeButton);
		add(new JLabel(" "));
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(RESIZE_COMMAND)) {
			Integer newW =0, newH=0;
			String fileName="" , dstFileName="";

			if (!widthField.getText().isEmpty()) {
				newW = Integer.parseInt(widthField.getText());
			}
			if (!heightField.getText().isEmpty()) {
				newH = Integer.parseInt(heightField.getText());
			}
			if (!srcTextField.getText().isEmpty()) {
				fileName = srcTextField.getText();
			}
			try {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"GIF Images", "gif");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(this.getRootPane());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String saveFile = chooser.getSelectedFile().getPath();
					if(!saveFile.toLowerCase().contains(".gif")){
						saveFile = saveFile + ".gif";
					}
					ResizeGifImage.resize(fileName, saveFile, newW, newH);
					MyDialog dlg = new MyDialog(null);
					dlg.setVisible(Boolean.TRUE);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("New Height:" + newH + " New width:" + newW + " file name:" + fileName + " destination file:" + dstFileName);

		} else if (e.getActionCommand().equals(SELECT_FILE_COMMAND)) {
			System.out.println("Selected the select file command");
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"GIF Images", "gif");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(this.getRootPane());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				srcTextField.setText(chooser.getSelectedFile().getPath());
			}

		}

	}

}

class MyDialog extends JDialog {
	  public MyDialog(JFrame parent) {
	    super(parent, "Resize dialog", true);
	       Container cp = getContentPane();
	    cp.setLayout(new GridLayout(2,1));
	    cp.add(new JLabel("Success"));
	    JButton ok = new JButton("OK");
	    ok.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        dispose(); // Closes the dialog
	      }
	    });
	    cp.add(ok);
	    setLocation(300,200);
	    setSize(150, 125);
	  }
	}
