package com.reve.abroady.ui.community

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.reve.abroady.R
import com.reve.abroady.base.BaseActivity
import com.reve.abroady.databinding.ActivityWritePostBinding
import com.reve.abroady.model.dao.BoardDao
import com.reve.abroady.model.data.RetrofitInstance
import com.reve.abroady.model.data.post.OnePostSendModel
import com.reve.abroady.util.addTo
import com.reve.abroady.viewmodel.MainViewModel
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.util.*

class WritePostActivity : BaseActivity<ActivityWritePostBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_write_post

    companion object {
        private const val GET_GALLERY_IMAGE = 1
    }

    private val boardDao: BoardDao by lazy {
        RetrofitInstance.getBoardDaoInstance()
    }

    // api 26 이상부터 사용 가능 (추후 이하 버전 대응 모색)
   // @RequiresApi(Build.VERSION_CODES.O)
    private fun writePost() {
        boardDao.sendPost(
            OnePostSendModel(
                categoryId = 2,
                title = binding.writePostTitle.text.toString(),
                content = binding.writePostContent.text.toString(),
                isAnonymous = binding.writePostAnonymousCheckBox.isChecked,
            )
        ).observeOn(Schedulers.io())
            .doOnError {
                Log.d(TAG, "getAllPost Error : ${it}")
            }
            .unsubscribeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    response.body()?.let { postSendResult ->
                        if (postSendResult.httpStatus == 200) {
                            runOnUiThread {
                                Toast.makeText(this, "Successfully posted.", Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        }
                    }
                }
                Log.d(TAG, "${response.body()?.httpStatus} , ${response.body()?.message}")
            }, {
                if (it is HttpException) {
                    runOnUiThread {
                        Toast.makeText(this, "Failed to post. Please try later.", Toast.LENGTH_SHORT).show()
                    }
                    Log.e(TAG, "WritePost HttpException : $it")
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Failed to post. Check your Internet connection.", Toast.LENGTH_SHORT).show()
                    }
                }
                Log.e(TAG, "${it.cause}")
            }).addTo(compositeDisposable)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initStartView() {
        binding.chooseBoardButton.setOnClickListener {
            chooseBoard()
        }
        binding.closeWritePostButton.setOnClickListener {
            finish()
        }
        binding.writePostCamera.setOnClickListener {
            takePicture()
        }
        binding.writePostButton.setOnClickListener {
            writePost()
        }
    }

    private fun chooseBoard() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val itemList = arrayOf("Hot Board") // 추후 모든 게시판 이름 미리 받아오기
        builder.setTitle("Choose a board")
        builder.setItems(itemList) { dialog, which ->
            when (which) {
                0 -> binding.chooseBoardButton.text = itemList[0]
            }
        }
        builder.show()
    }

    fun takePicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        startActivityForResult(intent, GET_GALLERY_IMAGE)
    }

    // api 26 이상부터 사용 가능 (추후 이하 버전 대응 모색)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getImageString(img: Drawable): String {
        val bitmapDrawable = img as BitmapDrawable
        return convertImageToString(bitmapDrawable.bitmap)
    }

    // getEncoder().encodeToString -> api 26 이상부터 사용 가능 (추후 이하 버전 대응 모색)
    // bitmap을  string 형태로 변환하는 메서드 (이렇게 string 으로 변환된 데이터를 DB에 보냄)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertImageToString(bitmap: Bitmap): String {
        var image = ""
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        image = Base64.getEncoder().encodeToString(byteArray)
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImageUri: Uri = data?.data!!
            binding.writePostImage.setImageURI(selectedImageUri)
        }
    }
}