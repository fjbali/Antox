@echo off
echo not working yet

mkdir app\src\main\jniLibs\armeabi
mkdir app\src\main\jniLibs\x86
mkdir app\src\main\jniLibs\arm64-v8a

DEL /F /Q app\src\main\jniLibs\armeabi\libtox4j.so
DEL /F /Q app\src\main\jniLibs\x86\libtox4j.so
DEL /F /Q app\src\main\jniLibs\arm64-v8a\libtox4j.so

DEL /F /Q app\src\main\jniLibs\armeabi\libkaliumjni.so
DEL /F /Q app\src\main\jniLibs\x86\libkaliumjni.so
DEL /F /Q app\src\main\jniLibs\arm64-v8a\libkaliumjni.so


mkdir app\libs
DEL /F /Q app\libs\tox4j*.jar
echo Removed old version
echo Downloading latest version ...
powershell -Command "(New-Object Net.WebClient).DownloadFile('https://build.tox.chat/job/tox4j_build_android_armel_release/lastSuccessfulBuild/artifact/artifacts/tox4j_2.11-0.1-SNAPSHOT.jar', 'app\libs\tox4j_2.11.jar')"
echo Downloaded.
echo ...Finished!
pause
@echo ON
