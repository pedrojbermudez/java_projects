package com.family.account.libraries;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBase {
	private String path;
	private static Connection conn;
	private static final String TABLE_SOURCE = "money_source",
			TABLE_MOVEMENT = "movements";

	/**
	 * Constructor with database path.
	 * 
	 * @param path
	 *            - The path which the DB is located.
	 * @return - Connection if failed returns null.
	 */
	public DataBase(String path) {
		this.path = path;
		connection();
	}

	/**
	 * Constructor without arguments. Use a default path in the user home path.
	 */
	public DataBase() {
		this(System.getProperty("user.home") + File.separator + "program_file");
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create all tables if not exist.
	 */
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
			System.err.println("Table o tables exist");
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

	/**
	 * Get all movements from all money source with limit for not overload the
	 * program.
	 * 
	 * @param startIndex
	 *            Start from that row.
	 * @param numberElements
	 *            The number of elements per page.
	 * @return
	 */
	public ArrayList<String[]> getMovements(int startIndex, int numberElements) {
		Statement stm = null;
		ResultSet rs = null;
		ArrayList<String[]> result = null;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_MOVEMENT
					+ " order by movement_date desc limit " + startIndex + ", "
					+ numberElements + ";");
			result = new ArrayList<String[]>();
			while (rs.next()) {
				String[] arrayTmp = rs.getString("movement_date").split("-");
				String date = arrayTmp[2] + "-" + arrayTmp[1] + "-"
						+ arrayTmp[0];
				String[] tmp = { Integer.toString(rs.getInt("id")),
						rs.getString("name"),
						getSourceName(rs.getInt("source_id")), date,
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

	/**
	 * Get all sources.
	 * 
	 * @param startIndex
	 *            Start from that row.
	 * @param numberElements
	 *            Number of elements per page.
	 * @return
	 */
	public ArrayList<String[]> getSources(int startIndex, int numberElements) {
		Statement stm = null;
		ResultSet rs = null;
		ArrayList<String[]> result = null;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_SOURCE + " limit "
					+ startIndex + ", " + numberElements + ";");
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

	/**
	 * Get all sources without limit.
	 * 
	 * @return
	 */
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

	/**
	 * Get a source from an id.
	 * 
	 * @param id
	 *            The source id that we wish to get.
	 * @return
	 */
	public String[] getSource(int id) {
		Statement stm = null;
		ResultSet rs = null;
		String[] result = new String[2];

		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select name, total from " + TABLE_SOURCE
					+ " where id=" + id);
			if (rs.next()) {
				result[0] = rs.getString("name");
				result[1] = Double.toString(rs.getDouble("total"));
			}
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Update total filed when you put a new movement.
	 * 
	 * @param sourceId
	 *            Source id that we need to update total value.
	 * @param movement
	 *            The income or outgoing value from the movement.
	 */
	public void updateTotal(int sourceId, double movement) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("update " + TABLE_SOURCE + " set total=total+"
					+ movement + " where id=" + sourceId + ";");
			System.out.println("sql updateTotal: " + "update " + TABLE_SOURCE
					+ " set total=total+" + movement + " where id=" + sourceId
					+ ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all movements from a money source.
	 * 
	 * @param source_id
	 *            Source id that we wish to get all its movements.
	 * @return return ArrayList<String[]> with all movements.
	 */
	public ArrayList<String[]> getMovementsFromSouce(int source_id) {
		Statement stm = null;
		ResultSet rs = null;
		ArrayList<String[]> movements = new ArrayList<String[]>();
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_MOVEMENT
					+ " where source_id=" + source_id + ";");
			if (rs.next()) {
				String[] tmp = { rs.getString("name"),
						getSourceName(rs.getInt("source_id")),
						rs.getString("movement_date"),
						Double.toString(rs.getDouble("income")),
						Double.toString(rs.getDouble("outgoing")),
						Integer.toString(rs.getInt("id")),
						Integer.toString(rs.getInt("source_id")), };
				movements.add(tmp);
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
		return movements;
	}

	/**
	 * Get a movement from the database.
	 * 
	 * @param id
	 *            The movement id that we wish to get.
	 * @return Return a String[] with the movement.
	 */
	public String[] getMovement(int id) {
		System.out.println(id);
		Statement stm = null;
		ResultSet rs = null;
		String[] result = new String[7];
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_MOVEMENT
					+ " where id=" + id + ";");
			if (rs.next()) {
				result[0] = rs.getString("name");
				result[1] = getSourceName(rs.getInt("source_id"));
				result[2] = rs.getString("movement_date");
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

	/**
	 * Get the name of the source. Useful when you want just the name.
	 * 
	 * @param sourceId
	 *            Source id on the database.
	 * @return Return a string with the name.
	 */
	public String getSourceName(int sourceId) {
		Statement stm = null;
		ResultSet rs = null;
		String name;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select * from " + TABLE_SOURCE
					+ " where id=" + sourceId + ";");
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

	/**
	 * Create a new row on the database (table: money source).
	 * 
	 * @param name
	 *            Name of the source.
	 * @param total
	 *            Total money.
	 */
	public void newSource(String name, double total) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("insert into " + TABLE_SOURCE
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

	/**
	 * Edit a money source.
	 * 
	 * @param sourceId
	 *            Source id we wish to edit.
	 * @param name
	 *            Name of the source.
	 * @param total
	 *            Total money.
	 */
	public void editSource(int sourceId, String name, double total) {
		Statement stm = null;
		ResultSet rs = null, rs2 = null;
		double actualTotal, countTotal = 0.0;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select total from " + TABLE_SOURCE
					+ " where id=" + sourceId + ";");
			if (rs.next()) {
				actualTotal = rs.getDouble("total");
				if (actualTotal != total) {
					rs2 = stm.executeQuery("select income, outgoing from "
							+ " where source_id=" + sourceId);
					while (rs2.next()) {
						countTotal -= rs2.getDouble("outgoing");
						countTotal += rs2.getDouble("income");
					}
					stm.executeUpdate("update " + TABLE_SOURCE + " set name=\""
							+ name + "\", total=" + (total + countTotal)
							+ " where id=" + sourceId);
				} else {
					stm.executeUpdate("update " + TABLE_SOURCE + " set name=\""
							+ name + "\" where id=" + sourceId);
				}
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

	/**
	 * Create a new row on the database (table: movement).
	 * 
	 * @param sourceId
	 *            Source id on the database.
	 * @param name
	 *            Name of the movement.
	 * @param movementDate
	 *            Date of movement.
	 * @param income
	 *            Income value.
	 * @param outgoing
	 *            Outgoing value.
	 */
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
			System.out
					.println("sql newMovement: "
							+ "insert into "
							+ TABLE_MOVEMENT
							+ "(source_id, name, movement_date, income, outgoing) values ("
							+ sourceId + ", \"" + name + "\", \""
							+ movementDate + "\", " + income + ", " + outgoing
							+ ");");
			if (outgoing > 0.0) {
				updateTotal(sourceId, -outgoing);
			} else {
				updateTotal(sourceId, income);
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

	/**
	 * Edit a movement.
	 * 
	 * @param movId
	 *            movement id in the database that we wish to edit.
	 * @param sourceId
	 *            source id in the database.
	 * @param name
	 *            Name of the movement
	 * @param movementDate
	 *            Date of the movement.
	 * @param income
	 *            Income value.
	 * @param outgoing
	 *            Outgoing value.
	 */
	public void editMovement(int movId, int sourceId, String name,
			String movementDate, double income, double outgoing) {
		Statement stm = null;
		String[] movement = getMovement(movId);
		try {
			stm = conn.createStatement();
			stm.executeUpdate("update " + TABLE_MOVEMENT + " set source_id="
					+ sourceId + ", name=\"" + name + "\", movement_date=\""
					+ movementDate + "\", income=" + income + ", outgoing="
					+ outgoing + " where id=" + movId + ";");
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

	/**
	 * Delete a money source.
	 * 
	 * @param id
	 *            Money source id that we want to delete.
	 */
	public void deleteSource(int id) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("delete from " + TABLE_SOURCE + " where id=" + id);
			stm.executeUpdate("delete from " + TABLE_MOVEMENT
					+ " where source_id=" + id);
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a movement
	 * 
	 * @param id
	 *            Movement id that we want to delete.
	 */
	public void deleteMovement(int id) {
		String[] movement = getMovement(id);
		Statement stm = null;
		updateTotal(Integer.parseInt(movement[6]),
				-Double.parseDouble(movement[3]));
		updateTotal(Integer.parseInt(movement[6]),
				Double.parseDouble(movement[4]));
		try {
			stm = conn.createStatement();
			stm.executeUpdate("delete from " + TABLE_MOVEMENT + " where id="
					+ id);
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Number of row on the database (table: money source).
	 * 
	 * @return
	 */
	public int countRowSource() {
		Statement stm = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select count(id) as count from "
					+ TABLE_SOURCE + ";");
			if (rs.next()) {
				result = rs.getInt("count");
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

	/**
	 * Number of row on the data base(table_ movements)
	 * 
	 * @return total row of movements.
	 */
	public int countRowMovements() {
		Statement stm = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery("select count(id) as count from "
					+ TABLE_MOVEMENT + ";");
			if (rs.next()) {
				result = rs.getInt("count");
			}
			if(stm != null)
				stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
