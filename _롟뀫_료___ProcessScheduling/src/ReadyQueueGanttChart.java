import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class ReadyQueueGanttChart extends JPanel implements ActionListener{
	public Timer tm; 
	public Process p;
	public Queue<Process> temp,allQueue;
	public int x=5;
	public int cnt=0;
	public int array[];
	public int totalTime;
	
	public ReadyQueueGanttChart(int t){
		tm=new Timer(1000,this); 
		temp=new LinkedList<Process>();
		allQueue=new LinkedList<Process>();
		array=new int[1000];
		totalTime=t;
		tm.start();
	}

	public void actionPerformed(ActionEvent e) {
		int len=array[cnt];
		temp.clear(); //한 사이클 진행 후 temp 초기화
		for(int i=0;i<len;i++) temp.add(allQueue.remove()); //저장된 array배열에 따른 len값에 의한 temp설정
		x=5;
		cnt++;
		if(cnt>=totalTime) tm.stop(); //cnt가 totalTime에 도달하면 paint 중단
		repaint(); //tm마다 paint갱신
	}
	
	public void paint(Graphics g){
		super.paint(g);
		for(int i=0;i<temp.size();i++){ //매 temp크기마다 GanttChart 생성
			Process p = temp.remove();
			g.setColor(p.getColor());
			g.fillRect(x, 10, 29, 80);
			g.setColor(Color.WHITE);
	        g.drawString(Integer.toString(1+p.getID()), x, 25);
			x+=30; //x좌표 30씩 증가
			temp.add(p);
		}
	}
}