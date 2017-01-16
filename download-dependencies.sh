#!/bin/sh
mkdir -p app/src/main/jniLibs/armeabi
mkdir -p app/src/main/jniLibs/x86
mkdir -p app/src/main/jniLibs/arm64-v8a

rm -f app/src/main/jniLibs/armeabi/libtox4j.so
rm -f app/src/main/jniLibs/x86/libtox4j.so
rm -f app/src/main/jniLibs/arm64-v8a/libtox4j.so

rm -f app/src/main/jniLibs/armeabi/libkaliumjni.so
rm -f app/src/main/jniLibs/x86/libkaliumjni.so
rm -f app/src/main/jniLibs/arm64-v8a/libkaliumjni.so

cd ${0%/*}
mkdir -p app/libs
rm -f app/libs/tox4j_2.11.jar
# wget https://build.tox.chat/job/tox4j_build_android_armel_release/lastSuccessfulBuild/artifact/artifacts/tox4j_2.11-0.1-SNAPSHOT.jar -O app/libs/tox4j_2.11.jar

REPOUSER="zoff99"
REPO="Antox"
BRANCH="z_new_source"

wget 'https://circleci.com/api/v1/project/'"$REPOUSER"'/'"$REPO"'/latest/artifacts/0/$CIRCLE_ARTIFACTS/supplement.zip&branch='"$BRANCH" -O ./supplement.zip
unzip ./suppl.zip

exit 0
