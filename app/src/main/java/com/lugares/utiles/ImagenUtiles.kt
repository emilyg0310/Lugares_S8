package com.lugares.utiles

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.isc.lugares.utiles.OtrosUtiles
import com.lugares.BuildConfig
import java.io.File

class ImagenUtiles (
    private val contexto: Context,
    private val btPhoto: ImageButton,
    private val btRotaL: ImageButton,
    private val btRotaR: ImageButton,
    private val imagen: ImageView,
    private var tomarFotoActivity: ActivityResultLauncher<Intent>
){
    init {
        btPhoto.setOnClickListener{tomarFoto()}
        btRotaL.setOnClickListener{imagen.rotation=imagen.rotation-90f}
        btRotaR.setOnClickListener{imagen.rotation=imagen.rotation+90f}
    }

    lateinit var imagenFile: File
    private lateinit var currentPhotoPath: String

    private fun tomarFoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(contexto.packageManager) != null){
            imagenFile = createImageFile()
            val photoURI = FileProvider.getUriForFile(
                contexto,
                BuildConfig.APPLICATION_ID + ".provider",
                imagenFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            tomarFotoActivity.launch(intent)

        }
    }

    private fun createImageFile(): File {
        val archivo = OtrosUtiles.getTempFile("imagen_")
        val storageDir: File? =
            contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            archivo,
            ".jpg",
            storageDir)
        currentPhotoPath = image.absolutePath
        return image
    }

    fun actualizarFoto(){
        imagen.setImageBitmap(
            BitmapFactory.decodeFile(imagenFile.absolutePath))
    }
}