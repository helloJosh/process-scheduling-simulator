import java.awt.*;

public class Process {
	/*
	    ID = Process Num
	    ATime = Arrival Time
	    BTime = Burst Time
	    RTime = Remaining Time
	    WTime = Waiting Time
	    TT = Turnaround Time
	    NTT = Normalized Turnaround Time
	    
	 */
   private int ID=0;
   private int ATime, BTime, RTime, WTime, TT;
   private double NTT;
   private Color color;
   private float RR;

   public Process(int id, int atime, int burst_time, Color c) {
      ID = id;
      ATime = atime;
      BTime = burst_time;
      color = c;
      RTime = burst_time;
      RR=0;  
      WTime=0;
   }



   // ------------------------------get field------------------------------
   public int getID() {
      return ID;
   }
   public int getArrivalTime() {
      return ATime;
   }
   public int getBTime() {
      return BTime;
   }
   public Color getColor() {
      return color;
   }


   public int getRemainingTime() {
      return RTime;
   }
   public int getWaitingTime(){
      return WTime;
   }
   public float getResponseRatio() {
      return RR;
   }
   public int getTT() {
      return TT;
   }
   public double getNTT() {
      return NTT;
   }



   // ------------------------------set field------------------------------   
   public void setResponseRatio() {
      RR = (float)(WTime+BTime) / (float)BTime;
   }
   //WTime = 끝난 시점 시간 - 총 작업 진행 시간 - 도착 시간 + 1
   public void setWaitingTime(int t) {
      WTime = t-BTime-ATime+1;
   }
   public void setWaitingTimeforHRRN(int t, int ctime) {
	   if(ATime > ctime) WTime += t-ATime+1;
	   else WTime += t-ctime+1;
   }
   public void setTT() {
      TT = BTime + WTime;
   }
   public void setNTT() {
      NTT = (double)TT / (double)BTime;
   }

   // ------------------------------decrement------------------------------

   public void Decrement() {
      RTime--;
   }
}