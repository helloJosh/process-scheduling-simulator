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
	public int x=5; //x��ǥ ������
	public int cnt=0;
	
	public StateQueueGanttChart(int t){
		tm=new Timer(1000,this);
		allQueue=new LinkedList<Process>();
		temp=new LinkedList<Process>();
		totalTime=t;
		tm.start();
	}

	public void actionPerformed(ActionEvent e) {
		temp.clear(); //�� ����Ŭ ���� �� temp �ʱ�ȭ
		temp.addAll(allQueue);
		cnt++;
		if(cnt>totalTime) {
			tm.stop(); //cnt�� totalTime�� �����ϸ� paint �ߴ�
			showMessageDialog(null, "              Complete"); //�Ϸ� �ȳ� �޼���
		}
			
		
		x=5; //x��ǥ
		repaint(); //tm���� paint����
	}
	
	public void paint(Graphics g){
		super.paint(g);
		this.setBackground(Color.WHITE);
	
		for(int i=0;i<cnt;i++){ //1���� ���, 2���� ��� ... cnt-1���� ��� => �� �ʸ��� �� ���� �߰��� GanttChart ���
			if(temp.size()>0){ //temp�� ���μ����� 1���̻� ����Ǿ� ���� ���
				Process p = temp.remove();
				g.setColor(p.getColor());
				g.fillRect(x, 10, 15,75);
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(1+p.getID()), x, 25);
				g.drawString(Integer.toString(i+1), x, 80);
				x+=17; //x��ǥ 17�� �����ϸ鼭 ��Ʈ ǥ��
			}
		}
	}
}