REPOSITORY=/home/ubuntu/soolo-serv/build/libs
cd $REPOSITORY

APP_NAME=soolo-serv
JAR_NAME=$(ls $REPOSITORY | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$JAR_NAME

# JAR 파일에 실행 권한 설정
sudo chmod +x "$JAR_PATH"

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &




#
#BASE_PATH="/home/ubuntu/soolo-serv
#BUILD_PATH=$(ls $BASE_PATH/build/libs/*.jar)
#JAR_NAME=$(basename "$BUILD_PATH")
#
#cd $BASE_PATH
#
#APP_NAME="soolo-serv"
#JAR_PATH="$BASE_PATH/$JAR_NAME"
#
#sudo chmod +x "$JAR_PATH"
#
#CURRENT_PID=$(pgrep -f $APP_NAME)
#
#if [ -z "$CURRENT_PID" ]; then
#  echo "> No process to stop."
#else
#  echo "> kill -9 $CURRENT_PID"
#  kill -15 "$CURRENT_PID"
#  sleep 5
#fi
#
#echo "> Deploying $JAR_PATH"
#nohup java -jar "$JAR_PATH" > /dev/null 2> /dev/null < /dev/null &
