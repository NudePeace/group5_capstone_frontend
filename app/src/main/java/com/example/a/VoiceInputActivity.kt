package com.example.a

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a.model.AnalyzeResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.TimeUnit

class VoiceInputActivity : AppCompatActivity() {

    private val REQUEST_RECORD_AUDIO = 100

    override fun attachBaseContext(newBase: android.content.Context?) {
        if (newBase != null) {
            val contextWithLanguage = LanguageUtil.applySavedLanguage(newBase)
            super.attachBaseContext(contextWithLanguage)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private lateinit var btnBack: ImageButton
    private lateinit var btnMic: ImageButton
    private lateinit var tvRecordingTime: TextView
    private lateinit var btnStop: Button
    private lateinit var btnPlay: Button
    private lateinit var btnDelete: Button
    private lateinit var btnNext: Button

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var recordingFile: File? = null
    private var isRecording = false
    private var isPlaying = false
    private var recordingStartTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var updateTimeRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voiceinput)

        // UI 연결
        btnBack = findViewById(R.id.btnBack)
        btnMic = findViewById(R.id.btnMic)
        tvRecordingTime = findViewById(R.id.tvRecordingTime)
        btnStop = findViewById(R.id.btnStop)
        btnPlay = findViewById(R.id.btnPlay)
        btnDelete = findViewById(R.id.btnDelete)
        btnNext = findViewById(R.id.btnNext)

        updateControlButtons(false)
        tvRecordingTime.text = "00:00"

        // 뒤로가기
        btnBack.setOnClickListener {
            stopRecording()
            releaseMediaPlayer()
            finish()
        }

        // 녹음 버튼
        btnMic.setOnClickListener {
            if (!isRecording) {
                startRecording()
            } else {
                stopRecording()
            }
        }

        // 중지 버튼
        btnStop.setOnClickListener {
            if (isPlaying) stopPlayback()
        }

        // 재생 버튼
        btnPlay.setOnClickListener {
            if (recordingFile != null && recordingFile!!.exists()) {
                if (!isPlaying) startPlayback()
            } else {
                Toast.makeText(this, "녹음 파일이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 삭제 버튼
        btnDelete.setOnClickListener {
            deleteRecording()
        }

        // 다음 버튼
        btnNext.setOnClickListener {
            if (recordingFile != null && recordingFile!!.exists()) {
                uploadAudio(recordingFile!!)
            } else {
                Toast.makeText(this, "먼저 음성을 녹음해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadAudio(audioFile: File) {

        Toast.makeText(this, "분석 중..", Toast.LENGTH_LONG).show()

        try {
            val requestFile = audioFile.asRequestBody("audio/m4a".toMediaTypeOrNull())

            val filePart = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)

            // 3. Gọi API analyzeUsingAudio
            ApiClient.service.analyzeUsingAudio(filePart)
                .enqueue(object : Callback<AnalyzeResponse> {
                    override fun onResponse(
                        call: Call<AnalyzeResponse>,
                        response: Response<AnalyzeResponse>
                    ) {

                        if (response.isSuccessful) {
                            val result = response.body()?.result

                            if (result != null && result.isNotEmpty()) {
                                val intent = Intent(this@VoiceInputActivity, ResultActivity::class.java)
                                intent.putExtra("chat_result", result)
                                intent.putExtra("source_activity", "VoiceInputActivity")
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(
                                this@VoiceInputActivity,
                                "오류 발생: Code ${response.code()} - ${response.message()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<AnalyzeResponse>, t: Throwable) {
                        Toast.makeText(
                            this@VoiceInputActivity,
                            "${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(this, "파일 업로드 오류: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    // 권한 처리 + 녹음 시작
    private fun startRecording() {

        // 권한 체크
        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
            != android.content.pm.PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
            return
        }

        // 권한 허용됨 → 녹음 시작
        try {
            recordingFile = File(
                getExternalFilesDir(null),
                "voice_input_${System.currentTimeMillis()}.m4a"
            )

            mediaRecorder = MediaRecorder().apply {
                reset()
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(recordingFile!!.absolutePath)
                prepare()
                start()
            }

            isRecording = true
            recordingStartTime = System.currentTimeMillis()
            updateControlButtons(false)
            startTimeUpdate()

            Toast.makeText(this, "녹음이 시작되었습니다.", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, "녹음 시작 실패: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != REQUEST_RECORD_AUDIO) return

        if (grantResults.isNotEmpty() &&
            grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "마이크 권한 허용됨", Toast.LENGTH_SHORT).show()
            startRecording()
        } else {
            // "다시 묻지 않기" 체크 여부 확인
            val showRationale = shouldShowRequestPermissionRationale(
                android.Manifest.permission.RECORD_AUDIO
            )

            // ❌ 다시 묻지 않기로 차단된 경우
            if (!showRationale) {
                showPermissionBlockedDialog()
            } else {
                Toast.makeText(this, "마이크 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 권한이 영구적으로 차단된 경우 Dialog 띄우기
    private fun showPermissionBlockedDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("마이크 권한이 차단되었습니다.\n설정에서 직접 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // 녹음 중지
    private fun stopRecording() {
        try {
            if (isRecording && mediaRecorder != null) {
                mediaRecorder?.stop()
                mediaRecorder?.release()
                mediaRecorder = null

                isRecording = false
                stopTimeUpdate()
                updateControlButtons(true)

                Toast.makeText(this, "녹음이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) { }
    }

    // 재생
    private fun startPlayback() {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(recordingFile!!.absolutePath)
                prepare()
                start()
            }

            isPlaying = true
            btnMic.isEnabled = false
            btnStop.isEnabled = true
            btnDelete.isEnabled = false

            mediaPlayer?.setOnCompletionListener {
                isPlaying = false
                tvRecordingTime.text = "00:00"
                btnMic.isEnabled = true
                btnStop.isEnabled = false
                btnDelete.isEnabled = true
            }

            startPlaybackTimeUpdate()
            Toast.makeText(this, "재생 중...", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, "재생 실패: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 재생 중지
    private fun stopPlayback() {
        try {
            if (mediaPlayer != null && isPlaying) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                isPlaying = false
                tvRecordingTime.text = "00:00"
                stopTimeUpdate()

                btnMic.isEnabled = true
                btnStop.isEnabled = false
                btnDelete.isEnabled = true

                Toast.makeText(this, "재생이 중지되었습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) { }
    }

    // 삭제
    private fun deleteRecording() {
        try {
            releaseMediaPlayer()
            if (recordingFile?.exists() == true) {
                recordingFile!!.delete()
            }
            recordingFile = null
            isRecording = false
            isPlaying = false
            tvRecordingTime.text = "00:00"
            updateControlButtons(false)
            btnMic.isEnabled = true

            Toast.makeText(this, "녹음이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) { }
    }

    // 시간 업데이트
    private fun startTimeUpdate() {
        updateTimeRunnable = object : Runnable {
            override fun run() {
                if (isRecording) {
                    val elapsed = System.currentTimeMillis() - recordingStartTime
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed) % 60
                    tvRecordingTime.text = String.format("%02d:%02d", minutes, seconds)
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(updateTimeRunnable!!)
    }

    private fun startPlaybackTimeUpdate() {
        updateTimeRunnable = object : Runnable {
            override fun run() {
                if (isPlaying && mediaPlayer != null) {
                    val pos = mediaPlayer!!.currentPosition.toLong()
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(pos)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(pos) % 60
                    tvRecordingTime.text = String.format("%02d:%02d", minutes, seconds)
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(updateTimeRunnable!!)
    }

    private fun stopTimeUpdate() {
        updateTimeRunnable?.let { handler.removeCallbacks(it) }
    }

    private fun updateControlButtons(hasRecording: Boolean) {
        btnStop.isEnabled = false
        btnPlay.isEnabled = hasRecording
        btnDelete.isEnabled = hasRecording
    }

    private fun releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        isPlaying = false
        stopTimeUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        releaseMediaPlayer()
    }
}