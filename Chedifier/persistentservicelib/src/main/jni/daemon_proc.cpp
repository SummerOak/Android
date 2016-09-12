#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

#include "log.h"
#include "common.h"

char* pkgName;
char* svcName;

class A{
public:
	~A(){
		LOGD("~A called");
		startTargetService(pkgName,svcName);
	}
};

int main(int argc, char* argv[]) {

	LOGD(">>>daemon_proc started!");

	atexit(atexit_);

	pkgName = argv[1];
	svcName = argv[2];
	char* selfL = argv[3];
	char* otherL = argv[4];
	char* selfT = argv[5];
	char* otherT = argv[6];

	LOGD("main selfL %s otherL %s selfT %s otherT %s ",selfL,otherL,selfT,otherT);

	A a;

	startTargetService(pkgName,svcName);

//	trace(getppid());

	int lock_status = 0;
	int try_time = 0;
	while(try_time < 3 && !(lock_status = lock_file(selfL))){
		try_time++;
		LOGD("host lock file failed and try again as %d times", try_time);
		usleep(10000);
	}
	LOGD("daemon 1111");
	if(!lock_status){
		LOGE("host lock myself failed and exit");
		return -1;
	}

	createSelfAndDelOther(selfT, otherT);

	LOGD("daemon try lock other");
	lock_status = lock_file(otherL);
	LOGD("daemon lock_file return: %d",lock_status);

	if(lock_status){
		LOGE("Watch >>>>HOST<<<<< Daed !!");
		remove(selfT);// it`s important ! to prevent from deadlock

		startTargetService(pkgName,svcName);
	}

	return 0;

}

