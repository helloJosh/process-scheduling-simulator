########################################
#   운영체제 프로세스 스케줄링 프로그램   #
#   4분반 ㄱㄱㄱ조                      #
#   2014136019 김성찬                   #
#   2016136144 강동균                   #
########################################

from queue import Queue             # 큐 구현 모듈
import numpy as np                  # 간트 차트 구현 모듈
import matplotlib.pyplot as plt     # 간트 차트 구현 모듈 (matplotlib)

global PROCESS_NUM_MAX              # 시스템 최대값
PROCESS_NUM_MAX = 10

# 프로세스별 시간 클래스 (구조체 역할)
class Time:
    def __init__(self):
        self.id = 0
        self.arrivalTime = 0
        self.burstTime = 0
        self.leaveTime = 0
        self.waitingTime = 0
        self.turnaroundTime = 0
        self.endTime = 0
        self.totalTime = 0
        self.normalizedTT = 0
        self.responseRatio = 0

global nt                           # 프로세스 배열
nt = list()
for i in range(PROCESS_NUM_MAX):    # 배열 초기화
    nt.append(0)
    nt[i] = Time()

global dopyo_id         # 간트 차트에 출력할 값들의 id 리스트
dopyo_id = list()
global dopyo_length     # 간트 차트에 출력할 값들의 길이 리스트
dopyo_length = list()

def FCFS(num):
    p_num = 0           # 종료된 프로세스 개수
    p_id = 0            # 실행 중인 프로세스
    startTime = 0       # 해당 프로세스 시작 시간

    temp = Time()
    ready = Queue(num)
    
    initInformation()

    while True:
        # 진행 중 도착한 프로세스 enqueue
        for i in range(num):
            if i != p_num and startTime < nt[i].arrivalTime and startTime + nt[p_id].burstTime >= nt[i].arrivalTime:
                ready.put(nt[i])

        # 현재 프로세스 WT, TAT, NTT 계산
        nt[p_id].endTime = startTime + nt[p_id].burstTime
        nt[p_id].waitingTime = startTime - nt[p_id].arrivalTime
        nt[p_id].turnaroundTime = nt[p_id].waitingTime + nt[p_id].burstTime
        nt[p_id].normalizedTT = nt[p_id].turnaroundTime / nt[p_id].burstTime

        # 간트 차트에 출력할 값을 리스트에 저장
        dopyo_id.append(p_id)
        dopyo_length.append(nt[p_id].burstTime)

	    # 현재 프로세스에 대한 FCFS 종료
        p_num = p_num + 1
        if p_num == num:
            break

        # 프로세스 종료 후 다음 프로세스 실행
        startTime = nt[p_id].endTime
        temp = ready.get()
        p_id = temp.id

def RR(num):
    p_num = 0           # 종료된 프로세스 개수
    p_id = 0            # 실행 중인 프로세스
    startTime = 0       # 해당 프로세스 시작 시간
    timeQuantum = eval(input("사용 제한 시간: "))

    temp = Time()
    ready = Queue(num)
    
    initInformation()
    
    while True:
        # 간트 차트에 출력할 값의 id 저장
        dopyo_id.append(p_id)

        if nt[p_id].leaveTime > timeQuantum:
            # 정상 실행
            for i in range(PROCESS_NUM_MAX):
                # 실행 중 도착한 프로세스 enqueue
                if p_id != i and (nt[i].arrivalTime > startTime and nt[i].arrivalTime <= startTime + timeQuantum):
                    ready.put(nt[i])
                    #print("id : [", p_id + 1, "] , leaveTime : [", nt[p_id].leaveTime, "]")

            # 간트 차트에 출력할 값을 리스트에 저장
            dopyo_length.append(timeQuantum)

            # 현재 실행 프로세스 문맥교환
            startTime += timeQuantum
            nt[p_id].leaveTime -= timeQuantum
            ready.put(nt[p_id])
        elif nt[p_id].leaveTime <= timeQuantum:
            # 종료를 위한 실행
            for i in range(PROCESS_NUM_MAX):
                # 실행 중 도착한 프로세스 enqueue
                if p_id != i and (nt[i].arrivalTime > startTime and nt[i].arrivalTime <= startTime + nt[p_id].leaveTime):
                    ready.put(nt[i])

            if nt[p_id].leaveTime == timeQuantum:
                startTime += timeQuantum
            else:
                startTime += nt[p_id].leaveTime

            # 간트 차트에 출력할 값을 리스트에 저장
            dopyo_length.append(nt[p_id].leaveTime)

            nt[p_id].leaveTime = 0          # 해당 프로세스 exit
            nt[p_id].endTime = startTime    # 종료시간 저장
            p_num = p_num + 1

            # 현재 프로세스 WT, TAT, NTT 계산
            nt[p_id].totalTime = nt[p_id].endTime - nt[p_id].arrivalTime
            nt[p_id].waitingTime = nt[p_id].totalTime - nt[p_id].burstTime
            nt[p_id].turnaroundTime = nt[p_id].waitingTime + nt[p_id].burstTime
            nt[p_id].normalizedTT = nt[p_id].turnaroundTime / nt[p_id].burstTime

            # 종료 조건
            if p_num == 5:
                break

        # 다음 실행될 프로세스 dequeue
        temp = ready.get()
        p_id = temp.id

def SPN(num):
    p_num = 0           # 종료된 프로세스 개수
    p_id = 0            # 실행 중인 프로세스
    startTime = 0       # 해당 프로세스 시작 시간
    count = 0           # 대기 중인 프로세스 개수

    temp = Time()
    ready = Queue(num)

    s_temp = list()                     # 정렬하기 위한 배열
    for i in range(PROCESS_NUM_MAX):    # 배열 초기화
        s_temp.append(0)
        s_temp[i] = Time()
    
    initInformation()

    while True:
        # 간트 차트에 출력할 값의 id 저장
        dopyo_id.append(p_id)
        
        # 진행 중 도착한 프로세스 enqueue
        for i in range(num):
            if nt[i].leaveTime != 0 and i != p_id and (startTime < nt[i].arrivalTime and startTime + nt[p_id].burstTime >= nt[i].arrivalTime):
                ready.put(nt[i])
                count = count + 1
        
        # 실행 시간 기준으로 정렬
        for i in range(count):
            s_temp[i] = ready.get()
        for i in range(count):
            for j in range(count-1):
                if s_temp[j].burstTime > s_temp[j+1].burstTime: 
                    s_temp[j+1], s_temp[j] = s_temp[j], s_temp[j+1]     # 두 원소 교체
        for i in range(count):
            ready.put(s_temp[i])

        # 간트 차트에 출력할 값을 리스트에 저장
        dopyo_length.append(nt[p_id].burstTime)

        # 현재 프로세스 종료
        nt[p_id].leaveTime = 0
        
        # 현재 프로세스 WT, TAT, NTT 계산
        nt[p_id].waitingTime = startTime - nt[p_id].arrivalTime
        nt[p_id].turnaroundTime = nt[p_id].burstTime + nt[p_id].waitingTime
        nt[p_id].normalizedTT = nt[p_id].turnaroundTime / nt[p_id].burstTime

        # 종료 조건: 프로세스가 n개 종료되는 순간
        p_num = p_num + 1

        if p_num == num:
            break

        # dequeue 다음 프로세스 지정
        startTime += nt[p_id].burstTime
        temp = ready.get()
        p_id = temp.id
        count = 0

def SRTN(num):
    p_id = 0            # 실행 중인 프로세스
    count = 0           # 대기 중인 프로세스 개수
    sec = 0
    totalBurstTime = 0  # 모든 프로세스의 실행 시간 합

    temp = Time()
    ready = Queue(num)

    s_temp = list()                     # 정렬하기 위한 배열
    for i in range(PROCESS_NUM_MAX):    # 배열 초기화
        s_temp.append(0)
        s_temp[i] = Time()
    
    initInformation()

    for i in range(num):
        totalBurstTime += nt[i].burstTime

    while True:
        # 간트 차트에 출력할 값의 id 저장
        dopyo_id.append(p_id)
        
        sec = sec + 1
        #print(nt[p_id].leaveTime)
        nt[p_id].leaveTime = nt[p_id].leaveTime - 1

        # 현재 프로세스 종료
        if nt[p_id].leaveTime == 0:
            # 각 시간 구하기
            nt[p_id].endTime = sec

            nt[p_id].waitingTime = nt[p_id].endTime - nt[p_id].arrivalTime - nt[p_id].burstTime
            nt[p_id].turnaroundTime = nt[p_id].waitingTime + nt[p_id].burstTime
            nt[p_id].normalizedTT = nt[p_id].turnaroundTime / nt[p_id].burstTime

        for i in range(num):
            # 기존 프로세스 계속 진행
            if sec == nt[i].arrivalTime and nt[p_id].leaveTime < nt[i].leaveTime:
                ready.put(nt[i])
                count = count + 1
            elif sec == nt[i].arrivalTime and nt[p_id].leaveTime > nt[i].leaveTime:
                ready.put(nt[p_id])
                count = count + 1
                p_id = i

            # 간트 차트에 출력할 값을 리스트에 저장
            dopyo_length.append(1)

        # 종료 조건
        if sec == totalBurstTime:
            compressListForSRTN()
            break

        # 남는 시간으로 정렬
        for i in range(count):
            s_temp[i] = ready.get()
        for i in range(count):
            for j in range(count-1):
                if s_temp[j].leaveTime > s_temp[j+1].leaveTime: 
                    s_temp[j+1], s_temp[j] = s_temp[j], s_temp[j+1]     # 두 원소 교체
        for i in range(count):
            ready.put(s_temp[i])

        # 기존 프로세스를 enqueue하고 도착한 프로세스 실행
        if nt[p_id].leaveTime == 0:
            temp = ready.get()
            count = count - 1
            p_id = temp.id

def HRRN(num):
    p_num = 0           # 종료된 프로세스 개수
    p_id = 0            # 실행 중인 프로세스
    startTime = 0       # 해당 프로세스 시작 시간
    count = 0           # 대기 중인 프로세스 개수

    temp = Time()
    ready = Queue(num)

    s_temp = list()                     # 정렬하기 위한 배열
    for i in range(PROCESS_NUM_MAX):    # 배열 초기화
        s_temp.append(0)
        s_temp[i] = Time()
    
    initInformation()

    while True:
        # 간트 차트에 출력할 값의 id 저장
        dopyo_id.append(p_id)

        # 진행 중 도착한 프로세스 enqueue
        for i in range(num):
            if nt[i].leaveTime != 0 and i != p_id and (startTime < nt[i].arrivalTime and startTime + nt[p_id].burstTime >= nt[i].arrivalTime):
                ready.put(nt[i])
                count = count + 1

        # 현재 프로세스 종료
        nt[p_id].leaveTime = 0

        # 각 시간 구하기
        nt[p_id].waitingTime = startTime - nt[p_id].arrivalTime
        nt[p_id].turnaroundTime = nt[p_id].burstTime + nt[p_id].waitingTime
        nt[p_id].normalizedTT = nt[p_id].turnaroundTime / nt[p_id].burstTime

        # 간트 차트에 출력할 값을 리스트에 저장
        dopyo_length.append(nt[p_id].burstTime)

        # 종료 조건: 프로세스가 n개 종료될 때
        p_num = p_num + 1
        if p_num == num:
            break

        # 응답을 기준으로 정렬
        for i in range(count):
            s_temp[i] = ready.get()
        for i in range(count):
            s_temp[i].waitingTime = startTime + nt[p_id].burstTime - s_temp[i].arrivalTime
            s_temp[i].responseRatio = (s_temp[i].waitingTime + s_temp[i].burstTime) / s_temp[i].burstTime
        for i in range(count):
            for j in range(count-1):
                if s_temp[j].responseRatio < s_temp[j+1].responseRatio: 
                    s_temp[j+1], s_temp[j] = s_temp[j], s_temp[j+1]     # 두 원소 교체
        for i in range(count):
            ready.put(s_temp[i])

        # dequeue한 프로세스 p_id 저장
        startTime += nt[p_id].burstTime
        temp = ready.get()
        count = count - 1
        p_id = temp.id

# 콘솔 창에 테이블 출력
def printTable(num, sel):
    if sel == 1:
        print("{:-^92s}".format(" FCFS "))
    elif sel == 2:
        print("{:-^92s}".format(" RR "))
    elif sel == 3:
        print("{:-^92s}".format(" SPN "))
    elif sel == 4:
        print("{:-^92s}".format(" SRTN "))
    else:
        print("{:-^92s}".format(" HRRN "))
    print()

    print("Process ID    Arrival Time    Burst Time\
    Waiting Time    Turnaround Time    Normalized TT")
    for i in range(num):
        print("P[", i+1, "]            ", "{:2d}".format(nt[i].arrivalTime), 
        "            ", "{:2d}".format(nt[i].burstTime), 
        "            ", "{:2d}".format(nt[i].waitingTime), 
        "            ", "{:2d}".format(nt[i].turnaroundTime), 
        "            ", "{:2.2f}".format(nt[i].normalizedTT))
    
    print("{:-^92s}".format(""))
    
    showGante(sel)

# 간트 차트 출력
def showGante(sel):
    if sel == 1:
        selectedPS = "FCFS"
    elif sel == 2:
        selectedPS = "RR"
    elif sel == 3:
        selectedPS = "SPN"
    elif sel == 4:
        selectedPS = "SRTN"
    else:
        selectedPS = "HRRN"
    graph = [selectedPS]        # 메인 그래프
    y_pos = np.arange(len(graph))

    fig = plt.figure(figsize=(10,2))
    ax = fig.add_subplot(111)

    colors = ['r', 'g', 'b', 'm', 'y', 'c', 'orange', 'purple', 'brown', 'lime']
    patch_handles = []
    left = np.zeros(len(graph))     # 왼쪽으로 차트 정렬, 0부터 시작
    
    # 간트 차트 구현
    for i, d in enumerate(dopyo_length):
        selectedColor = colors[dopyo_id[i]]
        patch_handles.append(ax.barh(y_pos, d, 
            color = selectedColor, align = 'center', 
            left = left))
        left += d
    
    for j in range(len(patch_handles)):
        for i, patch in enumerate(patch_handles[j].get_children()):
            bl = patch.get_xy()
            x = 0.5 * patch.get_width() + bl[0]
            y = 0.4 * patch.get_height() + bl[1]
            ax.text(x,y, "P[%d]\n\n%d" % (dopyo_id[j]+1, dopyo_length[j]), ha='center')

    ax.set_yticks(y_pos)
    ax.set_yticklabels(graph)
    ax.set_xlabel('Time')

    plt.title("Process Scheduling - " + selectedPS)
    plt.show()          # 간트 차트 출력

# 도착 시간 및 실행 시간 초기화
def inputTime():
    nt[0].arrivalTime = 0
    nt[0].burstTime = 3
    nt[1].arrivalTime = 1
    nt[1].burstTime = 7
    nt[2].arrivalTime = 3
    nt[2].burstTime = 2
    nt[3].arrivalTime = 5
    nt[3].burstTime = 5
    nt[4].arrivalTime = 6
    nt[4].burstTime = 3

# 배열 식별자 등록, 남은 시간, 전체 시간 초기화
def initInformation():
    for i in range(PROCESS_NUM_MAX):
        nt[i].id = i
        nt[i].leaveTime = nt[i].burstTime
        nt[i].totalTime = 0

# 간트 차트 표시용 리스트 초기화
def listInit():
    while len(dopyo_id) != 0:
        dopyo_id.pop()
    while len(dopyo_length) != 0:
        dopyo_length.pop()

# 간트 차트 표시용 리스트 값 압축 메소드(SRTN용)
def compressListForSRTN():
    temp_id = list()        # 임시 dopyo_id
    temp_length = list()    # 임시 dopyo_length
    temp_count = 0

    for i in range(len(dopyo_id)-1):
        temp_count = temp_count + 1
        if dopyo_id[i] != dopyo_id[i+1]:        # id의 값이 다음 원소와 다르다면
            temp_id.append(dopyo_id[i])
            temp_length.append(temp_count)
            temp_count = 0
        if i == len(dopyo_id)-2:                # 마지막 바로 이전 원소일 경우
            temp_id.append(dopyo_id[i+1])
            if dopyo_id[i] != dopyo_id[i+1]:    # 다음 원소와 id가 같지 않으면
                temp_length.append(1)
            else:                               # 다음 원소와 id가 같으면
                temp_length.append(temp_count+1)
            
    listInit()                                  # 원래 두 리스트 초기화
    for i in range(len(temp_id)):               # 임시 저장된 리스트의 원소 값을 초기화된 두 리스트에 저장
        dopyo_id.append(temp_id[i])
    for i in range(len(temp_length)):
        dopyo_length.append(temp_length[i])

# 메인 메뉴
def menu():
    numOfProcess = 5    # 프로세스 개수 (사용자가 입력)
    
    title()             # 타이틀 화면 표시
    inputTime()         # 도착 시간 및 실행 시간 초기화

    while True:
        print("1 - FCFS (First-Come, First-Served)")
        print("2 - RR (Round-Robin)")
        print("3 - SPN (Shortest Process Next)")
        print("4 - SRTN (Shortest Remaining Time Next)")
        print("5 - HRRN (High Response Ratio Next)")
        print("6 - 프로세스 개수 변경 (최대 " + str(PROCESS_NUM_MAX) + "개, 현재 프로세스 개수: " + str(numOfProcess) + "개)")
        print("7 - 프로세스별 도착 시간 및 실행 시간 변경")
        print("0 - 종료")
        select = eval(input("-> "))

        if select == 0:     # 프로그램 종료
            break
        elif select == 6:   # 프로세스 개수 변경
            inputProcess = eval(input("프로세스 개수를 입력하세요: "))
            if inputProcess > PROCESS_NUM_MAX:
                print("최대 프로세스 개수를 넘었습니다.")
            else:
                numOfProcess = inputProcess
        elif select == 7:   # 프로세스별 도착 시간 및 실행 시간 변경
            for i in range(numOfProcess):
                nt[i].arrivalTime = eval(input("P[" + str(i+1) + "] 도착 시간 (Arrival Time): "))
                nt[i].burstTime = eval(input("P[" + str(i+1) + "] 실행 시간 (Burst Time): "))
        elif 1 <= select <= 5:
            listInit()

            if select == 1:         # FCFS
                FCFS(numOfProcess)
            elif select == 2:       # RR
                RR(numOfProcess)
            elif select == 3:       # SPN
                SPN(numOfProcess)
            elif select == 4:       # SRTN
                SRTN(numOfProcess)
            else:                   # HRRN
                HRRN(numOfProcess)

            printTable(numOfProcess, select)
        else:                   # 잘못된 커맨드 입력 시
            print("올바른 커맨드가 아닙니다.")

        print()

# 타이틀 표시용
def title():
    print("{:=^92s}".format(""))
    print("{:/^92s}".format(""))
    print("{:/^92s}".format("    Process Scheduling on Python 3.6    "))
    print("{:/^92s}".format(""))
    print("{:=^92s}".format(""))
    print()
    print("{:^77s}".format("운영체제 4분반 팀프로젝트 ㄱㄱㄱ조"))
    print("{:^86s}".format("2014136019 김성찬                2016136144 강동균"))
    print()

# 프로그램 실행
menu()
