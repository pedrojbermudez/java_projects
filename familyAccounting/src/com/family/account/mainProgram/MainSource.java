package com.family.account.mainProgram;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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

public class MainSource implements ActionListener {
	private static final String REGEX_DECIMAL = "\\d+([.]\\d{1,2})?";
	private static final String REGEX_NUMBER = "\\d+";
	private final String ENTER = "Enter";
	private final String CANCEL = "Cancel";
	private final String EDIT = "Edit";
	private final int START_YEAR_COUNT = 2000;
	private final int TOTAL_YEARS = 50;
	private final String[] SOURCE_NAME_COL = { "#", "Name", "Total" };
	private final String[] MOVEMENT_NAME_COL = { "#", "Name", "Source", "Date",
			"Income", "Outgoing" };
	private final static String DATABASE_PATH = "program_file";
	private static JPanel panel;
	private String[] columnNames;
	private Object[][] rawData;
	private ArrayList<String[]> listData;
	private int numCol;
	private static JFrame frame = new JFrame("Family Accounting");
	private static JFrame frame2;
	private static JTextField name, total, income, outgoing, actualPageInput;
	JComboBox<Item> list;
	static JComboBox<Integer> combo = new JComboBox<Integer>();
	private static JComboBox<String> month, day, year;
	private static DataBase db = new DataBase(DATABASE_PATH);
	private static JTable table;
	private static int actualPage = 0;
	private static int elementsPerPage = 25;
	private int totalPages = 0;
	private int databaseId;
	private static int mode = 0;

	private GridBagConstraints c = new GridBagConstraints();

	/**
	 * Display the indicate table with mode.
	 * 
	 * @param mode
	 *            Number that indicates if it is money source or movement.
	 */
	public MainSource(int mode, int start, int set) {
		int totalElementsDB;
		panel = new JPanel(new GridBagLayout());
		JLabel info = new JLabel("Number of elements on screen: ");
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		panel.add(info, c);
		c.gridy = 0;
		combo.addActionListener(this);
		combo.setActionCommand("pagination");
		panel.add(combo, c);
		switch (mode) {
		case 0:
			totalElementsDB = db.countRowSource();
			System.out.println("total elements in db: " + totalElementsDB);
			if (totalElementsDB > elementsPerPage) {
				totalPages = (totalElementsDB / elementsPerPage) + 1;
			}
			System.out.println("total pages: " + totalPages);
			System.out.println("actual page: " + actualPage);
			if (totalPages > 1 && (actualPage + 1) > 1) {
				// Previous button
				JButton button = new JButton("Previous");
				button.addActionListener(this);
				button.setActionCommand("previous");
				c.fill = GridBagConstraints.NONE;
				c.gridy = 0;
				c.gridx = 2;
				c.insets = new Insets(0, 5, 0, 0);
				panel.add(button, c);
			}
			if (totalPages > 1 && (actualPage + 1) < totalPages) {
				// next button
				JButton button = new JButton("Previous");
				button = new JButton("Next");
				button.addActionListener(this);
				button.setActionCommand("next");
				c.gridy = 0;
				c.gridx = 3;
				c.insets = new Insets(0, 5, 0, 0);
				panel.add(button, c);
			}
			if (totalPages > 1) {
				// Actual page / total pages label.

				// An input text for actual page.
				actualPageInput = new JTextField();
				actualPageInput.setText(Integer.toString(actualPage + 1));
				actualPageInput.setActionCommand("change_page");
				actualPageInput.addActionListener(this);
				actualPageInput.setPreferredSize(new Dimension(30, 20));
				c.gridx = 4;
				c.gridy = 0;
				c.insets = new Insets(0, 15, 0, 0);
				panel.add(actualPageInput, c);

				// A label for /total pages.
				JLabel pageNow = new JLabel("/" + totalPages);
				c.gridx = 4;
				c.gridy = 0;
				c.insets = new Insets(0, 45, 0, 0);
				panel.add(pageNow, c);
			}
			listData = db.getSources(actualPage * elementsPerPage,
					elementsPerPage);
			numCol = 3;
			columnNames = SOURCE_NAME_COL;
			if (listData == null) {
				rawData = null;
			} else {
				rawData = new Object[listData.size()][numCol];
			}
			if (rawData != null) {
				for (int i = 0; i < listData.size(); i++) {
					Object[] tmp = { (i + (actualPage * elementsPerPage)) + 1,
							listData.get(i)[1], listData.get(i)[2] };
					rawData[i] = tmp;
				}
				createTable(rawData, columnNames);
			} else {
				createTable(columnNames, numCol);
			}
			break;
		case 1:
			totalElementsDB = db.countRowMovements();
			if (totalElementsDB > elementsPerPage) {
				totalPages = totalElementsDB / elementsPerPage;
			}

			if (totalPages > 1 && (actualPage + 1) > 1) {
				// Previous button
				JButton button = new JButton("Previous");
				button.addActionListener(this);
				button.setActionCommand("previous");
				c.fill = GridBagConstraints.NONE;
				c.gridy = 0;
				c.gridx = 2;
				c.insets = new Insets(0, 5, 0, 0);
				panel.add(button, c);
			}
			if (totalPages > 1 && (actualPage + 1) < totalPages) {
				// next button
				JButton button = new JButton("Previous");
				button = new JButton("Next");
				button.addActionListener(this);
				button.setActionCommand("next");
				c.gridy = 0;
				c.gridx = 3;
				c.insets = new Insets(0, 5, 0, 0);
				panel.add(button, c);
			}
			if (totalPages > 1) {
				// Actual page / total pages label.

				// An input text for actual page.
				actualPageInput = new JTextField();
				actualPageInput.setText(Integer.toString(actualPage + 1));
				actualPageInput.setActionCommand("change_page");
				actualPageInput.addActionListener(this);
				actualPageInput.setPreferredSize(new Dimension(30, 20));
				c.gridx = 4;
				c.gridy = 0;
				c.insets = new Insets(0, 15, 0, 0);
				panel.add(actualPageInput, c);

				// A label for /total pages.
				JLabel pageNow = new JLabel("/" + totalPages);
				c.gridx = 4;
				c.gridy = 0;
				c.insets = new Insets(0, 45, 0, 0);
				panel.add(pageNow, c);
			}
			listData = db.getMovements(start, set);
			numCol = 6;
			columnNames = MOVEMENT_NAME_COL;
			if (listData == null) {
				rawData = null;
			} else {
				rawData = new Object[listData.size()][numCol];
			}
			if (rawData != null) {
				for (int i = 0; i < listData.size(); i++) {
					Object[] tmp = { (i + (actualPage * elementsPerPage)) + 1,
							listData.get(i)[1], listData.get(i)[2],
							listData.get(i)[3], listData.get(i)[4],
							listData.get(i)[5] };
					rawData[i] = tmp;
				}
				createTable(rawData, columnNames);

			} else {
				createTable(columnNames, numCol);
			}
			break;
		}
	}

	/**
	 * Used to edit both money source or movement.
	 * 
	 * @param mode
	 *            Number that indicates if it is money source or movement.
	 * @param id
	 *            The id in the database.
	 */
	public MainSource(int mode, int id) {
		JLabel header;
		JButton cancelButton = new JButton(CANCEL);
		JButton editButton = new JButton(EDIT);
		String[] getter;
		ArrayList<String[]> sourceList = db.getSources();
		Vector<Item> tmp = new Vector<Item>();
		Map<Integer, Integer> pos = new HashMap<Integer, Integer>();

		frame2 = new JFrame();
		frame2.setAlwaysOnTop(true);
		Container pane = frame2.getContentPane();
		Insets insets = frame2.getInsets();

		pane.setLayout(null);
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		switch (mode) {
		case 0:
			getter = db.getSource(id);
			frame2.setTitle("Edit Source: " + getter[0]);
			header = new JLabel("Name of source:");
			pane.add(header);
			header.setBounds(insets.left + 5, insets.top + 5,
					header.getPreferredSize().width,
					header.getPreferredSize().height);
			name = new JTextField(20);
			total = new JTextField(10);
			editButton.setActionCommand("edit_source_finished");
			editButton.addActionListener(this);
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
			pane.add(editButton);
			pane.add(cancelButton);
			total.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), total.getPreferredSize().width,
					total.getPreferredSize().height);
			editButton.setBounds(insets.left + 100,
					header.getY() + header.getHeight() + 20,
					editButton.getPreferredSize().width,
					editButton.getPreferredSize().height);
			cancelButton.setBounds(editButton.getX() + editButton.getWidth()
					+ 5, editButton.getY(),
					cancelButton.getPreferredSize().width,
					cancelButton.getPreferredSize().height);
			frame2.setMinimumSize(new Dimension(360, 120));
			name.setText(getter[0]);
			total.setText(getter[1]);
			databaseId = id;
			break;
		case 1:
			getter = db.getMovement(id);

			String[] months = { "January", "February", "March", "April", "May",
					"June", "July", "August", "September", "October",
					"November", "December" };
			String[] days = new String[31];
			String[] years = new String[TOTAL_YEARS];
			
			for (int i = 0; i < days.length; i++) {
				days[i] = Integer.toString(i + 1);
			}

			for (int i = 0; i < years.length; i++) {
				years[i] = Integer.toString(START_YEAR_COUNT + i);
			}

			frame2.setTitle("Edit Movement");

			for (int i = 0; i < sourceList.size(); i++) {
				tmp.addElement(new Item(Integer.parseInt(sourceList.get(i)[0]),
						sourceList.get(i)[1]));
				pos.put(Integer.parseInt(sourceList.get(i)[0]), i);
			}

			list = new JComboBox<Item>(tmp);
			month = new JComboBox<String>(months);
			day = new JComboBox<String>(days);
			year = new JComboBox<String>(years);

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

			// Date picker
			header = new JLabel("Introduce the date (mm/dd/yyyy)");
			pane.add(header);
			pane.add(month);
			pane.add(day);
			pane.add(year);
			header.setBounds(insets.left + 5, list.getY() + list.getHeight()
					+ 10, header.getPreferredSize().width,
					header.getPreferredSize().height);
			month.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), month.getPreferredSize().width,
					month.getPreferredSize().height);
			day.setBounds(month.getX() + month.getWidth() + 5, header.getY(),
					day.getPreferredSize().width, day.getPreferredSize().height);
			year.setBounds(day.getX() + day.getWidth() + 5, header.getY(),
					year.getPreferredSize().width,
					year.getPreferredSize().height);

			// A little info.
			JLabel info = new JLabel("Introduce one:");
			pane.add(info);
			info.setBounds(insets.left + 5, day.getY() + day.getHeight() + 5,
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

			// Button: creation.
			editButton.setActionCommand("edit_movement_finished");
			editButton.addActionListener(this);
			pane.add(editButton);
			pane.add(cancelButton);
			editButton.setBounds(insets.left + 5,
					outgoing.getY() + outgoing.getHeight() + 10,
					editButton.getPreferredSize().width,
					editButton.getPreferredSize().height);
			cancelButton.setBounds(editButton.getX() + editButton.getWidth()
					+ 5, editButton.getY(),
					cancelButton.getPreferredSize().width,
					cancelButton.getPreferredSize().height);

			frame2.setMinimumSize(new Dimension(800, 500));

			String[] dateSplit = getter[2].split("-");

			name.setText(getter[0]);
			income.setText(getter[3]);
			outgoing.setText(getter[4]);
			list.setSelectedIndex(pos.get(Integer.parseInt(getter[6])));
			month.setSelectedIndex(Integer.parseInt(dateSplit[1]) - 1);
			day.setSelectedIndex(Integer.parseInt(dateSplit[2]) - 1);
			year.setSelectedIndex(Integer.parseInt(dateSplit[0])
					- START_YEAR_COUNT);
			databaseId = id;
			break;
		}
		frame2.setResizable(false);
		frame2.pack();
		frame2.setVisible(true);
	}

	/**
	 * Used to create a new money source or movement.
	 * 
	 * @param mode
	 *            Number that indicates if it is money source or movement.
	 * @param activated
	 *            Just a parameter to difference to other constructors.
	 */
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

			// Name input field
			header = new JLabel("Name of source:");
			pane.add(header);
			header.setBounds(insets.left + 5, insets.top + 5,
					header.getPreferredSize().width,
					header.getPreferredSize().height);
			name = new JTextField(20);
			name.addActionListener(this);
			pane.add(name);
			name.setBounds(header.getX() + header.getWidth() + 5,
					insets.top + 5, name.getPreferredSize().width,
					name.getPreferredSize().height);

			// Total money input
			header = new JLabel("Total money on the source:");
			pane.add(header);
			header.setBounds(insets.left + 5, name.getY() + name.getHeight()
					+ 5, header.getPreferredSize().width,
					header.getPreferredSize().height);
			total = new JTextField(10);
			pane.add(total);
			total.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), total.getPreferredSize().width,
					total.getPreferredSize().height);

			// Buttons (accept and cancel)
			pane.add(acceptButton);
			pane.add(cancelButton);
			acceptButton.setActionCommand("create_source");
			acceptButton.addActionListener(this);
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
			ArrayList<String[]> sourceList = db.getSources(actualPage
					* elementsPerPage, elementsPerPage);
			Vector<Item> tmp = new Vector<Item>();
			String[] months = { "January", "February", "March", "April", "May",
					"June", "July", "August", "September", "October",
					"November", "December" };
			String[] days = new String[31];
			String[] years = new String[TOTAL_YEARS];
			for (int i = 0; i < days.length; i++) {
				days[i] = Integer.toString(i + 1);
			}

			for (int i = 0; i < years.length; i++) {
				years[i] = Integer.toString(START_YEAR_COUNT + i);
			}

			frame2.setTitle("New Movement");

			for (int i = 0; i < sourceList.size(); i++) {
				tmp.addElement(new Item(Integer.parseInt(sourceList.get(i)[0]),
						sourceList.get(i)[1]));
			}

			list = new JComboBox<Item>(tmp);
			month = new JComboBox<String>(months);
			day = new JComboBox<String>(days);
			year = new JComboBox<String>(years);

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

			// Date picker.
			header = new JLabel("Introduce the date (mm/dd/yyyy): ");
			pane.add(header);
			pane.add(month);
			pane.add(day);
			pane.add(year);
			header.setBounds(insets.left + 5, list.getY() + list.getHeight()
					+ 10, header.getPreferredSize().width,
					header.getPreferredSize().height);
			month.setBounds(header.getX() + header.getWidth() + 5,
					header.getY(), month.getPreferredSize().width,
					month.getPreferredSize().height);
			day.setBounds(month.getX() + month.getWidth() + 5, header.getY(),
					day.getPreferredSize().width, day.getPreferredSize().height);
			year.setBounds(day.getX() + day.getWidth() + 5, header.getY(),
					year.getPreferredSize().width,
					year.getPreferredSize().height);

			// A little info.
			JLabel info = new JLabel("Introduce one:");
			pane.add(info);
			info.setBounds(insets.left + 5, day.getY() + day.getHeight() + 5,
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

	/**
	 * Used when the database has got some data.
	 * 
	 * @param data
	 *            The same parameters in JTable constructor
	 * @param colsName
	 *            The same parameters in JTable constructor
	 * @return Return the JTable
	 */
	public void createTable(Object[][] data, String[] colsName) {
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
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 6;
		c.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, c);

	}

	/**
	 * Used when you haven't got some data into the database.
	 * 
	 * @param colsName
	 *            Columns name used to display in the header.
	 * @param numCols
	 *            Number of column to pass to JTable constructor
	 * @return
	 */
	public void createTable(String[] colsName, int numCols) {
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
		panel.add(scrollPane);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	public JMenuBar createMenuBar(int mode) {
		JMenuBar menuBar = null;
		JMenu menu, submenu;
		JMenuItem menuItem;
		ImageIcon icon = new ImageIcon("exit.png");

		switch (mode) {
		case 0:
			// Create the menu bar.
			menuBar = new JMenuBar();

			// File menu
			menu = new JMenu("File");
			menu.setMnemonic(KeyEvent.VK_F);
			menuBar.add(menu);

			// Sub-menu new
			submenu = new JMenu("New");
			submenu.setMnemonic(KeyEvent.VK_N);

			menuItem = new JMenuItem("Money source");
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

			// Edit source
			menuItem = new JMenuItem("Edit money source");
			menuItem.setActionCommand("edit_source");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			// Delete source
			menuItem = new JMenuItem("Delete money source");
			menuItem.setActionCommand("delete_source");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menu.addSeparator();

			menuItem = new JMenuItem("Exit", icon);
			menuItem.setMnemonic(KeyEvent.VK_E);
			menuItem.setActionCommand("exit");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			// View menu
			menu = new JMenu("View");
			menuItem = new JMenuItem("Money source");
			menuItem.setActionCommand("view_source");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuItem = new JMenuItem("Movements");
			menuItem.setActionCommand("view_movements");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuBar.add(menu);

			break;

		case 1:
			// Create the menu bar.
			menuBar = new JMenuBar();

			// File menu
			menu = new JMenu("File");
			menu.setMnemonic(KeyEvent.VK_F);
			menuBar.add(menu);

			// Sub-menu new
			submenu = new JMenu("New");
			submenu.setMnemonic(KeyEvent.VK_N);

			// Menu item in new
			menuItem = new JMenuItem("Money source");
			menuItem.setActionCommand("new_source");
			menuItem.addActionListener(this);
			submenu.add(menuItem);

			// Menu item in new
			menuItem = new JMenuItem("Movement");
			submenu.add(menuItem);
			menuItem.setActionCommand("new_movement");
			menuItem.addActionListener(this);
			menu.add(submenu);

			// Edit movement
			menuItem = new JMenuItem("Edit movement");
			menuItem.setActionCommand("edit_movement");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			// Delete movement
			menuItem = new JMenuItem("Delete movement");
			menuItem.setActionCommand("delete_movement");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menu.addSeparator();

			menuItem = new JMenuItem("Exit", icon);
			menuItem.setMnemonic(KeyEvent.VK_E);
			menuItem.setActionCommand("exit");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			// View menu
			menu = new JMenu("View");
			menuItem = new JMenuItem("Money source");
			menuItem.setActionCommand("view_source");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuItem = new JMenuItem("Movements");
			menuItem.setActionCommand("view_movements");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuBar.add(menu);

			break;

		default:
			// Create the menu bar.
			menuBar = new JMenuBar();

			// File menu
			menu = new JMenu("File");
			menu.setMnemonic(KeyEvent.VK_F);
			menuBar.add(menu);

			// Sub-menu new
			submenu = new JMenu("New");
			submenu.setMnemonic(KeyEvent.VK_N);

			menuItem = new JMenuItem("Money source");
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

			menuItem = new JMenuItem("Exit", icon);
			menuItem.setMnemonic(KeyEvent.VK_E);
			menuItem.setActionCommand("exit");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			// View menu
			menu = new JMenu("View");
			menuItem = new JMenuItem("Money source");
			menuItem.setActionCommand("view_source");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuItem = new JMenuItem("Movements");
			menuItem.setActionCommand("view_movements");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuBar.add(menu);
			break;
		}
		return menuBar;
	}

	public void actionPerformed(ActionEvent e) {
		String incomeTmp, outgoingTmp, monthTmp, dayTmp, date;
		double incomeResult, outgoingResult;
		int selectedRow;
		switch (e.getActionCommand()) {
		case "new_source":
			// User clicks on the menu in File -> New -> Money Source
			mode = 0;
			createAndShowGUI(mode, false);
			break;
		case "new_movement":
			// User clicks on the menu in File -> New -> Movement
			mode = 1;
			createAndShowGUI(mode, false);
			break;
		case "edit_source":
			// User clicks on the menu in File -> Edit Money Source

			selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				mode = 0;
				createAndShowGUI(mode,
						Integer.parseInt(listData.get(selectedRow)[0]));
			} else {
				JOptionPane.showMessageDialog(null, "Select a money source.");
			}

			break;
		case "edit_source_finished":
			// User clicks edit button in edit money source.

			// Check if user lest name and total fields empty.
			if (!total.getText().equals("") && !name.getText().equals("")) {
				// Change text and check if a number and if it has decimal check
				// if the correct format.
				if (total.getText().replace(',', '.').matches(REGEX_DECIMAL)) {
					db.editSource(databaseId, name.getText(),
							Double.parseDouble(total.getText()));
					frame2.dispatchEvent(new WindowEvent(frame2,
							WindowEvent.WINDOW_CLOSING));
					actualPage = 0;
					mode = 0;
					createAndShowGUI(mode, actualPage, elementsPerPage);
				} else {
					JOptionPane.showMessageDialog(null,
							"Number format incorrect.");
				}
			} else {
				// Display a message if name or total is empty.
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Name can't be empty.");
				} else {
					JOptionPane
							.showMessageDialog(null, "Total can't be empty.");
				}
			}
			break;
		case "edit_movement":
			// User clicks on the menu in File -> Edit movement.

			selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				createAndShowGUI(1,
						Integer.parseInt(listData.get(selectedRow)[0]));
			} else {
				JOptionPane.showMessageDialog(null, "Select a movement.");
			}
			break;
		case "edit_movement_finished":
			// Called when user clicks on edit button in edit movement.

			// Check if user lets income, outgoing and name fields empty.
			if ((!income.getText().equals("") || !outgoing.getText().equals(""))
					&& !name.getText().equals("")) {
				// Check if income is empty or not.
				if (income.getText().equals("")) {
					incomeTmp = "0.00";
				} else {
					incomeTmp = income.getText().replace(',', '.');
				}

				// Check if outgoing is empty or not.
				if (outgoing.getText().equals("")) {
					outgoingTmp = "0.00";
				} else {
					outgoingTmp = outgoing.getText().replace(',', '.');
				}

				// Check if income and outgoing are numbers and check if were
				// typed correctly.
				if (incomeTmp.matches(REGEX_DECIMAL)
						&& outgoingTmp.matches(REGEX_DECIMAL)) {
					incomeResult = Double.parseDouble(incomeTmp);
					outgoingResult = Double.parseDouble(outgoingTmp);

					// Check if user types in income and outgoing at the same
					// time.
					if (incomeResult > 0 && outgoingResult > 0) {
						JOptionPane.showMessageDialog(null,
								"Income and Outgoing can't be together.");
					} else {
						// Format month date.
						if (month.getSelectedIndex() + 1 < 10) {
							monthTmp = "0"
									+ Integer
											.toString(month.getSelectedIndex() + 1);
						} else {
							monthTmp = Integer.toString(month
									.getSelectedIndex() + 1);
						}

						// Format day date.
						if (day.getSelectedIndex() + 1 < 10) {
							dayTmp = "0"
									+ Integer
											.toString(day.getSelectedIndex() + 1);
						} else {
							dayTmp = Integer
									.toString(day.getSelectedIndex() + 1);
						}

						// Create date with database format.
						date = Integer.toString(START_YEAR_COUNT
								+ year.getSelectedIndex())
								+ "-" + monthTmp + "-" + dayTmp;

						db.editMovement(databaseId,
								((Item) list.getSelectedItem()).getId(),
								name.getText(), date,
								Double.parseDouble(income.getText()),
								Double.parseDouble(outgoing.getText()));
						frame2.dispatchEvent(new WindowEvent(frame2,
								WindowEvent.WINDOW_CLOSING));
						mode = 1;
						actualPage = 0;
						createAndShowGUI(mode, actualPage, elementsPerPage);
					}

				} else {
					JOptionPane.showMessageDialog(null,
							"Number format incorrect.");
				}
			} else {
				// Check if name is empty and show a message.In the other case
				// income and outgoing are empty.
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Name can't be empty.");
				} else {
					JOptionPane.showMessageDialog(null,
							"Both income and outgoing can't be empty.");
				}

			}

			break;
		case "delete_source":
			// Delete a money source.

			selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				int confirm = JOptionPane
						.showConfirmDialog(
								null,
								"Are you sure you want to delete? Also all movements will be deleted.",
								"Confirm", JOptionPane.YES_NO_OPTION);
				if (confirm == 0) {

					db.deleteSource(Integer.parseInt(listData.get(selectedRow)[0]));
					mode = 0;
					actualPage = 0;
					createAndShowGUI(mode, actualPage, elementsPerPage);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Please select a money source.");
			}
			break;
		case "delete_movement":
			// Delete a money source.
			selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				db.deleteMovement(Integer.parseInt(listData.get(selectedRow)[0]));
				mode = 1;
				createAndShowGUI(mode, actualPage, elementsPerPage);
			} else {
				JOptionPane
						.showMessageDialog(null, "Please select a movement.");
			}
			break;
		case "view_source":
			// User clicks on the menu in View -> View Money Source.
			mode = 0;
			actualPage = 0;
			createAndShowGUI(mode, actualPage, elementsPerPage);
			break;
		case "view_movements":
			// User clicks on the menu in View -> View Movement.
			mode = 1;
			actualPage = 0;
			createAndShowGUI(mode, actualPage, elementsPerPage);
			break;
		case "create_source":
			// User clicks on accept button in create money source.

			// Check if total and name are not empty.
			if (!total.getText().equals("") && !name.getText().equals("")) {
				// Check if total has got desire format by application software.
				if (total.getText().matches(REGEX_DECIMAL)) {
					db.newSource(name.getText(),
							Double.parseDouble(total.getText()));
					frame2.dispatchEvent(new WindowEvent(frame2,
							WindowEvent.WINDOW_CLOSING));
					mode = 0;
					actualPage = 0;
					createAndShowGUI(mode, actualPage, elementsPerPage);
				}

			} else {
				// Show a message if name or total is empty.
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Name can't be empty.");
				} else {
					JOptionPane.showMessageDialog(null,
							"Number format incorrect.");
				}
			}
			break;
		case "pagination":
			elementsPerPage = combo.getItemAt(combo.getSelectedIndex());
			createAndShowGUI(mode, actualPage * elementsPerPage,
					elementsPerPage);
			break;
		case "change_page":
			if (actualPageInput.getText().matches(REGEX_NUMBER)) {
				if (Integer.parseInt(actualPageInput.getText()) > 0) {
					actualPage = Integer.parseInt(actualPageInput.getText()) - 1;
					createAndShowGUI(mode, actualPage * elementsPerPage,
							elementsPerPage);
				} else {
					JOptionPane.showMessageDialog(null,
							"Invalid number. Number must be 1 or greater.");
				}

			} else {
				JOptionPane.showMessageDialog(null, "Invalid number format");
			}

			break;
		case "create_movement":
			// Called when user clicks on accept button in create movement.

			if ((!income.getText().equals("") || !outgoing.getText().equals(""))
					&& !name.getText().equals("")) {
				if (income.getText().equals("")) {
					incomeTmp = "0.00";
				} else {
					incomeTmp = income.getText().replace(',', '.');
				}

				if (outgoing.getText().equals("")) {
					outgoingTmp = "0.00";
				} else {
					outgoingTmp = outgoing.getText().replace(',', '.');
				}
				if (incomeTmp.matches(REGEX_DECIMAL)
						&& outgoingTmp.matches(REGEX_DECIMAL)) {
					incomeResult = Double.parseDouble(incomeTmp);
					outgoingResult = Double.parseDouble(outgoingTmp);
					if (incomeResult > 0 && outgoingResult > 0) {
						JOptionPane.showMessageDialog(null,
								"Icome and Outgoing can't be together.");
					} else {
						// Format month date.
						if (month.getSelectedIndex() + 1 < 10) {
							monthTmp = "0"
									+ Integer
											.toString(month.getSelectedIndex() + 1);
						} else {
							monthTmp = Integer.toString(month
									.getSelectedIndex() + 1);
						}

						// Format day date.
						if (day.getSelectedIndex() + 1 < 10) {
							dayTmp = "0"
									+ Integer
											.toString(day.getSelectedIndex() + 1);
						} else {
							dayTmp = Integer
									.toString(day.getSelectedIndex() + 1);
						}

						date = Integer.toString(START_YEAR_COUNT
								+ year.getSelectedIndex())
								+ "-" + monthTmp + "-" + dayTmp;

						db.newMovement(((Item) list.getSelectedItem()).getId(),
								name.getText(), date, incomeResult,
								outgoingResult);
						frame2.dispatchEvent(new WindowEvent(frame2,
								WindowEvent.WINDOW_CLOSING));
						mode = 1;
						actualPage = 0;
						createAndShowGUI(mode, actualPage, elementsPerPage);
					}

				} else {
					JOptionPane.showMessageDialog(null,
							"Number format incorrect.");
				}
			} else {
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Name can't be empty.");
				} else {
					JOptionPane.showMessageDialog(null,
							"Both income and outgoing can't be empty.");
				}

			}
			break;
		case "cancel":
			// Exit in create or edit frame.

			frame2.dispatchEvent(new WindowEvent(frame2,
					WindowEvent.WINDOW_CLOSING));
			break;
		case "next":
			actualPage++;
			createAndShowGUI(mode, actualPage * elementsPerPage,
					elementsPerPage);
			break;
		case "previous":
			actualPage--;
			createAndShowGUI(mode, actualPage * elementsPerPage,
					elementsPerPage);
			break;
		case "exit":
			// Close the application.

			System.exit(0);
			break;
		}
	}

	/**
	 * Show the GUI. This one is only to display a table
	 * 
	 * @param mode
	 *            It indicates if must be displayed money source or movement.
	 * @return
	 */
	private static void createAndShowGUI(int mode, int page, int set) {
		long starTime = System.nanoTime();
		MainSource mainSource = new MainSource(mode, page, set);
		JMenuBar menuBar = mainSource.createMenuBar(mode);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		frame.revalidate();
		frame.repaint();
		long endTime = System.nanoTime();
		System.out.println((endTime - starTime) + "ns");
	}

	/**
	 * Used when we want to create a new money source or movement
	 * 
	 * @param mode
	 *            It indicates if is money source or movement.
	 * @param table
	 *            Just for difference among other methods.
	 */
	private static void createAndShowGUI(int mode, boolean table) {
		MainSource mainSource = new MainSource(mode, table);
		JMenuBar menuBar = mainSource.createMenuBar(mode);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.revalidate();
		frame.repaint();
	}

	/**
	 * Used for edit frame.
	 * 
	 * @param mode
	 *            Indicate if it is money source or movement.
	 * @param id
	 *            Id in database.
	 */
	private static void createAndShowGUI(int mode, int id) {
		MainSource mainSource = new MainSource(mode, id);
		JMenuBar menuBar = mainSource.createMenuBar(mode);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.revalidate();
		frame.repaint();
	}

	public static void main(String[] args) {
		db.createTables();
		combo.addItem(10);
		combo.addItem(25);
		combo.addItem(50);
		combo.addItem(100);
		combo.setSelectedItem(elementsPerPage);
		if (System.getProperty("os.name").contains("Linux")) {

		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI(mode, actualPage, elementsPerPage);
			}
		});
	}
}
