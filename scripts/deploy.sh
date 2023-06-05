REPOSITORY=/home/ubuntu/soolo-serv

echo "> 현재 Pid 확인"

cd $REPOSITORY

APP_NAME=soolo-serv

JAR_NAME=$(ls $REPOSITORY/ | grep 'SNAPSHOT.jar' | tail -n 1)

JAR_PATH=$REPOSITORY/$JAR_NAME

JAR 파일에 실행 권한 설정
sudo chmod +x $JAR_PATH
sudo chmod +w $JAR_PATH

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

sudo chmod +x $JAR_NAME
echo "> $JAR_PATH 배포"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &

# 스크립트 종료 후 변경 사항이 저장되는지 확인
sleep 10  # 충분한 대기 시간을 주어 변경 사항 저장 확인

echo "> 배포 후 변경 사항 저장 완료"
