	import java.awt.GridLayout;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.MouseEvent;
	import java.awt.event.MouseListener;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.util.ArrayList;
	
	import javax.swing.Box;
	import javax.swing.JButton;
	import javax.swing.JComboBox;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;
	import javax.swing.JTextArea;
	import javax.swing.JTextField;
	import javax.swing.table.DefaultTableModel;
	
	public class UIFrame extends JFrame implements ActionListener{
		
		//DB
		static DatabaseConnection databaseConnection;
		ResultSet resultSet;
		int rs;
		int rse[];
		
		String semester[] = {"1 학기", "여름 학기", "2 학기", "겨울 학기"};
	
		JButton btnAdmin, btnProfessor, btnStudent;
		JPanel mainPanel, adminPanel, professorPanel, studentPanel, searchLecturePanel, searchProfessorLecturePanel,clubHeadStudentPanel;
		
		//메인으로 돌아오는 버튼
		JButton btnReturnMainFromAdmin, btnReturnMainFromProfessor, btnReturnMainFromStudent;
		
		//관리자 페이지 adminPanel의 버튼
		JButton btnResetDB, btnInsert, btnDelete, btnModify, btnViewAll;
		
		//교수 페이지 professorPanel의 버튼
		JButton btnProfessorLectureInfo, btnStudentInfo, btnDepartmentInfo, btnProfessorTimetable, btnInsertGrade;//btnStudentInfo는 지도 학생 정보 조회하는 버튼
		
		//학생 페이지 studentPanel의 버튼
		JButton btnStudentLectureInfo, btnStudentTimetable, btnClubInfo, btnGrade;
		
		//강의 내역 조회시 searchLecturePanel의 구성요소
		JTextField txtFieldYear, txtFieldProfYear;
		JComboBox<String> comboSemester, comboProfSemester;
		JLabel labelYear, labelSemester, labelProfYear, labelProfSemester;
		JButton btnSearchLecture, btnSearchProfessorLecture;
		
		JButton btnSearchClubMemeber;
		
		//JOptionpane 초기 교수/학생 번호 입력창 
		JOptionPane inputProfessorIdPane;
		JOptionPane inputStudentIdPane;
		int studentId=111; //학생 아이디 저장 변수
		int professorId = 1; //교수 아이디 저장 변수
		int clubId = 5;  //소속 동아리 저장 변수
		
		//시간표
		JScrollPane timetablePane;
		JTable table;
		String header[];
		String contents[][];
		DefaultTableModel model;
		
		//교수 강의 내역 JTable 구성요소
		JScrollPane lectureTablePane;
		JTable lectureTable;
		String lectureHeader[];
		String lectureContents[][];
		DefaultTableModel lectureModel;
		
		//교수 지도학생 JTable 구성요소 //$$
		JScrollPane adviseTablePane;
		JTable adviseTable;
		String adviseHeader[];
		String adviseContents[][];
		DefaultTableModel adviseModel;
		
		JScrollPane scrollPane;
		JTextArea txtResult;
	
		public UIFrame() {
			
	//		DB 연결
			databaseConnection = new DatabaseConnection(); 
			databaseConnection.connection();
			
			setTitle("18011563 김채운 / 18011548 조용재");  //JFrame 타이틀 '학번 이름'으로
			initLayout();
			setVisible(true);
			setBounds(200, 50, 700, 500); //x좌표 ,y좌표 , 가로길이, 세로길이
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		public void initLayout() {
			//JButton
			btnAdmin = new JButton("관리자");
			btnProfessor = new JButton("교수");
			btnStudent = new JButton("학생");
			btnReturnMainFromAdmin = new JButton("메인으로");
			btnReturnMainFromProfessor = new JButton("메인으로");
			btnReturnMainFromStudent = new JButton("메인으로");
			
			btnResetDB = new JButton("DB 초기화");
			btnInsert = new JButton("입력");
			btnDelete = new JButton("삭제");
			btnModify = new JButton("변경");
			btnViewAll = new JButton("전체 테이블 조회");
			
			btnProfessorLectureInfo = new JButton("강의 내역 조회");
			btnStudentInfo = new JButton("지도 학생 조회");
			btnDepartmentInfo = new JButton("소속학과 정보");
			btnProfessorTimetable = new JButton("강의 시간표");
			btnInsertGrade = new JButton("성적 입력");
			
			btnStudentLectureInfo = new JButton("수강 내역 조회");
			btnStudentTimetable = new JButton("강의 시간표");
			btnClubInfo = new JButton("소속 동아리 조회");
			btnGrade = new JButton("성적 조회");
			
			btnSearchLecture = new JButton("조회");
			btnSearchProfessorLecture = new JButton("조회");
			btnSearchClubMemeber = new JButton("동아리원 조회");
			
			//searchLecturePanel 구성 요소
			txtFieldYear = new JTextField(10);
			labelYear = new JLabel("년도 (YYYY) : ");
			labelSemester = new JLabel("학기 : ");
			comboSemester = new JComboBox<String>(semester);
			
			txtFieldProfYear = new JTextField(10);
			labelProfYear = new JLabel("년도 (YYYY) : ");
			labelProfSemester = new JLabel("학기 : ");
			comboProfSemester = new JComboBox<String>(semester);
		
			//JPanel
			mainPanel = new JPanel();
			adminPanel = new JPanel();
			professorPanel = new JPanel();
			studentPanel = new JPanel();
			searchLecturePanel = new JPanel();  //강의 조회시 연도, 학기 입력하는 패널
			searchProfessorLecturePanel = new JPanel();  //교수 페이지의 강의 조회시 연도, 학기 입력하는 패널
			clubHeadStudentPanel = new JPanel(); //동아리 회장일 경우 동아리원 정보 조회 패널
			add(mainPanel);
			
			//ScrollPane, TextArea
			txtResult = new JTextArea();
			txtResult.setEditable(false);
			scrollPane = new JScrollPane(txtResult);
			
			//첫번째 화면
			//mainPanel 레이아웃
			mainPanel.setLayout(null);
			mainPanel.add(btnAdmin);
			mainPanel.add(btnProfessor);
			mainPanel.add(btnStudent);
			btnAdmin.setBounds(80, 170, 140, 100);
			btnProfessor.setBounds(272, 170, 140, 100);
			btnStudent.setBounds(465, 170, 140, 100);
			
			//관리자 페이지
			//adminPanel 레이아웃
			adminPanel.setVisible(false);
			adminPanel.add(btnReturnMainFromAdmin);
			adminPanel.add(btnResetDB);
			adminPanel.add(btnInsert);
			adminPanel.add(btnDelete);
			adminPanel.add(btnModify);
			adminPanel.add(btnViewAll);
			
			//교수 페이지
			//professorPanel 레이아웃
			professorPanel.setVisible(false);
			professorPanel.add(btnReturnMainFromProfessor);
			professorPanel.add(btnProfessorLectureInfo);
			professorPanel.add(btnStudentInfo);
			professorPanel.add(btnDepartmentInfo);
			professorPanel.add(btnProfessorTimetable);
			professorPanel.add(btnInsertGrade);
			
			//시간표 초기값
			header = new String[]{"교시/요일","월", "화", "수", "목", "금"};
			contents = new String[][]{
					{"1", "", "", "", "", ""},
					{"2", "", "", "", "", ""},
					{"3", "", "", "", "", ""},
					{"4", "", "", "", "", ""},
					{"5", "", "", "", "", ""},
					{"6", "", "", "", "", ""},
					{"7", "", "", "", "", ""},
					{"8", "", "", "", "", ""},
					{"9", "", "", "", "", ""}
			};
			
			model = new DefaultTableModel(contents, header);  //@@@@@@@
			
			table = new JTable(model); //@@@@@@@@@@@@@@@
			table.setEnabled(false);
			timetablePane = new JScrollPane(table);
			 
			//교수 강의내역 테이블 초기값
			lectureHeader = new String[]{"강좌번호","분반", "교수", "강좌명", "요일", "교시", "학점","강의시간", "개설학과", "강의실"};
			lectureContents = new String[][]{
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""},
				{"", "", "", "", "", "", "", "", "", ""}
			};
			
			lectureModel = new DefaultTableModel(lectureContents, lectureHeader);  //@@
			lectureTable = new JTable(lectureModel); //@@
			lectureTablePane = new JScrollPane(lectureTable);
			
			lectureTable.getColumn("강좌번호").setPreferredWidth(230);
			lectureTable.getColumn("분반").setPreferredWidth(150);
			lectureTable.getColumn("교수").setPreferredWidth(240);
			lectureTable.getColumn("강좌명").setPreferredWidth(270);
			lectureTable.getColumn("요일").setPreferredWidth(180);
			lectureTable.getColumn("교시").setPreferredWidth(160);
			lectureTable.getColumn("학점").setPreferredWidth(150);
			lectureTable.getColumn("강의시간").setPreferredWidth(240);
			lectureTable.getColumn("개설학과").setPreferredWidth(250);
			lectureTable.getColumn("강의실").setPreferredWidth(190);
			
			
			//교수 지도학생 테이블 초기값 $$$
			adviseHeader = new String[]{"학생ID","이름", "학과", "전화번호", "주소", "이메일"};
			adviseContents = new String[][]{
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""},
				{"", "", "", "", "", ""}
			};
			adviseModel = new DefaultTableModel(adviseContents, adviseHeader);
			adviseTable = new JTable(adviseModel);
			adviseTablePane= new JScrollPane(adviseTable);
			
			adviseTable.getColumn("학생ID").setPreferredWidth(100);
			adviseTable.getColumn("이름").setPreferredWidth(100);
			adviseTable.getColumn("학과").setPreferredWidth(240);
			adviseTable.getColumn("전화번호").setPreferredWidth(350);
			adviseTable.getColumn("주소").setPreferredWidth(350);
			adviseTable.getColumn("이메일").setPreferredWidth(350);
			
			//학생 페이지
			//studnetPanel 레이아웃
			studentPanel.setVisible(false);
			studentPanel.add(btnReturnMainFromStudent);
			studentPanel.add(btnStudentLectureInfo);
			studentPanel.add(btnStudentTimetable);
			studentPanel.add(btnClubInfo);
			studentPanel.add(btnGrade);
			
			//searchLecturePanel 레이아웃
			searchLecturePanel.setVisible(false);
			searchLecturePanel.add(labelYear);
			searchLecturePanel.add(txtFieldYear);
			searchLecturePanel.add(labelSemester);
			searchLecturePanel.add(comboSemester);
			searchLecturePanel.add(btnSearchLecture);
			
			//searchProfessorLecturePanel 레이아웃
			searchProfessorLecturePanel.setVisible(false);
			searchProfessorLecturePanel.add(labelProfYear);
			searchProfessorLecturePanel.add(txtFieldProfYear);
			searchProfessorLecturePanel.add(labelProfSemester);
			searchProfessorLecturePanel.add(comboProfSemester);
			searchProfessorLecturePanel.add(btnSearchProfessorLecture);
			
			//clubHeadStudentPanel 레이아웃
			clubHeadStudentPanel.setVisible(false);
			clubHeadStudentPanel.add(btnSearchClubMemeber);
			
			//버튼 이벤트
			btnAdmin.addActionListener(this);
			btnProfessor.addActionListener(this);
			btnStudent.addActionListener(this);
			btnReturnMainFromAdmin.addActionListener(this);
			btnReturnMainFromProfessor.addActionListener(this);
			btnReturnMainFromStudent.addActionListener(this);
			
			btnResetDB.addActionListener(this);
			btnInsert.addActionListener(this);
			btnDelete.addActionListener(this);
			btnModify.addActionListener(this);
			btnViewAll.addActionListener(this);
			
			btnProfessorLectureInfo.addActionListener(this);
			btnStudentInfo.addActionListener(this);
			btnDepartmentInfo.addActionListener(this);
			btnProfessorTimetable.addActionListener(this);
			btnInsertGrade.addActionListener(this);
			
			btnStudentLectureInfo.addActionListener(this);
			btnStudentTimetable.addActionListener(this);
			btnClubInfo.addActionListener(this);
			btnGrade.addActionListener(this);
			
			btnSearchLecture.addActionListener(this);
			btnSearchProfessorLecture.addActionListener(this);
			btnSearchClubMemeber.addActionListener(this);
			
			//수강 내역 테이블 lectureTable 클릭 이벤트
			lectureTable.addMouseListener(new MouseListener() {
				int row = -1;
				int column = -1;
				int classId = 1;
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					ResultSet rs1, rs2;
					row = lectureTable.getSelectedRow();
					column = lectureTable.getSelectedColumn();
					String content = lectureContents[row][0];
					int classId;  
					int year;
					String semester;
				
					if(content == null || content == "") {  //공백 클릭했을 경우 return
						return;
					}
					classId = Integer.parseInt(lectureContents[row][0]);
					year= Integer.parseInt(txtFieldProfYear.getText());
					semester = (String) comboProfSemester.getSelectedItem();
					
					try {
						Statement stmt1 = databaseConnection.connection().createStatement();
						Statement stmt2 = databaseConnection.connection().createStatement();
						
						//해당 과목 수강 학생 찾는 쿼리
						String query1 = "select Student.* from Student, class, course where class.classid = "+classId+" and class.classid = course.classid and course.studentid = student.studentid ";	
						rs1 = stmt1.executeQuery(query1);		
						
						txtResult.setText("학생번호	이름		주소		전화번호			이메일		담당교수		학과\n");
						
						while(rs1.next()) {
							String str = rs1.getInt(1) + "\t" +rs1.getString(2)+ "\t\t" +rs1.getString(3)
							+ "\t\t" +rs1.getString(4)+ "\t\t\t" +rs1.getString(5)+ "\t" +rs1.getString(7)+ "\t\t" +rs1.getString(6)+"\n";
								txtResult.append(str);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			//교수 페이지 지도 학생 테이블 클릭 이벤트
			adviseTable.addMouseListener(new MouseListener() {
				ResultSet rs1;
				int row = -1;
				int column = -1;
				int studentId = 111;
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					row = adviseTable.getSelectedRow();
					column = adviseTable.getSelectedColumn();
					String content = adviseContents[row][0];
					
					if(content == null || content == "") {  //공백 클릭했을 경우 return
						return;
					}
					
					studentId = Integer.parseInt(content);
					System.out.println(studentId);
						
					try {
						Statement stmt1 = databaseConnection.connection().createStatement();
						Statement stmt2 = databaseConnection.connection().createStatement();
						
						//해당 과목 수강 학생 찾는 쿼리
						String query1 = "select * from course where StudentID = " + studentId;	
						rs1 = stmt1.executeQuery(query1);		
						
						txtResult.setText("강의번호	출석점수	중간고사점수		기말고사점수		기타점수	총점	평점\n");
						
						while(rs1.next()) {
							//해당 학생 성적 찾는 쿼리
							String str =rs1.getInt(2)+"\t" +rs1.getInt(4) +"\t"+ rs1.getInt(5) +"\t\t"
							+ rs1.getInt(6)+"\t\t" + rs1.getInt(7) +"\t"+ rs1.getInt(8) +"\t"+ rs1.getString(9) + "\n";
							
							txtResult.append(str);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
		
			if(e.getSource() == btnAdmin) { //관리자 페이지로 panel 전환
				add("North", adminPanel);
				add("Center", scrollPane);
				mainPanel.setVisible(false);
				adminPanel.setVisible(true);
				searchLecturePanel.setVisible(false);
				txtResult.setText("다음과 같이 입력하세요.\n테이블)\n학생 : Student\n교수 : Professor\n학과 : Department\n강좌 : Class\n수강 내역 : Course \n등록금 납부 내역 : Payment \n동아리 : Club \n지도관계 : RelationShip \n소속 동아리 : ClubRelation\n교수 소속학과 : ProfessorDept \n학생 소속학과 : StudentDept"
						+ "\n\n조건식 예시)\nStudentID = 111\nClassID = 1");
			}
			else if(e.getSource() == btnInsert) {
				
				String tableName = JOptionPane.showInputDialog("입력할 테이블 이름을 입력하시오");
				try {
					if(tableName.equals("Student")) {
						
						
						JTextField SID = new JTextField(4);
					    JTextField SName = new JTextField(4);
					    JTextField SAddress = new JTextField(4);
					    JTextField SPhone = new JTextField(4);
					    JTextField SEmail = new JTextField(4);
					    JTextField SMajor = new JTextField(4);
					    JTextField SAdvisor = new JTextField(4);
					    JTextField SAccount = new JTextField(4);
					    JPanel myPanel = new JPanel();
					    myPanel.add(new JLabel("StudentID:"));
					    myPanel.add(SID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("StudentName:"));
					    myPanel.add(SName);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("StudentAddress:"));
					    myPanel.add(SAddress);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("StudentPhone:"));
					    myPanel.add(SPhone);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("StudentEmail:"));
					    myPanel.add(SEmail);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("Major:"));
					    myPanel.add(SMajor);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("Advisor:"));
					    myPanel.add(SAdvisor);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("Account:"));
					    myPanel.add(SAccount);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
					    
					    int SID_int = Integer.parseInt(SID.getText());
					    String SName_str = (SName.getText());
					    String SAddres_str = (SAddress.getText());
					    String SPhone_str = (SPhone.getText());
					    String SEmail_str = (SEmail.getText());
					    String SMajor_str = (SMajor.getText());
					    String SAdvisor_str = (SAdvisor.getText());
					    int SAccount_int = Integer.parseInt(SAccount.getText());
					    
					    try {
					    	
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	
				    		String insert = "insert into Student (StudentID, StudentName, StudentAddress, StudentPhone, StudentEmail, Major, Advisor, Account)"
							    	+ "values("+SID_int+",'"+SName_str+"','"+SAddres_str+"', '"+SPhone_str+"' , '"+SEmail_str+"', '"+SMajor_str+"', '"+SAdvisor_str+"', "+SAccount_int+")";
							rs = stmt.executeUpdate(insert);
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
					else if(tableName.equals("StudentDept")) {
						
						
						JTextField Unit = new JTextField(4);
					    JTextField Dept = new JTextField(4);
					    JTextField Stu = new JTextField(4);
	
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("UnitID:"));
					    myPanel.add(Unit);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("DeptID:"));
					    myPanel.add(Dept);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("StudentID:"));
					    myPanel.add(Stu);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
					    
					    int Unit_int = Integer.parseInt(Unit.getText());
					    int Dept_int = Integer.parseInt(Dept.getText());
					    int Stu_int = Integer.parseInt(Stu.getText());
					    
					    int check =0;
					    String checkQuery = "select count(*) from studentdept where studentdept.StudentID = " + Stu_int + ";";
					    try {
					    	Statement stmt = databaseConnection.connection().createStatement();
									
							resultSet = stmt.executeQuery(checkQuery);
							while (resultSet.next()) {
								check = resultSet.getInt(1);
					    	 }
					    	resultSet.close();
					    	
					    	if(check < 2) {
					    		String insert2 = "insert into StudentDept(UnitID, DeptID, StudentID)"
										+ "values("+Unit_int+", "+Dept_int+", "+Stu_int+")";
								
								rs = stmt.executeUpdate(insert2);
								
					    	}
					    	else {
					    		JOptionPane.showMessageDialog(null, "부전공이 이미 있습니다.\n", "오류", JOptionPane.ERROR_MESSAGE);
					    	}
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
					else if(tableName.equals("Professor")) {
						JTextField ID = new JTextField(4);
					    JTextField Name = new JTextField(4);
					    JTextField Address = new JTextField(4);
					    JTextField Phone = new JTextField(4);
					    JTextField Email = new JTextField(4);
					    JTextField DID = new JTextField(4);
					    
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("ProfID:"));
					    myPanel.add(ID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("ProfName:"));
					    myPanel.add(Name);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("ProfAddress:"));
					    myPanel.add(Address);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("ProfPhone:"));
					    myPanel.add(Phone);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("ProfEmail:"));
					    myPanel.add(Email);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("DeptID:"));
					    myPanel.add(DID);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
					    
					    int ID_int = Integer.parseInt(ID.getText());
					    String Name_str = (Name.getText());
					    String Addres_str = (Address.getText());
					    String Phone_str = (Phone.getText());
					    String Email_str = (Email.getText());
					    int DID_int = Integer.parseInt(DID.getText());
					    
					    try {
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	String insert = "insert into Professor (ProfID, ProfName, ProfAddress, ProfPhone, ProfEmail, DeptID)"
					    	+ "values("+ID_int+",'"+Name_str+"','"+Addres_str+"', '"+Phone_str+"' , '"+Email_str+"', "+DID_int+")";
							rs = stmt.executeUpdate(insert);
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
					
					
					else if(tableName.equals("RelationShip")) {
						
						
						JTextField RID = new JTextField(4);
					    JTextField PID = new JTextField(4);
					    JTextField SID = new JTextField(4);
					    JTextField G = new JTextField(4);
					    JTextField S = new JTextField(4);
	
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("RelationID:"));
					    myPanel.add(RID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("ProfID:"));
					    myPanel.add(PID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("StudentID:"));
					    myPanel.add(SID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("Grade:"));
					    myPanel.add(G);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("Semester:"));
					    myPanel.add(S);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
					    
					    int RID_int = Integer.parseInt(RID.getText());
					    int PID_int = Integer.parseInt(PID.getText());
					    int SID_int = Integer.parseInt(SID.getText());
					    int G_int = Integer.parseInt(G.getText());
					    int S_int = Integer.parseInt(S.getText());
	
					    try {
					    	Statement stmt = databaseConnection.connection().createStatement();
									
					    	
				    		String insert = "insert into RelationShip(RelationID, ProfID, StudentID, Grade, Semester)"
									+ "values("+RID_int+", "+PID_int+", "+SID_int+" , "+G_int+", "+S_int+")";
							
							rs = stmt.executeUpdate(insert);
								
					    	
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
					
					else if(tableName.equals("ProfessorDept")) {
						JTextField UID = new JTextField(4);
					    JTextField DID = new JTextField(4);
					    JTextField PID = new JTextField(4);
					    
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("UnitID:"));
					    myPanel.add(UID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("DeptID:"));
					    myPanel.add(DID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("ProfID:"));
					    myPanel.add(PID);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
					    
					    int UID_int = Integer.parseInt(UID.getText());
					    int DID_int = Integer.parseInt(DID.getText());
					    int PID_int = Integer.parseInt(PID.getText());
					    
					    try {
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	String insert = "insert into ProfessorDept (UnitID, DeptID, ProfID)"
					    	+ "values("+UID_int+","+DID_int+","+PID_int+")";
							rs = stmt.executeUpdate(insert);
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
					
					else if(tableName.equals("Department")) {
						
						JTextField ID = new JTextField(4);
					    JTextField Name = new JTextField(4);
					    JTextField Phone = new JTextField(4);
					    JTextField Office = new JTextField(4);
					    JTextField HeadID = new JTextField(4);
					    
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("DeptID:"));
					    myPanel.add(ID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("DeptName:"));
					    myPanel.add(Name);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("DeptPhone:"));
					    myPanel.add(Phone);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("DeptOffice:"));
					    myPanel.add(Office);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("HeadProfId:"));
					    myPanel.add(HeadID);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
					    
					    int ID_int = Integer.parseInt(ID.getText());
					    String Name_str = (Name.getText());
					    String Phone_str = (Phone.getText());
					    String Office_str = (Office.getText());
					    int HeadID_int = Integer.parseInt(HeadID.getText());
					    
					    try {
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	String insert = "insert into Department (DeptID, DeptName, DeptPhone, DeptOffice, HeadProfID)"
					    	+ "values("+ID_int+",'"+Name_str+"', '"+Phone_str+"' , '"+Office_str+"', "+HeadID_int+")";
							rs = stmt.executeUpdate(insert);
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}		
					}
					
					
					
					
					else if(tableName.equals("Class")) {
						JTextField ID = new JTextField(3);
						JTextField Div = new JTextField(3);
					    JTextField ProfName = new JTextField(3);
					    JTextField ClassName = new JTextField(3);
					    JTextField Day = new JTextField(3);
					    JTextField Period = new JTextField(3);
					    JTextField Credit = new JTextField(3);
					    JTextField Time = new JTextField(3);
					    JTextField DeptName = new JTextField(3);
					    JTextField Location = new JTextField(3);
					    JTextField DeptID = new JTextField(3);
					    
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("ClassID:"));
					    myPanel.add(ID);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("DivClassID:"));
					    myPanel.add(Div);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("ProfName:"));
					    myPanel.add(ProfName);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("ClassName:"));
					    myPanel.add(ClassName);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("Day:"));
					    myPanel.add(Day);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("Period:"));
					    myPanel.add(Period);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("Credit:"));
					    myPanel.add(Credit);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("Time:"));
					    myPanel.add(Time);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("DeptName:"));
					    myPanel.add(DeptName);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("Location:"));
					    myPanel.add(Location);
					    myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					    myPanel.add(new JLabel("DeptID:"));
					    myPanel.add(DeptID);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
	
					    int ID_int = Integer.parseInt(ID.getText());
					    int Div_int = Integer.parseInt(Div.getText());
					    String ProfName_str = (ProfName.getText());
					    String ClassName_str = (ClassName.getText());
					    String Day_str = (Day.getText());
					    String Period_str = (Period.getText());
					    int Time_int = Integer.parseInt(Time.getText());
					    int Credit_int = Integer.parseInt(Credit.getText());
					    String Location_str = (Location.getText());
					    String DeptName_str = (DeptName.getText());
					    int DeptID_int = Integer.parseInt(DeptID.getText());
					    
					    if(Credit_int > 4 || Credit_int < 0 || Time_int < 1 || Time_int > 6) {
					    	JOptionPane.showMessageDialog(null, "학점이나 시간이 범위에 맞지 않습니다!\n", "오류", JOptionPane.ERROR_MESSAGE);
					    }
					    else {
					    	try {
						    	Statement stmt = databaseConnection.connection().createStatement();
						    	String insert = "insert into Class (ClassID, DivClassID, ProfName, ClassName, Day, Period, Credit, Time, DeptName, ClassLocation, DeptID)"
						    	+ "values("+ID_int+","+Div_int+",'"+ProfName_str+"', '"+ClassName_str+"' , '"+Day_str+"', '"+Period_str+"',"+Credit_int+","+Time_int+", '"+DeptName_str+"' , '"+Location_str+"',"+DeptID_int+")";
								rs = stmt.executeUpdate(insert);
								
						    }catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
							}
					    }
					    
					}
					
					
					
					
					
					else if(tableName.equals("Course")) {
						JTextField SID = new JTextField(3);
						JTextField CID = new JTextField(3);
					    JTextField PID = new JTextField(3);
					    JTextField Attend = new JTextField(3);
					    JTextField Mid = new JTextField(3);
					    JTextField Final = new JTextField(3);
					    JTextField Etc = new JTextField(3);
					    JTextField Total = new JTextField(3);
					    JTextField Grade = new JTextField(3);
					    JTextField Year = new JTextField(3);
					    JTextField Sems = new JTextField(3);
					    
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("StudentID:"));
					    myPanel.add(SID);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("ClassID:"));
					    myPanel.add(CID);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("ProfessorID:"));
					    myPanel.add(PID);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Attendance:"));
					    myPanel.add(Attend);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Midterm:"));
					    myPanel.add(Mid);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Final:"));
					    myPanel.add(Final);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Etc:"));
					    myPanel.add(Etc);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Total:"));
					    myPanel.add(Total);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Grade:"));
					    myPanel.add(Grade);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Year:"));
					    myPanel.add(Year);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Semester:"));
					    myPanel.add(Sems);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
	
					    int SID_int = Integer.parseInt(SID.getText());
					    int CID_int = Integer.parseInt(CID.getText());
					    int PID_int = Integer.parseInt(PID.getText());
					    int Attend_int = Integer.parseInt(Attend.getText());
					    int Mid_int = Integer.parseInt(Mid.getText());
					    int Final_int = Integer.parseInt(Final.getText());
					    int Etc_int = Integer.parseInt(Etc.getText());
					    int Total_int = Integer.parseInt(Total.getText());
					    String Grade_str = (Grade.getText());
					    int Year_int = Integer.parseInt(Year.getText());
					    String Sems_str = (Sems.getText());
					    
					    String postcredit = "select sum(class.credit) from course, class where course.studentid = " + SID_int + " and course.classid = class.classid and course.year = " + Year_int + " and course.semester = '" + Sems_str + "';";
						String newCredit = "select credit from class where classid = " + CID_int + ";";
						String nopay = "select count(*) from payment where totalfee > paymentfee and studentid = " + SID_int + ";";
						
						
						if(( Grade_str.equals("A") || Grade_str.equals("B") || Grade_str.equals("C") || Grade_str.equals("D") || Grade_str.equals("F") )  && ( Total_int <= 100 && Total_int >= 0 )) {
							
							try {
						    	int credit = 0, newC =0, pay = 0 ;
						    	Statement stmt = databaseConnection.connection().createStatement();
						    	
						    	resultSet = stmt.executeQuery(postcredit);
						    	while(resultSet.next()) {
						    		credit = resultSet.getInt(1);
						    	}
						    	resultSet.close();
						    	
						    	resultSet = stmt.executeQuery(newCredit);
						    	while(resultSet.next()) {
							    	newC = resultSet.getInt(1);
						    	}
						    	resultSet.close();
						    	
						    	resultSet = stmt.executeQuery(nopay); 
						    	while(resultSet.next()) {
							    	pay = resultSet.getInt(1);
						    	}
						    	resultSet.close();
	
						    	if(pay == 1) {
						    		JOptionPane.showMessageDialog(null, "등록금 미납자 입니다!\n", "오류", JOptionPane.ERROR_MESSAGE);
						    	}
						    	else {
						    		if(credit + newC <= 10 ) {
								    	String insert = "insert into Course (StudentID, ClassID, ProfID, Attendance, Midterm, Final, Etc, Total, Grade,Year,Semester)"
								    	+ "values("+SID_int+","+CID_int+","+PID_int+", "+Attend_int+" , "+Mid_int+", "+Final_int+","+Etc_int+","+Total_int+", '"+Grade_str+"' , "+Year_int+", '"+Sems_str+"')";
										rs = stmt.executeUpdate(insert);
							    	}
							    	else {
							    		JOptionPane.showMessageDialog(null, "취득 학점이 10학점을 넘깁니다!\n", "오류", JOptionPane.ERROR_MESSAGE);
							    	}
						    	}
						    	
						    }catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "총점(0~100), 학점(A~F) 범위에 맞춰 입력하세요!\n", "오류", JOptionPane.ERROR_MESSAGE);
						}
					    
					}
					
					
					
					
					
					else if(tableName.equals("Payment")) {
						
						JTextField ID = new JTextField(3);
						JTextField Year = new JTextField(3);
					    JTextField Sems = new JTextField(3);
					    JTextField Total = new JTextField(3);
					    JTextField Pay = new JTextField(3);
					    JTextField Date = new JTextField(3);
					    JTextField SID = new JTextField(3);
					    
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("PaymentID:"));
					    myPanel.add(ID);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Year:"));
					    myPanel.add(Year);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Semester:"));
					    myPanel.add(Sems);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("TotalFee:"));
					    myPanel.add(Total);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("PaymentFee:"));
					    myPanel.add(Pay);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("PaymentDate:"));
					    myPanel.add(Date);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("StudentID:"));
					    myPanel.add(SID);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
	
					   
					    int ID_int = Integer.parseInt(ID.getText());
					    int Year_int = Integer.parseInt(Year.getText());
					    String Sems_str = (Sems.getText());
					    int Total_int = Integer.parseInt(Total.getText());
					    int Pay_int = Integer.parseInt(Pay.getText());
					    String Date_str = (Date.getText());
					    int SID_int = Integer.parseInt(SID.getText());
	
	
					    try {
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	String insert = "insert into Payment (PaymentID, Year, Semester, TotalFee, PaymentFee, PaymentDate, StudentID)"
					    	+ "values("+ID_int+","+Year_int+",'"+Sems_str+"', "+Total_int+" , "+Pay_int+", '"+Date_str+"',"+SID_int+")";
							rs = stmt.executeUpdate(insert);
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					
					
					else if(tableName.equals("Club")) {
						JTextField CID = new JTextField(3);
						JTextField Name = new JTextField(3);
					    JTextField Num = new JTextField(3);
					    JTextField HeadStdID = new JTextField(3);
					    JTextField ProfID = new JTextField(3);
					    JTextField Room = new JTextField(3);
					    
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("ClubID:"));
					    myPanel.add(CID);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("ClubName:"));
					    myPanel.add(Name);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("Number:"));
					    myPanel.add(Num);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("HeadStudentID:"));
					    myPanel.add(HeadStdID);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("AdvisorProfID:"));
					    myPanel.add(ProfID);
					    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
					    myPanel.add(new JLabel("ClubRoom:"));
					    myPanel.add(Room);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
		  
					    int CID_int = Integer.parseInt(CID.getText());
					    String Name_str = (Name.getText());
					    int Num_int = Integer.parseInt(Num.getText());
					    int HeadStdID_int = Integer.parseInt(HeadStdID.getText());
					    int ProfID_int = Integer.parseInt(ProfID.getText());
					    String Room_str = (Room.getText());
	
	
					    try {
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	String insert = "insert into Club (ClubID, ClubName, Number, HeadStudentID, AdvisorProfID, ClubRoom)"
					    	+ "values("+CID_int+",'"+Name_str+"','"+Num_int+"', "+HeadStdID_int+" , "+ProfID_int+", '"+Room_str+"')";
							rs = stmt.executeUpdate(insert);
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					else if(tableName.equals("ClubRelation")) {
						
						
						JTextField ID = new JTextField(4);
					    JTextField CID = new JTextField(4);
					    JTextField SID = new JTextField(4);
	
					    JPanel myPanel = new JPanel();
					    
					    myPanel.add(new JLabel("ID:"));
					    myPanel.add(ID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("ClubID:"));
					    myPanel.add(CID);
					    myPanel.add(Box.createHorizontalStrut(10)); // a spacer
					    myPanel.add(new JLabel("StudentID:"));
					    myPanel.add(SID);
					    
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"속성을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
					    
					    int RID_int = Integer.parseInt(ID.getText());
					    int PID_int = Integer.parseInt(CID.getText());
					    int SID_int = Integer.parseInt(SID.getText());
	
					    try {
					    	Statement stmt = databaseConnection.connection().createStatement();
									
					    	
				    		String insert = "insert into ClubRelation(ID, ClubID, StudentID)"
									+ "values("+RID_int+", "+PID_int+", "+SID_int+")";
							
							rs = stmt.executeUpdate(insert);
								
					    	
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
				}catch(NumberFormatException e3) {
					JOptionPane.showMessageDialog(null, "값을 제대로 입력해주세요!\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				catch (Exception e2) {
				}
			
				
			
			}
		
		
	
	
			
			else if(e.getSource() == btnDelete) {
				
				JTextField table = new JTextField(8);
				JTextField where = new JTextField(8);
			    
			    JPanel myPanel = new JPanel();
			    
			    myPanel.add(new JLabel("삭제 진행할 테이블:"));
			    myPanel.add(table);
			    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
			    myPanel.add(new JLabel("조건식(속성 연산자 값 순으로 쓰시오):"));
			    myPanel.add(where);
			    
			    try {
			    	int result = JOptionPane.showConfirmDialog(null, myPanel,"입력하시오", JOptionPane.OK_CANCEL_OPTION);
	
		
			    	String table_str = (table.getText());
				    String where_str = (where.getText());
				    Statement stmt = databaseConnection.connection().createStatement();
			    	String delete = "Delete from " + table_str + " where " + where_str + ";";
	
					rs = stmt.executeUpdate(delete);
					
				}
			    catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "정확한 값을 넣어주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
				}
			    catch (Exception e2) {
				}
				
				
			}
			
			
			else if(e.getSource() == btnModify) {
				
				JTextField table = new JTextField(8);
				JTextField set = new JTextField(8);
			    JTextField where = new JTextField(8);
			    
			    
			    JPanel myPanel = new JPanel();
			    
			    myPanel.add(new JLabel("속성 변경할 테이블:"));
			    myPanel.add(table);
			    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
			    myPanel.add(new JLabel("변경내용(속성 연산자 값 순으로 쓰시오):"));
			    myPanel.add(set);
			    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
			    myPanel.add(new JLabel("조건식(속성 연산자 값 순으로 쓰시오):"));
			    myPanel.add(where);
			    
			    int result = JOptionPane.showConfirmDialog(null, myPanel,"입력하시오", JOptionPane.OK_CANCEL_OPTION);
	
			    String table_str = (table.getText());
			    String set_str = (set.getText());
			    String where_str = (where.getText());
	
	
			    try {
			    	Statement stmt = databaseConnection.connection().createStatement();
			    	String delete = "Update " + table_str + " Set " + set_str + " where " + where_str + ";";
	
					rs = stmt.executeUpdate(delete);
					
			    }catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
				}
				
				
				
			}
			
			
			else if(e.getSource() == btnViewAll) {
				String tableName = JOptionPane.showInputDialog("테이블 이름을 입력하시오");
				
				try {
					if(tableName.equals("Student")) {
						
						String Squery = "select * from student order by StudentID ASC";
						
					    
					    try {
					    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("학생번호\t학생이름\t학생주소\t학생전화번호\t\t학생이메일\t\t\t학생전공학과\t\t담당교수\t계좌\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getString(3) + "\t" + resultSet.getString(4)
			        			+ "\t" + resultSet.getString(5)+ "\t\t" + resultSet.getString(6)+ "\t\t" + resultSet.getString(7)+ "\t" + resultSet.getString(8)+"\n";
								
								txtResult.append(str);
							}
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
					
					
					else if(tableName.equals("Professor")) {
						String Squery = "select * from professor order by ProfID ASC";
						
					    
					    try {
					    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("교수번호\t교수이름\t교수주소\t교수전화번호\t\t교수이메일\t\t\t소속학과\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getString(3) + "\t" + resultSet.getString(4)
			        			+ "\t" + resultSet.getString(5)+ "\t\t" + resultSet.getInt(6)+"\n";
								
								txtResult.append(str);
							}
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
					
					
					
					else if(tableName.equals("Department")) {
						
						String Squery = "select * from department order by DeptID ASC";
						
					    
					    try {
					    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("학과번호\t학과이름\t학과전화번호\t학과사무실\t학과장번호\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getString(3) + "\t" + resultSet.getString(4)
			        			+ "\t" + resultSet.getInt(5)+"\n";
								
								txtResult.append(str);
							}
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}	
					}
					
					
					
					
					else if(tableName.equals("Class")) {
						String Squery = "select * from class order by classid ASC";
						
					    
					    try {
					    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					    	txtResult.setText("강좌번호\t분반번호\t교수이름\t강좌이름\t요일\t교시\t취득학점\t시간\t학과이름\t교실위치\t학과번호\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getString(3) + "\t" + resultSet.getString(4)
			        			+ "\t" + resultSet.getString(5)+ "\t" + resultSet.getString(6)+ "\t" + resultSet.getInt(7)+ "\t" + resultSet.getInt(8)+ "\t" + resultSet.getString(9)+ "\t" + resultSet.getString(10)+ "\t" + resultSet.getInt(11)+"\n";
								
								txtResult.append(str);
							}
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					
					
					
					
					else if(tableName.equals("Course")) {
						String Squery = "select * from course order by studentid ASC";
						
					    
					    try {
					    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("학생번호\t강좌번호\t교수번호\t출석\t중간고사\t기말고사\t기타점수\t총점\t평점\t연도\t학기\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getInt(3) + "\t" + resultSet.getInt(4)
			        			+ "\t" + resultSet.getInt(5)+ "\t" + resultSet.getInt(6)+ "\t" + resultSet.getInt(7)+ "\t" + resultSet.getInt(8)+ "\t" + resultSet.getString(9)+ "\t" + resultSet.getInt(10)+ "\t" + resultSet.getString(11)+"\n";
								
								txtResult.append(str);
							}
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					
					
					
					
					else if(tableName.equals("Payment")) {
						
	
						String Squery = "select * from Payment order by paymentid ASC";
						
					    
					    try {
					    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("납부번호\t연도\t학기\t총금액\t납부금액\t마지막납부일\t학생번호\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getString(3) + "\t" + resultSet.getDouble(4)
			        			+ "\t" + resultSet.getDouble(5)+ "\t" + resultSet.getDate(6)+ "\t" + resultSet.getInt(7)+"\n";
								
								txtResult.append(str);
							}
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					
					
					else if(tableName.equals("Club")) {
	
	
						String Squery = "select * from club order by clubid ASC";
						
					    
					    try {
					    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("동아리번호\t동아리이름\t회원수\t회장학생번호\t지도교수번호\t동아리방\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getInt(3) + "\t" + resultSet.getInt(4)
			        			+ "\t" + resultSet.getInt(5)+ "\t" + resultSet.getString(6)+ "\n";
								
								txtResult.append(str);
							}
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					else if(tableName.equals("ClubRelation")) {
	
	
						String Squery = "select * from ClubRelation order by id ASC";
						
					    
					    try {
					    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("번호\t동아리번호\t학생번호\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getInt(3) + "\n";
								
								txtResult.append(str);
							}
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					else if(tableName.equals("StudentDept")) {
	
	
						String Squery = "select * from StudentDept order by unitid ASC";
						
					    
						try {
						    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("번호\t학과번호\t학생번호\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getInt(3) + "\n";
								
								txtResult.append(str);
							}
					
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					else if(tableName.equals("ProfessorDept")) {
	
	
						String Squery = "select * from ProfessorDept order by unitid ASC";
						
					    
						try {
						    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("번호\t학과번호\t교수번호\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getInt(3) + "\n";
								
								txtResult.append(str);
							}
					
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					else if(tableName.equals("RelationShip")) {
	
	
						String Squery = "select * from RelationShip order by relationid ASC";
						
					    
						try {
						    
					    	Statement stmt = databaseConnection.connection().createStatement();
					    	txtResult.setText("");
					        txtResult.setText("번호\t교수번호\t학생번호\t학년\t학기\n");
					        
					    	resultSet = stmt.executeQuery(Squery);
							while(resultSet.next()) {
								String str = resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getInt(3)+ "\t" + resultSet.getInt(4)+ "\t" + resultSet.getString(5) + "\n";
								
								txtResult.append(str);
							}
					
					
							
					    }catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
					
				}
				catch (Exception e2) {
				}
				
			}
	
			else if(e.getSource() == btnProfessor) { //교수 사용자 페이지로 panel 전환
				
				inputProfessorIdPane = new JOptionPane();
				String id = inputProfessorIdPane.showInputDialog("교수 ID를 입력하세요");
				
				if(id != null) {
					
					try {
						professorId = Integer.parseInt(id);
						
					}catch(NumberFormatException e3) {
						JOptionPane.showMessageDialog(null, "10자리 이하 숫자를 입력해주세요!\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						return;
					}
					catch (Exception e2) {
					}
					
					//DB에 해당 id 있는지 확인하는 쿼리
					String query = "SELECT * FROM professor WHERE ProfID = " + professorId;
					
					 try {
						Statement stmt = databaseConnection.connection().createStatement();
						resultSet = stmt.executeQuery(query);
						
						 if (resultSet.next()) {  //해당 아이디가 존재할 경우 로그인 완료
							 
							add("North", professorPanel);
							add("East", timetablePane);
							add("Center", scrollPane);
							add("East", lectureTablePane);//@@
							add("East", adviseTablePane);//$$$$
							
							lectureTablePane.setVisible(false);//@@
							adviseTablePane.setVisible(false);//$$
							mainPanel.setVisible(false);
							timetablePane.setVisible(false);
							professorPanel.setVisible(true);
							searchLecturePanel.setVisible(false);
						 }else {
							 JOptionPane.showMessageDialog(null, "존재하지 않는 ID 입니다!\n예시)1", "로그인 실패", JOptionPane.ERROR_MESSAGE);
						 }
						 
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
					}		
				}
			}
			else if(e.getSource() == btnStudent) { //학생 사용자 페이지로 panel 전환
				
				inputStudentIdPane = new JOptionPane();
				String id = inputStudentIdPane.showInputDialog("학생 ID를 입력하세요");
			
				
				if(id != null) {
					
					try {
						studentId = Integer.parseInt(id);
						
					}catch(NumberFormatException e3) {
						JOptionPane.showMessageDialog(null, "10자리 이하 숫자를 입력해주세요!\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
						return;
					}
					catch (Exception e2) {
					}
					
					//DB에 해당 id 있는지 확인하는 쿼리
					String query = "SELECT * FROM student WHERE StudentID = " + studentId;
					
					 try {
						Statement stmt = databaseConnection.connection().createStatement();
						resultSet = stmt.executeQuery(query);
						
						 if (resultSet.next()) {  //해당 아이디가 존재할 경우 로그인 완료
							add("North", studentPanel);
							add("East", timetablePane); 
							add("Center", scrollPane);
							
							mainPanel.setVisible(false);
							studentPanel.setVisible(true);
							searchLecturePanel.setVisible(false);
							timetablePane.setVisible(false);
						 }else {
							 JOptionPane.showMessageDialog(null, "존재하지 않는 ID 입니다!\n예시)111", "로그인 실패", JOptionPane.ERROR_MESSAGE);
						 }
						 
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
					}
		          
				}
			}
			else if(e.getSource() == btnReturnMainFromAdmin) { //메인으로 돌아가기
				adminPanel.setVisible(false);
				mainPanel.setVisible(true);
				searchLecturePanel.setVisible(false);
				txtResult.setText("");
			}
			else if(e.getSource() == btnReturnMainFromProfessor) { //메인으로 돌아가기
				professorPanel.setVisible(false);
				mainPanel.setVisible(true);
				searchLecturePanel.setVisible(false);
				timetablePane.setVisible(false);
				lectureTablePane.setVisible(false);
				searchProfessorLecturePanel.setVisible(false);
				adviseTablePane.setVisible(false);
				txtResult.setText("");
				lectureContents = new String[][]{
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""}
				};
				setSize(700,500);
			}
			else if(e.getSource() == btnReturnMainFromStudent) { //메인으로 돌아가기
				contents = new String[][]{
					{"1", "", "", "", "", ""},
					{"2", "", "", "", "", ""},
					{"3", "", "", "", "", ""},
					{"4", "", "", "", "", ""},
					{"5", "", "", "", "", ""},
					{"6", "", "", "", "", ""},
					{"7", "", "", "", "", ""},
					{"8", "", "", "", "", ""},
					{"9", "", "", "", "", ""}
				};
				studentPanel.setVisible(false);
				mainPanel.setVisible(true);
				searchLecturePanel.setVisible(false);
				timetablePane.setVisible(false);
				clubHeadStudentPanel.setVisible(false);
				setSize(700,500);
				txtResult.setText("");
			}
			else if(e.getSource() == btnProfessorLectureInfo) {  //교수 페이지의 강의 내역 조회 버튼 클릭시
				add("South", searchProfessorLecturePanel);
				searchProfessorLecturePanel.setVisible(true);
				timetablePane.setVisible(false);
				lectureTablePane.setVisible(false);
				txtResult.setText(" 연도(YYYY) 형식에 맞춰 입력하세요.\n 학기 선택 후 조회 버튼을 누르세요.");
				setSize(700,500);
				timetablePane.setVisible(false);
				adviseTablePane.setVisible(false);
			}
			else if(e.getSource() == btnSearchProfessorLecture) {
				ResultSet rs1, rs2;
				String strYear = txtFieldProfYear.getText();
				int year = 2021;
				String semester = "1 학기";
				int row = 0, column = 0;
				
				lectureContents = new String[][]{
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""},
					{"", "", "", "", "", "", "", "", "", ""}
				};
				
				try {
					year = Integer.parseInt(strYear);
					semester = comboProfSemester.getSelectedItem().toString();
					
				}catch(NumberFormatException e3) {
					 JOptionPane.showMessageDialog(null, "올바른 형식으로 입력해주세요!\n예)2021", "오류", JOptionPane.ERROR_MESSAGE);
					 return;
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				try {
					Statement stmt1 = databaseConnection.connection().createStatement();
					Statement stmt2 = databaseConnection.connection().createStatement();
					
					String query1 = "select distinct Class.* from Class,Course, Professor"
							+ " where Course.ProfID = Professor.ProfID and Course.ClassID = Class.ClassID and Professor.ProfID ="+ professorId + " and Course.Year =" + year + " and Course.Semester = '" + semester + "';";
					rs1 = stmt1.executeQuery(query1);
					
					while(rs1.next()) {
						
						lectureContents[row][0] = String.valueOf(rs1.getInt(1));
						lectureContents[row][1] = String.valueOf(rs1.getInt(2));
						lectureContents[row][2] = rs1.getString(3);
						lectureContents[row][3] = rs1.getString(4);
						lectureContents[row][4] = rs1.getString(5);
						lectureContents[row][5] = rs1.getString(6);
						lectureContents[row][6] = String.valueOf(rs1.getInt(7));
						lectureContents[row][7] = String.valueOf(rs1.getInt(8));
						lectureContents[row][8] = rs1.getString(9);
						lectureContents[row][9] = rs1.getString(10);
						
						row++;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//@@
				lectureModel = new DefaultTableModel(lectureContents, lectureHeader);
				lectureModel.fireTableDataChanged();
				lectureTable.setModel(lectureModel);
				lectureTable.setVisible(true);
				adviseTablePane.setVisible(false);
	//			adviseTable.setVisible(false);
				lectureTablePane.setVisible(true);
				add("East", lectureTablePane);   ///@@@ 이부분 때문에 수강 내역 테이블과 시간표 테이블이 겹쳐서 나온다!!!
				txtResult.setText("강의 내역 입니다.    ▶▶▶");
				setSize(1000,500);
			}
			else if(e.getSource() == btnStudentInfo) {
				ResultSet rs1;
				int row = 0, column = 0;
				
				adviseContents = new String[][]{
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""},
					{"", "", "", "", "", ""}
				};
				
				try {
					Statement stmt1 = databaseConnection.connection().createStatement();
					
					//교수의 지도 학생 검색 쿼리
					String query1 = "select Student.* from Student, Professor, RelationShip where Professor.ProfID ="+professorId+" and Professor.ProfID = RelationShip.ProfID and Student.StudentID = RelationShip.StudentID;";
					rs1 = stmt1.executeQuery(query1);
					
					if(rs1.next()) {
						
						adviseContents[row][0] = String.valueOf(rs1.getInt(1)); //학생id
						adviseContents[row][1] = rs1.getString(2); //이름
						adviseContents[row][2] = rs1.getString(6); //학과
						adviseContents[row][3] = rs1.getString(4); //전화번호
						adviseContents[row][4] = rs1.getString(3); //주소
						adviseContents[row][5] = rs1.getString(5); //이메일
						
						row++;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//$$
				add("East", adviseTablePane);
				adviseModel = new DefaultTableModel(adviseContents, adviseHeader);
				adviseModel.fireTableDataChanged();
				adviseTable.setModel(adviseModel);
				adviseTablePane.setVisible(true);
				adviseTable.setVisible(true);
	//			lectureTable.setVisible(false);
				searchLecturePanel.setVisible(false);
				timetablePane.setVisible(false);
				searchProfessorLecturePanel.setVisible(false);
				lectureTablePane.setVisible(false);
				txtResult.setText("지도 학생 정보 입니다.	▶▶▶");
				setSize(1000,500);
			}
			else if(e.getSource() == btnDepartmentInfo) { 
				int deptId;
				ResultSet rs1, rs2;
				
				try {
					Statement stmt1 = databaseConnection.connection().createStatement();
					Statement stmt2 = databaseConnection.connection().createStatement();
					
					String query1 = "select * from professordept where ProfID = " + professorId;
					rs1 = stmt1.executeQuery(query1);
					
					txtResult.setText("\t학과ID	학과명		학과전화번호		학과사무실	학과장ID\n");
					
					while(rs1.next()) {
						deptId = rs1.getInt(2);
						
						String query2 = "select * from department where DeptID =" + deptId;
						rs2 = stmt2.executeQuery(query2);
						
						while(rs2.next()) {
							String str = "\t" +rs2.getInt(1) + "\t" + rs2.getString(2) + "\t\t" + rs2.getString(3) + "\t\t" + rs2.getString(4)
		        			+ "\t" + rs2.getInt(5)+ "\n";
							
							txtResult.append(str);
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				searchLecturePanel.setVisible(false);
				timetablePane.setVisible(false);
				lectureTablePane.setVisible(false);
				searchProfessorLecturePanel.setVisible(false);
				adviseTablePane.setVisible(false);
				setSize(700,500);
			}
			else if(e.getSource() == btnProfessorTimetable) {     //교수 강의 시간표@@@@@@@@@@@@@@ 업데이트 되도록!!!!
				String name = "";
				ResultSet rs1, rs2;
				
				contents = new String[][]{
					{"1", "", "", "", "", ""},
					{"2", "", "", "", "", ""},
					{"3", "", "", "", "", ""},
					{"4", "", "", "", "", ""},
					{"5", "", "", "", "", ""},
					{"6", "", "", "", "", ""},
					{"7", "", "", "", "", ""},
					{"8", "", "", "", "", ""},
					{"9", "", "", "", "", ""}
				};
				
				try {
					Statement stmt1 = databaseConnection.connection().createStatement();
					Statement stmt2 = databaseConnection.connection().createStatement();
					
					String query1 = "select * from professor where ProfID = " + professorId;
					rs1 = stmt1.executeQuery(query1);
					
					while(rs1.next()) {
						name = rs1.getString(2);
						
						String query2 = "select * from class where ProfName = '"+ name +"'";
						rs2 = stmt2.executeQuery(query2);
						
						while(rs2.next()) {
							//시간표에 넣기
							setTimetable(rs2.getString(4), rs2.getString(5), rs2.getString(6));
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//table = new JTable(contents, header);
				//timetablePane = new JScrollPane(table);
				add("East", timetablePane);
				model = new DefaultTableModel(contents, header);
				model.fireTableDataChanged();
				table.setModel(model);
				
				txtResult.setText("강의 시간표 입니다.  ▶▶▶ ");
				timetablePane.setVisible(true);
				table.setVisible(true);
				
				searchLecturePanel.setVisible(false);
				lectureTablePane.setVisible(false);
				adviseTablePane.setVisible(false);
				adviseTable.setVisible(false);
				searchProfessorLecturePanel.setVisible(false);
				lectureTable.setVisible(false);    //이걸 꼭 해주어야 강의 내역 테이블과 겹치지 않게 보인다!!!!!!
				setSize(900,500);
			}
			else if(e.getSource() == btnInsertGrade) {  
				searchLecturePanel.setVisible(false);
				timetablePane.setVisible(false);
				lectureTablePane.setVisible(false);
				searchProfessorLecturePanel.setVisible(false);
				txtResult.setText("");
				setSize(700,500);
				
				try {
					JTextField attn = new JTextField(4);
					JTextField mid = new JTextField(4);
					JTextField fin = new JTextField(4);
				    JTextField etc = new JTextField(4);
				    JTextField CID = new JTextField(4);
				    JTextField total = new JTextField(4);
				    JTextField grade = new JTextField(4);
				    JTextField SID = new JTextField(4);
				    
				    JPanel myPanel = new JPanel();
				    
				    myPanel.add(new JLabel("학생번호:"));
				    myPanel.add(SID);
				    myPanel.add(Box.createHorizontalStrut(2));
				    myPanel.add(new JLabel("강좌번호:"));
				    myPanel.add(CID);
				    myPanel.add(Box.createHorizontalStrut(2));
				    myPanel.add(new JLabel("출석점수:"));
				    myPanel.add(attn);
				    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
				    myPanel.add(new JLabel("중간고사점수:"));
				    myPanel.add(mid);
				    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
				    myPanel.add(new JLabel("기말고사점수:"));
				    myPanel.add(fin);
				    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
				    myPanel.add(new JLabel("기타점수:"));
				    myPanel.add(etc);
				    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
				    myPanel.add(new JLabel("총점:"));
				    myPanel.add(total);
				    myPanel.add(Box.createHorizontalStrut(2)); // a spacer
				    myPanel.add(new JLabel("학점:"));
				    myPanel.add(grade);
				    
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"입력하시오", JOptionPane.OK_CANCEL_OPTION);
				    
				    int SID_int = Integer.parseInt(SID.getText());
				    int CID_int = Integer.parseInt(CID.getText());
				    int attn_int = Integer.parseInt(attn.getText());
				    int mid_int = Integer.parseInt(mid.getText());
				    int fin_int = Integer.parseInt(fin.getText());
				    int etc_int = Integer.parseInt(etc.getText());
				    int total_int = Integer.parseInt(total.getText());
				    String grade_str = (grade.getText());
				    
				    
			    	if(( grade_str.equals("A") || grade_str.equals("B") || grade_str.equals("C") || grade_str.equals("D") || grade_str.equals("F") )  && ( total_int <= 100 && total_int >= 0 )) {
								
					    	try {
						    	Statement stmt1 = databaseConnection.connection().createStatement();
						    	String delete = "Update Course Set Attendance = " + attn_int + ", Midterm = " + mid_int + ", Final = "+ fin_int +", Etc = "+ etc_int +", Total = " + total_int + ", Grade = '" +grade_str+ "' where profid = "+ professorId +" and classid = " + CID_int + " and studentid = " +SID_int+ " and Year = 2021 and Semester = '1 학기';";
				
								rs = stmt1.executeUpdate(delete);
								
						    }catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, "오류를 확인해주세요!\n"+e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
							}
					    	
					}
					else {
							JOptionPane.showMessageDialog(null, "총점(0~100), 학점(A~F) 범위에 맞춰 입력하세요!\n", "오류", JOptionPane.ERROR_MESSAGE);
					}
				}
				catch(NumberFormatException e3) {
					JOptionPane.showMessageDialog(null, "값을 제대로 입력해주세요!\n"+e3.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				catch (Exception e2) {
				}
				
					 
			    
			}
				
				
			
			else if(e.getSource() == btnStudentLectureInfo) {  //학생 페이지의 수강 내역 조회 버튼 클릭시
				add("South", searchLecturePanel);
				searchLecturePanel.setVisible(true);
				timetablePane.setVisible(false);
				clubHeadStudentPanel.setVisible(false);
				adviseTablePane.setVisible(false);
				txtResult.setText(" 연도 입력형식: YYYY\n 학기 선택 후 조회 버튼을 눌러주세요.");
				setSize(700,500);
			}
			else if(e.getSource() == btnSearchLecture) {
				
				String strYear = txtFieldYear.getText();
				int year = 2021;
				String semester = "1 학기";
				
				try {
					year = Integer.parseInt(strYear);
					semester = comboSemester.getSelectedItem().toString();
					
				}catch(NumberFormatException e3) {
					 JOptionPane.showMessageDialog(null, "올바른 형식으로 입력해주세요!\n예)2021", "오류", JOptionPane.ERROR_MESSAGE);
					 return;
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				try {
					Statement stmt = databaseConnection.connection().createStatement();
					
					txtResult.setText("학생번호	강좌번호	교수번호	출석점수	중간고사점수	기말고사점수	기타점수	총점	평점\n");
					
					String query = "select * from course where studentID = "+ studentId +" and Year = "+ year +" and Semester = '" + semester + "';";
					resultSet = stmt.executeQuery(query);
					
					while(resultSet.next()) {
						String str = resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getInt(3) + "\t" + resultSet.getInt(4)
	        			+ "\t" + resultSet.getInt(5)+ "\t" + resultSet.getInt(6)+ "\t" + resultSet.getInt(7)+ "\t" + resultSet.getInt(8)+ "\t" + resultSet.getString(9)+ "\n";
						
						txtResult.append(str);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(e.getSource() == btnStudentTimetable) {
				ResultSet rs1, rs2;
				
				try {
					Statement stmt1 = databaseConnection.connection().createStatement();
					Statement stmt2 = databaseConnection.connection().createStatement();
					
					String query1 = "select * from course where StudentID = "+ studentId + " and Year = 2021 and Semester = '1 학기';";
					rs1 = stmt1.executeQuery(query1);
					
					while(rs1.next()) {
						String query2 = "select * from class where ClassID = "+ rs1.getInt(2);
						rs2 = stmt2.executeQuery(query2);
						
						while(rs2.next()) {
							//시간표에 넣기
							setTimetable(rs2.getString(4), rs2.getString(5), rs2.getString(6));
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//시간표 업데이트
				table = new JTable(contents, header);
				timetablePane = new JScrollPane(table);
				add("East", timetablePane);
	
				searchLecturePanel.setVisible(false);
				txtResult.setText("강의 시간표 입니다.  ▶▶▶ ");
				timetablePane.setVisible(true);
				clubHeadStudentPanel.setVisible(false);
				setSize(900,500);
			}
			else if(e.getSource() == btnClubInfo) { 
				clubId = 5;
				ResultSet rs1, rs2;
				
				try {
					Statement stmt1 = databaseConnection.connection().createStatement();
					Statement stmt2 = databaseConnection.connection().createStatement();
					
					txtResult.setText("	동아리번호	동아리명	인원	회장ID	담당교수ID	동아리방\n");
					
					String query1 = "select * from clubrelation where StudentID = "+ studentId;
					rs1 = stmt1.executeQuery(query1);
					
					while(rs1.next()) {
						
						clubId = rs1.getInt(2);
					}
					
					String query2 = "select * from club where ClubID = "+ clubId;
					rs2 = stmt2.executeQuery(query2);
					
					while(rs2.next()) {
						String str = "\t" + rs2.getInt(1) + "\t" + rs2.getString(2) + "\t" + rs2.getInt(3) + "\t" + rs2.getInt(4)
	        			+ "\t" + rs2.getInt(5)+ "\t" + rs2.getString(6) + "\n";
						
						txtResult.append(str);
					
						if(rs2.getInt(4) == studentId) { //동아리 회장이면 동아리원 정보 확인할 수 있음
							add("South", clubHeadStudentPanel);
							clubHeadStudentPanel.setVisible(true);
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				searchLecturePanel.setVisible(false);
				timetablePane.setVisible(false);
				setSize(700,500);
			}
			else if(e.getSource() == btnSearchClubMemeber) {  //동아리 회장의 동아리원 조회 버튼 클릭 시 
				ResultSet rs1, rs2;
				ArrayList<Integer> memberList = new ArrayList<Integer>();
				
				try {
					Statement stmt1 = databaseConnection.connection().createStatement();
					Statement stmt2 = databaseConnection.connection().createStatement();
					
					txtResult.append("\n\n\n\t" + "학생번호	이름	학과		핸드폰번호		주소\n");
					
					String query1 = "select * from clubrelation where clubID = "+ clubId;
					rs1 = stmt1.executeQuery(query1);
					
					while(rs1.next()) {
						String query2 = "select * from student where StudentID = "+ rs1.getInt(3);
						rs2 = stmt2.executeQuery(query2);
						
						while(rs2.next()) {
							String str = "\t" +rs2.getInt(1) + "\t" + rs2.getString(2) + "\t" + rs2.getString(6) + "\t\t"
											+ rs2.getString(4) + "\t\t" + rs2.getString(3) + "\n";
	
							txtResult.append(str);
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(e.getSource() == btnGrade) {  
				ResultSet rs1, rs2;
				double gpa, sum = 0, creditSum = 0;
				
				try {
					Statement stmt = databaseConnection.connection().createStatement();
					Statement stmt2 = databaseConnection.connection().createStatement();
					
					txtResult.setText("		과목번호	과목명	취득학점	평점\n");
					
					String query1 = "select * from course where studentID = "+ studentId;
					rs1 = stmt.executeQuery(query1);
					
					while(rs1.next()) {
						//classId.add(rs1.getInt(2));
						String query2 = "select * from class where ClassID = " + rs1.getInt(2);
						rs2 = stmt2.executeQuery(query2);
						
						if(rs2.next()) {
							String str = "\t\t" + rs1.getInt(2) + "\t" + rs2.getString(4) + "\t" + rs2.getInt(7) + "\t" + rs1.getString(9) + "\n";
							txtResult.append(str);
							
							sum += calculateSum(rs1.getString(9), rs2.getInt(7));
							
							creditSum += rs2.getInt(7);
						}
					}
					
					gpa = sum/creditSum;
					
					System.out.println(sum);
					System.out.println(creditSum);
					String strGPA = String.format("%.2f", gpa);
	
					searchLecturePanel.setVisible(false);
					timetablePane.setVisible(false);
					clubHeadStudentPanel.setVisible(false);
					setSize(700,500);
					txtResult.append("\n		GPA : " + strGPA);
				
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(e.getSource() == btnResetDB) {
				
				//테이블 삭제 및 초기화 쿼리문
				String query1 = "SET @tables = NULL;";
				String query2 = "SELECT GROUP_CONCAT(table_schema, '.', table_name) INTO @tables FROM information_schema.tables WHERE table_schema = 'madang';";
				String query3 = "SET @tables = CONCAT('DROP TABLE ', @tables);";
				String query4 = "PREPARE stmt FROM @tables;";
				String query5 = "EXECUTE stmt;";
				String query6 = "DEALLOCATE PREPARE stmt;";
				
				//테이블 생성 쿼리문
	//			String createTableLecture = "CREATE TABLE IF NOT EXISTS `madang`.`lecture`(\r\n" + 
	//					"`lecture_id` INT NOT NULL AUTO_INCREMENT,\r\n" + 
	//					"`subgroup_number` INT NOT NULL,\r\n" + 
	//					"`professor_name` VARCHAR(30) NOT NULL,\r\n" + 
	//					"`lecture_name` VARCHAR(30) NOT NULL,\r\n" + 
	//					"`day_of_week` VARCHAR(30) NOT NULL,\r\n" + 
	//					"`lecture_period` INT NOT NULL, \r\n" + 
	//					"`credit` INT NOT NULL,\r\n" + 
	//					"`lecture_time` VARCHAR(30) NOT NULL,\r\n" + 
	//					"`offering_department` VARCHAR(30) NOT NULL,\r\n" + 
	//					"`lecture_room` VARCHAR(30) NOT NULL,\r\n" + 
	//					"PRIMARY KEY (`lecture_id`));";
				
				String createTableStudent = "CREATE TABLE IF NOT EXISTS `madang`.`Student` (\r\n" + 
						"  `StudentID` INT NOT NULL,\r\n" + 
						"  `StudentName` VARCHAR(15) NULL,\r\n" + 
						"  `StudentAddress` VARCHAR(45) NULL,\r\n" + 
						"  `StudentPhone` VARCHAR(45) NULL,\r\n" + 
						"  `StudentEmail` VARCHAR(45) NULL,\r\n" + 
						"  `Major` VARCHAR(15) NULL,\r\n" + 
						"  `Advisor` VARCHAR(15) NULL,\r\n" + 
						"  `Account` VARCHAR(45) NULL,\r\n" + 
						"  PRIMARY KEY (`StudentID`))\r\n" + 
						"ENGINE = InnoDB;";
				
				String createTableProfessor = "CREATE TABLE IF NOT EXISTS `madang`.`Professor` (\r\n" + 
						"  `ProfID` INT NOT NULL,\r\n" + 
						"  `ProfName` VARCHAR(45) NULL,\r\n" + 
						"  `ProfAddress` VARCHAR(45) NULL,\r\n" + 
						"  `ProfPhone` VARCHAR(45) NULL,\r\n" + 
						"  `ProfEmail` VARCHAR(45) NULL,\r\n" + 
						"  `DeptID` INT NULL,\r\n" + 
						"  PRIMARY KEY (`ProfID`))\r\n" + 
						"ENGINE = InnoDB;";
				
				String createTableDepartment = "CREATE TABLE IF NOT EXISTS `madang`.`Department` (\r\n" + 
						"  `DeptID` INT NOT NULL,\r\n" + 
						"  `DeptName` VARCHAR(45) NULL,\r\n" + 
						"  `DeptPhone` VARCHAR(45) NULL,\r\n" + 
						"  `DeptOffice` VARCHAR(45) NULL,\r\n" + 
						"  `HeadProfID` INT NULL,\r\n" + 
						"  PRIMARY KEY (`DeptID`))\r\n" + 
						"ENGINE = InnoDB;";
				
				String createTableClass = "CREATE TABLE IF NOT EXISTS `madang`.`Class` (\r\n" + 
						"  `ClassID` INT NOT NULL,\r\n" + 
						"  `DivClassID` INT NULL,\r\n" + 
						"  `ProfName` VARCHAR(45) NULL,\r\n" + 
						"  `ClassName` VARCHAR(45) NULL,\r\n" + 
						"  `Day` VARCHAR(45) NULL,\r\n" + 
						"  `Period` INT NULL,\r\n" + 
						"  `Credit` INT NULL,\r\n" + 
						"  `Time` INT NULL,\r\n" + 
						"  `DeptName` VARCHAR(45) NULL,\r\n" + 
						"  `ClassLocation` VARCHAR(45) NULL,\r\n" + 
						"  `DeptID` INT NOT NULL,\r\n" + 
						"  PRIMARY KEY (`ClassID`),\r\n" + 
						"  INDEX `fk_Class_Department1_idx` (`DeptID` ASC) VISIBLE,\r\n" + 
						"  CONSTRAINT `fk_Class_Department1`\r\n" + 
						"    FOREIGN KEY (`DeptID`)\r\n" + 
						"    REFERENCES `madang`.`Department` (`DeptID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION)\r\n" + 
						"ENGINE = InnoDB;";
				
				//수강내역
				String createTableCourse = "CREATE TABLE IF NOT EXISTS `madang`.`Course` (\r\n" + 
						"  `StudentID` INT NOT NULL,\r\n" + 
						"  `ClassID` INT NOT NULL,\r\n" + 
						"  `ProfID` INT NOT NULL,\r\n" + 
						"  `Attendance` INT NULL,\r\n" + 
						"  `Midterm` INT NULL,\r\n" + 
						"  `Final` INT NULL,\r\n" + 
						"  `Etc` INT NULL,\r\n" + 
						"  `Total` INT NULL,\r\n" + 
						"  `Grade` VARCHAR(45) NULL,\r\n" + 
						"  `Year` INT NULL,\r\n" + 
						"  `Semester` VARCHAR(45) NULL,\r\n" + 
						"  PRIMARY KEY (`StudentID`, `ClassID`, `ProfID`),\r\n" + 
						"  INDEX `fk_Course_Student1_idx` (`StudentID` ASC) VISIBLE,\r\n" + 
						"  INDEX `fk_Course_Class1_idx` (`ClassID` ASC) VISIBLE,\r\n" + 
						"  INDEX `fk_Course_Professor1_idx` (`ProfID` ASC) VISIBLE,\r\n" + 
						"  CONSTRAINT `fk_Course_Student1`\r\n" + 
						"    FOREIGN KEY (`StudentID`)\r\n" + 
						"    REFERENCES `madang`.`Student` (`StudentID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION,\r\n" + 
						"  CONSTRAINT `fk_Course_Class1`\r\n" + 
						"    FOREIGN KEY (`ClassID`)\r\n" + 
						"    REFERENCES `madang`.`Class` (`ClassID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION,\r\n" + 
						"  CONSTRAINT `fk_Course_Professor1`\r\n" + 
						"    FOREIGN KEY (`ProfID`)\r\n" + 
						"    REFERENCES `madang`.`Professor` (`ProfID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION)\r\n" + 
						"ENGINE = InnoDB;";
				
				String createTableClub = "CREATE TABLE IF NOT EXISTS `madang`.`Club` (\r\n" + 
						"  `ClubID` INT NOT NULL,\r\n" + 
						"  `ClubName` VARCHAR(45) NULL,\r\n" + 
						"  `Number` INT NULL,\r\n" + 
						"  `HeadStudentID` INT NULL,\r\n" + 
						"  `AdvisorProfID` INT NULL,\r\n" + 
						"  `ClubRoom` VARCHAR(45) NULL,\r\n" + 
						"  PRIMARY KEY (`ClubID`))\r\n" + 
						"ENGINE = InnoDB;";
				
				String createTablePayment = "CREATE TABLE IF NOT EXISTS `madang`.`Payment` (\r\n" + 
						"  `PaymentID` INT NOT NULL,\r\n" + 
						"  `Year` INT NULL,\r\n" + 
						"  `Semester` VARCHAR(45) NULL,\r\n" + 
						"  `TotalFee` DOUBLE NULL,\r\n" + 
						"  `PaymentFee` DOUBLE NULL,\r\n" + 
						"  `PaymentDate` DATETIME NULL,\r\n" + 
						"  `StudentID` INT NOT NULL,\r\n" + 
						"  PRIMARY KEY (`PaymentID`),\r\n" + 
						"  INDEX `fk_Payment_Student1_idx` (`StudentID` ASC) VISIBLE,\r\n" + 
						"  CONSTRAINT `fk_Payment_Student1`\r\n" + 
						"    FOREIGN KEY (`StudentID`)\r\n" + 
						"    REFERENCES `madang`.`Student` (`StudentID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION)\r\n" + 
						"ENGINE = InnoDB;";
				
				String createTableProfessorDept = "CREATE TABLE IF NOT EXISTS `madang`.`ProfessorDept` (\r\n" + 
						"  `UnitID` INT NOT NULL,\r\n" + 
						"  `DeptID` INT NOT NULL,\r\n" + 
						"  `ProfID` INT NOT NULL,\r\n" + 
						"  PRIMARY KEY (`UnitID`, `DeptID`, `ProfID`),\r\n" + 
						"  INDEX `fk_ProfessorDept_Professor1_idx` (`ProfID` ASC) VISIBLE,\r\n" + 
						"  INDEX `fk_ProfessorDept_Department1_idx` (`DeptID` ASC) VISIBLE,\r\n" + 
						"  CONSTRAINT `fk_ProfessorDept_Professor1`\r\n" + 
						"    FOREIGN KEY (`ProfID`)\r\n" + 
						"    REFERENCES `madang`.`Professor` (`ProfID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION,\r\n" + 
						"  CONSTRAINT `fk_ProfessorDept_Department1`\r\n" + 
						"    FOREIGN KEY (`DeptID`)\r\n" + 
						"    REFERENCES `madang`.`Department` (`DeptID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION)\r\n" + 
						"ENGINE = InnoDB;";
				
				//지도관계
				String createTableRelationship = "CREATE TABLE IF NOT EXISTS `madang`.`RelationShip` (\r\n" + 
						"  `RelationID` INT NOT NULL,\r\n" + 
						"  `ProfID` INT NOT NULL,\r\n" + 
						"  `StudentID` INT NOT NULL,\r\n" + 
						"  `Grade` INT NULL,\r\n" + 
						"  `Semester` VARCHAR(45) NULL,\r\n" + 
						"  PRIMARY KEY (`RelationID`),\r\n" + 
						"  INDEX `fk_RelationShip_Student1_idx` (`StudentID` ASC) VISIBLE,\r\n" + 
						"  INDEX `fk_RelationShip_Professor1_idx` (`ProfID` ASC) VISIBLE,\r\n" + 
						"  CONSTRAINT `fk_RelationShip_Student1`\r\n" + 
						"    FOREIGN KEY (`StudentID`)\r\n" + 
						"    REFERENCES `madang`.`Student` (`StudentID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION,\r\n" + 
						"  CONSTRAINT `fk_RelationShip_Professor1`\r\n" + 
						"    FOREIGN KEY (`ProfID`)\r\n" + 
						"    REFERENCES `madang`.`Professor` (`ProfID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION)\r\n" + 
						"ENGINE = InnoDB;";
				
				String createTableClubRelation = "CREATE TABLE IF NOT EXISTS `madang`.`ClubRelation` (\r\n" + 
						"  `ID` INT NOT NULL,\r\n" + 
						"  `ClubID` INT NOT NULL,\r\n" + 
						"  `StudentID` INT NOT NULL,\r\n" + 
						"  PRIMARY KEY (`ID`),\r\n" + 
						"  INDEX `fk_ClubRelation_Club1_idx` (`ClubID` ASC) VISIBLE,\r\n" + 
						"  INDEX `fk_ClubRelation_Student1_idx` (`StudentID` ASC) VISIBLE,\r\n" + 
						"  CONSTRAINT `fk_ClubRelation_Club1`\r\n" + 
						"    FOREIGN KEY (`ClubID`)\r\n" + 
						"    REFERENCES `madang`.`Club` (`ClubID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION,\r\n" + 
						"  CONSTRAINT `fk_ClubRelation_Student1`\r\n" + 
						"    FOREIGN KEY (`StudentID`)\r\n" + 
						"    REFERENCES `madang`.`Student` (`StudentID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION)\r\n" + 
						"ENGINE = InnoDB;";
				
				String createTableStudentDept = "CREATE TABLE IF NOT EXISTS `madang`.`StudentDept` (\r\n" + 
						"  `UnitID` INT NOT NULL,\r\n" + 
						"  `DeptID` INT NOT NULL,\r\n" + 
						"  `StudentID` INT NOT NULL,\r\n" + 
						"  PRIMARY KEY (`UnitID`, `DeptID`, `StudentID`),\r\n" + 
						"  INDEX `fk_StudentDept_Department1_idx` (`DeptID` ASC) VISIBLE,\r\n" + 
						"  INDEX `fk_StudentDept_Student1_idx` (`StudentID` ASC) VISIBLE,\r\n" + 
						"  CONSTRAINT `fk_StudentDept_Department1`\r\n" + 
						"    FOREIGN KEY (`DeptID`)\r\n" + 
						"    REFERENCES `madang`.`Department` (`DeptID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION,\r\n" + 
						"  CONSTRAINT `fk_StudentDept_Student1`\r\n" + 
						"    FOREIGN KEY (`StudentID`)\r\n" + 
						"    REFERENCES `madang`.`Student` (`StudentID`)\r\n" + 
						"    ON DELETE NO ACTION\r\n" + 
						"    ON UPDATE NO ACTION)\r\n" + 
						"ENGINE = InnoDB;";
				String insertTableStudent = "insert into student (StudentID, StudentName, StudentAddress, StudentPhone, StudentEmail, Major, Advisor, Account)";
				String insertTableClub = "insert into Club (ClubID, ClubName, Number, HeadStudentID, AdvisorProfID, ClubRoom)";
				String insertTablePayment = "insert into Payment (PaymentID, Year, Semester, TotalFee, PaymentFee, PaymentDate, StudentID)";
				String insertTableCourse = "insert into Course (StudentID, ClassID, ProfID, Attendance, Midterm, Final, Etc, Total, Grade,Year,Semester)";
				String insertTableClass = "insert into Class (ClassID, DivClassID, ProfName, ClassName, Day, Period, Credit, Time, DeptName, ClassLocation, DeptID)";
				String insertTalbeDepartment = "insert into Department (DeptID, DeptName, DeptPhone, DeptOffice, HeadProfID)";
				String insertTableProfessorDept = "insert into ProfessorDept (UnitID, DeptID, ProfID)";
				String insertTableProfessor = "insert into Professor (ProfID, ProfName, ProfAddress, ProfPhone, ProfEmail, DeptID)";
				String insertTableStudentDept = "insert into StudentDept(UnitID, DeptID, StudentID)";
				String insertTableClubRelation = "insert into ClubRelation(ID, ClubID, StudentID)";
				String insertTableRelationShip = "insert into RelationShip(RelationID, ProfID, StudentID, Grade, Semester)";
				try {
					Statement stmt = databaseConnection.connection().createStatement();
				
					//모든 테이블 삭제
					stmt.execute(query1);
					stmt.execute(query2);
					stmt.execute(query3);
					stmt.execute(query4);
					stmt.execute(query5);
					stmt.execute(query6);
					
					//테이블 생성
					//stmt.executeUpdate(createTableLecture);
					stmt.executeUpdate(createTableStudent);
					stmt.executeUpdate(createTableProfessor);
					stmt.executeUpdate(createTableDepartment);
					stmt.executeUpdate(createTableClass);
					stmt.executeUpdate(createTableCourse);
					
					stmt.executeUpdate(createTableClub);
					stmt.executeUpdate(createTablePayment);
					stmt.executeUpdate(createTableProfessorDept);
					stmt.executeUpdate(createTableRelationship);
					stmt.executeUpdate(createTableClubRelation);
					
					stmt.executeUpdate(createTableStudentDept);
					
					
					stmt.addBatch(insertTableStudent + "values (111, '김가나', '서울시 광진구', '010-1111-1111', 'kgana@sejong.ac.kr', '컴퓨터공학과', '가교수', '11111')");
					stmt.addBatch(insertTableStudent + "values (112, '김다라', '서울시 송파구', '010-1111-2222', 'kdara@sejong.ac.kr', '정보보호학과', '나교수', '11112')");
					stmt.addBatch(insertTableStudent + "values (113, '김마바', '서울시 용산구', '010-1111-3333', 'kmaba@sejong.ac.kr', '소프트웨어학과', '다교수', '11113')");
					stmt.addBatch(insertTableStudent + "values (114, '김사아', '서울시 관악구', '010-1111-4444', 'ksaah@sejong.ac.kr', '데이터사이언스학과', '라교수', '11114')");
					stmt.addBatch(insertTableStudent + "values (115, '김자차', '서울시 도봉구', '010-1111-5555', 'kjacha@sejong.ac.kr', '지능기전학과', '마교수', '11115')");
					stmt.addBatch(insertTableStudent + "values (116, '김카타', '서울시 마포구', '010-1111-6666', 'kkata@sejong.ac.kr', '디지털이노베이션학과', '바교수', '11116')");
					stmt.addBatch(insertTableStudent + "values (117, '김파하', '서울시 노원구', '010-1111-7777', 'kpaha@sejong.ac.kr', '만화애니메이션학과', '사교수', '11117')");
					stmt.addBatch(insertTableStudent + "values (118, '박가나', '서울시 강남구', '010-1111-8888', 'pgana@sejong.ac.kr', '건축학과', '아교수', '11118')");
					stmt.addBatch(insertTableStudent + "values (119, '박다라', '서울시 마포구', '010-1111-9999', 'pdara@sejong.ac.kr', '건설환결공학과', '자교수', '11119')");
					stmt.addBatch(insertTableStudent + "values (120, '박마바', '경기도 남양주', '010-2222-0000', 'pmaba@sejong.ac.kr', '환경에너지공학과', '차교수', '11120')");
					stmt.addBatch(insertTableStudent + "values (121, '박사아', '경기도 양평', '010-2222-1111', 'psaah@sejong.ac.kr', '지구자원시스템공학과', '카교수', '11121')");
					stmt.addBatch(insertTableStudent + "values (122, '박자차', '경가도 광주', '010-2222-2222', 'pjacha@sejong.ac.kr', '기계항공우주공학과', '타교수', '11122')");
					stmt.addBatch(insertTableStudent + "values (123, '박카타', '경기도 가평', '010-2222-3333', 'pkata@sejong.ac.kr', '나노신소재학과', '파교수', '11123')");
					stmt.addBatch(insertTableStudent + "values (124, '박파하', '경기도 고양', '010-2222-4444', 'ppaha@sejong.ac.kr', '양자원자력공학과', '하교수', '11124')");
					stmt.addBatch(insertTableStudent + "values (125, '이가나', '경기도 수원', '010-2222-5555', 'lgana@sejong.ac.kr', '국방시스템공학과', '가교수', '11125')");
					stmt.addBatch(insertTableStudent + "values (126, '이다라', '경기도 평택', '010-2222-6666', 'ldara@sejong.ac.kr', '항공시스템공학과', '나교수', '11126')");
					stmt.addBatch(insertTableStudent + "values (127, '이마바', '서울시 광진구', '010-2222-7777', 'lmaba@sejong.ac.kr', '회화과', '다교수', '11127')");
					stmt.addBatch(insertTableStudent + "values (128, '이사아', '서울시 광진구', '010-2222-8888', 'lsaah@sejong.ac.kr', '패션디자인학과', '라교수', '11128')");
					stmt.addBatch(insertTableStudent + "values (129, '이자차', '서울시 도봉구', '010-2222-9999', 'ljacha@sejong.ac.kr', '음악과', '마교수', '11129')");
					stmt.addBatch(insertTableStudent + "values (130, '이카타', '서울시 서초구', '010-3333-0000', 'lkata@sejong.ac.kr', '체육학과', '바교수', '11130')");
					stmt.addBatch(insertTableStudent + "values (131, '이파하', '서울시 광진구', '010-3333-1111', 'lpaha@sejong.ac.kr', '무용과', '사교수', '11131')");
					stmt.addBatch(insertTableStudent + "values (132, '조가나', '서울시 강남구', '010-3333-2222', 'cgana@sejong.ac.kr', '영화예술학과', '아교수', '11132')");
					stmt.addBatch(insertTableStudent + "values (133, '조다라', '서울시 강서구', '010-3333-3333', 'cdara@sejong.ac.kr', '경영학과', '자교수', '11133')");
					stmt.addBatch(insertTableStudent + "values (134, '조마바', '서울시 중랑구', '010-3333-4444', 'cmaba@sejong.ac.kr', '경제학과', '차교수', '11134')");
					stmt.addBatch(insertTableStudent + "values (135, '조사아', '서울시 강동구', '010-3333-5555', 'csaah@sejong.ac.kr', '법학과', '카교수', '11135')");
					stmt.addBatch(insertTableStudent + "values (136, '조자차', '경기도 남양주', '010-3333-6666', 'cjacha@sejong.ac.kr', '컴퓨터공학과', '가교수', '11136')");
					stmt.addBatch(insertTableStudent + "values (137, '조카타', '경기도 양평', '010-3333-7777', 'ckata@sejong.ac.kr', '컴퓨터공학과', '가교수', '11137')");
					stmt.addBatch(insertTableStudent + "values (138, '조파하', '경가도 광주', '010-3333-8888', 'cpaha@sejong.ac.kr', '소프트웨어학과', '다교수', '11138')");
					stmt.addBatch(insertTableStudent + "values (139, '신가나', '경기도 가평', '010-3333-9999', 'sgana@sejong.ac.kr', '회화과', '다교수', '11139')");
					stmt.addBatch(insertTableStudent + "values (140, '신다라', '경기도 고양', '010-4444-0000', 'sdara@sejong.ac.kr', '지능기전학과', '마교수', '11140')");
					stmt.addBatch(insertTableStudent + "values (141, '신마바', '경기도 수원', '010-4444-1111', 'smaba@sejong.ac.kr', '데이터사이언스학과', '라교수', '11141')");
					stmt.addBatch(insertTableStudent + "values (142, '신사아', '경기도 평택', '010-4444-2222', 'ssaah@sejong.ac.kr', '컴퓨터공학과', '가교수', '11142')");
					stmt.addBatch(insertTableStudent + "values (143, '신자차', '서울시 광진구', '010-4444-3333', 'sjacha@sejong.ac.kr', '회화과', '다교수', '11143')");
					stmt.addBatch(insertTableStudent + "values (144, '신카타', '서울시 광진구', '010-4444-4444', 'skatasejong.ac.kr', '패션디자인학과', '라교수', '11144')");
					stmt.addBatch(insertTableStudent + "values (145, '신파하', '서울시 도봉구', '010-4444-5555', 'spahaa@sejong.ac.kr', '음악과', '마교수', '11145')");
					stmt.addBatch(insertTableStudent + "values (146, '우가나', '서울시 서초구', '010-4444-6666', 'ugana@sejong.ac.kr', '체육학과', '바교수', '11146')");
					stmt.addBatch(insertTableStudent + "values (147, '우다라', '서울시 광진구', '010-4444-7777', 'udara@sejong.ac.kr', '무용과', '사교수', '11147')");
					stmt.addBatch(insertTableStudent + "values (148, '우마바', '서울시 강남구', '010-4444-8888', 'umaba@sejong.ac.kr', '영화예술학과', '아교수', '11148')");
					stmt.addBatch(insertTableStudent + "values (149, '우사아', '서울시 강서구', '010-4444-9999', 'usaah@sejong.ac.kr', '경영학과', '자교수', '11149')");
					stmt.addBatch(insertTableStudent + "values (150, '우자차', '서울시 중랑구', '010-5555-0000', 'ujacha@sejong.ac.kr', '경제학과', '차교수', '11150')");
					rse = stmt.executeBatch();
					
					stmt.addBatch(insertTableProfessor + "values (1, '가교수', '서울시 광진구', '010-0000-0001','gprof@sejong.ac.kr', 1)");
					stmt.addBatch(insertTableProfessor + "values (2, '나교수', '서울시 광진구', '010-0000-0002','nprof@sejong.ac.kr', 2)");
					stmt.addBatch(insertTableProfessor + "values (3, '다교수', '서울시 광진구', '010-0000-0003','dprof@sejong.ac.kr', 3)");
					stmt.addBatch(insertTableProfessor + "values (4, '라교수', '서울시 광진구', '010-0000-0004','rprof@sejong.ac.kr', 4)");
					stmt.addBatch(insertTableProfessor + "values (5, '마교수', '서울시 광진구', '010-0000-0005','mprof@sejong.ac.kr', 5)");
					stmt.addBatch(insertTableProfessor + "values (6, '바교수', '서울시 강남구', '010-0000-0006','bprof@sejong.ac.kr', 6)");
					stmt.addBatch(insertTableProfessor + "values (7, '사교수', '서울시 강남구', '010-0000-0007','sprof@sejong.ac.kr', 7)");
					stmt.addBatch(insertTableProfessor + "values (8, '아교수', '서울시 강남구', '010-0000-0008','aprof@sejong.ac.kr', 8)");
					stmt.addBatch(insertTableProfessor + "values (9, '자교수', '서울시 강남구', '010-0000-0009','jprof@sejong.ac.kr', 9)");
					stmt.addBatch(insertTableProfessor + "values (10, '차교수', '서울시 강남구', '010-0000-0010','cprof@sejong.ac.kr',10)");
					stmt.addBatch(insertTableProfessor + "values (11, '카교수', '서울시 노원구', '010-0000-0011','kprof@sejong.ac.kr', 11)");
					stmt.addBatch(insertTableProfessor + "values (12, '타교수', '서울시 노원구', '010-0000-0012','tprof@sejong.ac.kr', 12)");
					stmt.addBatch(insertTableProfessor + "values (13, '파교수', '서울시 노원구', '010-0000-0013','pprof@sejong.ac.kr', 13)");
					stmt.addBatch(insertTableProfessor + "values (14, '하교수', '서울시 노원구', '010-0000-0014','hprof@sejong.ac.kr', 14)");
					stmt.addBatch(insertTableProfessor + "values (15, '가교수', '서울시 노원구', '010-0000-0015','ggprof@sejong.ac.kr', 15)");
					stmt.addBatch(insertTableProfessor + "values (16, '나교수', '서울시 용산구', '010-0000-0016','nnprof@sejong.ac.kr', 16)");
					stmt.addBatch(insertTableProfessor + "values (17, '다교수', '서울시 용산구', '010-0000-0017','ddprof@sejong.ac.kr', 17)");
					stmt.addBatch(insertTableProfessor + "values (18, '라교수', '서울시 용산구', '010-0000-0018','rrprof@sejong.ac.kr', 18)");
					stmt.addBatch(insertTableProfessor + "values (19, '마교수', '서울시 용산구', '010-0000-0019','mmprof@sejong.ac.kr', 19)");
					stmt.addBatch(insertTableProfessor + "values (20, '바교수', '서울시 용산구', '010-0000-0020','bbprof@sejong.ac.kr', 20)");
					stmt.addBatch(insertTableProfessor + "values (21, '사교수', '서울시 강동구', '010-0000-0021','ssprof@sejong.ac.kr', 21)");
					stmt.addBatch(insertTableProfessor + "values (22, '아교수', '서울시 강동구', '010-0000-0022','aaprof@sejong.ac.kr', 22)");
					stmt.addBatch(insertTableProfessor + "values (23, '자교수', '서울시 강동구', '010-0000-0023','jjprof@sejong.ac.kr', 23)");
					stmt.addBatch(insertTableProfessor + "values (24, '차교수', '서울시 강동구', '010-0000-0024','ccprof@sejong.ac.kr', 24)");
					stmt.addBatch(insertTableProfessor + "values (25, '카교수', '서울시 강동구', '010-0000-0025','kkprof@sejong.ac.kr', 25)");
					rse = stmt.executeBatch();
					
					stmt.addBatch(insertTalbeDepartment + "values (1, '컴퓨터공학과', '0000001', '센101', 1);");
					stmt.addBatch(insertTalbeDepartment + "values (2, '정보보호학과', '0000002', '센102', 2);");
					stmt.addBatch(insertTalbeDepartment + "values (3, '소프트웨어학과', '0000003', '센103', 3);");
					stmt.addBatch(insertTalbeDepartment + "values (4, '데이터사이언스학과', '0000004', '센104', 4);");
					stmt.addBatch(insertTalbeDepartment + "values (5, '지능기전학과', '0000005', '센105', 5);");
					stmt.addBatch(insertTalbeDepartment + "values (6, '디지털이노베이션학과', '0000006', '센106', 6);");
					stmt.addBatch(insertTalbeDepartment + "values (7, '만화애니메이션학과', '0000007', '센107', 7);");
					stmt.addBatch(insertTalbeDepartment + "values (8, '건축학과', '0000008', '센108', 8);");
					stmt.addBatch(insertTalbeDepartment + "values (9, '건설환경공학과', '0000009', '센109', 9);");
					stmt.addBatch(insertTalbeDepartment + "values (10, '환경에너지공학과', '0000010', '센110', 10);");
					stmt.addBatch(insertTalbeDepartment + "values (11, '지구자원시스템공학과', '0000011', '센111', 11);");
					stmt.addBatch(insertTalbeDepartment + "values (12, '기계항공우주공학과', '0000012', '센112', 12);");
					stmt.addBatch(insertTalbeDepartment + "values (13, '나노신소재학과', '0000013', '센113', 13);");
					stmt.addBatch(insertTalbeDepartment + "values (14, '양자원자력공학과', '0000014', '센114', 14);");
					stmt.addBatch(insertTalbeDepartment + "values (15, '국방시스템공학과', '0000015', '센115', 15);");
					stmt.addBatch(insertTalbeDepartment + "values (16, '항공시스템공학과', '0000016', '센116', 16);");
					stmt.addBatch(insertTalbeDepartment + "values (17, '회화과', '0000017', '센117', 17);");
					stmt.addBatch(insertTalbeDepartment + "values (18, '패션디자인학과', '0000018', '센118', 18);");
					stmt.addBatch(insertTalbeDepartment + "values (19, '음악과', '0000019', '센119', 19);");
					stmt.addBatch(insertTalbeDepartment + "values (20, '체육학과', '0000020', '센120', 20);");
					stmt.addBatch(insertTalbeDepartment + "values (21, '무용과', '0000021', '센121', 21);");
					stmt.addBatch(insertTalbeDepartment + "values (22, '영화예술학과', '0000022', '센122', 22);");
					stmt.addBatch(insertTalbeDepartment + "values (23, '경영학과', '0000023', '센123', 23);");
					stmt.addBatch(insertTalbeDepartment + "values (24, '경제학과', '0000024', '센124', 24);");
					stmt.addBatch(insertTalbeDepartment + "values (25, '법학과', '0000025', '센125', 25);");
					rse = stmt.executeBatch();
					
					
					stmt.addBatch(insertTableStudentDept + "values (1, 1,111);");
					stmt.addBatch(insertTableStudentDept + "values (2, 2,112);");
					stmt.addBatch(insertTableStudentDept + "values (3, 3,113);");
					stmt.addBatch(insertTableStudentDept + "values (4, 4,114);");
					stmt.addBatch(insertTableStudentDept + "values (5, 5,115);");
					stmt.addBatch(insertTableStudentDept + "values (6, 6,116);");
					stmt.addBatch(insertTableStudentDept + "values (7, 7,117);");
					stmt.addBatch(insertTableStudentDept + "values (8, 8,118);");
					stmt.addBatch(insertTableStudentDept + "values (9, 9,119);");
					stmt.addBatch(insertTableStudentDept + "values (10, 10, 120);");
					stmt.addBatch(insertTableStudentDept + "values (11, 11, 121);");
					stmt.addBatch(insertTableStudentDept + "values (12, 12, 122);");
					stmt.addBatch(insertTableStudentDept + "values (13, 13, 123);");
					stmt.addBatch(insertTableStudentDept + "values (14, 14, 124);");
					stmt.addBatch(insertTableStudentDept + "values (15, 15, 125);");
					stmt.addBatch(insertTableStudentDept + "values (16, 16, 126);");
					stmt.addBatch(insertTableStudentDept + "values (17, 17, 127);");
					stmt.addBatch(insertTableStudentDept + "values (18, 18, 128);");
					stmt.addBatch(insertTableStudentDept + "values (19, 19, 129);");
					stmt.addBatch(insertTableStudentDept + "values (20, 20, 130);");
					stmt.addBatch(insertTableStudentDept + "values (21, 21, 131);");
					stmt.addBatch(insertTableStudentDept + "values (22, 22, 132);");
					stmt.addBatch(insertTableStudentDept + "values (23, 23, 133);");
					stmt.addBatch(insertTableStudentDept + "values (24, 24, 134);");
					stmt.addBatch(insertTableStudentDept + "values (25, 25, 135);");
					stmt.addBatch(insertTableStudentDept + "values (26, 1, 136);");
					stmt.addBatch(insertTableStudentDept + "values (27, 2, 137);");
					stmt.addBatch(insertTableStudentDept + "values (28, 3, 138);");
					stmt.addBatch(insertTableStudentDept + "values (29, 4, 139);");
					stmt.addBatch(insertTableStudentDept + "values (30, 5, 140);");
					stmt.addBatch(insertTableStudentDept + "values (31, 6, 141);");
					stmt.addBatch(insertTableStudentDept + "values (32, 7, 142);");
					stmt.addBatch(insertTableStudentDept + "values (33, 8, 143);");
					stmt.addBatch(insertTableStudentDept + "values (34, 9, 144);");
					stmt.addBatch(insertTableStudentDept + "values (35, 10, 145);");
					stmt.addBatch(insertTableStudentDept + "values (36, 11, 146);");
					stmt.addBatch(insertTableStudentDept + "values (37, 12, 147);");
					stmt.addBatch(insertTableStudentDept + "values (38, 13, 148);");
					stmt.addBatch(insertTableStudentDept + "values (39, 14, 149);");
					stmt.addBatch(insertTableStudentDept + "values (40, 15, 150);");
					rse = stmt.executeBatch();
					
					
					stmt.addBatch(insertTableProfessorDept + "values (1, 1, 1);");
					stmt.addBatch(insertTableProfessorDept + "values (2, 2, 2);");
					stmt.addBatch(insertTableProfessorDept + "values (3, 3, 3);");
					stmt.addBatch(insertTableProfessorDept + "values (4, 4, 4);");
					stmt.addBatch(insertTableProfessorDept + "values (5, 5, 5);");
					stmt.addBatch(insertTableProfessorDept + "values (6, 6, 6);");
					stmt.addBatch(insertTableProfessorDept + "values (7, 7, 7);");
					stmt.addBatch(insertTableProfessorDept + "values (8, 8, 8);");
					stmt.addBatch(insertTableProfessorDept + "values (9, 9, 9);");
					stmt.addBatch(insertTableProfessorDept + "values (10, 10, 10);");
					stmt.addBatch(insertTableProfessorDept + "values (11, 11, 11);");
					stmt.addBatch(insertTableProfessorDept + "values (12, 12, 12);");
					stmt.addBatch(insertTableProfessorDept + "values (13, 13, 13);");
					stmt.addBatch(insertTableProfessorDept + "values (14, 14, 14);");
					stmt.addBatch(insertTableProfessorDept + "values (15, 15, 15);");
					stmt.addBatch(insertTableProfessorDept + "values (16, 16, 16);");
					stmt.addBatch(insertTableProfessorDept + "values (17, 17, 17);");
					stmt.addBatch(insertTableProfessorDept + "values (18, 18, 18);");
					stmt.addBatch(insertTableProfessorDept + "values (19, 19, 19);");
					stmt.addBatch(insertTableProfessorDept + "values (20, 20, 20);");
					stmt.addBatch(insertTableProfessorDept + "values (21, 21, 21);");
					stmt.addBatch(insertTableProfessorDept + "values (22, 22, 22);");
					stmt.addBatch(insertTableProfessorDept + "values (23, 23, 23);");
					stmt.addBatch(insertTableProfessorDept + "values (24, 24, 24);");
					stmt.addBatch(insertTableProfessorDept + "values (25, 25, 25);");
					rse = stmt.executeBatch();
					
					stmt.addBatch(insertTableClass + "values(1, 001, '가교수', 'C', '월', 1, 2 , 3, '컴퓨터공학과', '센200', 1);");
					stmt.addBatch(insertTableClass + "values(2, 002, '가교수', 'C', '월', 4, 2 , 3, '컴퓨터공학과', '센201', 1);");
					stmt.addBatch(insertTableClass + "values(3, 001, '나교수', 'Python', '화', 3, 2 , 1, '정보보호학과', '센202', 2);");
					stmt.addBatch(insertTableClass + "values(4, 001, '다교수', 'C++', '금', 2, 2 , 1, '소프트웨어학과', '센203', 3);");
					stmt.addBatch(insertTableClass + "values(5, 001, '라교수', 'C++', '월', 3, 2 , 2, '데이터사이언스학과', '센204', 4);");
					stmt.addBatch(insertTableClass + "values(6, 001, '마교수', 'C#', '화', 6, 2 , 3, '지능기전학과', '센205', 5);");
					stmt.addBatch(insertTableClass + "values(7, 001, '바교수', 'C#', '월', 4, 2 , 1, '디지털이노베이션학과', '센206', 6);");
					stmt.addBatch(insertTableClass + "values(8, 001, '사교수', '만화그리기', '월', 3, 2 , 1, '만화애니메이션학과', '센207', 7);");
					stmt.addBatch(insertTableClass + "values(9, 001, '자교수', '건물구조', '월', 3, 2 , 1, '건설환경공학과', '센208', 9);");
					stmt.addBatch(insertTableClass + "values(10, 001, '차교수', '쓰레기줍기', '월', 5, 2 , 2, '환경에너지공학과', '센209', 10);");
					stmt.addBatch(insertTableClass + "values(11, 001, '카교수', '핵', '화', 5, 2 , 3, '지구자원시스템공학과', '센210', 11);");
					stmt.addBatch(insertTableClass + "values(12, 001, '타교수', '우주왕복', '월', 3, 2 , 3, '기계항공우주공학과', '센211', 12);");
					stmt.addBatch(insertTableClass + "values(13, 001, '타교수', '항공', '화', 4, 2 , 1, '기계항공우주공학과', '센212', 12);");
					stmt.addBatch(insertTableClass + "values(14, 001, '파교수', '나노에 대해', '월', 2, 2 , 2, '나노신소재공학과', '센213', 13);");
					stmt.addBatch(insertTableClass + "values(15, 001, '파교수', '나노에 대한 고찰', '월', 4, 2 , 1, '나노신소재공학과', '센214', 13);");
					stmt.addBatch(insertTableClass + "values(16, 001, '파교수', '나노의 심도깊은 고찰 ', '월', 4, 2 , 1, '나노신소재공학과', '센215', 13);");
					stmt.addBatch(insertTableClass + "values(17, 001, '하교수', '원자력', '월', 1, 2 , 1, '양자원자력공학과', '센216', 14);");
					stmt.addBatch(insertTableClass + "values(18, 001, '하교수', '양자론', '월', 1, 2 , 3, '양자원자력공학과', '센217', 14);");
					stmt.addBatch(insertTableClass + "values(19, 001, '가교수', '전투', '월', 3, 2 , 3, '국방시스템공학과', '센218', 15);");
					stmt.addBatch(insertTableClass + "values(20, 001, '가교수', '사격', '월', 5, 2 , 1, '국방시스템공학과', '센219', 15);");
					stmt.addBatch(insertTableClass + "values(21, 001, '가교수', '해킹', '화', 5, 2, 1, '국방시스템공학과', '센220', 15);");
					stmt.addBatch(insertTableClass + "values(22, 001, '나교수', '비행기 구조', '월', 5, 2 , 1, '항공시스템공학과', '센221', 16);");
					stmt.addBatch(insertTableClass + "values(23, 001, '다교수', '소묘', '목', 4, 2 , 1, '회화과', '센222', 17);");
					stmt.addBatch(insertTableClass + "values(24, 001, '다교수', '점묘', '금', 4, 2 , 1, '회화과', '센223', 17);");
					stmt.addBatch(insertTableClass + "values(25, 001, '라교수', '패션트렌드', '월', 4, 2 , 3, '패션디자인학과', '센224', 18);");
					stmt.addBatch(insertTableClass + "values(26, 001, '라교수', '명품 고찰', '수', 4, 2 , 1, '패션디자인학과', '센225', 18);");
					stmt.addBatch(insertTableClass + "values(27, 001, '마교수', '피아노', '화', 3, 2 , 1, '음악과', '센226', 19);");
					stmt.addBatch(insertTableClass + "values(28, 001, '마교수', '기타', '월', 3, 2 , 1, '음악과', '센227', 19);");
					stmt.addBatch(insertTableClass + "values(29, 001, '바교수', '축구', '월', 2, 2 , 3, '체육학과', '센228', 20);");
					stmt.addBatch(insertTableClass + "values(30, 001, '사교수', '현대무용', '금', 2, 2 , 3, '무용과', '센229', 21);");
					stmt.addBatch(insertTableClass + "values(31, 001, '아교수', '연기', '금', 2, 2 , 1, '영화예술학과', '센230', 22);");
					stmt.addBatch(insertTableClass + "values(32, 001, '자교수', '마케팅', '목', 5, 2 , 1, '경영학과', '센231', 23);");
					stmt.addBatch(insertTableClass + "values(33, 001, '자교수', '기업 구조', '월', 4, 2 , 1, '경영학과', '센232', 23);");
					stmt.addBatch(insertTableClass + "values(34, 001, '자교수', '인사', '수', 3, 2 , 1, '경영학과', '센233', 23);");
					stmt.addBatch(insertTableClass + "values(35, 001, '차교수', '미시론', '화', 1, 2 ,1 , '경제학과', '센234', 24);");
					stmt.addBatch(insertTableClass + "values(36, 001, '차교수', '거시론', '화', 3, 2 , 1, '경제학과', '센235', 24);");
					stmt.addBatch(insertTableClass + "values(37, 001, '차교수', '통계', '월', 4, 2 , 1, '경제학과', '센236', 24);");
					stmt.addBatch(insertTableClass + "values(38, 001, '차교수', '확률', '월', 5, 2 , 1, '경제학과', '센237', 24);");
					stmt.addBatch(insertTableClass + "values(39, 001, '아교수', '건물 구조 고찰', '금', 3, 2 , 1, '건축학과', '센238', 8);");
					stmt.addBatch(insertTableClass + "values(40, 001, '카교수', '우리나라 법', '수', 1, 2 , 1, '법학과', '센239', 25);");
					rse = stmt.executeBatch();
					
					
					stmt.addBatch(insertTableClub + "values(1, '엔샵', 25, 111, 1, '학201');");
					stmt.addBatch(insertTableClub + "values(2, '인터페이스', 100, 136, 2, '학501');");
					stmt.addBatch(insertTableClub + "values(3, 'SSG', 40, 113, 3, '학502');");
					stmt.addBatch(insertTableClub + "values(4, '가동아리', 55, 114, 4, '학503');");
					stmt.addBatch(insertTableClub + "values(5, '나동아리', 60, 115, 5, '학504');");
					stmt.addBatch(insertTableClub + "values(6, '다동아리', 72, 116, 6, '학505');");
					stmt.addBatch(insertTableClub + "values(7, '라동아리', 125, 117, 7, '학506');");
					stmt.addBatch(insertTableClub + "values(8, '마동아리', 35, 118, 8, '학507');");
					stmt.addBatch(insertTableClub + "values(9, '바동아리', 92, 119, 9, '학508');");
					stmt.addBatch(insertTableClub + "values(10, '사동아리', 46, 120, 10, '학509');");
					stmt.addBatch(insertTableClub + "values(11, '아동아리', 75, 121, 11, '학510');");
					stmt.addBatch(insertTableClub + "values(12, '자동아리', 61, 122, 12, '학511');");
					stmt.addBatch(insertTableClub + "values(13, '차동아리', 120, 123, 13, '학512');");
					stmt.addBatch(insertTableClub + "values(14, '카동아리', 115, 124, 14, '학513');");
					stmt.addBatch(insertTableClub + "values(15, '타동아리', 90, 125, 15, '학514');");
					stmt.addBatch(insertTableClub + "values(16, '파동아리', 84, 126, 16, '학515');");
					stmt.addBatch(insertTableClub + "values(17, '하동아리', 25, 127, 17, '학601');");
					stmt.addBatch(insertTableClub + "values(18, '고동아리', 102, 128, 18, '학602');");
					stmt.addBatch(insertTableClub + "values(19, '노동아리', 101, 129, 19, '학603');");
					stmt.addBatch(insertTableClub + "values(20, '도동아리', 66, 130, 20, '학604');");
					stmt.addBatch(insertTableClub + "values(21, '로동아리', 45, 131, 21, '학605');");
					stmt.addBatch(insertTableClub + "values(22, '모동아리', 65, 132, 22, '학606');");
					stmt.addBatch(insertTableClub + "values(23, '보동아리', 27, 133, 23, '학607');");
					stmt.addBatch(insertTableClub + "values(24, '소동아리', 75, 134, 24, '학608');");
					stmt.addBatch(insertTableClub + "values(25, '오동아리', 85, 135, 25, '학609');");
					rse = stmt.executeBatch();
					
					stmt.addBatch(insertTablePayment + "values(1, 2021, '1 학기', 4274000, 4274000, '2021-03-02', 111);");
					stmt.addBatch(insertTablePayment + "values(2, 2021, '1 학기', 4150000, 4150000, '2021-03-03', 112);");
					stmt.addBatch(insertTablePayment + "values(3, 2021, '1 학기', 5105000, 5105000, '2021-03-05', 113);");
					stmt.addBatch(insertTablePayment + "values(4, 2021, '1 학기', 5105000, 5105000, '2021-03-05', 114);");
					stmt.addBatch(insertTablePayment + "values(5, 2021, '1 학기', 3800000, 3000000, '2021-03-11', 115);");
					stmt.addBatch(insertTablePayment + "values(6, 2020, '겨울 학기', 210000, 210000, '2020-12-20', 116);");
					stmt.addBatch(insertTablePayment + "values(7, 2020, '겨울 학기', 140000, 140000, '2020-12-22', 117);");
					stmt.addBatch(insertTablePayment + "values(8, 2021, '1 학기', 5105000, 5105000, '2021-03-10', 118);");
					stmt.addBatch(insertTablePayment + "values(9, 2021, '1 학기', 4100000, 4100000, '2021-03-10', 119);");
					stmt.addBatch(insertTablePayment + "values(10, 2021, '1 학기', 3800000, 3800000, '2021-03-11', 120);");
					stmt.addBatch(insertTablePayment + "values(11, 2021, '1 학기', 4274000, 4274000, '2021-03-02', 121);");
					stmt.addBatch(insertTablePayment + "values(12, 2021, '1 학기', 4150000, 4150000, '2021-03-05', 122);");
					stmt.addBatch(insertTablePayment + "values(13, 2021, '1 학기', 5105000, 5105000, '2021-03-10', 123);");
					stmt.addBatch(insertTablePayment + "values(14, 2021, '1 학기', 4100000, 4100000, '2021-03-10', 124);");
					stmt.addBatch(insertTablePayment + "values(15, 2021, '1 학기', 3800000, 3800000, '2021-03-11', 125);");
					stmt.addBatch(insertTablePayment + "values(16, 2021, '1 학기', 4274000, 4274000, '2021-03-02', 126);");
					stmt.addBatch(insertTablePayment + "values(17, 2021, '1 학기', 4150000, 4150000, '2021-03-05', 127);");
					stmt.addBatch(insertTablePayment + "values(18, 2021, '1 학기', 5105000, 5105000, '2021-03-10', 128);");
					stmt.addBatch(insertTablePayment + "values(19, 2021, '1 학기', 4100000, 4100000, '2021-03-10', 129);");
					stmt.addBatch(insertTablePayment + "values(20, 2021, '1 학기', 4400000, 4400000, '2021-03-11', 130);");
					stmt.addBatch(insertTablePayment + "values(21, 2021, '1 학기', 4274000, 4274000, '2021-03-11', 131);");
					stmt.addBatch(insertTablePayment + "values(22, 2021, '1 학기', 4150000, 4150000, '2021-03-05', 132);");
					stmt.addBatch(insertTablePayment + "values(23, 2021, '1 학기', 5105000, 5105000, '2021-03-10', 133);");
					stmt.addBatch(insertTablePayment + "values(24, 2021, '1 학기', 4100000, 4100000, '2021-03-10', 134);");
					stmt.addBatch(insertTablePayment + "values(25, 2021, '1 학기', 3850000, 3850000, '2021-03-11', 135);");
					stmt.addBatch(insertTablePayment + "values(26, 2021, '1 학기', 4274000, 4274000, '2021-03-07', 136);");
					stmt.addBatch(insertTablePayment + "values(27, 2021, '1 학기', 4150000, 4150000, '2021-03-05', 137);");
					stmt.addBatch(insertTablePayment + "values(28, 2021, '1 학기', 5105000, 5105000, '2021-03-10', 138);");
					stmt.addBatch(insertTablePayment + "values(29, 2021, '1 학기', 4100000, 4100000, '2021-03-10', 139);");
					stmt.addBatch(insertTablePayment + "values(30, 2021, '1 학기', 3800000, 3800000, '2021-03-11', 140);");
					stmt.addBatch(insertTablePayment + "values(31, 2021, '1 학기', 4274000, 4274000, '2021-03-02', 141);");
					stmt.addBatch(insertTablePayment + "values(32, 2021, '1 학기', 4150000, 4150000, '2021-03-06', 142);");
					stmt.addBatch(insertTablePayment + "values(33, 2021, '1 학기', 5105000, 5105000, '2021-03-07', 143);");
					stmt.addBatch(insertTablePayment + "values(34, 2021, '1 학기', 4100000, 4100000, '2021-03-12', 144);");
					stmt.addBatch(insertTablePayment + "values(35, 2021, '1 학기', 3800000, 3800000, '2021-03-12', 145);");
					stmt.addBatch(insertTablePayment + "values(36, 2021, '1 학기', 4274000, 4274000, '2021-03-02', 146);");
					stmt.addBatch(insertTablePayment + "values(37, 2021, '1 학기', 4150000, 4150000, '2021-03-05', 147);");
					stmt.addBatch(insertTablePayment + "values(38, 2021, '1 학기', 5105000, 5105000, '2021-03-10', 148);");
					stmt.addBatch(insertTablePayment + "values(39, 2021, '1 학기', 4100000, 4100000, '2021-03-10', 149);");
					stmt.addBatch(insertTablePayment + "values(40, 2021, '1 학기', 3800000, 3800000, '2021-03-11', 150);");
					rse = stmt.executeBatch();
					
					stmt.addBatch(insertTableRelationShip + "values(1, 1, 111, 1, '여름학기');");
					stmt.addBatch(insertTableRelationShip + "values(2, 2, 112, 1, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(3, 3, 113, 1, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(4, 4, 114, 1, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(5, 5, 115, 1, '여름학기');");
					stmt.addBatch(insertTableRelationShip + "values(6, 6, 116, 1, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(7, 7, 117, 1, '겨울학기');");
					stmt.addBatch(insertTableRelationShip + "values(8, 8, 118, 1, '여름학기');");
					stmt.addBatch(insertTableRelationShip + "values(9, 9, 119, 1, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(10, 10, 120, 2, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(11, 11, 121, 2, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(12, 12, 122, 2, '겨울학기');");
					stmt.addBatch(insertTableRelationShip + "values(13, 13, 123, 2, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(14, 14, 124, 2, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(15, 15, 125, 2, '겨울학기');");
					stmt.addBatch(insertTableRelationShip + "values(16, 16, 126, 2, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(17, 17, 127, 2, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(18, 18, 128, 2, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(19, 19, 129, 2, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(20, 20, 130, 2, '겨울학기');");
					stmt.addBatch(insertTableRelationShip + "values(21, 21, 131, 2, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(22, 22, 132, 3, '여름학기');");
					stmt.addBatch(insertTableRelationShip + "values(23, 23, 133, 3, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(24, 24, 134, 3, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(25, 25, 135, 3, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(26, 1, 136, 3, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(27, 1, 137, 3, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(28, 3, 138, 3, '겨울학기');");
					stmt.addBatch(insertTableRelationShip + "values(29, 17, 139, 3, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(30, 5, 140, 3, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(31, 4, 141, 4, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(32, 1, 142, 4, '여름학기');");
					stmt.addBatch(insertTableRelationShip + "values(33, 17, 143, 4, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(34, 18, 144, 4, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(35, 19, 145, 4, '여름학기');");
					stmt.addBatch(insertTableRelationShip + "values(36, 20, 146, 4, '2학기');");
					stmt.addBatch(insertTableRelationShip + "values(37, 21, 147, 4, '겨울학기');");
					stmt.addBatch(insertTableRelationShip + "values(38, 22, 148, 4, '1학기');");
					stmt.addBatch(insertTableRelationShip + "values(39, 23, 149, 4, '여름학기');");
					stmt.addBatch(insertTableRelationShip + "values(40, 24, 150, 4, '2학기');");
					rse = stmt.executeBatch();
					
					stmt.addBatch(insertTableClubRelation + "values(1, 1, 111);");
					stmt.addBatch(insertTableClubRelation + "values(2, 2, 136);");
					stmt.addBatch(insertTableClubRelation + "values(3, 3, 113);");
					stmt.addBatch(insertTableClubRelation + "values(4, 4, 114);");
					stmt.addBatch(insertTableClubRelation + "values(5, 5, 115);");
					stmt.addBatch(insertTableClubRelation + "values(6, 6, 116);");
					stmt.addBatch(insertTableClubRelation + "values(7, 7, 117);");
					stmt.addBatch(insertTableClubRelation + "values(8, 8, 118);");
					stmt.addBatch(insertTableClubRelation + "values(9, 9, 119);");
					stmt.addBatch(insertTableClubRelation + "values(10, 10, 120);");
					stmt.addBatch(insertTableClubRelation + "values(11, 11, 121);");
					stmt.addBatch(insertTableClubRelation + "values(12, 12, 122);");
					stmt.addBatch(insertTableClubRelation + "values(13, 13, 123);");
					stmt.addBatch(insertTableClubRelation + "values(14, 14, 124);");
					stmt.addBatch(insertTableClubRelation + "values(15, 15, 125);");
					stmt.addBatch(insertTableClubRelation + "values(16, 16, 126);");
					stmt.addBatch(insertTableClubRelation + "values(17, 17, 127);");
					stmt.addBatch(insertTableClubRelation + "values(18, 18, 128);");
					stmt.addBatch(insertTableClubRelation + "values(19, 19, 129);");
					stmt.addBatch(insertTableClubRelation + "values(20, 20, 130);");
					stmt.addBatch(insertTableClubRelation + "values(21, 21, 131);");
					stmt.addBatch(insertTableClubRelation + "values(22, 22, 132);");
					stmt.addBatch(insertTableClubRelation + "values(23, 23, 133);");
					stmt.addBatch(insertTableClubRelation + "values(24, 24, 134);");
					stmt.addBatch(insertTableClubRelation + "values(25, 25, 135);");
					rse = stmt.executeBatch();
					
					stmt.addBatch(insertTableCourse + "values(111, 1, 1, 10, 10, 30, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(111, 3, 2, 5, 30, 25, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(111, 4, 3, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(112, 5, 4, 10, 30, 20, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(112, 6, 5, 10, 30, 20, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(112, 3, 2, 10, 10, 10, 30, 60, 'D', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(113, 7, 6, 10, 20, 20, 20, 70, 'C', 2021,'1 학기');");
					stmt.addBatch(insertTableCourse + "values(113, 5, 4, 7, 30, 23, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(113, 2, 1, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(114, 7, 6, 10, 30, 10, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(114, 4, 3, 10, 15, 20, 30, 75, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(114, 2, 1, 10, 25, 20, 30, 85, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(115, 2, 1, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(115, 8, 7, 10, 30, 20, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(115, 7, 6, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(116, 9, 9, 10, 20, 30, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(116, 6, 5, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(116, 1, 1, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(117, 9, 9, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(117, 2, 1, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(117, 3, 2, 10, 0, 20, 30, 60, 'D', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(118, 7, 6, 10, 13, 23, 30, 76, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(118, 5, 4, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(118, 1, 1, 10, 20, 20, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(119, 2, 1, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(119, 9, 9, 10, 22, 11, 30, 73, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(119, 10, 10, 10, 30, 20, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(120, 3, 2, 10, 20, 20, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(120, 1, 1, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(120, 5, 4, 10, 20, 20, 20, 70, 'C', 2020, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(121, 11, 11, 10, 30, 30, 20, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(121, 10, 10, 10, 30, 20, 20, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(121, 3, 2, 10, 20, 20, 20, 70, 'C', 2020, '여름 학기');");
					stmt.addBatch(insertTableCourse + "values(122, 7, 6, 6, 20, 20, 20, 66, 'D', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(122, 1, 1, 10, 20, 30, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(122, 5, 4, 10, 20, 20, 20, 70, 'C', 2020, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(123, 9, 9, 10, 30, 20, 20, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(123, 1, 1, 10, 10, 20, 20, 60, 'D', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(123, 6, 5, 10, 0, 20, 20, 50, 'F', 2020, '여름 학기');");
					stmt.addBatch(insertTableCourse + "values(124, 5, 4, 10, 20, 20, 20, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(124, 12, 12, 10, 20, 20, 20, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(124, 11, 11, 10, 10, 30, 20, 70, 'C', 2020, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(125, 13, 12, 10, 30, 20, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(125, 14, 13, 10, 20, 30, 20, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(125, 15, 13, 10, 20, 20, 20, 70, 'C', 2020, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(126, 12, 12, 10, 10, 20, 20, 60, 'D', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(126, 17, 14, 10, 10, 30, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(126, 18, 14, 10, 30, 30, 20, 90, 'A', 2020, '여름 학기');");
					stmt.addBatch(insertTableCourse + "values(127, 19, 15, 10, 30, 20, 20, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(127, 20, 15, 10, 10, 10, 10, 40, 'F', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(127, 9, 9, 10, 30, 30, 20, 90, 'A', 2020, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(128, 21, 15, 10, 30, 20, 20, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(128, 22, 16, 10, 20, 20, 20, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(128, 23, 17, 10, 15, 15, 30, 70, 'C', 2020, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(129, 23, 17, 10, 30, 30, 20, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(129, 24, 17, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(129, 20, 15, 10, 30, 20, 20, 80, 'B', 2020, '겨울 학기');");
					stmt.addBatch(insertTableCourse + "values(130, 19, 15, 1, 30, 30, 20, 81, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(130, 20, 15 , 10, 30, 30, 20, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(130, 1, 1, 10, 30, 0, 20, 60, 'D', 2020, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(131, 17, 14, 10, 30, 30, 30, 100, 'A', 2021, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(131, 15, 13, 10, 20, 20, 20, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(131, 20, 15, 10, 10, 20, 20, 60, 'D', 2020, '2학기');");
					stmt.addBatch(insertTableCourse + "values(132, 17, 14, 10, 10, 30, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(132, 23, 17, 10, 10, 30, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(132, 4, 3, 10, 30, 30, 30, 100, 'A', 2019, '여름 학기');");
					stmt.addBatch(insertTableCourse + "values(133, 18, 14, 10, 20, 20, 20, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(133, 24, 17, 10, 10, 30, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(133, 14, 13, 10, 20, 20, 30, 80, 'B', 2020, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(134, 15, 13, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(134, 24, 17, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(134, 20, 15, 10, 10, 30, 30, 80, 'B', 2019, '겨울 학기');");
					stmt.addBatch(insertTableCourse + "values(135, 25, 18, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(135, 26, 18, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(135, 2, 1, 10, 10, 30, 30, 80, 'B', 2020, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(136, 15, 13, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(136, 26, 18, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(136, 27, 19, 10, 0, 30, 30, 70, 'C', 2019, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(137, 28, 19, 10, 0, 0, 30, 40, 'F', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(137, 24, 17, 10, 30, 30, 0, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(137, 6, 5, 10, 20, 30, 30, 90, 'A', 2020, '여름 학기');");
					stmt.addBatch(insertTableCourse + "values(138, 25, 18, 10, 30, 30, 10, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(138, 1, 1, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(138, 29, 20, 10, 25, 30, 30, 95, 'A', 2020, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(139, 30, 21, 10, 10, 20, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(139, 31, 22, 10, 30, 10, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(139, 14, 13, 10, 20, 30, 30, 90, 'B', 2019, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(140, 24, 17, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(140, 27, 19, 10, 10, 30, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(140, 30, 21, 10, 10, 30, 30, 80, 'B', 2019, '겨울 학기');");
					stmt.addBatch(insertTableCourse + "values(141, 3, 2, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(141, 32, 23, 10, 10, 30, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(141, 33, 23, 10, 20, 30, 30, 90, 'A', 2018, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(142, 33, 23, 10, 0, 20, 30, 60, 'D', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(142, 25, 18, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(142, 30, 21, 10, 20, 30, 30, 90, 'A', 2019, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(143, 33, 23, 10, 20, 20, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(143, 34, 23, 10, 30, 30, 10, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(143, 30, 21, 10, 10, 30, 30, 80, 'B', 2019, '여름 학기');");
					stmt.addBatch(insertTableCourse + "values(144, 35, 24, 10, 20, 20, 20, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(144, 36, 24, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(144, 37, 24, 10, 20, 10, 30, 70, 'C', 2020, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(145, 38, 24, 10, 20, 20, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(145, 24, 17, 10, 20, 30, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(145, 30, 21, 10, 30, 30, 30, 100, 'A', 2018, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(146, 39, 8, 10, 20, 20, 10, 60, 'D', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(146, 33, 23, 10, 30, 10, 0, 50, 'F', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(146, 30, 21, 10, 10, 30, 30, 80, 'B', 2018, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(147, 25, 18, 10, 20, 20, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(147, 40, 25, 10, 10, 30, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(147, 36, 24, 10, 30, 30, 30, 100, 'A', 2020, '2 학기');");
					stmt.addBatch(insertTableCourse + "values(148, 30, 21, 10, 10, 30, 10, 60, 'D', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(148, 35, 24, 10, 20, 30, 30, 90, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(148, 38, 24, 10, 10, 30, 30, 80, 'B', 2020, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(149, 40, 25, 10, 30, 30, 30, 100, 'A', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(149, 33, 23, 10, 20, 20, 30, 80, 'B', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(149, 24, 17, 10, 20, 30, 30, 90, 'A', 2019, '여름 학기');");
					stmt.addBatch(insertTableCourse + "values(150, 30, 21, 10, 30, 0, 0, 40, 'F', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(150, 38, 24, 10, 20, 10, 30, 70, 'C', 2021, '1 학기');");
					stmt.addBatch(insertTableCourse + "values(150, 25, 18, 10, 10, 30, 30, 80, 'B', 2018, '겨울 학기');");
					rse = stmt.executeBatch();
					
					
					
					
					
					
					
					
					
					
					
					
					
	//				//테이블 초기값 입력
	//				 stmt.executeUpdate("INSERT INTO lecture VALUES(1, 1, '신동일', '데이터베이스', '화', 1, 3, '9:00-12:00', '컴퓨터공학과', '센B106');");
	
					txtResult.setText("\t\t\t              초기화 완료!");
					JOptionPane.showMessageDialog(null, "초기화가 완료되었습니다.");
				} catch (SQLException e1) {
					
					JOptionPane.showMessageDialog(null, "오류를 확인해주세요\n" + e1.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		}
		
		public void setTimetable(String className, String day, String period) {
			
			String day1= "월", day2 = null;
			String time1, time2 = "0", time3 = "0";
			int row = 0, column = -1;
			int row2 = 0, column2 = 0;
			int row3 = 0, countComma = 0;
	
			if(day.contains(",")) {
				day1 = day.substring(0, 1);
				day2 = day.substring(2, 3);
			}else {
				day1 = day;
			}
		
			column = setDay(day1);
			
			if(day2 != null) {
				column2 = setDay(day2);
			}	
			
			System.out.println(period.length());
			for(int i=0;i<period.length();i++) {
				if(period.charAt(i)== ',') {
					countComma++;
				}
			}
			
			time1 = period.substring(0, 1);
			row = setTime(time1);
			if(countComma>=1) {
				time2 = period.substring(2,3);
				row2 = setTime(time2);
				if(countComma>=2) {
					time3 = period.substring(4,5);
					row3 = setTime(time3);
				}
			}
			
			if(column != 0) {			
				
				contents[row][column] = className;
				
				if(row2!=0) {
					contents[row2][column] = className;
				}
				if(row3!=0) {
					contents[row3][column] = className;
				}
			}	
			if(column2 != 0) {			
				
				contents[row][column2] = className;
				
				if(row2!=0) {
					contents[row2][column2] = className;
				}
				if(row3!=0) {
					contents[row3][column2] = className;
				}
			}	
		}
		
		public int setDay(String day) {
			int column = 0;
			
			switch(day) {
				case "월":
					column = 1;
					break;
				case "화":
					column = 2;
					break;
				case "수":
					column = 3;
					break;
				case "목":
					column = 4;
					break;
				case "금":
					column = 5;
					break;
				default:
					break;
				}
			
			return column;
		}
		
		public int setTime(String time) {
			int row = 0;
			
			switch(time) {
				case "1":
					row = 0;
					break;
				case "2":
					row = 1;
					break;
				case "3":
					row = 2;
					break;
				case "4":
					row = 3;
					break;
				case "5":
					row = 4;
					break;
				case "6":
					row = 5;
					break;
				case "7":
					row = 6;
					break;
				default:
					break;
				}
			
			return row;
		}
		
		public double calculateSum(String g, int credit) {
			double grade = 0;
			
			switch (g) {
				case "A+": 
					grade += 4.5;
					break;
				case "A": 
					grade += 4.0;
					break;
				case "B+": 
					grade += 3.5;
					break;
				case "B": 
					grade += 3.0;
					break;
				case "C+": 
					grade += 2.5;
					break;
				case "C": 
					grade += 2.0;
					break;
				case "D+": 
					grade += 1.5;
					break;
				case "D": 
					grade += 1.0;
					break;
				default:
					break;
			}
			return grade * credit;
		}
		
		public static void main(String[] args) {
	
			UIFrame uiFrame = new UIFrame();
		}
	
	}