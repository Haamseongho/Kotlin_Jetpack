package com.example.musicplayer

import CHANNEL_ID
import MEDIA_PLAYER_PAUSE
import MEDIA_PLAYER_PLAY
import MEDIA_PLAYER_STOP
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi

class MediaPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val lowBatteryReceiver : LowBatteryReceiver ?= null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object;

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        // 1. 채널 먼저 만들기
        createNotificationChannel()
        initReceiver()
        // 2. Notification 추가하기 - notificationBuilder
        // 3.
        val playIcon = Icon.createWithResource(baseContext, R.drawable.baseline_arrow_right_24)
        val pauseIcon = Icon.createWithResource(baseContext, R.drawable.baseline_backspace_24)
        val stopIcon = Icon.createWithResource(baseContext, R.drawable.baseline_airline_stops_24)
        val pausePendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_PAUSE
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        val stopPendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_STOP
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        // 액티비티 이동은 getActivity
        val mainPendingIntent = PendingIntent.getActivity(
            baseContext,
            0, Intent(baseContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        // 서비스로 하는건 GetService
        val playPendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_PLAY
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(baseContext, CHANNEL_ID)
            .setStyle(
                Notification.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            ).setVisibility(Notification.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .addAction(
                Notification.Action.Builder(
                    pauseIcon,
                    "pause",
                    pausePendingIntent
                    // icon, pendingIntent
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    stopIcon,
                    "stop",
                    stopPendingIntent
                    // icon, pendingIntent
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    playIcon,
                    "play",
                    playPendingIntent
                    // icon, pendingIntent
                ).build()
            )
            // Notification 눌렀을때 하는 행동
            .setContentIntent(mainPendingIntent)
            .setContentTitle("음악재생")
            .setContentText("음원이 재생중 입니다.").build()


        startForeground(100, notification)
    }

    private fun initReceiver() {
        // IntentFilter 필요
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        // 등록할 때 리시버 달고 인텐트 필터도 달 것
        registerReceiver(lowBatteryReceiver, filter)
    }

    private fun createNotificationChannel() {
        // channel ID 필요
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(CHANNEL_ID, "MEDIA_PLAYER", NotificationManager.IMPORTANCE_DEFAULT)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val notificationManager = baseContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MEDIA_PLAYER_PLAY -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(baseContext, R.raw.cheer)
                }
                mediaPlayer?.start()
            }

            MEDIA_PLAYER_PAUSE -> {
                mediaPlayer?.pause()
            }

            MEDIA_PLAYER_STOP -> {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                stopSelf() // 서비스 종료 (백그라운드에서 서비스가 메모리를 차지하고 있는지 모르기 떄문에 종료가 필요)
            }
        }
        // 리턴값 인티저
        /*
        START_NOT_STICKY
        START_STICKY
        START_REDELIVER_INTENT
         */
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null

        unregisterReceiver(lowBatteryReceiver)  // 종료될 때 리시버 해제
        super.onDestroy()
    }
}