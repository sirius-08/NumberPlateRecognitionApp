package com.example.numberplaterecognition

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

class ClassifyActivity : AppCompatActivity() {
    lateinit var imageUri : Uri
    lateinit var bitmap : Bitmap
    lateinit var inputImageView : ImageView
    lateinit var interpreter : Interpreter
    lateinit var result : FloatBuffer
    lateinit var classifyTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classify)

        inputImageView = findViewById(R.id.inputImage) as ImageView
        classifyTextView = findViewById(R.id.classifyTextViw) as TextView

        var imageIntent = intent
        if(imageIntent != null){
            imageUri = imageIntent.data!!
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            //inputImageView.setImageBitmap(bitmap)

            loadModel()
        }
    }

    fun loadModel(){
        val conditions = CustomModelDownloadConditions.Builder() // Also possible: .requireCharging() and .requireDeviceIdle()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("VEHICLEclassifier_final", DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model: CustomModel? ->
                // Download complete. Depending on your app, you could enable the ML
                // feature, or switch from the local model to the remote model, etc.

                // The CustomModel object contains the local path of the model file,
                // which you can use to instantiate a TensorFlow Lite interpreter.
                Log.d("ClassifyActivity","Model downloaded")
                val modelFile = model?.file
                if (modelFile != null) {
                    interpreter = Interpreter(modelFile)
                    startClassification()
                }
            }
    }

    fun startClassification(){

        Log.d("ClassifyActivity","Classification Started")

        bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
        val input = ByteBuffer.allocateDirect(150*150*3*4).order(ByteOrder.nativeOrder())
        for (y in 0 until 150) {
            for (x in 0 until 150) {
                val px = bitmap.getPixel(x, y)

                // Get channel values from the pixel value.
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)

                // Normalize channel values to [0, 1.0]. This requirement depends on the model.
                val rf = r / 255f
                val gf = g / 255f
                val bf = b / 255f

                input.putFloat(rf)
                input.putFloat(gf)
                input.putFloat(bf)
            }
        }

        Log.d("ClassifyActivity","Input Ready")

        val bufferSize = 2 * java.lang.Float.SIZE / java.lang.Byte.SIZE
        val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
        interpreter?.run(input, modelOutput)

        Log.d("ClassifyActivity","Output found")

        modelOutput.rewind()
        val result = modelOutput.asFloatBuffer()
        if(result.get(0) > result.get(1)){
            classifyTextView.setText("car not detected" + " " + result.get(0).toString() + " "  + result.get(1).toString());
        }
        else{
            classifyTextView.setText("Car detected" + " " + result.get(0).toString() + " " + result.get(1).toString());
        }
    }
}