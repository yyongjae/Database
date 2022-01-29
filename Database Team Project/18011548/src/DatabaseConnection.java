import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {

	//중간고사와 동일한 코드를 사용할 것!!
	static String driver, URL, user, password, sql;
	PreparedStatement preparedStatement;
	ResultSet resultSet;

	public DatabaseConnection() {		
		driver = "com.mysql.cj.jdbc.Driver";
		URL = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul&useSSL=false";
		user = "madang";
		password = "madang";
		sql = "INSERT INTO ORDERS VALUES (?,?,?,?,?)";
	}
	
	public static Connection connection() {
		try {
			Class.forName(driver);
			return DriverManager.getConnection(URL, user, password);
			
		} catch (Exception e) {
			System.out.println("Connection failed!");
		}
		return null;
	}
	
	//테이블 쿼리값 돌려주기
	public ResultSet ReturnTableQuery(String inputTableQuery) {
		try {
			return preparedStatement.executeQuery(inputTableQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public int ReturnInputLimitedNumber(String inputQuery) {
//		ResultSet resultSet;
//		try {
//			resultSet = preparedStatement.executeQuery(inputQuery);
//			resultSet.next();
//		    return resultSet.getInt(1);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}
	
}