import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import static javax.swing.JOptionPane.*;

public class StateQueueGanttChart extends JPanel implements ActionListener {
	public Timer tm;
	public Process p;
	public Queue<Process> allQueue,temp;
	public int totalTime;
	public int x=5; //x좌표 시작점
	public int cnt=0;
	
	public StateQueueGanttChart(int t){
		tm=new Timer(1000,this);
		allQueue=new LinkedList<Process>();
		temp=new LinkedList<Process>();
		totalTime=t;
		tm.start();
	}

	public void actionPerformed(ActionEvent e) {
		temp.clear(); //한 사이클 진행 후 temp 초기화
		temp.addAll(allQueue);
		cnt++;
		if(cnt>totalTime) {
			tm.stop(); //cnt가 totalTime에 도달하면 paint 중단
			showMessageDialog(null, "              Complete"); //완료 안내 메세지
		}
			
		
		x=5; //x좌표
		repaint(); //tm마다 paint갱신
	}
	
	public void paint(Graphics g){
		super.paint(g);
		this.setBackground(Color.WHITE);
	
		for(int i=0;i<cnt;i++){ //1개씩 출력, 2개씩 출력 ... cnt-1개씩 출력 => 매 초마다 한 개씩 추가된 GanttChart 출력
			if(temp.size()>0){ //temp에 프로세스가 1개이상 저장되어 있을 경우
				Process p = temp.remove();
				g.setColor(p.getColor());
				g.fillRect(x, 10, 15,75);
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(1+p.getID()), x, 25);
				g.drawString(Integer.toString(i+1), x, 80);
				x+=17; //x좌표 17씩 증가하면서 차트 표시
			}
		}
	}
}