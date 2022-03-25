package com.lugares.utiles

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import java.io.File
import android.Manifest
import android.media.MediaPlayer
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.isc.lugares.utiles.OtrosUtiles
import com.lugares.R
import java.io.IOException
import java.lang.IllegalStateException

class AudioUtiles (
    private val actividad: Activity,
    private val contexto: Context,
    private val btAccion: ImageButton,
    private val btPlay: ImageButton,
    private val btDelete: ImageButton,
    private val msgIniciaNotaAudio: String,
    private val msgDetieneNotaAudio: String)

{
    init {
        btAccion.setOnClickListener { grabaStop() }
        btPlay.setOnClickListener { playNota() }
        btDelete.setOnClickListener { borrarNota() }
        btPlay.isEnabled = false
        btDelete.isEnabled = false
    }

    private var mediaRecord: MediaRecorder? = null
    private var grabando: Boolean = false
    var audioFile: File = File.createTempFile("audio_", ".mp3")

    private fun mediaRecorderInit(){
        if(audioFile.exists()&& audioFile.isFile){
            audioFile.delete()
        }
        val archivo=OtrosUtiles.getTempFile("audio_")
        audioFile = File.createTempFile(archivo,".mp3")
        mediaRecord = MediaRecorder()
        mediaRecord!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecord!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecord!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaRecord!!.setOutputFile(audioFile)
        }
    }

    private fun grabaStop() {
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED)
            {
            val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(actividad, permissions, 0)
        } else {
            grabando = if (!grabando) {
                mediaRecorderInit()
                iniciaGrabacion()
                true
            } else {
                detenerNota()
                false
            }
        }
    }

    private fun iniciaGrabacion(){
        try{
            mediaRecord?.prepare()
            mediaRecord?.start()
            Toast.makeText(contexto,msgIniciaNotaAudio,Toast.LENGTH_LONG).show()
            btAccion.setImageResource(R.drawable.ic_stop)
            btPlay.isEnabled = false
            btDelete.isEnabled = false
        }catch (e: IllegalStateException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun detenerNota(){
        btPlay.isEnabled = true
        btDelete.isEnabled = true
        mediaRecord?.stop()
        mediaRecord?.release()
        Toast.makeText(contexto,msgDetieneNotaAudio,Toast.LENGTH_SHORT).show()
        btAccion.setImageResource(R.drawable.ic_mic)
    }

    private fun playNota(){
        try {
            if (audioFile.exists()&& audioFile.canRead()){
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(audioFile.path)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun borrarNota(){
        try {
            if (audioFile.exists()){
                audioFile.delete()
                btPlay.isEnabled = false
                btDelete.isEnabled = false
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}

