# VMaiL

Let your mail speak

VMaiL is the next-generation Application, which targets providing voice interaction with an email. 

VMaiL provides Login, Check Inbox, Read message, Delete the message and compose an email.

VMaiL Project is based on [VMaiL](https://gitlab.com/teamsixer/vmail). We separated the project because we want the developer team to pay attention to codes and provide a separate code repository to make the code structure clearer for users.

This project is the release software for module Software Engineering Group Project ([COMP2043 UNNC](https://moodle.nottingham.ac.uk/course/view.php?id=101776)) (FCH1 20-21).

Detailed documentation can be found in all classes. Change history, paired coding, proper use of version control software can be found in this project.



## 1. Features

**Reliable**: tested with CI(Currently, we run out of free CI time. Sponsor this project).

**Out of Box**: easy to use, voice is enough.

**Pair Programming**: This project development has strictly followed the proper Pair-Programming Development. 

**Extensible**: Easy for developers to achieve more functions based on the voice interaction framework. See Developer Guide section.



## 2. Getting Started

VMaiL App only works on the **Physical Android Phone** (No support for emulator). The reason for this is that IFLYTEK API only support ARM architecture in Android Phone.



There are two ways to install VMaiL on the **Physical Android Phone**.

1. The easier way to run VMaiL Application is to install [APK](https://gitlab.com/teamsixer/vmailandroid/-/blob/master/app-release.apk) provided in this project.
2. Alternative way is to **git clone** this project and run this project in **Android Studio**. Make sure folloMake sure **Physcial Android Phone** connected to Computer is used to run this App. 

Please Check the **Prerequisites Section** to make sure all requirements have been satisfied. The prerequisites Section will introduce prerequisites for two installation ways separately.



### 2.1 Install VMaiL Via APK

#### 2.1.1 Prerequisites For APK Installation

1. Device: Only works on the **Physical Android Device**.
2. Android Version: The project is developed and tested in **Android 11 (Minimum Android 7.0)**. Please check whether your phone has set up the prerequisite environment by you run this project.

Once you have these prerequisites, you may go to the installation section.

#### 2.1.2 Installation For APK

If you have already had this project **as a Zip file**, you may find app-release.apk in this zip file.

Follow the APK link: [https://gitlab.com/teamsixer/vmailandroid/-/blob/master/app-release.apk](https://gitlab.com/teamsixer/vmailandroid/-/blob/master/app-release.apk) to download APK and directly install it on Android Device. 

Open APK `app-realse.apk` in a Physical Android device. Follow the installation procedure in Android.









### 2.2 Install VMaiL Via Source Code

#### 2.2.1 Prerequisites For Building Source Code.

Firstly, installing VMaiL via source code requires **a laptop** to build source code and **a physical android device** to run VMaiL.

Android Device Requirements:

1. Device: Only works on the **Physical Android Device**.
2. Android Version: The project is developed and tested in **Android 11 (Minimum Android 7.0)**. Please check whether your phone has set up the prerequisite environment by you run this project.

Laptop Requirements:

1. Install [Android Studio](https://developer.android.com/studio) on your laptop for building source code.
   1. The installation procedure will install Android SDK for your laptop. You may choose custom installation to remove virtual devices , increasing download speed,  and just download SDK Platform 30 (Android 11).
   2. Make sure SDK 30 installation is sucessful. You may check Android SDK Manager in Android Studio.
2. Gradle: 
   1. Gralde should be installed in the procedure of installing Android Studio. If not, follow the guidance poping up from Android Studio.
   2. Make sure Gradle is installed on your laptop. At least 6.5+ version of Gradle. Plugin Gradle in Android Studio should at least 4.1.0+.
3. At least Java 8 SDK is installed.




#### 2.2.2 Installation For Source Code

1. Open `build.gradle app` in App Module and click synchronize to install all dependencies for this project.
2. Using USB to connect your physical Android device to your laptop.
   1. Android Device should in the state of developer mode.
   2. Make sure your Android device trust your laptop.
   3. Make sure your Android device connected to network.
3. Change the running device to your physical Android device. If you are not clear about how to select your device to run, [follow this link to run on real device](https://developer.android.com/training/basics/firstapp/running-app#RealDevice).
4. Click Run on Android Studio.
5. After a while, VMaiL should be installed in your Android device.



If something goes wrong,

Please doulbe check Gradle Version, SDK version, Java Version and finally install all dependencies written in `build.gralde app`.

## 3. User Manual

[Click to see detail User Manual](/docs/UserManual.md)



## 4. Developer Guide

If you are interested in our project, you may see [developer guide](/docs/DeveloperGuide.md) to join the maintenance with our project.

Check and see how codes are organized.



## 5. Notes

Our software uses the free version of Auto speech recognition (ASR) API from IFLYTEK, with only 500 requests available each day. Moreover, the voice wake-up API can only be installed on ten phones(six left).



## 6. Feedback

- If something is not working, [create an issue](https://gitlab.com/teamsixer/vmail/-/issues/new).
- If feature wanted,  [create an issue](https://gitlab.com/teamsixer/vmail/-/issues/new).



## 7. License

[MIT](https://gitlab.com/teamsixer/vmailandroid/-/blob/master/LICENSE)



