import java.awt.*;
import java.awt.event.*;

import java.util.LinkedList;
import java.util.Queue;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Scheduler extends JFrame {
	// 페이지
	JFrame f1, f2, fadd;
	// 폰트
	Font font1;

	// page1 버튼(add, delete, start, deleteAll), page2 버튼(insert, selectColor, done)
	JButton add, delete, start, deleteAll, insert, done, selectColor, goToFormalPage;
	JLabel readyQueueState, schedule, timeQuantum, bt, at, processState, ProcessInfo;
	JTable table,table2;
	JScrollPane scrollPane, scrollPane2, scrollPane3;

	String[][] valueProcessInfo = new String[20][4];
	String[][] valueProcessInfo2 = new String[20][7];
	String[][] readyData;

	JComboBox schedulingInfo;
	JTextField index, btInput, atInput, tqInput;
	int quantumValue;
	int cnt;
	int totalTime=0;
	String columnNames[] = { "P.No", "ID", "BT","AT" };
	String columnNames2[] = {"P.No", "ID", "BT", "AT", "WT", "TT", "NTT"};
	Color color;
	Color[] arrayColor = new Color[100];
	ReadyQueueGanttChart gcRQ;
	StateQueueGanttChart gcSQ;

	public Queue<Process> JobQueue;
	public Queue<Process> ReadyQueue;

	public Scheduler() {
		cnt = 0;// counter
		color = Color.WHITE;    // 디폴트 color

		// f1은 첫번째 페이지
		f1 = new JFrame();
		f1.setSize(510, 380);
		f1.setVisible(true);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.setResizable(false);
		f1.setLocationRelativeTo(null);
		f1.setLayout(null);
		f1.setTitle("Process Scheduling Simulator");

		//실행 페이지
		f2 = new JFrame();
		f2.setSize(690, 580);
		f2.getContentPane().setBackground(new Color(217, 217, 217));
		f2.setVisible(false);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setResizable(false);
		f2.setLocationRelativeTo(null);
		f2.setLayout(null);
		f2.setTitle("Process Scheduling Simulator");
		// 프로세스 정보입력 페이지
		fadd = new JFrame();
		fadd.setSize(220, 230);
		fadd.getContentPane().setBackground(new Color(217, 217, 217));
		fadd.setVisible(false);            // f1 페이지에서 '+' 버튼을 눌렀을 때 활성화되도록 최초에는 보이지 않도록 설정한다.
		fadd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fadd.setResizable(false);
		fadd.setLocationRelativeTo(null);
		fadd.setLayout(null);
		fadd.setTitle("Process");

		load1();
		load2();

		// 프로세스 정보 입력 페이지의 프로세스 번호
		index=new JTextField();
		index.setBounds(10, 20, 30, 80);
		index.setFont(new Font("Garamond",  Font.BOLD , 20));
		index.setBackground(new Color(217, 217, 217));
		index.setEditable(false);
		index.setBorder(new LineBorder(Color.BLACK, 1));

		// 프로세스 정보 입력 페이지의 실행시간 레이블
		bt=new JLabel();
		bt.setBounds(50, 20, 70, 20);
		bt.setText("BurstTime");
		bt.setBackground(new Color(217, 217, 217));

		// 프로세스 정보 입력 페이지의 도착시간 레이블
		at = new JLabel();
		at.setBounds(50, 50, 70, 20);
		at.setText("ArrivalTime");
		at.setBackground(new Color(217, 217, 217));

		// 프로세스 정보 입력 페이지의 실행시간 입력 텍스트 필드
		btInput=new JTextField();
		btInput.setBounds(130, 20, 70, 20);
		// 유효하지 않은 입력을 했을 경우 처리
		btInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char cnt = e.getKeyChar();
				if (!((cnt >= '0') && (cnt <= '9') || (cnt == KeyEvent.VK_BACK_SPACE) || (cnt == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null,"문자는 허용되지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});

		// 프로세스 정보 입력 페이지의 도착시간 입력 텍스트 필드
		atInput=new JTextField();
		atInput.setBounds(130, 50, 70, 20);
		atInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char cnt = e.getKeyChar();
				if (!((cnt >= '0') && (cnt <= '9') || (cnt == KeyEvent.VK_BACK_SPACE) || (cnt == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null, "문자는 허용되지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});      

		// 프로세스 정보 입력 페이지의 "INSERT" 버튼 
		insert=new JButton("INSERT");
		insert.setBackground(new Color(217, 217, 217));
		insert.setForeground(Color.BLACK);
		insert.setFont(new Font("Garamond",  Font.BOLD , 12));
		insert.setBounds(10, 150, 90, 30);
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setData();
				table = new JTable( valueProcessInfo, columnNames );
				f1.add(scrollPane);
				index.setText(Integer.toString(cnt+1));
				btInput.setText("");
				atInput.setText("");
				//System.out.println("job queue size : " + JobQueue.size());
				color = Color.WHITE;
			}
		});

		// 프로세스 정보 입력 페이지의 "DONE" 버튼 (입력 완료 시 프로세스 정보를 table에 저장)    
		done=new JButton("DONE");
		done.setBackground(new Color(217, 217, 217));
		done.setForeground(Color.BLACK);
		done.setFont(new Font("Garamond",  Font.BOLD , 12));
		done.setBounds(110, 150, 90, 30);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fadd.setVisible(false);
			}
		});

		// 프로세스 정보 입력 페이지의 "TIME QUANTUM" 레이블
		timeQuantum = new JLabel();
		timeQuantum.setText("Time Quantum");
		timeQuantum.setFont(new Font("Garamond",  Font.BOLD , 12));
		timeQuantum.setBounds(50, 80, 100, 20);

		// 프로세스 정보 입력 페이지의 "TIME QUANTUM" 텍스트필드 (스케줄링이 "RR"인 경우에만 입력 가능)
		tqInput = new JTextField();
		tqInput.setBounds(160, 80, 40, 20);
		tqInput.setBorder(new LineBorder(Color.BLACK, 1));
		tqInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (!((ch >= '0') && (cnt <= '9') || (ch == KeyEvent.VK_BACK_SPACE) || (ch == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null, "문자는 허용되지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
				if(ch=='0') {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null, "0 이상의 값을 입력해주세요.", "Error", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});

		// 프로세스 정보 입력 페이지의 "CHOOSE COLOR" 버튼 (프로세스의 색상을 표시해 각기 다른 프로세스의 진행과정을 보여줌)
		selectColor=new JButton("CHOOSE COLOR");
		selectColor.setBackground(new Color(217, 217, 217));
		selectColor.setForeground(Color.BLACK);
		selectColor.setFont(new Font("Garamond",  Font.BOLD , 12));
		selectColor.setBounds(10, 110, 190, 30);
		selectColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color = JColorChooser.showDialog(null, "색상 선택", color);
				arrayColor[cnt]=color;
			}
		});

		fadd.add(index);
		fadd.add(bt);
		fadd.add(at);
		fadd.add(btInput);
		fadd.add(atInput);
		fadd.add(timeQuantum);
		fadd.add(tqInput);
		fadd.add(done);
		fadd.add(insert);
		fadd.add(selectColor);
		f1.validate();
		f1.revalidate();
	}

	// 스케쥴링 정보 선택
	public void load1() {
		schedulingInfo = new JComboBox();
		schedulingInfo.setModel(new DefaultComboBoxModel(new String[] {"FCFS", "RR", "SPN", "SRTN", "HRRN"}));
		schedulingInfo.setBounds(300, 60, 180, 30);

		schedule = new JLabel();
		schedule.setText("    Process Information");
		schedule.setFont(new Font("Garamond",  Font.BOLD , 20));
		schedule.setBounds(20, 30, 250, 30);

		f1.getContentPane().setBackground(new Color(217, 217, 217));
		f1.add(schedule);
		f1.add(schedulingInfo);

	}


	public void load2() {
		JobQueue = new LinkedList<Process>(); //모든 Process 저장, ReadyQueue에 들어갈 프로세스를 선별할 때 사용
		ReadyQueue = new LinkedList<Process>(); //준비 큐

		tableSet();                                 

		// 프로세스의 정보를 저장할 표(table)
		table = new JTable( valueProcessInfo, columnNames );
		table.setFont(new Font("Garamond",  Font.BOLD , 13));
		table.setEnabled(false);
		table.setRowHeight(20);

		scrollPane = new JScrollPane( table );
		scrollPane.setBounds(20, 60, 250, 270);

		// 프로세스 정보 및 추가/삭제 페이지의 '+' 버튼
		add=new JButton("+");
		add.setBackground(new Color(217, 217, 217));
		add.setForeground(Color.BLACK);
		add.setFont(new Font("Garamond",  Font.BOLD , 15));
		add.setBounds(320, 100, 60, 40);
		add.addActionListener(new ActionListener() {
			// '+' 버튼을 누르면 보이지 않던 finput페이지를 보이도록 설정한다.
			public void actionPerformed(ActionEvent e) {
				index.setText(Integer.toString(cnt+1));
				btInput.setText("");
				atInput.setText("");
				fadd.setVisible(true);
				// 스케쥴링 정보가 "RR"인 경우 Time quantum을 설정할 수 있도록 텍스트 필드를 true로 설정한다.
				if(schedulingInfo.getSelectedItem() == "RR") {
					tqInput.setText("");
					tqInput.setEnabled(true);
					tqInput.setBackground(Color.WHITE);
				}
				// 스케쥴링 정보가 "RR"이 아닌 경우 Time quantum을 설정할 수 없도록 텍스트 필드를 false로 설정한다.
				else {
					tqInput.setEnabled(false);
					tqInput.setBackground(new Color(217, 217, 217));
				}
			}
		});

		// 프로세스 정보 및 추가/삭제 페이지의 '-' 버튼
		delete=new JButton("-");
		delete.setBackground(new Color(217, 217, 217));
		delete.setForeground(Color.BLACK);
		delete.setFont(new Font("Garamond",  Font.BOLD , 20));
		delete.setBounds(400, 100, 60, 40);
		delete.addActionListener(new ActionListener() {
			// '-' 버튼이 눌릴 경우 
			public void actionPerformed(ActionEvent e) {
				deleteData();
				f1.add(scrollPane);
			}
		});

		// // 프로세스 정보 및 추가/삭제 페이지의 'START' 버튼
		start=new JButton("START");
		start.setBackground(new Color(217, 217, 217));
		start.setForeground(Color.BLACK);
		start.setFont(new Font("Garamond",  Font.BOLD , 30));
		start.setBounds(300, 200, 180, 130);
		start.addActionListener(new ActionListener() {
			// "START" 버튼을 누를 경우 간트차트를 생성하고, 프로세스 실행 페이지를 열어준다.
			public void actionPerformed(ActionEvent e) {
				if (JobQueue.size() == 0) {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null, "작업이 존재하지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					gcRQ = new ReadyQueueGanttChart(totalTime);
					gcSQ = new StateQueueGanttChart(totalTime);
					load3();
					f1.setVisible(false);
					f2.setVisible(true);
					// 처음에 설정된 scheduling information에 따라 맞는 프로세스 스케쥴링을 진행한다.

					if(schedulingInfo.getSelectedItem() == "RR") {
						RR();
					}else if(schedulingInfo.getSelectedItem() == "SPN") {
						SPN();
					}else if(schedulingInfo.getSelectedItem() == "FCFS") {
						FCFS();
					}else if(schedulingInfo.getSelectedItem() == "SRTN") {
						SRTN();
					}else if(schedulingInfo.getSelectedItem() == "HRRN"){
						HRRN();
					}
					setData2();
				}
			}
		});

		//전체 삭제 버튼
		deleteAll = new JButton("DELETE ALL");
		deleteAll.setBackground(new Color(217, 217, 217));
		deleteAll.setForeground(Color.BLACK);
		deleteAll.setFont(new Font("Garamond",  Font.BOLD , 15));
		deleteAll.setBounds(320, 150, 140, 40);
		deleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAll();
				f1.add(scrollPane);
			}
		});   

		f1.add(scrollPane);
		f1.add(add);
		f1.add(delete);
		f1.add(start);
		f1.add(deleteAll);
		f1.validate();
		f1.revalidate();
		f1.repaint();
	}

	// 프로세스 실행 페이지
	public void load3() {   

		readyQueueState = new JLabel();
		readyQueueState.setBounds(20, 200, 200, 40);
		readyQueueState.setText("Ready Queue State");
		readyQueueState.setFont(new Font("SansSerif", Font.BOLD, 20));
		readyQueueState.setBackground(new Color(217, 217, 217));
		readyQueueState.setVisible(true);

		//ReadyQueue GanttChart
		gcRQ.setBounds(20, 250, 630, 100);
		gcRQ.setBackground(Color.WHITE);
		gcRQ.setBorder(new LineBorder(Color.BLACK, 2));

		processState = new JLabel();
		processState.setBounds(20, 360, 200, 40);
		processState.setText("Process State");
		processState.setFont(new Font("SansSerif", Font.BOLD, 20));

		ProcessInfo = new JLabel();
		ProcessInfo.setText("Process Information");
		ProcessInfo.setFont(new Font("SansSerif", Font.BOLD, 20));
		ProcessInfo.setBounds(20, 10, 200, 40);

		scrollPane3 = new JScrollPane(gcSQ);
		scrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane3.setBounds(20, 410, 630, 100);
		scrollPane3.setPreferredSize(new Dimension(630, 100));
		scrollPane3.setBorder(new LineBorder(Color.BLACK, 2));
		//뒤로가기 버튼(f2->f1)
		goToFormalPage = new JButton("BACK");
		goToFormalPage.setBackground(new Color(217, 217, 217));
		goToFormalPage.setForeground(Color.BLACK);
		goToFormalPage.setFont(new Font("Garamond",  Font.BOLD , 12));
		goToFormalPage.setBounds(580, 20, 70, 30);
		goToFormalPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f2.setVisible(false);
				reset();
			}
		});
		f2.add(goToFormalPage);
		f2.add(ProcessInfo);
		f2.add(scrollPane3);
		f2.add(processState);
		f2.add(readyQueueState);
		f2.add(gcRQ);
		f2.validate();
		f2.revalidate();
		repaint();
	}
	public void reset() { //f2->f1으로 갈 때 초기화해야되는 큐 자료구조를 초기화
		ReadyQueue.clear();
		JobQueue.clear();

		gcRQ.allQueue.clear();
		gcRQ.temp.clear();
		gcRQ.cnt=totalTime;
		gcRQ.tm.stop();

		gcSQ.allQueue.clear();
		gcSQ.temp.clear();
		gcSQ.cnt=totalTime;
		gcSQ.tm.stop();

		reload();
	}

	public void reload() { //f2 창 종료, f1 로드
		reloadData(); //JobQueue에 프로세스 재삽입(f2의 scrollPane2 갱신용)
		f1.setVisible(true);
		f1.revalidate();
	}

	public void reloadData() { //JobQueue 프로세스 재삽입(f2의 scrollPane2 갱신용)
		int c;
		for(c=0; c<cnt; c++) {
			JobQueue.add(new Process(c, Integer.parseInt(valueProcessInfo[c][3]), Integer.parseInt(valueProcessInfo[c][2]), arrayColor[c]));
		}
	}

	public void setData() { //Insert눌렀을 때 동작되는 함수, 테이블 설정
		String str1=index.getText();
		String str2=btInput.getText();
		String str3=atInput.getText();
		String str4=tqInput.getText();
		if(schedulingInfo.getSelectedItem() == "RR") {
			quantumValue = Integer.parseInt(str4);
		}
		if(str2.isEmpty()||str3.isEmpty()||color==Color.WHITE) { //오류 제어
			getToolkit().beep();
			if(color==Color.WHITE) {
				JOptionPane.showMessageDialog(null,"흰색을 제외한 색상을 선택하세요.","색상 에러",JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null,"빈 칸이 존재합니다.","공백 에러",JOptionPane.ERROR_MESSAGE);
			}
		}else {
			valueProcessInfo[cnt][0]=Integer.toString(cnt+1);
			valueProcessInfo[cnt][1]="ID "+str1;
			valueProcessInfo[cnt][2]=str2; //BT
			valueProcessInfo[cnt][3]=str3; //AT
			totalTime += Integer.parseInt(str2);
			JobQueue.add(new Process(cnt, Integer.parseInt(str3), Integer.parseInt(str2), color));
			cnt++;
		}
	}

	public void setData2() { //스케줄링 종료 후 scrollPane2를 설정하기 위해
		int c;
		for(c=0; c<cnt; c++) {
			valueProcessInfo2[c][0] = valueProcessInfo[c][0];
			valueProcessInfo2[c][1] = valueProcessInfo[c][1];
			valueProcessInfo2[c][2] = valueProcessInfo[c][2];
			valueProcessInfo2[c][3] = valueProcessInfo[c][3];

			Process p = JobQueue.remove();
			valueProcessInfo2[c][4] = String.valueOf(p.getWaitingTime());
			valueProcessInfo2[c][5] = String.valueOf(p.getTT());
			valueProcessInfo2[c][6] = String.valueOf(p.getNTT());
			JobQueue.add(p);

			table2 = new JTable( valueProcessInfo2, columnNames2 );
			table2.setFont(new Font("Garamond",  Font.BOLD , 13));
			table2.setEnabled(true);
			table2.setRowHeight(20);

			scrollPane2 = new JScrollPane( table2 );
			scrollPane2.setBounds(20, 60, 630, 130);
			f2.add(scrollPane2);
			f2.revalidate();
		}
	}

	public void tableSet() { //테이블 설징 초기화
		for(int i=0;i<20;i++) {
			valueProcessInfo[i][0]="";
			valueProcessInfo[i][1]="";
			valueProcessInfo[i][2]="";
			valueProcessInfo[i][3]="";

			valueProcessInfo2[cnt][0]="";
			valueProcessInfo2[cnt][1]="";
			valueProcessInfo2[cnt][2]="";
			valueProcessInfo2[cnt][3]="";
			valueProcessInfo2[cnt][4]="";
			valueProcessInfo2[cnt][5]="";
			valueProcessInfo2[cnt][6]="";
		}
	}
	public void deleteData() { //가장 최근 테이블 열 삭제
		if(cnt==0) {
			getToolkit().beep();
			JOptionPane.showMessageDialog(null, "삭제 할 작업이 없습니다. ");
		}else {
			cnt--;
			totalTime -= Integer.parseInt(valueProcessInfo[cnt][2]);

			valueProcessInfo[cnt][0]="";
			valueProcessInfo[cnt][1]="";
			valueProcessInfo[cnt][2]="";
			valueProcessInfo[cnt][3]="";

			valueProcessInfo2[cnt][0]="";
			valueProcessInfo2[cnt][1]="";
			valueProcessInfo2[cnt][2]="";
			valueProcessInfo2[cnt][3]="";
			valueProcessInfo2[cnt][4]="";
			valueProcessInfo2[cnt][5]="";
			valueProcessInfo2[cnt][6]="";

			//JobQueue의 맨 뒷 항목 제거
			Process p;
			p=JobQueue.remove();
			for(int i=0;i<JobQueue.size();i++) {
				JobQueue.add(p);
				p=JobQueue.remove();
			}

		}
	}
	public void deleteAll() { //테이블 전체 삭제
		if(cnt==0) {
			getToolkit().beep();
			JOptionPane.showMessageDialog(null, "삭제 할 작업이 없습니다. ");
		}else {
			while(cnt!=0) {
				cnt--;
				valueProcessInfo[cnt][0]="";
				valueProcessInfo[cnt][1]="";
				valueProcessInfo[cnt][2]="";
				valueProcessInfo[cnt][3]="";

				valueProcessInfo2[cnt][0]="";
				valueProcessInfo2[cnt][1]="";
				valueProcessInfo2[cnt][2]="";
				valueProcessInfo2[cnt][3]="";
				valueProcessInfo2[cnt][4]="";
				valueProcessInfo2[cnt][5]="";
				valueProcessInfo2[cnt][6]="";


			}
			totalTime=0;
			JobQueue.clear();
			reloadData();
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------

	//레디 큐 갱신
	public void setReadyQueue(int t) {
		Process temp; //JobQueue에서 remove된 프로세스를 임시 저장하기 위한 임시 프로세스

		for(int i=0;i<JobQueue.size();i++) { //JobQueue 내에서 ReadyQueue에 갱신하기 위함
			temp=JobQueue.remove(); //임시저장 후 삽입
			if(temp.getArrivalTime()==t) { //AT에 도착한 시간과 같을 때 ReadyQueue에 삽입
				JobQueue.add(temp); //JobQueue의 뒤쪽부터 배치
				ReadyQueue.add(temp);  //ReadyQueue에 추가
			}else 
				JobQueue.add(temp); //AT에 도착한 시간이 다를 때 그냥 다시 JobQueue에 원래대로 삽입
		}
	}

	//매 t시간마다 레디큐 갱신(paint 목적)
	public void alltoReadyQueue(int  t) {
		if(!ReadyQueue.isEmpty()) { //레디큐가 비어있지 않다면
			gcRQ.allQueue.addAll(ReadyQueue); //prq의 all에 레디큐 전부 삽입후
			gcRQ.array[t]=ReadyQueue.size(); //array[t]에 t시점의 레디큐의 크기값을 저장
		}else //비어있다면
			gcRQ.array[t]=0; //레디큐의 크기(0)을 저장
	}




	// -------------------------------------------First Come First Set-------------------------------------------
	public void FCFS() {
		Process CpuProcess; //Cpu할당 프로세스
		CpuProcess=null; //프로세스의 시작점을 null로 설정

		for(int i=0;i<totalTime;i++) { //0~totalTime까지 ReadyQueue 및 gcRQ 갱신
			setReadyQueue(i); //i시점 레디큐 갱신
			if(CpuProcess==null) {//CpuProcess를 처음 할당받을 때
				if(!ReadyQueue.isEmpty()) { //레디큐가 비어있지 않다면
					CpuProcess=ReadyQueue.remove(); //큐의 맨앞 프로세스를 빼옴
					//setStartWTime(i);
				}
			}
			alltoReadyQueue(i); //i시점 gcRQ 갱신(allQueue,array)
			CpuProcess.Decrement(); //남은 할당시간(Remaining Time) 1감소
			gcSQ.allQueue.add(CpuProcess); //StateQueue의 allQueue에 CpuProcess를 매 i번째 마다 삽입
			if(CpuProcess.getRemainingTime()==0) {//CpuProcess가 모든 할당시간을 사용한 경우
				//setEndWTime(i);
				CpuProcess.setWaitingTime(i); //종료된 CpuProcess에 대한 WaitingTime 설정
				CpuProcess.setTT(); //TT값 설정
				CpuProcess.setNTT(); //NTT값 설정
				CpuProcess=null; //CpuProcess를 null로 맞춤(다음 프로세스가 할당받을 준비)
			}
		}
	}







	// -------------------------------------------Round Robin-------------------------------------------
	public void RR() {
		int quantum=quantumValue;
		Process CpuProcess=null;
		for(int i=0;i<totalTime;i++) {
			setReadyQueue(i); //i시점 레디큐 갱신
			if(quantum==0||CpuProcess==null) { //quantum을 모두 소비 혹은 새로운 CpuProcess를 설정해야 할 때
				if(!ReadyQueue.isEmpty()) { //레디큐가 비어있지 않다면
					//CpuProcess=loadProcess(CpuProcess); //CpuProcess를 새로 설정
					if(CpuProcess == null) CpuProcess = ReadyQueue.remove();
					else {
						ReadyQueue.add(CpuProcess);
						CpuProcess = ReadyQueue.remove();
					}
				}
				quantum=quantumValue; //quantum 초기화
			}
			gcSQ.allQueue.add(CpuProcess); //StateQueue의 allQueue에 CpuProcess를 매 i번째 마다 삽입
			CpuProcess.Decrement();
			quantum--;
			if(CpuProcess.getRemainingTime()==0) { //RT 모두 소진 시
				CpuProcess.setWaitingTime(i); //종료된 CpuProcess에 대한 WaitingTime 설정
				CpuProcess.setTT(); //TT값 설정
				CpuProcess.setNTT(); //NTT값 설정
				CpuProcess=null; //CpuProcess를 null로 맞춤(다음 프로세스가 할당받을 준비)
			}   
			alltoReadyQueue(i); //i시점 gcRQ 갱신(allQueue,array)
		}
	}


	//-------------------------------------------Shortest Process Next-------------------------------------------
	public void SPN() {
		Process CpuProcess=null;

		for(int i=0;i<totalTime;i++) {
			setReadyQueue(i); //i시점 레디큐 갱신
			if(CpuProcess==null) { //CpuProcess를 처음 할당받을 때
				if(!ReadyQueue.isEmpty()) { //레디큐가 비어있지 않다면
					CpuProcess=loadminBTime(); //현재 레디큐에 있는 프로세스 중 가장 작은BT를 가진 프로세스를 가져옴
				}
			}
			alltoReadyQueue(i); //i시점 gcRQ 갱신(allQueue,array)
			gcSQ.allQueue.add(CpuProcess); //StateQueue의 allQueue에 CpuProcess를 매 i번째 마다 삽입
			CpuProcess.Decrement(); //남은 할당시간(Remaining Time) 1감소
			if(CpuProcess.getRemainingTime()==0) {//CpuProcess가 모든 할당시간을 사용한 경우
				CpuProcess.setWaitingTime(i); //종료된 CpuProcess에 대한 WaitingTime 설정
				CpuProcess.setTT(); //TT값 설정
				CpuProcess.setNTT(); //NTT값 설정
				CpuProcess=null; //CpuProcess를 null로 맞춤(다음 프로세스가 할당받을 준비)
			}
		}
	}

	//최소 BT Process 검출(SPN)
	public Process loadminBTime() { //CpuProcess가 null일 때, 즉 새로운 Process를 할당할 때 최소의 BT값을 찾는 함수
		int min; //최소 BT
		int pos=0; //최소 BT Process 위치

		Process p; //임시 Process
		Process minProcess; //최소 BT Process
		Queue<Process> temp=new LinkedList<Process>(); //임시 큐(수시 변경)
		temp.addAll(ReadyQueue); //temp에 모든 ReadyQueue 복사
		minProcess=temp.peek(); //temp의 맨 앞 프로세스를 저장(순차 탐색)
		min=minProcess.getBTime(); //minProcess의 BT값으로 min을 초기화
		for(int i=0;i<temp.size();i++) { //temp에서 계속 빼오면서 비교 후 pos값 설정
			p=temp.remove();
			if(p.getBTime()<min) {
				min=p.getBTime();
				pos=i;
			}
			temp.add(p);
		}
		for(int i=0;i<temp.size();i++) { //pos값 설정 후 pos에 해당하는 프로세스를 레디큐에서 빼고 저장(minProcess)
			p=ReadyQueue.remove();
			if(i==pos) {
				minProcess=p;
			}else{
				ReadyQueue.add(p);
			}

		}
		return minProcess; //최소BT 프로세스 반납
	}





	// -------------------------------------------Shortest Remaining Time Next-------------------------------------------
	public void SRTN() {
		Process CpuProcess=null;
		Process currentProcess; //현재 Cpu할당받은 프로세스(선점 발생시 이용)
		for(int i=0;i<totalTime;i++) {
			setReadyQueue(i); //i시점 레디큐 갱신
			if(CpuProcess==null) { //CpuProcess를 처음 할당받을 때
				if(!ReadyQueue.isEmpty()) { //레디큐가 비어있지 않다면
					CpuProcess = loadminRTime(); //남은시간(Remaining Time)이 가장 작은 프로세스로 설정
				}
			}
			if(CpuProcess!=null) { //CpuProcess가 설정 된 후(선점 발생 고려부분)
				if(!ReadyQueue.isEmpty()) { //레디큐가 비어있지 않다면
					currentProcess = CpuProcess; //현재 Cpu할당받은 프로세스를 임시 저장(선점 후 비교목적)
					CpuProcess = checkPreemption(CpuProcess); //선점검사
					if(CpuProcess!=currentProcess) { //선점 발생시
						ReadyQueue.add(currentProcess); //이전 프로세스를 레디큐의 맨 뒤에 삽입
					}
				}
			}      
			alltoReadyQueue(i); //i시점 gcRQ 갱신(allQueue,array)
			gcSQ.allQueue.add(CpuProcess); //StateQueue의 allQueue에 CpuProcess를 매 i번째 마다 삽입
			CpuProcess.Decrement(); //남은 할당시간(Remaining Time) 1감소
			if(CpuProcess.getRemainingTime()==0) {
				CpuProcess.setWaitingTime(i); //종료된 CpuProcess에 대한 WaitingTime 설정
				CpuProcess.setTT(); //TT값 설정
				CpuProcess.setNTT(); //NTT값 설정
				CpuProcess=null; //CpuProcess를 null로 맞춤(다음 프로세스가 할당받을 준비)
			}
		}
	}

	public Process loadminRTime() { //CpuProcess 처음 할당될 경우
		int min; //최소 BT값
		int pos=0; //최소 BT프로세스 위치
		Process p; //임시 프로세스
		Process minProcess; //최소 BT 프로세스
		Queue<Process> temp=new LinkedList<Process>();
		temp.addAll(ReadyQueue);
		minProcess=temp.peek();
		min=temp.peek().getRemainingTime();
		for(int i=0;i<temp.size();i++) { //최소 BT 프로세스 위치 탐색(순차 탐색)
			p=temp.remove();
			if(p.getRemainingTime()<min) {
				min=p.getRemainingTime();
				pos=i;
			}
			temp.add(p);
		}
		for(int i=0;i<temp.size();i++) { //설정된 pos값을 이용하여 레디큐의 프로세스를 뺀다.
			p=ReadyQueue.remove();
			if(i==pos) {
				minProcess=p;
			}else {
				ReadyQueue.add(p);
			}
		}
		return minProcess; //반환
	}

	public Process checkPreemption(Process p) { //선점 발생 여부 검사
		int min;
		int pos=-1; //선점 발생하지 않을 시 pos = -1

		Queue<Process> temp=new LinkedList<Process>();
		temp.addAll(ReadyQueue);
		Process minProcess=p; //최소 RT 프로세스
		Process tp; //임시 프로세스
		min=p.getRemainingTime();
		for(int i=0;i<temp.size();i++) { 
			tp=temp.remove();
			if(tp.getRemainingTime()<min) { //선점 발생 시
				min=tp.getRemainingTime();
				pos=i; //pos값 설정
			}
			temp.add(tp);
		}
		if(pos>=0) { //선점 발생 시
			for(int i=0;i<temp.size();i++) {
				tp=ReadyQueue.remove();
				if(pos==i) {
					minProcess=tp; //minProcess만 ReadyQueue에서 빼옴
				}else 
					ReadyQueue.add(tp);
			}   
		}
		return minProcess; //반환
	}






	// -------------------------------------------Highest Remaining Ratio Next-------------------------------------------
	public void HRRN() {
		Process CpuProcess=null;
		for(int i=0;i<totalTime;i++) {
			setReadyQueue(i); //i시점 레디큐 갱신
			if(CpuProcess==null) { //CpuProcess를 처음 할당받을 때
				if(!ReadyQueue.isEmpty()) { //레디큐가 비어있지 않다면
					CpuProcess = loadmaxResponseR(); //최소 RR값을 가진 프로세스로 설정, 레디큐에서 제거
				}
			}
			alltoReadyQueue(i); //i시점 gcRQ 갱신(allQueue,array)
			gcSQ.allQueue.add(CpuProcess); //StateQueue의 allQueue에 CpuProcess를 매 i번째 마다 삽입
			CpuProcess.Decrement(); //남은 할당시간(Remaining Time) 1감소
			if(CpuProcess.getRemainingTime()==0) { //할당 시간 모두 소비 시
				setWT(i,CpuProcess.getArrivalTime()); //WaitingTime(WT) 설정
				setRR(); //CpuProcess를 제외한 나머지 프로세스의 RR값 설정
				CpuProcess.setTT(); //TT값 설정
				CpuProcess.setNTT(); //NTT값 설정
				CpuProcess=null; //CpuProcess를 null로 맞춤(다음 프로세스가 할당받을 준비)
			}
		}
	}
	public void setWT(int t,int ctime) { //WT값 설정
		Process p;
		for(int i=0;i<ReadyQueue.size();i++) {
			p=ReadyQueue.remove();
			p.setWaitingTimeforHRRN(t,ctime);
			System.out.println("ID : " + p.getID() + " WT : " + p.getWaitingTime());
			ReadyQueue.add(p);
		}
		System.out.println();
	}
	
	public void setRR() { //RR값 설정
		Process p;
		for(int i=0;i<ReadyQueue.size();i++) {
			p=ReadyQueue.remove();
			p.setResponseRatio();
			System.out.println("ID : " + p.getID() + " RR : " + p.getResponseRatio());
			ReadyQueue.add(p);
		}
		System.out.println();
	}
	
	public Process loadmaxResponseR() { //최소 RR값을 가진 프로세스 탐색 후 반환
		float max; //최소 RR값
		int pos=0; //최소 RR값을 가진 프로세스의 위치
		Process p; //임시 프로세스
		Process maxProcess=null; //minProcess 설정
		Queue<Process> temp=new LinkedList<Process>(); //임시 큐
		temp.addAll(ReadyQueue); //temp에 ReadyQueue 전부 복사
		p=temp.peek();
		max=p.getResponseRatio();
		for(int i=0;i<temp.size();i++) { //순차탐색, pos값 설정
			p=temp.remove();
			if(p.getResponseRatio()>max) {
				max=p.getResponseRatio();
				pos=i;
			}
			temp.add(p);
		}

		for(int i=0;i<temp.size();i++) { //pos에 해당되는 프로세스만 빼내옴
			p=ReadyQueue.remove();
			if(i==pos) {
				maxProcess=p;
			}else {
				ReadyQueue.add(p);
			}
		}
		return maxProcess;
	}

	public static void main(String args[]) {
		new Scheduler();
	}
}