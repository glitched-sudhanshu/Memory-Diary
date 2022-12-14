package com.example.memodiary.views.activities

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memodiary.R
import com.example.memodiary.application.MemoDiaryApplication
import com.example.memodiary.databinding.ActivityAddUpdateMemoryBinding
import com.example.memodiary.databinding.DialogCustomListBinding
import com.example.memodiary.databinding.DialogImageSelectionBinding
import com.example.memodiary.models.entities.MemoDiary
import com.example.memodiary.utils.Constants
import com.example.memodiary.viewmodels.MemoDiaryViewModel
import com.example.memodiary.viewmodels.MemoDiaryViewModelFactory
import com.example.memodiary.views.adapters.CustomListItemAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import javax.security.auth.login.LoginException


class AddUpdateMemoryActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        //folder in which our images are gonna be stored
        private const val IMAGE_DIRECTORY = "MemoDiary"
    }

    private val mMemoDiaryViewModel: MemoDiaryViewModel by viewModels {
        //we are passing the repository from our application to the model factory, which then basically which then basically creates our viewModel object here.
        MemoDiaryViewModelFactory((application as MemoDiaryApplication).repository)
    }

    private lateinit var mBinding: ActivityAddUpdateMemoryBinding

    private lateinit var mCustomListBinding: Dialog

    private var mEditMemoryDetails : MemoDiary? = null

    private var tempImageUri: Uri? = null

    private var mImagePath = ""
    private val takePictureFromCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                Glide.with(this)
                    .load(tempImageUri)
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .into(mBinding.ivMemoryImage)
                val source = ImageDecoder.createSource(this.contentResolver, tempImageUri!!)
                mImagePath = saveImageToInternalStorage(ImageDecoder.decodeBitmap(source))


                Log.i(TAG, "PathImage: $mImagePath")
                mBinding.ivAddMemoryImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateMemoryActivity,
                    R.drawable.ic_edit))
                //TODO: DELETE THE TEMPORARY FILE WHICH HAS BEEN CREATED
//            val file = tempImageUri!!.toFile()
//            file.delete()
            }
        }

    private val selectPictureFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            Glide.with(this)
                .load(it)
                .centerCrop()
                //because we have our background color. so no need of this
//            .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        Toast.makeText(this@AddUpdateMemoryActivity,
                            "Application is not able to store the image in internal storage",
                            Toast.LENGTH_LONG).show()
                        Log.e(TAG,
                            "onLoadFailed: Application is not able to store the image in internal storage")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        resource?.let {
                            val bitmap = resource.toBitmap()
                            mImagePath = saveImageToInternalStorage(bitmap)

                            Log.i(TAG, "PathImage: $mImagePath")
                        }
                        return false
                    }
                })
                .into(mBinding.ivMemoryImage)

            mBinding.ivAddMemoryImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateMemoryActivity,
                R.drawable.ic_edit))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateMemoryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //receiving data from the fragment
        if(intent.hasExtra(Constants.EXTRA_MEMORY_DETAILS)){
            mEditMemoryDetails = intent.getParcelableExtra(Constants.EXTRA_MEMORY_DETAILS)

            //populating data with already present values
            mEditMemoryDetails?.let {
                if(it.id!=0){
                    mImagePath = it.image
                    Glide.with(this)
                        .load(mImagePath)
                        .into(mBinding.ivMemoryImage)

                    mBinding.etTitle.setText(it.title)
                    mBinding.etType.setText(it.type)
                    mBinding.etDate.setText(it.date)
                    mBinding.etTimeAddingInDiary.setText(it.time)
                    mBinding.etDescription.setText(it.description)
                    mBinding.etPeopleInvolved.setText(it.peopleInvolved)
                    mBinding.btnAddUpdateMemory.setText(R.string.update_memory)
                }
            }
        }

        //it must come after the [mEditMemoryDetails] check
        setupActionBar()

        mBinding.ivAddMemoryImage.setOnClickListener(this)

        mBinding.etType.setOnClickListener(this)
        mBinding.etPeopleInvolved.setOnClickListener(this)
        mBinding.btnAddUpdateMemory.setOnClickListener(this)
        mBinding.etDate.setOnClickListener(this)
        mBinding.etTimeAddingInDiary.setOnClickListener(this)

    }

    private fun setupActionBar() {
        //toolbar ko action bar bana dia
        setSupportActionBar(mBinding.toolbarAddMemoryActivity)
        if(mEditMemoryDetails != null && mEditMemoryDetails!!.id != 0){
            supportActionBar?.let{
                //here [title] is title of the action bar
                it.title = resources.getString(R.string.title_edit_memory)
            }
        }
        //TODO: app bar else case
//        else{
//            supportActionBar?.let {
//                it.title = resources.getString(R.string.title_add_memory)
//            }
//        }
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
                R.id.et_type -> {
                    customItemsDialog(resources.getString(R.string.title_select_memory_type),
                        Constants.memoryTypes(),
                        Constants.MEMORY_TYPE)
                    return
                }
                R.id.et_people_involved -> {
                    customItemsDialog(resources.getString(R.string.title_select_people_involved),
                        Constants.peopleInvolvedInMemory(),
                        Constants.PEOPLE_INVOLVED)
                }

                R.id.btn_add_update_memory -> {
//                    trim{it <= ' '} to take out the empty spaces from the input
                    val title = mBinding.etTitle.text.toString().trim { it <= ' ' }
                    val type = mBinding.etType.text.toString().trim { it <= ' ' }
                    val peopleInvolved =
                        mBinding.etPeopleInvolved.text.toString().trim { it <= ' ' }
                    val date = mBinding.etDate.text.toString().trim { it <= ' ' }
                    val time = mBinding.etTimeAddingInDiary.text.toString().trim { it <= ' ' }
                    val description = mBinding.etDescription.text.toString().trim { it <= ' ' }

                    when {
                        TextUtils.isEmpty(mImagePath) -> {
                            Toast.makeText(this,
                                R.string.err_msg_image_not_selected,
                                Toast.LENGTH_LONG).show()
                        }
                        TextUtils.isEmpty(title) -> {
                            Toast.makeText(this,
                                R.string.err_msg_title_not_entered,
                                Toast.LENGTH_LONG).show()
                        }
                        TextUtils.isEmpty(type) -> {
                            Toast.makeText(this,
                                R.string.err_msg_type_not_selected,
                                Toast.LENGTH_LONG).show()
                        }
                        TextUtils.isEmpty(peopleInvolved) -> {
                            Toast.makeText(this,
                                R.string.err_msg_people_not_selected,
                                Toast.LENGTH_LONG).show()
                        }
                        TextUtils.isEmpty(date) -> {
                            Toast.makeText(this,
                                R.string.err_msg_date_not_selected,
                                Toast.LENGTH_LONG).show()
                        }
                        TextUtils.isEmpty(time) -> {
                            Toast.makeText(this,
                                R.string.err_msg_time_not_selected,
                                Toast.LENGTH_LONG).show()
                        }
                        TextUtils.isEmpty(description) -> {
                            Toast.makeText(this,
                                R.string.err_msg_description_not_selected,
                                Toast.LENGTH_LONG).show()
                        }
                        else -> {

                            var memoryId = 0;
                            var imageSource = Constants.MEMO_IMAGE_SOURCE_LOCAL
                            var favouriteMemory = false

                            //if we have data from the edit dish it will change the [memoryId] and it will not remain 0. We can use this to check whether we are creating a new memory or we are editing a existing one
                            mEditMemoryDetails?.let{
                                if(it.id!=0){
                                    memoryId = it.id
                                    imageSource = it.imageSource
                                    favouriteMemory = it.favouriteMemory
                                }
                            }

                            //creating an entity of MemoDiary
                            val memoDiaryDetails: MemoDiary = MemoDiary(
                                mImagePath,
                                imageSource,
                                title, type, date, peopleInvolved, time, description, favouriteMemory, memoryId
                            )

                            //if it is 0 it means we have created a new memory
                            if(memoryId == 0){
                                mMemoDiaryViewModel.insert(memoDiaryDetails)
                                Toast.makeText(this, "You successfully added your memory details in the diary.", Toast.LENGTH_LONG).show()
                                Log.e("Insertion", "Success")
                            }
                            else{
                                //memoDiaryDetails is the entity that we have created above
                                mMemoDiaryViewModel.update(memoDiaryDetails)
                                Toast.makeText(this, "You successfully updated your memory details in the diary.", Toast.LENGTH_LONG).show()
                                Log.e("Updating", "Success")
                            }


                            //finishing our addUpdateMemory activity
                            finish()
                        }


                    }
                }
                R.id.et_date -> {
                    chooseDate()
                    return
                }
                R.id.et_time_adding_in_diary -> {
                    chooseTime()
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
                            tempImageUri = FileProvider.getUriForFile(this@AddUpdateMemoryActivity,
                                "com.example.memodiary.provider",
                                createImageFile())
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


    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        //ki ye bitmap jo bana rahe h vo kiss application ke liye assigned hai
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
//        overriding an actual file with the file we created and name of the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            //we prepare a file output stream with our file, which is where we want to store it
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun customItemsDialog(title: String, itemsList: List<String>, selection: String) {
        mCustomListBinding = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        //setting up the ui of the customListDialog to the created UI  DialogCustomList i.e binding here
        mCustomListBinding.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)
        val adapter = CustomListItemAdapter(this, null, itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListBinding.show()
    }

    //not private because is used in adapter class
    fun selectedListItem(item: String, selection: String) {
        when (selection) {
            Constants.MEMORY_TYPE -> {
                mCustomListBinding.dismiss()
                mBinding.etType.setText(item)
                return
            }
            Constants.PEOPLE_INVOLVED -> {
                mCustomListBinding.dismiss()
                mBinding.etPeopleInvolved.setText(item)
            }
        }
    }

    //function to choose the date
    private fun chooseDate() {
        val calendar = Calendar.getInstance()
        //this will be the date that will be selected by the date picker by default
        val curDay = calendar.get(Calendar.DAY_OF_MONTH)
        val curMonth = calendar.get(Calendar.MONTH)
        val curYear = calendar.get(Calendar.YEAR)
        val picker = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                if (year > curYear || (year == curYear && month > curMonth) || (year == curYear && month == curMonth && dayOfMonth > curDay)) {
                    mBinding.etDate.setText("")
                    Toast.makeText(this,
                        "Enter valid date",
                        Toast.LENGTH_LONG).show()
                } else
                //dk why but month has 0 based indexing that's why +1
                    mBinding.etDate.setText("$dayOfMonth / ${month + 1} / $year")
            }, curYear, curMonth, curDay)
        picker.show()
    }

    private fun chooseTime() {
        val calendar = Calendar.getInstance()
        //this will be the date that will be selected by the date picker by default
        val curHour = calendar.get(Calendar.HOUR_OF_DAY)
        val curMinutes = calendar.get(Calendar.MINUTE)
        val picker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hour: Int, minutes: Int) {
                mBinding.etTimeAddingInDiary.setText("$hour : $minutes")
            }
        }, curHour, curMinutes, true)
        picker.show()
    }
}

