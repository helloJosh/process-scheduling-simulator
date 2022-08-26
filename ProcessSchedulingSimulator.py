import sys

from PyQt5.QtWidgets import *
from PyQt5.QtCore import *
from PyQt5.QtGui import *
from PyQt5.Qt import *


order = 0
sum = 0
processName =""
data = []
sequence_data =[]
resultWT = []
resultTT = []
resultNTT = []
col_data =[QColor(235,235,235)]

class MainWindow(QWidget):

    def __init__(self):
        super(MainWindow,self).__init__()
        global processName
        global sequence_data
        global data
        self.left = 0
        self.top = 0
        self.width = 650
        self.height = 500

        self.result = []
        self.enter_TimeList = []
        self.end_TimeList = []
        self.sequence_TimeList = []


        self.setGeometry(self.left, self.top, self.width, self.height)
        self.setWindowTitle('Process Information')

        self.tableWidget = QTableWidget(10,4,self)
        self.tableWidget.setHorizontalHeaderLabels(["Process Number", "Arrival Time", "Burst Time", "Time Quantum"])
        self.tableWidget.setMaximumSize(420, 400)
        self.tableWidget.setItem(0, 0, QTableWidgetItem("1"))
        self.tableWidget.setEditTriggers(QAbstractItemView.NoEditTriggers)

        self.createBtn()
        self.createComboBox()

        self.layout = QVBoxLayout()
        self.layout.addWidget(self.tableWidget)
        self.setLayout(self.layout)
        self.show()

    ######### createBtn ###########
    def createBtn(self):
        btn = QPushButton('+', self)
        btn.move(490, 150)
        btn.resize(btn.sizeHint())
        btn.clicked.connect(self.__btn_clicked)

        btn1 = QPushButton('-', self)
        btn1.move(560, 150)
        btn1.resize(btn.sizeHint())
        btn1.clicked.connect(self.__btn1_clicked)

        btn2 = QPushButton('Delete All', self)
        btn2.move(500, 300)
        btn2.resize(100,30)
        btn2.clicked.connect(self.__btn2_clicked)

        btn3 = QPushButton('Start', self)
        btn3.move(500, 400)
        btn3.resize(100,30)
        btn3.clicked.connect(self.__btn3_clicked)

    @pyqtSlot()  # +
    def __btn_clicked(self):
        self.SW = SecondWindow()
        self.SW.show()

    @pyqtSlot()  # -
    def __btn1_clicked(self):
        global data
        global order
        global sum


        #테이블에 아무것도 없을 때의 예외처리
        if order <= 0:
            pass
        else:
            del data[order-1]
            self.tableWidget.removeRow(order-1)
            order -= 1

    @pyqtSlot()  # Delete All
    def __btn2_clicked(self):
        global data
        global order
        global sum
        data = []
        order = 0
        sum = 0
        self.tableWidget.clearContents()

    @pyqtSlot()  # Start
    def __btn3_clicked(self):
        global processName
        global sequence_data
        global data
        global order
        global resultWT
        global resultTT
        global resultNTT

        if (processName == 'FCFS'):
            self.pc = Scheduling(data)
            tResult =self.pc.FCFS()
            result = self.pc.getSequenceList()

            for i in range(order):
                resultWT.append(tResult[i].WT)
                resultTT.append(tResult[i].TT)
                resultNTT.append(round(tResult[i].TT/tResult[i].BT,1))

            self.TW = ThirdWindow(result)
            self.TW.show()

            resultWT = []
            resultTT = []
            resultNTT = []

        elif processName == 'RR':
            self.pc = Scheduling(data)
            tResult = self.pc.RR()
            result = self.pc.getSequenceList()

            for i in range(order):
                resultWT.append(tResult[i].WT)
                resultTT.append(tResult[i].TT)
                resultNTT.append(round(tResult[i].TT / tResult[i].BT, 1))

            self.TW = ThirdWindow(result)
            self.TW.show()

            resultWT = []
            resultTT = []
            resultNTT = []
        elif processName == 'SPN':
            self.pc = Scheduling(data)
            tResult = self.pc.SPN()
            result = self.pc.getSequenceList()

            for i in range(order):
                resultWT.append(tResult[i].WT)
                resultTT.append(tResult[i].TT)
                resultNTT.append(round(tResult[i].TT / tResult[i].BT, 1))

            self.TW = ThirdWindow(result)
            self.TW.show()

            resultWT = []
            resultTT = []
            resultNTT = []
        elif processName == 'SRTN':
            self.pc = Scheduling(data)
            tResult = self.pc.SRTN()
            result = self.pc.getSequenceList()

            for i in range(order):
                resultWT.append(tResult[i].WT)
                resultTT.append(tResult[i].TT)
                resultNTT.append(round(tResult[i].TT / tResult[i].BT, 1))

            self.TW = ThirdWindow(result)
            self.TW.show()

            resultWT = []
            resultTT = []
            resultNTT = []
        elif processName == 'HRRN':
            self.pc = Scheduling(data)
            tResult = self.pc.HRRN()
            result = self.pc.getSequenceList()

            for i in range(order):
                resultWT.append(tResult[i].WT)
                resultTT.append(tResult[i].TT)
                resultNTT.append(round(tResult[i].TT / tResult[i].BT, 1))

            self.TW = ThirdWindow(result)
            self.TW.show()

            resultWT = []
            resultTT = []
            resultNTT = []
        elif processName == 'OurScheduler':
            self.pc = Scheduling(data)
            tResult = self.pc.OurScheduler()
            result = self.pc.getSequenceList()

            for i in range(order):
                resultWT.append(tResult[i].WT)
                resultTT.append(tResult[i].TT)
                resultNTT.append(round(tResult[i].TT / tResult[i].BT, 1))

            self.TW = ThirdWindow(result)
            self.TW.show()

            resultWT = []
            resultTT = []
            resultNTT = []
        elif processName == '    ':
            pass
        # 타임시퀀스랑 스케줄링기법 받아서 넘겨준다.


    ########## createComboBox #########
    def createComboBox(self):
        #self.lbl = QLabel('FCFS', self)
        #self.lbl.move(560, 50)

        self.cb = QComboBox(self)
        self.cb.move(490, 70)
        self.cb.resize(130,50)
        self.cb.addItem('    ')
        self.cb.addItem('FCFS')
        self.cb.addItem('RR')
        self.cb.addItem('SPN')
        self.cb.addItem('SRTN')
        self.cb.addItem('HRRN')
        self.cb.addItem('OurScheduler')

        self.cb.activated[str].connect(self.onActivated)

    def onActivated(self, text):
        global processName
        processName = self.cb.currentText()



###### 입력창 ####
class SecondWindow(QWidget):

    arrivalTime = 0
    burstTime = 0
    timeQuantum = 0


    def __init__(self):
        super().__init__()
        global order
        global col_data

        self.col = QColor(0, 0, 0)

        self.frm = QFrame(self)
        self.frm.setStyleSheet('QWidget { background-color: %s }' % self.col.name())
        self.frm.setGeometry(80, 130, 10, 10)

        self.lbl = QLabel(self)
        self.lbl.move(72, 20)
        self.lbl.setText('Arrival Time')

        self.lbl1 = QLabel(self)
        self.lbl1.move(70, 50)
        self.lbl1.setText('Burst Time')

        self.lbl2 = QLabel(self)
        self.lbl2.move(60, 80)
        self.lbl2.setText('Time Quantum')

        self.lbl3 = QLabel(self)
        self.lbl3.move(5, 50)
        lbl3text = 'P.No  ' + str(order+1)
        self.lbl3.setText(lbl3text)

        self.qle = QLineEdit(self)
        self.qle.move(160,20)
        # MainWindow.burstTime = qle.text() #burst time
        # self.qle.textChanged[str].connect(self)

        self.qle1 = QLineEdit(self)
        self.qle1.move(160, 50)
        # MainWindow.arrivalTime = qle1.text() #Arrival time
        # self.qle1.textChanged[str].connect(self.onChanged)

        self.qle2 = QLineEdit(self)
        self.qle2.move(160, 80)
        # MainWindow.t = qle2.text() #Time Quantum
        # self.qle2.textChanged[str].connect(self.onChanged)

        btn3 = QPushButton('Choose Color', self)
        btn3.move(90, 120)
        btn3.resize(btn3.sizeHint())
        btn3.clicked.connect(self.__btn4_clicked)

        btn4 = QPushButton('Insert', self)
        btn4.move(40, 150)
        btn4.resize(btn4.sizeHint())
        btn4.clicked.connect(self.__btn5_clicked)
        btn4.clicked.connect(self.close)

        btn5 = QPushButton('Exit', self)
        btn5.move(180, 150)
        btn5.resize(btn5.sizeHint())
        btn5.clicked.connect(self.__btnExit_clicked)
        btn5.clicked.connect(self.close)

        self.setWindowIcon(QIcon('web.png'))
        self.setGeometry(300, 300, 300, 200)
        self.setWindowTitle('Add Process')

    @pyqtSlot()  # Exit
    def __btnExit_clicked(self):
        col_data.remove(col_data[order])

    @pyqtSlot()  # Choose Color
    def __btn4_clicked(self):
        global col_data
        self.col = QColorDialog.getColor()
        col_data.append(self.col)
        if self.col.isValid():
            self.frm.setStyleSheet('QWidget { background-color: %s }' % self.col.name())

    @pyqtSlot()  # Insert
    def __btn5_clicked(self):
        global processName
        global data
        global order
        global col_data
        global sum

        error_dialog = QErrorMessage()


        self.arrivalTime = int('0'+self.qle.text())
        self.burstTime = int('0'+ self.qle1.text())
        self.timeQuantum = int('0'+self.qle2.text())

        # timequantum, arrival time의 입력 예외처리
        if processName == 'RR' or processName == 'OurScheduler':
            if (self.qle2.text() == '0'):
                self.timeQuantum = 1
                QMessageBox.about(self, "Error Message", "Needs timeQuantum")
        else:
            if (self.timeQuantum != 0):
                self.timeQuantum = 0
                QMessageBox.about(self, "Error Message", "No need to input timeQuantum")
        if order == 0 :
            if self.arrivalTime != 0 :
                self.arrivalTime = 0
                QMessageBox.about(self, "Error Message", "Arrival Time Must Be 0")

        order += 1
        data.append([order, self.arrivalTime, self.burstTime,self.timeQuantum])
        for idx, [order, arrivalTime, burstTime, timeQuantum] in enumerate(data):
            Window.tableWidget.setItem(idx,0,QTableWidgetItem(str(order)))
            Window.tableWidget.setItem(idx,1, QTableWidgetItem(str(arrivalTime)))
            Window.tableWidget.setItem(idx,2,QTableWidgetItem(str(burstTime)))
            Window.tableWidget.setItem(idx,3, QTableWidgetItem(str(timeQuantum)))

        try:
            col_data[order]
        except IndexError:
            col_data = QColor(0,0,0)

        sum += self.burstTime

        if(sum > 31):
            QMessageBox.about(self, "Error Message", "To much burst Time")
            self.close()


class ThirdWindow(QWidget):
    def __init__(self, list):
        super(ThirdWindow,self).__init__()
        global order
        global data
        self.flag = False
        self.i = 0
        self.list = list
        self.setFixedSize(1100, 500)
        self.setWindowTitle('Process Scheduling Table')
        self.string =""
        self.string1 = ""
        self.stringTT = "TT    :    "
        self.stringWT = "WT   :   "
        self.stringNTT = "NTT :  "

        self.btn = QPushButton("Paint", self)
        self.btn.move(1000, 250)
        self.btn.clicked.connect(self.onClicked)
        self.drawGraduation()
        self.printProcessSecquance()
        self.printProcessSecquance1()
        self.printT()
        self.show()

    def onClicked(self):
        self.flag = True
        self.update()

    def paintEvent(self, e):
        if self.flag:
            qp = QPainter()
            qp.begin(self)
            self.drawRectangles(qp)
            qp.end()

    def drawRectangles(self, qp):
        global col_data
        #print(col[self.list[1]])
        for self.i in range(len(self.list)):
            if (self.list[self.i] != 0):
                try:
                    qp.setPen(col_data[self.list[self.i]])
                    qp.setBrush(col_data[self.list[self.i]])
                except TypeError:
                    qp.setPen(QColor(0,0,0))
                    qp.setBrush(QColor(0,0,0))

                qp.drawRect(70 + 50 * self.i, 65, 30, 60)
            else:
                qp.setPen(col_data[self.list[self.i]])
                qp.setBrush(col_data[self.list[self.i]])
                qp.drawRect(70 + 50 * self.i, 65, 30, 60)


    def drawGraduation(self):
        self.lbl1 = QLabel(self)
        self.lbl1.move(15, 130)
        lbl1text = str('time')
        self.lbl1.setText(lbl1text)

        self.lbl1 = QLabel(self)
        self.lbl1.move(65, 130)
        lbl1text = str(0)
        self.lbl1.setText(lbl1text)

        self.lbl2 = QLabel(self)
        self.lbl2.move(115, 130)
        lbl2text = str(1)
        self.lbl2.setText(lbl2text)

        self.lbl3 = QLabel(self)
        self.lbl3.move(165, 130)
        lbl3text = str(2)
        self.lbl3.setText(lbl3text)

        self.lbl4 = QLabel(self)
        self.lbl4.move(215, 130)
        lbl4text = str(3)
        self.lbl4.setText(lbl4text)

        self.lbl5 = QLabel(self)
        self.lbl5.move(265, 130)
        lbl5text = str(4)
        self.lbl5.setText(lbl5text)

        self.lbl6 = QLabel(self)
        self.lbl6.move(315, 130)
        lbl6text = str(5)
        self.lbl6.setText(lbl6text)

        self.lbl7 = QLabel(self)
        self.lbl7.move(365, 130)
        lbl7text = str(6)
        self.lbl7.setText(lbl7text)

        self.lbl8 = QLabel(self)
        self.lbl8.move(415, 130)
        lbl8text = str(7)
        self.lbl8.setText(lbl8text)

        self.lbl9 = QLabel(self)
        self.lbl9.move(465, 130)
        lbl9text = str(8)
        self.lbl9.setText(lbl9text)

        self.lbl10 = QLabel(self)
        self.lbl10.move(515, 130)
        lbl10text = str(9)
        self.lbl10.setText(lbl10text)

        self.lbl11 = QLabel(self)
        self.lbl11.move(565, 130)
        lbl11text = str(10)
        self.lbl11.setText(lbl11text)

        self.lbl12 = QLabel(self)
        self.lbl12.move(615, 130)
        lbl12text = str(11)
        self.lbl12.setText(lbl12text)

        self.lbl13 = QLabel(self)
        self.lbl13.move(665, 130)
        lbl13text = str(12)
        self.lbl13.setText(lbl13text)

        self.lbl14 = QLabel(self)
        self.lbl14.move(715, 130)
        lbl14text = str(13)
        self.lbl14.setText(lbl14text)

        self.lbl15 = QLabel(self)
        self.lbl15.move(765, 130)
        lbl15text = str(14)
        self.lbl15.setText(lbl15text)

        self.lbl16 = QLabel(self)
        self.lbl16.move(815, 130)
        lbl16text = str(15)
        self.lbl16.setText(lbl16text)

        self.lbl17 = QLabel(self)
        self.lbl17.move(865, 130)
        lbl17text = str(16)
        self.lbl17.setText(lbl17text)

        self.lbl18 = QLabel(self)
        self.lbl18.move(915, 130)
        lbl18text = str(17)
        self.lbl18.setText(lbl18text)

        self.lbl19 = QLabel(self)
        self.lbl19.move(965, 130)
        lbl19text = str(18)
        self.lbl19.setText(lbl19text)

        self.lbl20 = QLabel(self)
        self.lbl20.move(1015, 130)
        lbl20text = str(19)
        self.lbl20.setText(lbl20text)

        self.lbl21 = QLabel(self)
        self.lbl21.move(1065, 130)
        lbl21text = str(20)
        self.lbl21.setText(lbl21text)

    def printProcessSecquance(self):
        self.string = "Pro. N :  "
        for i in self.list:
            self.string += "P"
            self.string += str(i)
            self.string += '          '
        self.lbl22 = QLabel(self)
        self.lbl22.move(20, 50)
        lbl22text = self.string
        self.lbl22.setText(self.string)

    def printProcessSecquance1(self):
        self.string1 = "Pro. N    :      "
        for i in range(order):
            self.string1 += "P"
            self.string1 += str(i+1)
            self.string1 += '      '
        self.lbl22 = QLabel(self)
        self.lbl22.move(25, 320)
        lbl22text = self.string1
        self.lbl22.setText(self.string1)

    def printT(self):
        global resultNTT
        global resultTT
        global resultWT
        for i in resultNTT:
            self.stringNTT += str(i)
            self.stringNTT += "      "
        for i in resultTT:
            self.stringTT += str(i)
            self.stringTT += "        "
        for i in resultWT:
            self.stringWT += str(i)
            self.stringWT += "         "
        self.lbl22 = QLabel(self)
        self.lbl22.move(60, 350)
        lbl22text = self.string
        self.lbl22.setText(self.stringNTT)

        self.lbl23 = QLabel(self)
        self.lbl23.move(60, 400)
        lbl23text = self.string
        self.lbl23.setText(self.stringTT)

        self.lbl24 = QLabel(self)
        self.lbl24.move(60, 450)
        lbl24text = self.string
        self.lbl24.setText(self.stringWT)



# 알고리즘 로직 모듈
# 조상우, 갈동건
#프로세스 클래스(p들의 리스트에서 분리한 P를 객체로 만듬)
#사용자는 이 클래스를 직접적으로 다루지 않음

# 추가 요구사항 TT


class Process:
    def __init__(self, process):
        self.Pid = process[0]
        self.ArrTime = process[1]
        self.BT = process[2]
        try:
            self.TQ = process[3]
        except IndexError as _:
            print(f'P{self.Pid} have no TQ')
        self.RemainBT = self.BT  # round-robin에서 BT이용한 계산에 사용함

    def setRR(self, curTime):
        self.curTime = curTime
        self.TT = curTime - self.ArrTime + 1
        self.WT = self.TT-self.BT
        if self.WT < 0:
            self.WT = 0
        self.RR = (self.WT + self.BT)/self.BT

    def getRR(self):
        return self.RR

    def getId(self):
        return self.Pid

    def getBT(self):
        return self.BT

    def getRemainBT(self):
        return self.RemainBT

#스케쥴링 클래스


class Scheduling:
    Input_Q = []  # 입력 받은값 저장용
    Ready_Q = []  # 스케줄링용
    total_Time = 0  # BT의 총합
    sequenceList = []

    # 생성자 : 이중배열 받고 배열을 분리해서 각각 객체로 만들고 다시 리스트에 집어넣음
    # BT의 총합도 바로 구함
    def __init__(self, ListOfProcesses):
        #각 리스트요소 : [pid, AT, BT], 타임퀀텀은 필요한 메소드에 따로 받음
        self.ListOfProcesses = ListOfProcesses
        #DEBUG용 예시데이터
        #self.ListOfProcesses = [[1,0,3], [2,1,7],[3,3,2],[4,5,5],[5,6,3]]

        #process 객체를 만들고 inputQ에 삽입
        for i in range(len(self.ListOfProcesses)):
            self.Input_Q.append(Process(self.ListOfProcesses[i]))

        # BT 총합 구하기
        for i in range(len(self.Input_Q)):
            self.total_Time += self.Input_Q[i].BT

        # 프로세스 수행 완료시간 기록 리스트
        self.EndTimeList = [0] * (self.total_Time + 1)  # +1은 시간이 0부터 시작해서

    ############################################################# end of 생성자

    # 언제 어느 큐가 진입했는지 기록하는 기록함수
    # ex : [1, 0, 0, 2, 3] ->T0, T3, T4에 p1, p2, p3프로세스 진입

    def getEnterTimeList(self):
        enterTimeList = [0] * (self.total_Time+1)
        for i in range(len(self.ListOfProcesses)):
            enterTimeList[self.ListOfProcesses[i]
                          [1]] = self.ListOfProcesses[i][0]
        return enterTimeList

    def setSequenceList(self):
        self.sequenceList=[]
        return self.sequenceList

    def getSequenceList(self):
        return self.sequenceList

    def getEndTimeList(self):
        return self.EndTimeList

    #함수 매개변수 : self = 메소드작성시 국룰임

    def FCFS(self):
        processing = False
        remain_Time = 0
        timeCount = 0
        resultList = []  # 결과 저장용 리스트
        self.setSequenceList()

        #총 단위시간 20 까지
        while timeCount <= self.total_Time:
            try:
                # 현재 시간이 p의 도착시간과 일치하면
                while timeCount == self.Input_Q[0].ArrTime:
                    # input 큐 맨 앞값을 빼서 ready 큐 맨 뒤에 삽입
                    self.Ready_Q.append(self.Input_Q.pop(0))

            except IndexError as _:
                pass

            #curTime 반영해서 RR구함
            for i in range(len(self.Ready_Q)):
                self.Ready_Q[i].setRR(timeCount)

            if not processing:  # 수행중인 프로세스가 없을 때
                try:
                    processing_p = self.Ready_Q.pop(0)
                    remain_Time = processing_p.BT - 1
                    processing = True
                    self.sequenceList.append(processing_p.Pid)
                except IndexError as _:
                    self.sequenceList.append(0)

            elif remain_Time == 0:
                processing = False
                resultList.append(processing_p)
                self.EndTimeList[timeCount] = processing_p.Pid

                if self.Ready_Q:
                    processing_p = self.Ready_Q.pop(0)
                    remain_Time = processing_p.BT - 1
                    processing = True
                    self.sequenceList.append(processing_p.Pid)

            elif timeCount >= 20:
                processing = False
                resultList.append(processing_p)
                self.EndTimeList[timeCount] = processing_p.Pid
            else:
                remain_Time -= 1
                processing_p.setRR(timeCount)
                self.sequenceList.append(processing_p.Pid)

            timeCount += 1
            if timeCount > 20:
                break
        ################################# end of while

        resultList.sort(key=Process.getId)  # 보기좋게 id 순으로 정렬
        return resultList

    def RR(self):
        TimeQuantum = self.Input_Q[0].TQ # 타임 퀀텀값 저장
        saveTQ = TimeQuantum
        processing = False
        timeCount = 0
        resultList = []  # 결과 저장용 리스트
        self.setSequenceList()

        #총 단위시간 20 까지
        while timeCount <= self.total_Time:
            try:
                # 현재 시간이 p의 도착시간과 일치하면
                while timeCount == self.Input_Q[0].ArrTime:
                    # input 큐 맨 앞값을 빼서 ready 큐 맨 뒤에 삽입
                    self.Ready_Q.append(self.Input_Q.pop(0))
            except IndexError as _:
                pass

             #curTime 반영해서 RR구함
            for i in range(len(self.Ready_Q)):
                self.Ready_Q[i].setRR(timeCount)

            if not processing:  # 수행중인 프로세스가 없을 때
                processing_p = self.Ready_Q.pop(0)
                processing_p.RemainBT = processing_p.BT - 1
                processing = True
                TimeQuantum -= 1
                self.sequenceList.append(processing_p.Pid)

            elif processing_p.RemainBT == 0:  # bt를 모두 소진하면
                processing = False
                resultList.append(processing_p)
                self.EndTimeList[timeCount] = processing_p.Pid

                if self.Ready_Q:
                    processing_p = self.Ready_Q.pop(0)
                    processing_p.RemainBT -= 1

                    processing = True
                    self.sequenceList.append(processing_p.Pid)
                    TimeQuantum = saveTQ - 1

            elif TimeQuantum == 0:  # 물러나서 readyQ 맨 뒤로감
                #processing_p.RemainBT = remain_Time
                self.Ready_Q.append(processing_p)  # x타임 퀀텀 다되면 readyq로 물러남

                processing_p = self.Ready_Q.pop(0)  # readyq 에서 새로 꺼내옴
                self.sequenceList.append(processing_p.Pid)

                processing_p.RemainBT -= 1
                TimeQuantum = saveTQ - 1  # 타임 퀀텀 초기화

            else:
                processing_p.RemainBT -= 1
                TimeQuantum -= 1
                processing_p.setRR(timeCount)
                self.sequenceList.append(processing_p.Pid)

            timeCount += 1
            if timeCount > 20:
                break
        ################################# end of while

        resultList.sort(key=Process.getId)  # 보기좋게 id 순으로 정렬
        return resultList

    def SRTN(self):
        processing = False
        #remain_Time = 0
        timeCount = 0
        resultList = []  # 결과 저장용 리스트
        self.setSequenceList()

        #총 단위시간 20 까지
        while timeCount <= self.total_Time:
            try:
                # 현재 시간이 p의 도착시간과 일치하면
                while timeCount == self.Input_Q[0].ArrTime:
                    # input 큐 맨 앞값을 빼서 ready 큐 맨 뒤에 삽입
                    self.Ready_Q.append(self.Input_Q.pop(0))

            except IndexError as _:
                pass

            #curTime 반영해서 RR구함
            for i in range(len(self.Ready_Q)):
                self.Ready_Q[i].setRR(timeCount)

            # BT기준 오름차순 정렬
            self.Ready_Q.sort(key=Process.getRemainBT)

            if not processing:  # 수행중인 프로세스가 없을 때
                processing_p = self.Ready_Q.pop(0)
                processing_p.RemainBT = processing_p.BT - 1
                processing = True
                self.sequenceList.append(processing_p.Pid)
            #preemption 기준
            elif self.Ready_Q and processing_p.RemainBT > self.Ready_Q[0].RemainBT:
                self.Ready_Q.append(processing_p)
                processing_p = self.Ready_Q.pop(0)
                processing_p.RemainBT -= 1
                self.sequenceList.append(processing_p.Pid)

            elif processing_p.RemainBT == 0:  # bt를 모두 소진하면
                processing = False
                resultList.append(processing_p)
                self.EndTimeList[timeCount] = processing_p.Pid

                if self.Ready_Q:
                    processing_p = self.Ready_Q.pop(0)
                    processing_p.RemainBT -= 1

                    processing = True
                    self.sequenceList.append(processing_p.Pid)

            else:
                processing_p.RemainBT -= 1
                processing_p.setRR(timeCount)
                self.sequenceList.append(processing_p.Pid)

            timeCount += 1
            if timeCount > 20:
                break
        ################################# end of while

        resultList.sort(key=Process.getId)  # 보기좋게 id 순으로 정렬
        return resultList

    def SPN(self):
        processing = False
        remain_Time = 0
        timeCount = 0
        resultList = []  # 결과 저장용 리스트
        self.setSequenceList()

        #총 단위시간 20 까지
        while timeCount <= self.total_Time:
            try:
                # 현재 시간이 p의 도착시간과 일치하면
                while timeCount == self.Input_Q[0].ArrTime:
                    # input 큐 맨 앞값을 빼서 ready 큐 맨 뒤에 삽입
                    self.Ready_Q.append(self.Input_Q.pop(0))

            except IndexError as _:
                pass

            #curTime 반영해서 RR구함
            for i in range(len(self.Ready_Q)):
                self.Ready_Q[i].setRR(timeCount)

            # BT기준 오름차순 정렬
            self.Ready_Q.sort(key=Process.getBT)

            if not processing:  # 수행중인 프로세스가 없을 때
                processing_p = self.Ready_Q.pop(0)
                remain_Time = processing_p.BT - 1
                processing = True
                self.sequenceList.append(processing_p.Pid)
            elif remain_Time == 0:
                processing = False
                resultList.append(processing_p)
                self.EndTimeList[timeCount] = processing_p.Pid

                if self.Ready_Q:
                    processing_p = self.Ready_Q.pop(0)
                    remain_Time = processing_p.BT - 1
                    processing = True
                    self.sequenceList.append(processing_p.Pid)
            else:
                remain_Time -= 1
                processing_p.setRR(timeCount)
                self.sequenceList.append(processing_p.Pid)

            timeCount += 1
            if timeCount > 20:
                break
        ################################# end of while

        resultList.sort(key=Process.getId)  # 보기좋게 id 순으로 정렬
        return resultList

    def HRRN(self):
        processing = False
        remain_Time = 0
        timeCount = 0
        resultList = []  # 결과 저장용 리스트
        self.setSequenceList()

        #총 단위시간 20 까지
        while timeCount <= self.total_Time:
            try:
                # 현재 시간이 p의 도착시간과 일치하면
                while timeCount == self.Input_Q[0].ArrTime:
                    # input 큐 맨 앞값을 빼서 ready 큐 맨 뒤에 삽입
                    self.Ready_Q.append(self.Input_Q.pop(0))

            except IndexError as _:
                pass

            #curTime 반영해서 RR구함
            for i in range(len(self.Ready_Q)):
                self.Ready_Q[i].setRR(timeCount)

            self.Ready_Q.sort(key=Process.getRR, reverse=True)  # RR기준 내림차순정렬

            if not processing:  # 수행중인 프로세스가 없을 때
                processing_p = self.Ready_Q.pop(0)
                remain_Time = processing_p.BT - 1
                processing = True
                self.sequenceList.append(processing_p.Pid)
            elif remain_Time == 0:
                processing = False
                resultList.append(processing_p)
                self.EndTimeList[timeCount] = processing_p.Pid

                if self.Ready_Q:
                    processing_p = self.Ready_Q.pop(0)
                    remain_Time = processing_p.BT - 1
                    processing = True
                    self.sequenceList.append(processing_p.Pid)
            else:
                remain_Time -= 1
                processing_p.setRR(timeCount)
                self.sequenceList.append(processing_p.Pid)

            timeCount += 1
            if timeCount > 20:
                break
        ################################# end of while

        resultList.sort(key=Process.getId)  # 보기좋게 id 순으로 정렬
        return resultList

    #가중치 RR -덩치가 클수록 타임퀀텀 커짐, 기본 TQ = 2, BT/TQ 만큼 TQ추가
    # ex :  bt가 7이면 7/2 = 3 -> TQ = 2 + 3 = 5
    def Ours(self):
        # 받은 값 하나 뽑아서 기본 TT설정
        TimeQuantum = self.Input_Q[0].TQ
        weightTQ = TimeQuantum
        #saveTQ = weightTQ # 타임 퀀텀값 저장
        processing = False
        timeCount = 0
        resultList = []  # 결과 저장용 리스트
        self.setSequenceList()

        #총 단위시간 20 까지
        while timeCount <= self.total_Time:
            try:
                # 현재 시간이 p의 도착시간과 일치하면
                while timeCount == self.Input_Q[0].ArrTime:
                    # input 큐 맨 앞값을 빼서 ready 큐 맨 뒤에 삽입
                    self.Ready_Q.append(self.Input_Q.pop(0))

            except IndexError as _:
                pass

             #curTime 반영해서 RR구함
            for i in range(len(self.Ready_Q)):
                self.Ready_Q[i].setRR(timeCount)

            if not processing:  # 수행중인 프로세스가 없을 때
                processing_p = self.Ready_Q.pop(0)
                weightTQ += int(processing_p.RemainBT /
                                TimeQuantum) - 1  # int는 소수점 절삭 연산
                processing_p.RemainBT = processing_p.BT - 1
                processing = True
                self.sequenceList.append(processing_p.Pid)

            elif processing_p.RemainBT == 0:  # bt를 모두 소진하면
                processing = False
                resultList.append(processing_p)
                self.EndTimeList[timeCount] = processing_p.Pid

                if self.Ready_Q:
                    processing_p = self.Ready_Q.pop(0)
                    weightTQ = TimeQuantum + \
                        int(processing_p.RemainBT / TimeQuantum) - 1
                    processing_p.RemainBT -= 1

                    processing = True
                    self.sequenceList.append(processing_p.Pid)
                    #weightTQ = saveTQ -1

            elif weightTQ == 0:  # 물러나서 readyQ 맨 뒤로감
                #processing_p.RemainBT = remain_Time
                self.Ready_Q.append(processing_p)  # x타임 퀀텀 다되면 readyq로 물러남
                processing_p = self.Ready_Q.pop(0)  # readyq 에서 새로 꺼내옴
                self.sequenceList.append(processing_p.Pid)
                weightTQ = TimeQuantum + \
                    int(processing_p.RemainBT / TimeQuantum) - 1
                processing_p.RemainBT -= 1
                #TimeQuantum = saveTQ -1 # 타임 퀀텀 초기화

            else:
                processing_p.RemainBT -= 1
                weightTQ -= 1
                processing_p.setRR(timeCount)
                self.sequenceList.append(processing_p.Pid)

            timeCount += 1
            if timeCount > 20:
                break
        ################################# end of while

        resultList.sort(key=Process.getId)  # 보기좋게 id 순으로 정렬
        return resultList

#사용자는 메소드만 바꾸면서 실행하면됨


def Test():
    # RR에서 0번째 TQ이 기준 TQ가 됨
    pc = Scheduling([[1, 0, 3, 2], [2, 1, 7, 2], [3, 3, 2, 2], [4, 5, 5, 2], [5, 6, 3, 2]])
    result = pc.HRRN()

    print("스케줄링 완료")
    print("Process ID    Arrival Time    Burst Time    Waiting Time    Turnaround Time    Normalized TT")
    for i in range(len(result)):
        print(f"{result[i].Pid}\t\t    {result[i].ArrTime}\t\t    {result[i].BT}\t\t    {result[i].WT}\t\t    {result[i].TT}\t\t    {'%0.2f' % float(result[i].TT/result[i].BT)}")

    print(f'enterTime : {pc.getEnterTimeList()}')  # 진입, 종료는 시간단위라서 0 ~ 20 임
    # 시퀸스는 시작 시점부터 종료하기 전 시간까지만 기록함(버스트 타임이랑 비교해서 확인하면 된다.)
    print(f'Sequence  :   {pc.getSequenceList()}')
    print(f'endTime   : {pc.getEndTimeList()}')
    # Sequence, endTime 초기화 : 다른 함수들과 데이터 충돌 방지
    pc.sequenceList = []
    pc.EndTimeList = [0] * (pc.total_Time + 1)



if __name__ == '__main__':

    app = QApplication(sys.argv)
    Window = MainWindow()
    Window.show()
    sys.exit(app.exec_())
