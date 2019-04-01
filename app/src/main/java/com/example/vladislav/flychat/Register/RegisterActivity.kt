package com.example.vladislav.flychat.Register

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.example.vladislav.flychat.AllChats.AllChatsActivity
import com.example.vladislav.flychat.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View {
    lateinit var presenter: RegisterContract.Presenter
    private lateinit var photoPath: String
    private lateinit var photoFileUri: Uri
    private val storageRef = FirebaseStorage.getInstance().reference

    override fun setRegisterError(exceptionMessage: String) {
        username_field.error = "This field is necessary"
        email_field.error = "This field is necessary"
        password_field.error = "This field is necessary"
        Toast.makeText(this, exceptionMessage, Toast.LENGTH_LONG).show()
    }

    override fun navigateToLogin() {
        finish()
    }

    override fun navigateToChat() {
        startActivity(
            Intent(
                this,
                AllChatsActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    override fun setupPresenter(presenter: RegisterContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        register_progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        register_progress_bar.visibility = View.GONE
    }

    private fun dispatchPickPictureIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PICTURE_CODE)
    }

    private fun dispatchTakePictureIntent() {
        val photoFile = createImageFile()
        photoFileUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri)
        startActivityForResult(intent, TAKE_PICTURE_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE_CODE && resultCode == RESULT_OK) {
            val imageBmp = BitmapFactory.decodeFile(photoPath)
            MediaStore.Images.Media.insertImage(contentResolver, imageBmp, "test_tile", "test_desc")
            uploadPicToFirebase()
        }
    }

    private fun createImageFile(): File {
        //TODO add write_external_storage permission request
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val tempFile: File = File.createTempFile("pic_$timeStamp", ".jpg", storageDir)
        photoPath = tempFile.absolutePath
        return tempFile
    }

    private fun uploadPicToFirebase() {
        val filename = UUID.randomUUID().toString()
        val fileRef = storageRef.child("avatars/$filename")

        val uploadTask = fileRef.putFile(photoFileUri)

        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                Log.d(TAG, "upload link: $it")
            }
            Log.d(TAG, "picture successfully uploaded")
        }.addOnFailureListener {
            Log.d(TAG, "picture not loaded, reason: ${it.localizedMessage}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupPresenter(
            RegisterPresenter(
                this,
                RegisterInteractor()
            )
        )

        presenter.onViewCreated()

        to_login_btn.setOnClickListener {
            finish()
        }

        perform_register.setOnClickListener {
            presenter.register(
                username_field.text.toString(),
                email_field.text.toString(),
                password_field.text.toString()
            )
        }

        profile_image.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Choose from?")
                .setItems(arrayOf("Gallery", "Camera")) { _, which ->
                    when (which) {
                        0 -> dispatchPickPictureIntent()
                        //TODO pick from gallery and take picture
                        1 -> dispatchTakePictureIntent()
                    }
                }
                .show()
        }
    }

    companion object {
        private const val TAKE_PICTURE_CODE = 0
        private const val PICK_PICTURE_CODE = 1
        private const val TAG = "RegisterActivityTag"
    }
}
