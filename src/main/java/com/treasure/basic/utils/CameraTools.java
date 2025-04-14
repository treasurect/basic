/*
 * *****************************************************
 * Copyright (c) 2018. EEO.All Rights Reserved.
 * Author: Xuechao Suo
 * Email: xuechao.suo@eeoa.com
 * ****************************************************
 */

package com.treasure.basic.utils;

import android.hardware.Camera;
import android.os.Build;

import com.treasure.basic.logger.CrashReport;

/**
 * 相机相关工具
 * Create By Suo Xuechao on 2018/12/29
 */
public class CameraTools {

  /**
   * 检测是否有摄像头
   */
  public static boolean hasCamera() {
    return hasBackFacingCamera() || hasFrontFacingCamera();
  }

  /**
   * 检测是否有后置摄像头
   */
  public static boolean hasBackFacingCamera() {
    final int CAMERA_FACING_BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
    return checkCameraFacing(CAMERA_FACING_BACK);
  }

  /**
   * 检测是否有前置摄像头
   */
  public static boolean hasFrontFacingCamera() {
    final int CAMERA_FACING_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;
    return checkCameraFacing(CAMERA_FACING_FRONT);
  }

  public static boolean hasMultiCamera() {
    return Camera.getNumberOfCameras() > 1;
  }

  /**
   * 检测相机位
   */
  private static boolean checkCameraFacing(final int facing) {
    if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
      return false;
    }

    final int cameraCount = Camera.getNumberOfCameras();
    Camera.CameraInfo info = new Camera.CameraInfo();
    for (int i = 0; i < cameraCount; i++) {
      Camera.getCameraInfo(i, info);
      if (facing == info.facing) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断摄像头是否可用
   */
  public static boolean isCameraCanUse() {
    boolean canUse = false;
    Camera mCamera = null;
    try {
      mCamera = Camera.open();
      canUse = true;
    } catch (Exception ignored) {
      CrashReport.INSTANCE.postException(ignored);
    }
    if (canUse) {
      mCamera.release();
      mCamera = null;
    }
    return false;
  }

  private static int getSdkVersion() {
    return Build.VERSION.SDK_INT;
  }
}
