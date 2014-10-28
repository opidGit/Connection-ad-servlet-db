package template.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class templateDao {

	private DBConnector dbc;

	public templateDao() {
		dbc = new DBConnector();
	}

	public int getData(int arg1) {
		int result = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = new String("select * from dbtable where ID = ?;");

		conn = dbc.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, arg1);
			rs = ps.executeQuery();
			rs.first();
			while (true) {
				result = rs.getInt("id");
				if (rs.isLast())
					break;
				rs.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB(rs, ps, conn);
		}

		return result;
	}

	/**
	 * DataBase 자원 반환
	 * 
	 * @param rs
	 * @param ps
	 * @param conn
	 */
	private void closeDB(ResultSet rs, Statement ps, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
}
