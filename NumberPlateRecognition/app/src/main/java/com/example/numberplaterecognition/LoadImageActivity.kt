package com.example.numberplaterecognition

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class LoadImageActivity : AppCompatActivity() {
    lateinit var galleryButton : FloatingActionButton
    lateinit var descriptionTextView : TextView
    lateinit var imageView : ImageView
    lateinit var imageUri : Uri
    val isCarKey = "com.example.numberplaterecognition.isCarKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_image)

        initViews()
        galleryButton.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent,100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            imageView.setImageBitmap(bitmap)
            imageView.visibility = View.VISIBLE
            descriptionTextView.visibility = View.GONE

            startClassification()
        }
    }

    fun startClassification(){

        var intent = Intent(this@LoadImageActivity,ClassifyActivity::class.java)
        intent.setData(imageUri)
        startActivity(intent)
    }

    fun initViews(){
        galleryButton = findViewById(R.id.galleryButton) as FloatingActionButton
        descriptionTextView = findViewById(R.id.descriptionTextView) as TextView
        imageView = findViewById(R.id.imageView) as ImageView
    }

}