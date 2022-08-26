import java.awt.*;
import java.awt.event.*;

import java.util.LinkedList;
import java.util.Queue;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Scheduler extends JFrame {
	// ������
	JFrame f1, f2, fadd;
	// ��Ʈ
	Font font1;

	// page1 ��ư(add, delete, start, deleteAll), page2 ��ư(insert, selectColor, done)
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
		color = Color.WHITE;    // ����Ʈ color

		// f1�� ù��° ������
		f1 = new JFrame();
		f1.setSize(510, 380);
		f1.setVisible(true);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.setResizable(false);
		f1.setLocationRelativeTo(null);
		f1.setLayout(null);
		f1.setTitle("Process Scheduling Simulator");

		//���� ������
		f2 = new JFrame();
		f2.setSize(690, 580);
		f2.getContentPane().setBackground(new Color(217, 217, 217));
		f2.setVisible(false);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setResizable(false);
		f2.setLocationRelativeTo(null);
		f2.setLayout(null);
		f2.setTitle("Process Scheduling Simulator");
		// ���μ��� �����Է� ������
		fadd = new JFrame();
		fadd.setSize(220, 230);
		fadd.getContentPane().setBackground(new Color(217, 217, 217));
		fadd.setVisible(false);            // f1 ���������� '+' ��ư�� ������ �� Ȱ��ȭ�ǵ��� ���ʿ��� ������ �ʵ��� �����Ѵ�.
		fadd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fadd.setResizable(false);
		fadd.setLocationRelativeTo(null);
		fadd.setLayout(null);
		fadd.setTitle("Process");

		load1();
		load2();

		// ���μ��� ���� �Է� �������� ���μ��� ��ȣ
		index=new JTextField();
		index.setBounds(10, 20, 30, 80);
		index.setFont(new Font("Garamond",  Font.BOLD , 20));
		index.setBackground(new Color(217, 217, 217));
		index.setEditable(false);
		index.setBorder(new LineBorder(Color.BLACK, 1));

		// ���μ��� ���� �Է� �������� ����ð� ���̺�
		bt=new JLabel();
		bt.setBounds(50, 20, 70, 20);
		bt.setText("BurstTime");
		bt.setBackground(new Color(217, 217, 217));

		// ���μ��� ���� �Է� �������� �����ð� ���̺�
		at = new JLabel();
		at.setBounds(50, 50, 70, 20);
		at.setText("ArrivalTime");
		at.setBackground(new Color(217, 217, 217));

		// ���μ��� ���� �Է� �������� ����ð� �Է� �ؽ�Ʈ �ʵ�
		btInput=new JTextField();
		btInput.setBounds(130, 20, 70, 20);
		// ��ȿ���� ���� �Է��� ���� ��� ó��
		btInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char cnt = e.getKeyChar();
				if (!((cnt >= '0') && (cnt <= '9') || (cnt == KeyEvent.VK_BACK_SPACE) || (cnt == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null,"���ڴ� ������ �ʽ��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});

		// ���μ��� ���� �Է� �������� �����ð� �Է� �ؽ�Ʈ �ʵ�
		atInput=new JTextField();
		atInput.setBounds(130, 50, 70, 20);
		atInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char cnt = e.getKeyChar();
				if (!((cnt >= '0') && (cnt <= '9') || (cnt == KeyEvent.VK_BACK_SPACE) || (cnt == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null, "���ڴ� ������ �ʽ��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});      

		// ���μ��� ���� �Է� �������� "INSERT" ��ư 
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

		// ���μ��� ���� �Է� �������� "DONE" ��ư (�Է� �Ϸ� �� ���μ��� ������ table�� ����)    
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

		// ���μ��� ���� �Է� �������� "TIME QUANTUM" ���̺�
		timeQuantum = new JLabel();
		timeQuantum.setText("Time Quantum");
		timeQuantum.setFont(new Font("Garamond",  Font.BOLD , 12));
		timeQuantum.setBounds(50, 80, 100, 20);

		// ���μ��� ���� �Է� �������� "TIME QUANTUM" �ؽ�Ʈ�ʵ� (�����ٸ��� "RR"�� ��쿡�� �Է� ����)
		tqInput = new JTextField();
		tqInput.setBounds(160, 80, 40, 20);
		tqInput.setBorder(new LineBorder(Color.BLACK, 1));
		tqInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (!((ch >= '0') && (cnt <= '9') || (ch == KeyEvent.VK_BACK_SPACE) || (ch == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null, "���ڴ� ������ �ʽ��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
				if(ch=='0') {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null, "0 �̻��� ���� �Է����ּ���.", "Error", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});

		// ���μ��� ���� �Է� �������� "CHOOSE COLOR" ��ư (���μ����� ������ ǥ���� ���� �ٸ� ���μ����� ��������� ������)
		selectColor=new JButton("CHOOSE COLOR");
		selectColor.setBackground(new Color(217, 217, 217));
		selectColor.setForeground(Color.BLACK);
		selectColor.setFont(new Font("Garamond",  Font.BOLD , 12));
		selectColor.setBounds(10, 110, 190, 30);
		selectColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color = JColorChooser.showDialog(null, "���� ����", color);
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

	// �����층 ���� ����
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
		JobQueue = new LinkedList<Process>(); //��� Process ����, ReadyQueue�� �� ���μ����� ������ �� ���
		ReadyQueue = new LinkedList<Process>(); //�غ� ť

		tableSet();                                 

		// ���μ����� ������ ������ ǥ(table)
		table = new JTable( valueProcessInfo, columnNames );
		table.setFont(new Font("Garamond",  Font.BOLD , 13));
		table.setEnabled(false);
		table.setRowHeight(20);

		scrollPane = new JScrollPane( table );
		scrollPane.setBounds(20, 60, 250, 270);

		// ���μ��� ���� �� �߰�/���� �������� '+' ��ư
		add=new JButton("+");
		add.setBackground(new Color(217, 217, 217));
		add.setForeground(Color.BLACK);
		add.setFont(new Font("Garamond",  Font.BOLD , 15));
		add.setBounds(320, 100, 60, 40);
		add.addActionListener(new ActionListener() {
			// '+' ��ư�� ������ ������ �ʴ� finput�������� ���̵��� �����Ѵ�.
			public void actionPerformed(ActionEvent e) {
				index.setText(Integer.toString(cnt+1));
				btInput.setText("");
				atInput.setText("");
				fadd.setVisible(true);
				// �����층 ������ "RR"�� ��� Time quantum�� ������ �� �ֵ��� �ؽ�Ʈ �ʵ带 true�� �����Ѵ�.
				if(schedulingInfo.getSelectedItem() == "RR") {
					tqInput.setText("");
					tqInput.setEnabled(true);
					tqInput.setBackground(Color.WHITE);
				}
				// �����층 ������ "RR"�� �ƴ� ��� Time quantum�� ������ �� ������ �ؽ�Ʈ �ʵ带 false�� �����Ѵ�.
				else {
					tqInput.setEnabled(false);
					tqInput.setBackground(new Color(217, 217, 217));
				}
			}
		});

		// ���μ��� ���� �� �߰�/���� �������� '-' ��ư
		delete=new JButton("-");
		delete.setBackground(new Color(217, 217, 217));
		delete.setForeground(Color.BLACK);
		delete.setFont(new Font("Garamond",  Font.BOLD , 20));
		delete.setBounds(400, 100, 60, 40);
		delete.addActionListener(new ActionListener() {
			// '-' ��ư�� ���� ��� 
			public void actionPerformed(ActionEvent e) {
				deleteData();
				f1.add(scrollPane);
			}
		});

		// // ���μ��� ���� �� �߰�/���� �������� 'START' ��ư
		start=new JButton("START");
		start.setBackground(new Color(217, 217, 217));
		start.setForeground(Color.BLACK);
		start.setFont(new Font("Garamond",  Font.BOLD , 30));
		start.setBounds(300, 200, 180, 130);
		start.addActionListener(new ActionListener() {
			// "START" ��ư�� ���� ��� ��Ʈ��Ʈ�� �����ϰ�, ���μ��� ���� �������� �����ش�.
			public void actionPerformed(ActionEvent e) {
				if (JobQueue.size() == 0) {
					getToolkit().beep();
					JOptionPane.showMessageDialog(null, "�۾��� �������� �ʽ��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					gcRQ = new ReadyQueueGanttChart(totalTime);
					gcSQ = new StateQueueGanttChart(totalTime);
					load3();
					f1.setVisible(false);
					f2.setVisible(true);
					// ó���� ������ scheduling information�� ���� �´� ���μ��� �����층�� �����Ѵ�.

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

		//��ü ���� ��ư
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

	// ���μ��� ���� ������
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
		//�ڷΰ��� ��ư(f2->f1)
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
	public void reset() { //f2->f1���� �� �� �ʱ�ȭ�ؾߵǴ� ť �ڷᱸ���� �ʱ�ȭ
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

	public void reload() { //f2 â ����, f1 �ε�
		reloadData(); //JobQueue�� ���μ��� �����(f2�� scrollPane2 ���ſ�)
		f1.setVisible(true);
		f1.revalidate();
	}

	public void reloadData() { //JobQueue ���μ��� �����(f2�� scrollPane2 ���ſ�)
		int c;
		for(c=0; c<cnt; c++) {
			JobQueue.add(new Process(c, Integer.parseInt(valueProcessInfo[c][3]), Integer.parseInt(valueProcessInfo[c][2]), arrayColor[c]));
		}
	}

	public void setData() { //Insert������ �� ���۵Ǵ� �Լ�, ���̺� ����
		String str1=index.getText();
		String str2=btInput.getText();
		String str3=atInput.getText();
		String str4=tqInput.getText();
		if(schedulingInfo.getSelectedItem() == "RR") {
			quantumValue = Integer.parseInt(str4);
		}
		if(str2.isEmpty()||str3.isEmpty()||color==Color.WHITE) { //���� ����
			getToolkit().beep();
			if(color==Color.WHITE) {
				JOptionPane.showMessageDialog(null,"����� ������ ������ �����ϼ���.","���� ����",JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null,"�� ĭ�� �����մϴ�.","���� ����",JOptionPane.ERROR_MESSAGE);
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

	public void setData2() { //�����ٸ� ���� �� scrollPane2�� �����ϱ� ����
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

	public void tableSet() { //���̺� ��¡ �ʱ�ȭ
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
	public void deleteData() { //���� �ֱ� ���̺� �� ����
		if(cnt==0) {
			getToolkit().beep();
			JOptionPane.showMessageDialog(null, "���� �� �۾��� �����ϴ�. ");
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

			//JobQueue�� �� �� �׸� ����
			Process p;
			p=JobQueue.remove();
			for(int i=0;i<JobQueue.size();i++) {
				JobQueue.add(p);
				p=JobQueue.remove();
			}

		}
	}
	public void deleteAll() { //���̺� ��ü ����
		if(cnt==0) {
			getToolkit().beep();
			JOptionPane.showMessageDialog(null, "���� �� �۾��� �����ϴ�. ");
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

	//���� ť ����
	public void setReadyQueue(int t) {
		Process temp; //JobQueue���� remove�� ���μ����� �ӽ� �����ϱ� ���� �ӽ� ���μ���

		for(int i=0;i<JobQueue.size();i++) { //JobQueue ������ ReadyQueue�� �����ϱ� ����
			temp=JobQueue.remove(); //�ӽ����� �� ����
			if(temp.getArrivalTime()==t) { //AT�� ������ �ð��� ���� �� ReadyQueue�� ����
				JobQueue.add(temp); //JobQueue�� ���ʺ��� ��ġ
				ReadyQueue.add(temp);  //ReadyQueue�� �߰�
			}else 
				JobQueue.add(temp); //AT�� ������ �ð��� �ٸ� �� �׳� �ٽ� JobQueue�� ������� ����
		}
	}

	//�� t�ð����� ����ť ����(paint ����)
	public void alltoReadyQueue(int  t) {
		if(!ReadyQueue.isEmpty()) { //����ť�� ������� �ʴٸ�
			gcRQ.allQueue.addAll(ReadyQueue); //prq�� all�� ����ť ���� ������
			gcRQ.array[t]=ReadyQueue.size(); //array[t]�� t������ ����ť�� ũ�Ⱚ�� ����
		}else //����ִٸ�
			gcRQ.array[t]=0; //����ť�� ũ��(0)�� ����
	}




	// -------------------------------------------First Come First Set-------------------------------------------
	public void FCFS() {
		Process CpuProcess; //Cpu�Ҵ� ���μ���
		CpuProcess=null; //���μ����� �������� null�� ����

		for(int i=0;i<totalTime;i++) { //0~totalTime���� ReadyQueue �� gcRQ ����
			setReadyQueue(i); //i���� ����ť ����
			if(CpuProcess==null) {//CpuProcess�� ó�� �Ҵ���� ��
				if(!ReadyQueue.isEmpty()) { //����ť�� ������� �ʴٸ�
					CpuProcess=ReadyQueue.remove(); //ť�� �Ǿ� ���μ����� ����
					//setStartWTime(i);
				}
			}
			alltoReadyQueue(i); //i���� gcRQ ����(allQueue,array)
			CpuProcess.Decrement(); //���� �Ҵ�ð�(Remaining Time) 1����
			gcSQ.allQueue.add(CpuProcess); //StateQueue�� allQueue�� CpuProcess�� �� i��° ���� ����
			if(CpuProcess.getRemainingTime()==0) {//CpuProcess�� ��� �Ҵ�ð��� ����� ���
				//setEndWTime(i);
				CpuProcess.setWaitingTime(i); //����� CpuProcess�� ���� WaitingTime ����
				CpuProcess.setTT(); //TT�� ����
				CpuProcess.setNTT(); //NTT�� ����
				CpuProcess=null; //CpuProcess�� null�� ����(���� ���μ����� �Ҵ���� �غ�)
			}
		}
	}







	// -------------------------------------------Round Robin-------------------------------------------
	public void RR() {
		int quantum=quantumValue;
		Process CpuProcess=null;
		for(int i=0;i<totalTime;i++) {
			setReadyQueue(i); //i���� ����ť ����
			if(quantum==0||CpuProcess==null) { //quantum�� ��� �Һ� Ȥ�� ���ο� CpuProcess�� �����ؾ� �� ��
				if(!ReadyQueue.isEmpty()) { //����ť�� ������� �ʴٸ�
					//CpuProcess=loadProcess(CpuProcess); //CpuProcess�� ���� ����
					if(CpuProcess == null) CpuProcess = ReadyQueue.remove();
					else {
						ReadyQueue.add(CpuProcess);
						CpuProcess = ReadyQueue.remove();
					}
				}
				quantum=quantumValue; //quantum �ʱ�ȭ
			}
			gcSQ.allQueue.add(CpuProcess); //StateQueue�� allQueue�� CpuProcess�� �� i��° ���� ����
			CpuProcess.Decrement();
			quantum--;
			if(CpuProcess.getRemainingTime()==0) { //RT ��� ���� ��
				CpuProcess.setWaitingTime(i); //����� CpuProcess�� ���� WaitingTime ����
				CpuProcess.setTT(); //TT�� ����
				CpuProcess.setNTT(); //NTT�� ����
				CpuProcess=null; //CpuProcess�� null�� ����(���� ���μ����� �Ҵ���� �غ�)
			}   
			alltoReadyQueue(i); //i���� gcRQ ����(allQueue,array)
		}
	}


	//-------------------------------------------Shortest Process Next-------------------------------------------
	public void SPN() {
		Process CpuProcess=null;

		for(int i=0;i<totalTime;i++) {
			setReadyQueue(i); //i���� ����ť ����
			if(CpuProcess==null) { //CpuProcess�� ó�� �Ҵ���� ��
				if(!ReadyQueue.isEmpty()) { //����ť�� ������� �ʴٸ�
					CpuProcess=loadminBTime(); //���� ����ť�� �ִ� ���μ��� �� ���� ����BT�� ���� ���μ����� ������
				}
			}
			alltoReadyQueue(i); //i���� gcRQ ����(allQueue,array)
			gcSQ.allQueue.add(CpuProcess); //StateQueue�� allQueue�� CpuProcess�� �� i��° ���� ����
			CpuProcess.Decrement(); //���� �Ҵ�ð�(Remaining Time) 1����
			if(CpuProcess.getRemainingTime()==0) {//CpuProcess�� ��� �Ҵ�ð��� ����� ���
				CpuProcess.setWaitingTime(i); //����� CpuProcess�� ���� WaitingTime ����
				CpuProcess.setTT(); //TT�� ����
				CpuProcess.setNTT(); //NTT�� ����
				CpuProcess=null; //CpuProcess�� null�� ����(���� ���μ����� �Ҵ���� �غ�)
			}
		}
	}

	//�ּ� BT Process ����(SPN)
	public Process loadminBTime() { //CpuProcess�� null�� ��, �� ���ο� Process�� �Ҵ��� �� �ּ��� BT���� ã�� �Լ�
		int min; //�ּ� BT
		int pos=0; //�ּ� BT Process ��ġ

		Process p; //�ӽ� Process
		Process minProcess; //�ּ� BT Process
		Queue<Process> temp=new LinkedList<Process>(); //�ӽ� ť(���� ����)
		temp.addAll(ReadyQueue); //temp�� ��� ReadyQueue ����
		minProcess=temp.peek(); //temp�� �� �� ���μ����� ����(���� Ž��)
		min=minProcess.getBTime(); //minProcess�� BT������ min�� �ʱ�ȭ
		for(int i=0;i<temp.size();i++) { //temp���� ��� �����鼭 �� �� pos�� ����
			p=temp.remove();
			if(p.getBTime()<min) {
				min=p.getBTime();
				pos=i;
			}
			temp.add(p);
		}
		for(int i=0;i<temp.size();i++) { //pos�� ���� �� pos�� �ش��ϴ� ���μ����� ����ť���� ���� ����(minProcess)
			p=ReadyQueue.remove();
			if(i==pos) {
				minProcess=p;
			}else{
				ReadyQueue.add(p);
			}

		}
		return minProcess; //�ּ�BT ���μ��� �ݳ�
	}





	// -------------------------------------------Shortest Remaining Time Next-------------------------------------------
	public void SRTN() {
		Process CpuProcess=null;
		Process currentProcess; //���� Cpu�Ҵ���� ���μ���(���� �߻��� �̿�)
		for(int i=0;i<totalTime;i++) {
			setReadyQueue(i); //i���� ����ť ����
			if(CpuProcess==null) { //CpuProcess�� ó�� �Ҵ���� ��
				if(!ReadyQueue.isEmpty()) { //����ť�� ������� �ʴٸ�
					CpuProcess = loadminRTime(); //�����ð�(Remaining Time)�� ���� ���� ���μ����� ����
				}
			}
			if(CpuProcess!=null) { //CpuProcess�� ���� �� ��(���� �߻� ����κ�)
				if(!ReadyQueue.isEmpty()) { //����ť�� ������� �ʴٸ�
					currentProcess = CpuProcess; //���� Cpu�Ҵ���� ���μ����� �ӽ� ����(���� �� �񱳸���)
					CpuProcess = checkPreemption(CpuProcess); //�����˻�
					if(CpuProcess!=currentProcess) { //���� �߻���
						ReadyQueue.add(currentProcess); //���� ���μ����� ����ť�� �� �ڿ� ����
					}
				}
			}      
			alltoReadyQueue(i); //i���� gcRQ ����(allQueue,array)
			gcSQ.allQueue.add(CpuProcess); //StateQueue�� allQueue�� CpuProcess�� �� i��° ���� ����
			CpuProcess.Decrement(); //���� �Ҵ�ð�(Remaining Time) 1����
			if(CpuProcess.getRemainingTime()==0) {
				CpuProcess.setWaitingTime(i); //����� CpuProcess�� ���� WaitingTime ����
				CpuProcess.setTT(); //TT�� ����
				CpuProcess.setNTT(); //NTT�� ����
				CpuProcess=null; //CpuProcess�� null�� ����(���� ���μ����� �Ҵ���� �غ�)
			}
		}
	}

	public Process loadminRTime() { //CpuProcess ó�� �Ҵ�� ���
		int min; //�ּ� BT��
		int pos=0; //�ּ� BT���μ��� ��ġ
		Process p; //�ӽ� ���μ���
		Process minProcess; //�ּ� BT ���μ���
		Queue<Process> temp=new LinkedList<Process>();
		temp.addAll(ReadyQueue);
		minProcess=temp.peek();
		min=temp.peek().getRemainingTime();
		for(int i=0;i<temp.size();i++) { //�ּ� BT ���μ��� ��ġ Ž��(���� Ž��)
			p=temp.remove();
			if(p.getRemainingTime()<min) {
				min=p.getRemainingTime();
				pos=i;
			}
			temp.add(p);
		}
		for(int i=0;i<temp.size();i++) { //������ pos���� �̿��Ͽ� ����ť�� ���μ����� ����.
			p=ReadyQueue.remove();
			if(i==pos) {
				minProcess=p;
			}else {
				ReadyQueue.add(p);
			}
		}
		return minProcess; //��ȯ
	}

	public Process checkPreemption(Process p) { //���� �߻� ���� �˻�
		int min;
		int pos=-1; //���� �߻����� ���� �� pos = -1

		Queue<Process> temp=new LinkedList<Process>();
		temp.addAll(ReadyQueue);
		Process minProcess=p; //�ּ� RT ���μ���
		Process tp; //�ӽ� ���μ���
		min=p.getRemainingTime();
		for(int i=0;i<temp.size();i++) { 
			tp=temp.remove();
			if(tp.getRemainingTime()<min) { //���� �߻� ��
				min=tp.getRemainingTime();
				pos=i; //pos�� ����
			}
			temp.add(tp);
		}
		if(pos>=0) { //���� �߻� ��
			for(int i=0;i<temp.size();i++) {
				tp=ReadyQueue.remove();
				if(pos==i) {
					minProcess=tp; //minProcess�� ReadyQueue���� ����
				}else 
					ReadyQueue.add(tp);
			}   
		}
		return minProcess; //��ȯ
	}






	// -------------------------------------------Highest Remaining Ratio Next-------------------------------------------
	public void HRRN() {
		Process CpuProcess=null;
		for(int i=0;i<totalTime;i++) {
			setReadyQueue(i); //i���� ����ť ����
			if(CpuProcess==null) { //CpuProcess�� ó�� �Ҵ���� ��
				if(!ReadyQueue.isEmpty()) { //����ť�� ������� �ʴٸ�
					CpuProcess = loadmaxResponseR(); //�ּ� RR���� ���� ���μ����� ����, ����ť���� ����
				}
			}
			alltoReadyQueue(i); //i���� gcRQ ����(allQueue,array)
			gcSQ.allQueue.add(CpuProcess); //StateQueue�� allQueue�� CpuProcess�� �� i��° ���� ����
			CpuProcess.Decrement(); //���� �Ҵ�ð�(Remaining Time) 1����
			if(CpuProcess.getRemainingTime()==0) { //�Ҵ� �ð� ��� �Һ� ��
				setWT(i,CpuProcess.getArrivalTime()); //WaitingTime(WT) ����
				setRR(); //CpuProcess�� ������ ������ ���μ����� RR�� ����
				CpuProcess.setTT(); //TT�� ����
				CpuProcess.setNTT(); //NTT�� ����
				CpuProcess=null; //CpuProcess�� null�� ����(���� ���μ����� �Ҵ���� �غ�)
			}
		}
	}
	public void setWT(int t,int ctime) { //WT�� ����
		Process p;
		for(int i=0;i<ReadyQueue.size();i++) {
			p=ReadyQueue.remove();
			p.setWaitingTimeforHRRN(t,ctime);
			System.out.println("ID : " + p.getID() + " WT : " + p.getWaitingTime());
			ReadyQueue.add(p);
		}
		System.out.println();
	}
	
	public void setRR() { //RR�� ����
		Process p;
		for(int i=0;i<ReadyQueue.size();i++) {
			p=ReadyQueue.remove();
			p.setResponseRatio();
			System.out.println("ID : " + p.getID() + " RR : " + p.getResponseRatio());
			ReadyQueue.add(p);
		}
		System.out.println();
	}
	
	public Process loadmaxResponseR() { //�ּ� RR���� ���� ���μ��� Ž�� �� ��ȯ
		float max; //�ּ� RR��
		int pos=0; //�ּ� RR���� ���� ���μ����� ��ġ
		Process p; //�ӽ� ���μ���
		Process maxProcess=null; //minProcess ����
		Queue<Process> temp=new LinkedList<Process>(); //�ӽ� ť
		temp.addAll(ReadyQueue); //temp�� ReadyQueue ���� ����
		p=temp.peek();
		max=p.getResponseRatio();
		for(int i=0;i<temp.size();i++) { //����Ž��, pos�� ����
			p=temp.remove();
			if(p.getResponseRatio()>max) {
				max=p.getResponseRatio();
				pos=i;
			}
			temp.add(p);
		}

		for(int i=0;i<temp.size();i++) { //pos�� �ش�Ǵ� ���μ����� ������
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