# EasyDine Android App - Installation Guide

## Overview
EasyDine is a restaurant management system that helps customers browse menus, place orders, and track their order status. This guide explains how to install the EasyDine Android application.

## Installation Methods

### Method 1: Direct APK Installation

The easiest way to install the EasyDine app on your Android device is by downloading and installing the APK file directly.

#### Step 1: Download the APK

Download the APK file from Google Drive:
- [EasyDine APK Download Link](https://drive.google.com/file/d/1MxcHTDClST8RygbamOhMsCM6FkRcgXfe/view?usp=sharing)

#### Step 2: Install the APK

1. Locate the downloaded APK file in your device's storage
2. Tap on the file to begin installation
3. If prompted, allow installation from unknown sources:
   - Navigate to Settings > Security > Unknown Sources (or Settings > Apps > Special access > Install unknown apps)
   - Enable the permission for your file manager or browser
4. Follow the on-screen instructions to complete the installation

### Method 2: Build from Source Using Android Studio

For developers who want to build the app from source code:

#### Step 1: Clone the Android Repository

Clone the Android app repository from GitHub:

```bash
git clone https://github.com/vvthangdev/easydine-android
cd easydine-android
```

#### Step 2: Build and Run with Android Studio

1. Install [Android Studio](https://developer.android.com/studio) if you haven't already
2. Open Android Studio and select "Open an existing project"
3. Navigate to and select the cloned "easydine-android" directory
4. Wait for Android Studio to sync the project and download necessary dependencies
5. Connect an Android device via USB (with USB debugging enabled) or set up an Android emulator
6. Click the "Run" button (green triangle) in the toolbar to build and install the app on your device

## App Screenshots

Here are some screenshots showcasing the EasyDine mobile app:

### Login Screen
![image](https://github.com/user-attachments/assets/e66e532d-2c12-4ffc-83a9-6a1b24410c90)

*The authentication screen where users can sign in to their accounts*

### Food Menu
![image](https://github.com/user-attachments/assets/a50fe022-83c7-412e-9883-03d7994955af)

*Browse through available dishes and add them to cart*

### Order Infomation
![image](https://github.com/user-attachments/assets/02605fb3-9de8-4c7f-986d-5cf9dc390473)

*View cart infomation*

### User Profile
![image](https://github.com/user-attachments/assets/576e66ab-7b34-4f51-86fc-5abd2370424d)

*Manage account information and preferences*

## Requirements

- Android 6.0 (Marshmallow) or higher
- Internet connection for ordering features
- Location services (optional) for delivery features

## Troubleshooting

If you encounter any issues during installation:
- Make sure you've enabled installation from unknown sources
- Check that you have sufficient storage space on your device
- Ensure your Android version is compatible (Android 6.0+)

For additional support, please contact the development team or open an issue on the GitHub repository.
