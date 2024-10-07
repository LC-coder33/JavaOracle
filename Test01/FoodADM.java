package Test01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class FoodADM {

	FoodADM() {
		init();
		Scanner in = new Scanner(System.in);
		while(true) {
			menu();
			System.out.println("메뉴선택");
			int selNum = in.nextInt();
			in.nextLine();
			//공사중
			if(selNum == 1) {
				add();
			}else if(selNum == 2) {
				del();
			}else if(selNum == 3) {
				mod();
			}else if(selNum == 4) {
				list();
			}else {
				break;
			}
		}
	}
	private void init() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("오라클 드라이버 로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	private void menu() {
		System.out.println("1.음식추가");
		System.out.println("2.음식삭제");
		System.out.println("3.음식수정");		
		System.out.println("4.음식전체보기");
	}
	
	private void add() {
		Scanner in = new Scanner(System.in);
		FoodDTO f = new FoodDTO();
		System.out.println("추가할 음식의 ID를 입력하세요.");
		f.setFid(in.nextLine());
		System.out.println("추가할 음식의 이름을 입력하세요.");
		f.setFname(in.nextLine());
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
					"system",
					"11111111");
			String sql = "insert into foodone values(?,?,default)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, f.getFid());
			pstmt.setString(2, f.getFname());
			
			int result = pstmt.executeUpdate();
			if(result == 0) {
				conn.rollback();
			}else {
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(Exception e) {
					
				}
			}
		}
	}
	
	private void del() {
		Scanner in = new Scanner(System.in);
		FoodDTO f = new FoodDTO();
		System.out.println("삭제할 음식의 ID를 입력하세요.");
		f.setFid(in.nextLine());
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
						"system",
						"11111111");
			
			String sql = "DELETE FROM foodone WHERE fid = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, f.getFid());
			
			int result = pstmt.executeUpdate();
			if(result == 0) {
				conn.rollback();
				System.out.println("삭제할 레코드가 없습니다.");
			} else {
				conn.commit();
				System.out.println("레코드 삭제 성공");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        if(conn != null) {
	            try {
	                conn.close();
	            } catch(Exception e) {
	                e.printStackTrace();
	            }
	        }
		}
	}
	
	private void mod() {
		Scanner in = new Scanner(System.in);
		FoodDTO f = new FoodDTO();
		System.out.println("수정할 음식의 ID를 입력하세요.");
		String oldFid = in.nextLine();
		System.out.println("음식의 새로운 ID를 입력하세요.");
		String newFid = in.nextLine();
		System.out.println("음식의 새로운 ID를 입력하세요.");
		String newName = in.nextLine();
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
					"system",
					"11111111");
			
			String sql = "UPDATE foodone SET fid = ?, fname = ? WHERE fid = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newFid);
			pstmt.setString(2, newName);
			pstmt.setString(3, oldFid);
			
			int result = pstmt.executeUpdate();
			if(result == 0) {
				conn.rollback();
				System.out.println("수정할 레코드가 없습니다.");
			} else {
				conn.commit();
				System.out.println("레코드 수정 성공");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void list() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
						"system",
						"11111111");
			String sql = "SELECT * FROM foodone";
			Statement pstmt = conn.createStatement();
			ResultSet result = pstmt.executeQuery(sql);
			
			
			if(result == null) {
				conn.rollback();
				System.out.println("조회 할 수 있는 레코드가 없습니다.");
			} else {
				while(result.next()) {
					String num = result.getString(1);
					String name = result.getString(2);
					String time = result.getString(3);
					System.out.println("번호: " + num + " 이름: " + name + " 등록시간: " + time);
				}
				System.out.println("레코드 조회 성공");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
