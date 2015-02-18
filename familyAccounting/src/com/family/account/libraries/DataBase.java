package com.family.account.libraries;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBase {
	private String path;
	private static Connection conn;
	private static final String TABLE_SOURCE = "money_source",
			TABLE_MOVEMENT = "movements";

	/**
	 * A class with basic command and connection to a given database.
	 * 
	 * @param path
	 *            - The path which the DB is located.
	 * @return - Connection if failed returns null.
	 */
	public DataBase(String path) {
		this.path = path;
		connection();
	}

	public DataBase() {
		this(System.getProperty("user.home") + File.separator
				+ "h4k234hyuds864g2");
	}

	/**
	 * Connect to a database
	 * 
	 * @return - Default value is null but if the connection was realized return
	 *         a Connection
	 */
	private void connection() {
		conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + path);
			System.out.println("Connection realized - Path file: " + path);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createTables() {
		String createTable1 = "create table if not exists " + TABLE_SOURCE
				+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "name blob not null, total int not null);";
		String createTable2 = "create table if not exists " + TABLE_MOVEMENT
				+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "source_id int not null, name mediumblob not null, "
				+ "movement_date date not null, income int, outgoing int, "
				+ "constraint source_id_movementsfk foreign key(source_id) "
				+ "references money_source(id) on update cascade on delete "
				+ "cascade);";
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate(createTable1);
			stm.executeUpdate(createTable2);
		} catch (SQLException e) {
			System.err.println("Database exist");
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<String[]> getMovements() {
		Statement stm = null;
		ResultSet rs = null;
		ArrayList<String[]> result = null;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_MOVEMENT + ";");
			result = new ArrayList<String[]>();
			while (rs.next()) {
				Date date = rs.getDate("movement_date");
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String[] tmp = { Integer.toString(rs.getInt("id")),
						rs.getString("name"),
						getSourceName(rs.getInt("source_id")),
						sdf.format(date),
						Double.toString(rs.getDouble("income")),
						Double.toString(rs.getDouble("outgoing")),
						Integer.toString(rs.getInt("source_id")) };
				result.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ArrayList<String[]> getSources() {
		Statement stm = null;
		ResultSet rs = null;
		ArrayList<String[]> result = null;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_SOURCE + ";");
			result = new ArrayList<String[]>();
			while (rs.next()) {
				String[] tmp = { Integer.toString(rs.getInt("id")),
						rs.getString("name"),
						Double.toString(rs.getDouble("total")) };
				result.add(tmp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String[] getSource(int id) {
		Statement stm = null;
		ResultSet rs = null;
		String[] result = new String[2]; 
		try {
			stm = conn.createStatement();
			 rs = stm.executeQuery("select name, total from " + TABLE_SOURCE
					+ " where id=" + id);
			 if(rs.next()){
				 result[0] = rs.getString("name");
				 result[1] = Double.toString(rs.getDouble("total"));
			 }
			 stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void updateTotal(int sourceId, double total) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("update " + TABLE_SOURCE + " set total=total+"
					+ total + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet getMovementsFromSouce(int source_id) {
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_MOVEMENT
					+ " where source_id=" + source_id + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stm != null) {
				try {
					stm.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rs;
	}

	public String[] getMovement(int id) {
		Statement stm = null;
		ResultSet rs = null;
		String[] result = new String[7];
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_MOVEMENT
					+ " where id=" + id + ";");
			if(rs.next()){
				result[0] = rs.getString("name");
				result[1] = getSourceName(rs.getInt("source_id"));
				result[2] = rs.getString("date");
				result[3] = Double.toString(rs.getDouble("income"));
				result[4] = Double.toString(rs.getDouble("outgoing"));
				result[5] = Integer.toString(rs.getInt("id"));
				result[6] = Integer.toString(rs.getInt("source_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public String getSourceName(int sourceId) {
		System.out.println("Id: " + sourceId);
		Statement stm = null;
		ResultSet rs = null;
		String name;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_SOURCE
					+ " where id=" + sourceId + ";");
			System.out.println("select * from " + TABLE_SOURCE + " where id="
					+ sourceId + ";");
			if (rs.next())
				name = rs.getString("name");
			else
				name = "";
		} catch (SQLException e) {
			e.printStackTrace();
			name = "";
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return name;
	}

	public void newSource(String name, double total) {
		System.out.println(name);
		System.out.println(total);
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("insert into " + TABLE_SOURCE
					+ " (name, total) values ('" + name + "', " + total + ");");
			System.out.println("insert into " + TABLE_SOURCE
					+ " (name, total) values ('" + name + "', " + total + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stm != null) {
					stm.close();

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void editSource(int sourceId, String name, double total) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("update " + TABLE_SOURCE + " set name=\"" + name
					+ "\", total=" + total + " where id=" + sourceId);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stm != null) {
					stm.close();

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void newMovement(int sourceId, String name, String movementDate,
			double income, double outgoing) {
		Statement stm = null;
		try {
			stm = conn.createStatement();

			stm.executeUpdate("insert into "
					+ TABLE_MOVEMENT
					+ "(source_id, name, movement_date, income, outgoing) values ("
					+ sourceId + ", \"" + name + "\", \"" + movementDate
					+ "\", " + income + ", " + outgoing + ");");
			if (outgoing > 0.0) {
				stm.executeUpdate("update " + TABLE_SOURCE
						+ " set total=total-" + outgoing + " where id="
						+ sourceId);
			} else {
				stm.executeUpdate("update " + TABLE_SOURCE
						+ " set total=total+" + income + " where id="
						+ sourceId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void editMovement(int movId, int sourceId, String name,
			String movementDate, double income, double outgoing) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			String[] movement = getMovement(movId);
			stm.executeUpdate("update " + TABLE_MOVEMENT + " set source_id="
					+ sourceId + ", name=\"" + name + "\", movement_date=\""
					+ movementDate + "\", income=" + income + ", outgoing="
					+ outgoing + ";");
			if (income != Double.parseDouble(movement[3])
					|| income != Double.parseDouble(movement[4])
					|| outgoing != Double.parseDouble(movement[4])
					|| outgoing != Double.parseDouble(movement[3])) {
				updateTotal(sourceId, income);
				updateTotal(sourceId, -outgoing);
				updateTotal(Integer.parseInt(movement[6]),
						-Double.parseDouble(movement[3]));
				updateTotal(Integer.parseInt(movement[6]),
						Double.parseDouble(movement[4]));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}