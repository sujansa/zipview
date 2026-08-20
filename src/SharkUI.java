import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.BorderLayout;
import java.awt.Color;

public class SharkUI {
	public static final int DEFAULT_WIDTH = 800;

	public static final int DEFAULT_HEIGHT = 590;

	public JPanel previewPanel;

	public JPanel optionPanel;

	public JPanel listPanel;

	public JPanel infoPanel;

	public JPanel regexPanel;

	public JSplitPane splitPane;

	public JTextField regexField;

	public JLabel regexLabel;

	public JFileChooser fileChooser;

	public JFrame frame;

	public SharkUI() {
		initialize();
	}

	public void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Exception: setLookAndFeel() failed: " + e);
			e.printStackTrace();
		}
		frame = new JFrame("SharkUI");
		optionPanel = new JPanel();
		previewPanel = new JPanel();
		listPanel = new JPanel();
		infoPanel = new JPanel();
		regexPanel = new JPanel();
		optionPanel.setLayout(new java.awt.BorderLayout());
		previewPanel.setLayout(new java.awt.BorderLayout());
		infoPanel.setLayout(new java.awt.BorderLayout());
		regexPanel.setLayout(new java.awt.BorderLayout());
		// listPanel : GridLayout
		fileChooser = new JFileChooser();
		regexField = new JTextField(20);
		regexLabel = new JLabel("Regex:");
		infoPanel.setBackground(new Color(145, 160, 255));
		regexPanel.add(regexLabel, BorderLayout.WEST);
		regexPanel.add(regexField, BorderLayout.CENTER);
		optionPanel.add(regexPanel, BorderLayout.NORTH);
		optionPanel.add(new JScrollPane(listPanel), BorderLayout.CENTER);
		optionPanel.add(infoPanel, BorderLayout.WEST);
		infoPanel.add(new JLabel("Info"));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, optionPanel,
				new JScrollPane(previewPanel));
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(250);
		frame.add(splitPane);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		frame.setLocation(100, 100);
		// frame.pack();
	}

	public static void main(String[] argv) {
		SharkUI shark = new SharkUI();
		shark.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		shark.frame.setVisible(true);
		return;
	}
}