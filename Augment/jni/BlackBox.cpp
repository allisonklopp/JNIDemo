#include <jni.h>
#include <string>

using namespace std;

extern "C" {
JNIEXPORT jobjectArray JNICALL Java_com_aklopp_augment_MainActivity_getList(JNIEnv *env)
{
	jobjectArray ret;
	int i;

	char *message[15]=
	{		"Eagle",
			"Swan",
			"Dove",
			"Pigeon",
			"Falcon",
			"Blackbird",
			"Robin",
			"Duck",
			"Goose",
			"Owl",
			"Osprey",
			"Puffin",
			"Flamingo",
			"Sparrow",
			"Vulture"};

	ret= (jobjectArray)env->NewObjectArray(15,env->FindClass("java/lang/String"),env->NewStringUTF(""));

	for(i=0;i<15;i++) {
		env->SetObjectArrayElement(ret,i,env->NewStringUTF(message[i]));
	}
	return(ret);
}
}
