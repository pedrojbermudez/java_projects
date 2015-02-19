package com.family.account.mainProgram;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import com.family.account.libraries.DataBase;

public class MainSource extends JPanel implements ActionListener {
	private static final long serialVersionUID = -3040429246924352278L;

	private String[] columnNames;
	private Object[][] rawData;
	private ArrayList<String[]> listData;
	private int numCol;
	private final String ENTER = "Enter";
	private final String CANCEL = "Cancel";
	private final String EDIT = "Edit";
	private final static String[] SOURCE_NAME_COL = { "#", "Name", "Total" };
	private final static String[] MOVEMENT_NAME_COL = { "#", "Name", "Source",
			"Date", "Income", "Outgoing" };
	private final static String DATABASE_PATH = System.getProperty("user.home")
			+ File.separator + "test_jarrr";
	private static JFrame frame = new JFrame("Family Accounting");
	private static JFrame frame2 = new JFrame("Family Accounting");
	private static JTextField name, total, income, outgoing;
	private static JComboBox<String> list;
	private static DataBase db = new DataBase(DATABASE_PATH);
	private JTable table;
	private int rowSelected;
	
	public MainSource(int mode) {
		super(new GridLayout(1, 0));
		switch (mode) {
		case 0:
			listData = db.getSources();
			numCol = 3;
			columnNames = SOURCE_NAME_COL;
			if (listData == null) {
				rawData = null;
			} else {
				rawData = new Object[listData.size()][numCol];
			}
			if (rawData != null) {
				for (int i = 0; i < listData.size(); i++) {
					rawData[i] = listData.get(i);
				}
				createTable(rawData, columnNames, numCol);
			} else {
				createTable(columnNames, numCol);
			}
			break;
		case 1:
			listData = db.getMovements();
			numCol = 6;
			columnNames = MOVEMENT_NAME_COL;
			if (listData == null) {
				rawData = null;
			} else {
				rawData = new Object[listData.size()][numCol];
			}
			if (rawData != null) {
				for (int i = 0; i < listData.size(); i++) {
					String[] tmp = { listData.get(i)[0], listData.get(i)[1],
							listData.get(i)[2], listData.get(i)[3],
							listData.get(i)[4], listData.get(i)[5] };
					rawData[i] = tmp;
				}
				createTable(rawData, columnNames, numCol);
			} else {
				createTable(columnNames, numCol);
			}
			break;
		}
	}

	public MainSource(int mode, int id){
		JLabel header;
		JButton acceptButton = new JButton(ENTER);
		JButton cancelButton = new JButton(CANCEL);
		String[] getter;
		
		frame2 = new JFrame();
		frame2.setAlwaysOnTop(true);
		Container pane = frame2.getContentPane();
		Insets insets = frame2.getInsets();

		pane.setLayout(null);
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		
		switch(mode){
		case 0:
			rowSelected = table.getSelectedRow();
			getter = db.getSource(id);
			getter.equals("");
			frame2.setTitle("New Source");
			header = new JLabel("Name of source:");
			pane.add(header);
			header.setBounds(insets.left + 5, insets.top + 5,
					header.getPreferredSize().width,
					header.getPreferredSize().height);
			name = new JTextField(20);
			total = new JTextField(10);
			acceptButton.setActionCommand("create_source");
			acceptButton.addActionListener(this);
			name.addActionListener(this);
			pane.add(name);
			name.setBounds(header.getX() + header.getWidth() + 5,
					insets.top + 5, name.getPreferredSize().width,
					name.getPreferredSize().height);

			header = new JLabel("Total money on the source:");
			pane.add(header);
			header.setBounds(insets.left + 5, name.getY() + name.getHeight()
					+ 5, header.getPreferredSize().width,
					header.getPreferredSize().height);
			pane.add(total);
			pane.add(acceptButton);
			pane.add(cancelButton);
			total.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), total.getPreferredSize().width,
					total.getPreferredSize().height);
			acceptButton.setBounds(insets.left + 100,
					header.getY() + header.getHeight() + 20,
					acceptButton.getPreferredSize().width,
					acceptButton.getPreferredSize().height);
			cancelButton.setBounds(
					acceptButton.getX() + acceptButton.getWidth() + 5,
					acceptButton.getY(), cancelButton.getPreferredSize().width,
					cancelButton.getPreferredSize().height);
			frame2.setMinimumSize(new Dimension(360, 120));
			break;
		case 1:
			ArrayList<String[]> sourceList = db.getSources();
			ArrayList<String> tmp = new ArrayList<String>();

			frame.setTitle("New Movement");

			for (int i = 0; i < sourceList.size(); i++) {
				tmp.add(sourceList.get(i)[1]);
			}

			list = new JComboBox<String>(tmp.toArray(new String[tmp.size()]));

			// Name for movement.
			header = new JLabel("Enter the name for this movement: ");
			name = new JTextField(30);
			pane.add(header);
			pane.add(name);
			header.setBounds(insets.left + 5, insets.top + 5,
					header.getPreferredSize().width,
					header.getPreferredSize().height);
			name.setBounds(header.getX() + header.getWidth() + 5,
					insets.top + 5, name.getPreferredSize().width,
					name.getPreferredSize().height);

			// Select a source
			header = new JLabel("Select the money source");
			pane.add(header);
			pane.add(list);
			header.setBounds(insets.left + 5, name.getY() + name.getHeight()
					+ 10, header.getPreferredSize().width,
					header.getPreferredSize().height);
			list.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), list.getPreferredSize().width,
					list.getPreferredSize().height);

			// A little info.
			JLabel info = new JLabel("Introduce one:");
			pane.add(info);
			info.setBounds(insets.left + 5, list.getY() + list.getHeight() + 5,
					info.getPreferredSize().width,
					info.getPreferredSize().height);

			// Income value.
			header = new JLabel("Income: ");
			income = new JTextField(10);
			pane.add(header);
			pane.add(income);
			header.setBounds(insets.left + 20, info.getY() + info.getHeight()
					+ 5, header.getPreferredSize().width,
					header.getPreferredSize().height);
			income.setBounds(header.getX() + header.getWidth() + 21,
					header.getY(), income.getPreferredSize().width,
					income.getPreferredSize().height);

			// Outgoing value.
			header = new JLabel("Outgoing: ");
			outgoing = new JTextField(10);
			pane.add(header);
			pane.add(outgoing);
			header.setBounds(insets.left + 20,
					income.getY() + income.getHeight() + 5,
					header.getPreferredSize().width,
					header.getPreferredSize().height);
			outgoing.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), outgoing.getPreferredSize().width,
					outgoing.getPreferredSize().height);

			// Button creation.
			acceptButton.setActionCommand("create_movement");
			acceptButton.addActionListener(this);
			pane.add(acceptButton);
			pane.add(cancelButton);
			acceptButton.setBounds(insets.left + 5,
					outgoing.getY() + outgoing.getHeight() + 10,
					acceptButton.getPreferredSize().width,
					acceptButton.getPreferredSize().height);
			cancelButton.setBounds(
					acceptButton.getX() + acceptButton.getWidth() + 5,
					acceptButton.getY(), cancelButton.getPreferredSize().width,
					cancelButton.getPreferredSize().height);

			frame2.setMinimumSize(new Dimension(800, 500));
			break;
		}
	}
	
	public MainSource(int mode, boolean activated) {
		JLabel header;
		JButton acceptButton = new JButton(ENTER);
		JButton cancelButton = new JButton(CANCEL);

		frame2 = new JFrame();
		frame2.setAlwaysOnTop(true);
		Container pane = frame2.getContentPane();
		Insets insets = frame2.getInsets();

		pane.setLayout(null);
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		
		switch (mode) {
		case 0:
			frame2.setTitle("New Source");
			header = new JLabel("Name of source:");
			pane.add(header);
			header.setBounds(insets.left + 5, insets.top + 5,
					header.getPreferredSize().width,
					header.getPreferredSize().height);
			name = new JTextField(20);
			total = new JTextField(10);
			acceptButton.setActionCommand("create_source");
			acceptButton.addActionListener(this);
			name.addActionListener(this);
			pane.add(name);
			name.setBounds(header.getX() + header.getWidth() + 5,
					insets.top + 5, name.getPreferredSize().width,
					name.getPreferredSize().height);

			header = new JLabel("Total money on the source:");
			pane.add(header);
			header.setBounds(insets.left + 5, name.getY() + name.getHeight()
					+ 5, header.getPreferredSize().width,
					header.getPreferredSize().height);
			pane.add(total);
			pane.add(acceptButton);
			pane.add(cancelButton);
			total.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), total.getPreferredSize().width,
					total.getPreferredSize().height);
			acceptButton.setBounds(insets.left + 100,
					header.getY() + header.getHeight() + 20,
					acceptButton.getPreferredSize().width,
					acceptButton.getPreferredSize().height);
			cancelButton.setBounds(
					acceptButton.getX() + acceptButton.getWidth() + 5,
					acceptButton.getY(), cancelButton.getPreferredSize().width,
					cancelButton.getPreferredSize().height);
			frame2.setMinimumSize(new Dimension(360, 120));
			break;
		case 1:
			ArrayList<String[]> sourceList = db.getSources();
			ArrayList<String> tmp = new ArrayList<String>();

			frame.setTitle("New Movement");

			for (int i = 0; i < sourceList.size(); i++) {
				tmp.add(sourceList.get(i)[1]);
			}

			list = new JComboBox<String>(tmp.toArray(new String[tmp.size()]));

			// Name for movement.
			header = new JLabel("Enter the name for this movement: ");
			name = new JTextField(30);
			pane.add(header);
			pane.add(name);
			header.setBounds(insets.left + 5, insets.top + 5,
					header.getPreferredSize().width,
					header.getPreferredSize().height);
			name.setBounds(header.getX() + header.getWidth() + 5,
					insets.top + 5, name.getPreferredSize().width,
					name.getPreferredSize().height);

			// Select a source
			header = new JLabel("Select the money source");
			pane.add(header);
			pane.add(list);
			header.setBounds(insets.left + 5, name.getY() + name.getHeight()
					+ 10, header.getPreferredSize().width,
					header.getPreferredSize().height);
			list.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), list.getPreferredSize().width,
					list.getPreferredSize().height);

			// A little info.
			JLabel info = new JLabel("Introduce one:");
			pane.add(info);
			info.setBounds(insets.left + 5, list.getY() + list.getHeight() + 5,
					info.getPreferredSize().width,
					info.getPreferredSize().height);

			// Income value.
			header = new JLabel("Income: ");
			income = new JTextField(10);
			pane.add(header);
			pane.add(income);
			header.setBounds(insets.left + 20, info.getY() + info.getHeight()
					+ 5, header.getPreferredSize().width,
					header.getPreferredSize().height);
			income.setBounds(header.getX() + header.getWidth() + 21,
					header.getY(), income.getPreferredSize().width,
					income.getPreferredSize().height);

			// Outgoing value.
			header = new JLabel("Outgoing: ");
			outgoing = new JTextField(10);
			pane.add(header);
			pane.add(outgoing);
			header.setBounds(insets.left + 20,
					income.getY() + income.getHeight() + 5,
					header.getPreferredSize().width,
					header.getPreferredSize().height);
			outgoing.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), outgoing.getPreferredSize().width,
					outgoing.getPreferredSize().height);

			// Button creation.
			acceptButton.setActionCommand("create_movement");
			acceptButton.addActionListener(this);
			pane.add(acceptButton);
			pane.add(cancelButton);
			acceptButton.setBounds(insets.left + 5,
					outgoing.getY() + outgoing.getHeight() + 10,
					acceptButton.getPreferredSize().width,
					acceptButton.getPreferredSize().height);
			cancelButton.setBounds(
					acceptButton.getX() + acceptButton.getWidth() + 5,
					acceptButton.getY(), cancelButton.getPreferredSize().width,
					cancelButton.getPreferredSize().height);

			frame2.setMinimumSize(new Dimension(800, 500));
			break;
				}

		frame2.setResizable(false);
		frame2.pack();
		frame2.setVisible(true);
	}

	public JTable createTable(Object[][] data, String[] colsName, int numCols) {
		table = new JTable(data, colsName);
		table.setPreferredScrollableViewportSize(new Dimension(800, 600));
		table.setFillsViewportHeight(true);
		TableColumn col = null;
		col = table.getColumnModel().getColumn(0);
		col.setMinWidth(100);
		col.setPreferredWidth(50);

		for (int i = 1; i < colsName.length; i++) {
			col = table.getColumnModel().getColumn(i);
			if (col.getHeaderValue().toString().contains("Name")
					|| col.getHeaderValue().toString().contains("Source")) {
				col.setPreferredWidth(50);
				col.setMinWidth(300);
			} else if (col.getHeaderValue().toString().contains("Total")
					|| col.getHeaderValue().toString().contains("Income")
					|| col.getHeaderValue().toString().contains("Outgoing")) {
				col.setPreferredWidth(50);
				col.setMinWidth(100);
			} else {
				col.setPreferredWidth(50);
				col.setMinWidth(100);
			}
		}

		JScrollPane scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	public JTable createTable(String[] colsName, int numCols) {
		table = new JTable(0, numCol);
		table.setPreferredScrollableViewportSize(new Dimension(800, 600));
		table.setFillsViewportHeight(true);
		TableColumn col = null;
		for (int i = 0; i < colsName.length; i++) {
			col = table.getColumnModel().getColumn(i);
			if (i == 0) {
				col.setMinWidth(100);
				col.setPreferredWidth(50);
			} else {
				col.setPreferredWidth(50);
				col.setMinWidth(250);
			}
			col.setHeaderValue(colsName[i]);
		}
		JScrollPane scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// File menu
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);

		// Sub-menu new
		submenu = new JMenu("New");
		submenu.setMnemonic(KeyEvent.VK_N);

		menuItem = new JMenuItem("Source");
		menuItem.setActionCommand("new_source");
		menuItem.addActionListener(this);
		// menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
		// ActionEvent.ALT_MASK));
		submenu.add(menuItem);

		menuItem = new JMenuItem("Movement");
		submenu.add(menuItem);
		menuItem.setActionCommand("new_movement");
		menuItem.addActionListener(this);
		menu.add(submenu);

		// Sub-menu Edit
		submenu = new JMenu("Edit");
		submenu.setMnemonic(KeyEvent.VK_E);
		menuItem = new JMenuItem("Source");
		menuItem.setActionCommand("edit_source");
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Movement");
		menuItem.setActionCommand("edit_movement");
		menuItem.addActionListener(this);
		submenu.add(menuItem);
		menu.add(submenu);

		menu.addSeparator();

		ImageIcon icon = new ImageIcon("/home/pedro/Imágenes/imagen.jpeg");
		menuItem = new JMenuItem("Exit", icon);
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.setActionCommand("exit");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		// View menu
		menu = new JMenu("View");
		menuItem = new JMenuItem("Source");
		menuItem.setActionCommand("view_source");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem("Movements");
		menuItem.setActionCommand("view_movements");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);
		return menuBar;
	}

	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "new_source":
			createAndShowGUI(0, false);
			break;
		case "new_movement":
			createAndShowGUI(1, false);
			break;
		case "edit_source":
			break;
		case "edit_movement":
			break;
		case "view_source":
			createAndShowGUI(0);
			break;
		case "view_movements":
			createAndShowGUI(1);
			break;
		case "create_source":
			db.newSource(name.getText(), Double.parseDouble(total.getText()));
			frame2.dispatchEvent(new WindowEvent(frame2,
					WindowEvent.WINDOW_CLOSING));
			createAndShowGUI(0);
			break;
		case "create_movement":
			String incomeResult,
			outgoingResult;
			if ((!income.getText().equals("") || !outgoing.getText().equals(""))
					&& !name.getText().equals("")) {
				if (income.getText().equals("")) {
					incomeResult = "0.0";
				} else {
					incomeResult = income.getText().replace(',', '.');
				}

				if (outgoing.getText().equals("")) {
					outgoingResult = "0.0";
				} else {
					outgoingResult = outgoing.getText().replace(',', '.');
				}
				System.out.println("Regex works for incomeResult("
						+ incomeResult + "): "
						+ incomeResult.matches("\\d+([.]\\d{1,2})?"));
				System.out.println("Regex works for outgoingResults("
						+ outgoingResult + "): "
						+ outgoingResult.matches("\\d+([.]\\d{1,2})?"));
				if (incomeResult.matches("\\d+([.]\\d{1,2})?")
						&& outgoingResult.matches("\\d+([.]\\d{1,2})?")) {
					System.out.println("Cumple");
					db.newMovement(list.getSelectedIndex() + 1, name.getText(),
							"2015/12/05", Double.parseDouble(incomeResult),
							Double.parseDouble(outgoingResult));
					frame2.dispatchEvent(new WindowEvent(frame2,
							WindowEvent.WINDOW_CLOSING));
				} else {
					JOptionPane.showMessageDialog(null,
							"Number format incorrect");
				}
			} else {
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Name can't be empty.");
				} else {
					JOptionPane.showMessageDialog(null,
							"Both income and outgoing can't be empty.");
				}

			}
			createAndShowGUI(1);
			break;
		case "cancel":
			frame2.dispatchEvent(new WindowEvent(frame2,
					WindowEvent.WINDOW_CLOSING));
			break;
		case "exit":
			System.exit(0);
			break;
		}
	}

	private static JFrame createAndShowGUI(int mode) {
		MainSource mainSource = new MainSource(mode);
		JMenuBar menuBar = mainSource.createMenuBar();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		mainSource.setOpaque(true);
		frame.setContentPane(mainSource);
		frame.pack();
		frame.setVisible(true);
		frame.revalidate();
		frame.repaint();
		return frame;
	}

	private static JFrame createAndShowGUI(int mode, boolean table) {
		MainSource mainSource = new MainSource(mode, table);
		JMenuBar menuBar = mainSource.createMenuBar();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.revalidate();
		frame.repaint();
		return frame;
	}

	public static void main(String[] args) {
		db.createTables();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI(0);
			}
		});

	}
}
