package com.daratus.node.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.daratus.node.windows.NodeWindow;

public class ApplicationExitListener extends WindowAdapter implements ActionListener {

	private NodeWindow window;
	
	public ApplicationExitListener(NodeWindow window) {
		this.window = window;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		window.exit();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		window.exit();
	}
	
}
