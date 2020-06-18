package com.uuabc.classroomlib.classroom

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ScreenUtils
import com.uuabc.classroomlib.R
import com.uuabc.classroomlib.RoomApplication
import com.uuabc.classroomlib.RoomConstant
import com.uuabc.classroomlib.common.RxTimer
import com.uuabc.classroomlib.observer.VolumeChangeObserver
import com.uuabc.classroomlib.tvdelivery.managers.PeersManager
import com.uuabc.classroomlib.tvdelivery.tasks.WebSocketTask
import com.uuabc.classroomlib.tvdelivery.ui.RemoteParticipant
import com.uuabc.classroomlib.utils.UtilsBigDecimal
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_monitor_room.*
import org.webrtc.AudioTrack
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.VideoRenderer

class MonitorRoomActivity : BaseClassRoomActivity(), VolumeChangeObserver.VolumeChangeListener {
    private var remoteRenderer: VideoRenderer? = null
    private var peersManager: PeersManager? = null
    private var webSocketTask: WebSocketTask? = null
    private var stayInDualTimer = RxTimer()
    private var isIceConnect: Boolean = false
    private var firstOnResume = true
    private var audioTrack: AudioTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.setFullScreen(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.setBackgroundDrawable(null)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setContentView(R.layout.activity_monitor_room)

        this.peersManager = PeersManager(this)
        tvTilte.text = intent.getStringExtra("title")

        initViews()
        doIceDisConnet()
    }

    override fun onResume() {
        super.onResume()
        interval()
        if (firstOnResume) {
            firstOnResume = false
            return
        }
        doIceDisConnet()
    }

    override fun onPause() {
        super.onPause()
        stayInDualTimer.cancel()
    }

    override fun onStop() {
        super.onStop()
        viewsContainer.removeAllViews()
        hangup()
    }

    override fun onDestroy() {
        super.onDestroy()
        hangup()
    }

    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return 667f
    }

    override fun onVolumeChanged(volume: Int) {
        super.onVolumeChanged(volume)
        setVolume()
    }

    private fun setVolume() {
        if (audioTrack != null) {
            val volume = UtilsBigDecimal.getDivValue(musicVoiceCurrent, musicVoiceMax)
            audioTrack?.setVolume(volume)
        }
    }

    private fun initViews() {
        showLoading()
        rlTitle.setOnClickListener {
            finish()
        }

        viewsContainer.setOnClickListener {
            rlTitle.visibility = if (rlTitle.isShown) View.GONE else View.VISIBLE
        }

        ivLoading.imageAssetsFolder = "images/"
    }

    private fun interval() {
        stayInDualTimer.interval((8 * 1000).toLong()) {
            if (isDestroyed || !NetworkUtils.isConnected()) return@interval
            if (!isIceConnect) {
                doIceDisConnet()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun doIceDisConnet() {
        showLoading()
        Observable.just("reInterRoom")
                .subscribeOn(Schedulers.io())
                .doOnNext { hangup() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ startPlay() }, { })
    }

    private fun startPlay() {
        peersManager!!.start()
        createLocalSocket()
    }

    private fun createLocalSocket() {
        webSocketTask = WebSocketTask(this, peersManager, RoomApplication.getInstance().openviduSocketUrl).execute(this) as WebSocketTask
    }

    fun onIceConnectionChange(iceConnectionState: PeerConnection.IceConnectionState) {
        mMainHandler.post {
            if (isDestroyed) return@post
            if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED || iceConnectionState == PeerConnection.IceConnectionState.FAILED) {
                showLoading()
                isIceConnect = false
                return@post
            }

            if (iceConnectionState == PeerConnection.IceConnectionState.CONNECTED || iceConnectionState == PeerConnection.IceConnectionState.COMPLETED) {
                isIceConnect = true
            }
        }
    }

    fun gotRemoteStream(stream: MediaStream, remoteParticipant: RemoteParticipant) {
        if (isDestroyed) return
        if (stream.videoTracks != null && stream.videoTracks.size > 0) {
            val videoTrack = stream.videoTracks[0]
            runOnUiThread {
                remoteRenderer = VideoRenderer(remoteParticipant.videoView)

                videoTrack.addRenderer(remoteRenderer!!)
                val mediaStream = peersManager!!.peerConnectionFactory.createLocalMediaStream("105")
                remoteParticipant.mediaStream = mediaStream
                remoteParticipant.peerConnection.removeStream(mediaStream)
                remoteParticipant.peerConnection.addStream(mediaStream)
            }
        }

        if (stream.audioTracks != null && stream.audioTracks.size > 0) {
            audioTrack = stream.audioTracks[0]
            setVolume()
        }
    }

    fun getViewsContainer(): LinearLayout {
        return viewsContainer
    }

    fun hangup() {
        if (webSocketTask != null) {
            webSocketTask?.isCancelled = true
        }
        peersManager?.hangup()
    }

    private fun dismissLoading() {
        loading?.visibility = View.GONE
    }

    private fun showLoading() {
        if (!loading?.isShown!!)
            loading?.visibility = View.VISIBLE
    }

    fun onFirstFrameRendered() {
        if (isDestroyed) return
        runOnUiThread {
            dismissLoading()
        }
    }

    override fun onNetWorkMsgReceived(event: String?) {
        when (event) {
            RoomConstant.NET_WORK_INCONNECTED -> {
                if (isDestroyed) return
                showLoading()
            }
        }
    }
}