package com.sujan.crisbox.zipview;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.*;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.sujan.crisbox.tools.*;

public class BrowserPanel extends JPanel {
	final static boolean ALLOW_ROW_SELECTION = true;

	public JPanel westPanel;

	public JPanel northPanel;

	public JScrollPane pane;

	public BrowserPanel() {
		super(new BorderLayout());
		this.setOpaque(true); // content panes must be opaque
		westPanel = new JPanel();
		northPanel = new JPanel();

		add(westPanel, BorderLayout.WEST);
		add(northPanel, BorderLayout.NORTH);
	}
}
