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
    result = pc.RR()

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


Test()
