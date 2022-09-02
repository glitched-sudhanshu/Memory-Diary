package com.example.memodiary.views.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.memodiary.R
import com.example.memodiary.databinding.ActivityAddUpdateMemoryBinding
import com.example.memodiary.databinding.DialogImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File


class AddUpdateMemoryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateMemoryBinding

    private var tempImageUri : Uri? = null
    private var tempImageFilePath = ""
    private val takePictureFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if(it)
        {
            mBinding.ivMemoryImage.setImageURI(tempImageUri)
            mBinding.ivAddMemoryImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateMemoryActivity, R.drawable.ic_edit))
        }
    }

    private val selectPictureFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        mBinding.ivMemoryImage.setImageURI(it)
        mBinding.ivAddMemoryImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateMemoryActivity, R.drawable.ic_edit))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateMemoryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupActionBar()

        mBinding.ivAddMemoryImage.setOnClickListener(this)


    }

    private fun setupActionBar() {
        //toolbar ko action bar bana dia
        setSupportActionBar(mBinding.toolbarAddMemoryActivity)
        //back button aagya
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddMemoryActivity.setNavigationOnClickListener {
            //jo back button se hota h... yaha bhi ab vahi hoga
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_add_memory_image -> {
                    customImageSelectionDialog()
                    return
                }

            }
        }
    }

    private fun customImageSelectionDialog() {
        /**
         * The camera and gallery are implemented for sdk > 28
         * If you want to implement it for lower versions of android use permission to WRITE_EXTERNAL_STORAGE also along with READ
         * And add statement to use permission in manifest file
         * */
        val dialog = Dialog(this)
        val binding: DialogImageSelectionBinding =
            DialogImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {

                            tempImageUri = FileProvider.getUriForFile(this@AddUpdateMemoryActivity, "com.example.memodiary.provider", createImageFile().also {
                                tempImageFilePath = it.absolutePath
                            })
                            takePictureFromCamera.launch(tempImageUri)


                        }
                    }
                }

                private fun createImageFile(): File {
                    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    return File.createTempFile("tmp_memoDiary_image", ".jpg", storageDir)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?,
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()
            dialog.dismiss()
        }
        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener {
                override fun onPermissionGranted(report: PermissionGrantedResponse?) {
                    selectPictureFromGallery.launch("image/*")
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@AddUpdateMemoryActivity,
                        "Nahi hua gallery select",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?,
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have not granted the permission required for this feature. It can be enabled under application settings")
            .setPositiveButton("Go to settings")
            { _, _ ->
                try {
                    //go to settings of out phone
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    //it specifies where do we actually need to go in settings. So we use our app link(i.e unique package name)
                    val uri = Uri.fromParts("package", packageName, null)
                    //then we set that uri to our intent's data and start the activity
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@AddUpdateMemoryActivity,
                        "Activity not found",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton("Cancel") {
                //iss case me hume ek variable ka use hai (upar bhi do variables the, par hume unka use nahi tha) A dialog is returned which we need to dismiss if the user cancels the request.
                    dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}

