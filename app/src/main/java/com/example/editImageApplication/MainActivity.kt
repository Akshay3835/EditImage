package com.example.editImageApplication

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity() {

    private var addPhoto: ImageButton? = null
    private var circularImage: CircleImageView? = null
    private var sheetDialog: BottomSheetDialog? = null
    private lateinit var view: View
    var imageCapture : Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addPhoto = findViewById(R.id.add_imageButton)
        circularImage = findViewById(R.id.actionImage)

        addPhoto?.setOnClickListener {
            showBottomSheetDialog()
        }

    }

    private fun showBottomSheetDialog() {
        sheetDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
        view = LayoutInflater.from(this)
            .inflate(R.layout.bottomsheet_dailog, findViewById(R.id.linear_layout))
        view.findViewById<ImageButton>(R.id.camera).setOnClickListener {
            takePictureIntent()
            Log.d("clicked", "camera")
            Toast.makeText(this, "camera is clicked", Toast.LENGTH_SHORT).show()
            sheetDialog!!.dismiss()
        }
        view.findViewById<ImageButton>(R.id.gallery).setOnClickListener {
            pickImageGallery()
            Toast.makeText(this, "gallery is clicked", Toast.LENGTH_SHORT).show()
            sheetDialog!!.dismiss()
        }
        view.findViewById<ImageButton>(R.id.remove).setOnClickListener {
            circularImage!!.setImageBitmap(null)
            Log.d("remove", "Clicked")
            Toast.makeText(this, "remove button is clicked", Toast.LENGTH_SHORT).show()
            this.sheetDialog!!.dismiss()
        }
        sheetDialog!!.setContentView(view)
        sheetDialog!!.show()
    }

    private fun takePictureIntent() {
        val takePictureIntent = Intent(ACTION_IMAGE_CAPTURE)
        imageCapture = true
        requestCameraCode.launch(takePictureIntent)
    }

    private fun pickImageGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        imageCapture = false
        intent.type = "image/"
        intent.action = ACTION_GET_CONTENT
        requestCameraCode.launch(intent)

    }


    private val requestCameraCode =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { request ->
                if (imageCapture == true) {
                    try {
                        val imageBitmap = request.data!!.extras!!.get("data") as Bitmap
                        circularImage!!.setImageBitmap(imageBitmap)
                    } catch (e: NullPointerException) {
                        Toast.makeText(this, "circular Image error", Toast.LENGTH_SHORT).show()
                    }
                }
                    if (imageCapture == false) {
                    try {
                        val imageUri = request.data?.data as Uri
                        circularImage!!.setImageURI(imageUri)
                    } catch (e: NullPointerException) {
                        Toast.makeText(this, "gallery error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

//    private  val requestGalleryCode = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ code ->
//
//
//}
