package com.example.album

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.album.media.MediaQuery
import com.example.album.media.model.DefaultImageInfo
import com.example.album.media.model.DefaultVideoInfo
import com.example.album.media.model.MimeType
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
const val PICK_FILE = 1

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivityTAG"

    /** 照片类型 */
    private val IMG_TYPES = arrayOf<String>(
        MimeType.Image.JPEG, MimeType.Image.PNG, MimeType.Image.WEBP,
        MimeType.Image.HEIF, MimeType.Image.GIF
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "执行---")

        // 权限申请
        val permissionsToRequire = ArrayList<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequire.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequire.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionsToRequire.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequire.toTypedArray(), 0)
        }

        pathTest()

        findViewById<Button>(R.id.picturePick).setOnClickListener {
            mediaFileQuery()
        }

        findViewById<Button>(R.id.saf).setOnClickListener {
            pickFileAndCopyUriToExternalFilesDir()
        }

        findViewById<Button>(R.id.pictureToAlbum).setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.suiyin)
            val displayName = "${System.currentTimeMillis()}_suiyin.jpg"
            val mimeType = "image/jpeg"
            val compressFormat = Bitmap.CompressFormat.JPEG
            addBitmapToAlbum(bitmap, displayName, mimeType, compressFormat)
        }

        findViewById<Button>(R.id.downLoadFile).setOnClickListener {
            val fileUrl = "https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&hs=0&pn=8&spn=0&di=132800&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=3720181670%2C558228719&os=4083749329%2C2797144916&simid=3321868309%2C413809310&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=11&oriquery=%E5%9B%BE%E7%89%87&objurl=https%3A%2F%2Fgimg2.baidu.com" +
                    "%2Fimage_search%2Fsrc%3Dhttp%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F0820%252F0142054dp00qy3hyz0019d200u0015dg00u0015d.png%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg%26refer%3Dhttp%3A%2F%2Fnimg.ws.126.net%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Djpeg%3Fsec%3D1634982071%26t%3D8aed193cb8e07e6ac09032361a73087e" +
                    "&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B8mn_z%26e3Bv54AzdH3F1yAzdH3Fw6ptvsjAzdH3FGHQbHd0GacdclQMR_z%26e3Bip4s&gsm=1&islist=&querylist="
            val fileName = "download_suiyin.jpg"
            downloadFile(fileUrl, fileName)
        }

    }

    /**
     * 内外存储路径打印
     *
     */
    private fun pathTest() {

        Log.d(TAG, "内部存储，filesDir:${this.filesDir}")
        Log.d(TAG, "内部存储，cacheDir:${this.cacheDir}")


        Log.d(TAG, "外部存储，私有存储，getExternalFilesDir:${this.getExternalFilesDir("")?.absolutePath}")
        Log.d(TAG, "外部存储，私有存储，externalCacheDir:${this.externalCacheDir?.absolutePath}")
        Log.d(TAG, "外部存储，私有存储，obbDir:${this.obbDir}")

        Log.d(TAG, "外部存储，getExternalStorageState:${Environment.getExternalStorageState()}")
        Log.d(TAG, "外部存储，getExternalStorageDirectory:${Environment.getExternalStorageDirectory()}")
        Log.d(TAG, "外部存储，getExternalStoragePublicDirectory:${Environment.getExternalStoragePublicDirectory("")}")
        Log.d(TAG, "外部存储，getExternalStoragePublicDirectory,DIRECTORY_DCIM:${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}")
        Log.d(TAG, "外部存储，getExternalStoragePublicDirectory,DIRECTORY_DOWNLOADS:${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}")

        Log.d(TAG, "系统，getRootDirectory:${Environment.getRootDirectory()}")
        Log.d(TAG, "系统，getDataDirectory:${Environment.getDataDirectory()}")
        Log.d(TAG, "系统，getDownloadCacheDirectory:${Environment.getDownloadCacheDirectory()}")
        Log.d(TAG, "系统，getStorageDirectory:${Environment.getStorageDirectory()}")
    }

    /**
     * 通过 SAF 打开各种文档
     *
     */
    private fun pickFileAndCopyUriToExternalFilesDir() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE)
    }


    /**
     * 向相册插入一张图片
     *
     * @param bitmap
     * @param displayName
     * @param mimeType
     * @param compressFormat
     */
    private fun addBitmapToAlbum(bitmap: Bitmap, displayName: String,
                                 mimeType: String, compressFormat: Bitmap.CompressFormat) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        } else {
            values.put(MediaStore.MediaColumns.DATA, "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName")
        }
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            val outputStream = contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                bitmap.compress(compressFormat, 100, outputStream)
                outputStream.close()
                Toast.makeText(this, "Add bitmap to album succeeded.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 下载到公共存储 Download 目录下
     *
     * @param fileUrl
     * @param fileName
     */
    private fun downloadFile(fileUrl: String, fileName: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(this, "You must use device running Android 10 or higher", Toast.LENGTH_SHORT).show()
            return
        }
        thread {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                val inputStream = connection.inputStream
                val bis = BufferedInputStream(inputStream)
                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    val outputStream = contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        val bos = BufferedOutputStream(outputStream)
                        val buffer = ByteArray(1024)
                        var bytes = bis.read(buffer)
                        while (bytes >= 0) {
                            bos.write(buffer, 0 , bytes)
                            bos.flush()
                            bytes = bis.read(buffer)
                        }
                        bos.close()
                        runOnUiThread {
                            Toast.makeText(this, "$fileName is in Download directory now.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                bis.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Media file query
     * 需要存储权限
     */
    private fun mediaFileQuery() {

        thread {
            // 原始
//           val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
//               null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
//           if (cursor != null) {
//               while (cursor.moveToNext()) {
//                   val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
//                   val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//
//                   Log.d(TAG, "图片uri：${uri}")
//               }
//               cursor.close()
//
//           }


            // 视频
//           val videoList = MediaQuery.with(this, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//               .build()
//               .query(DefaultVideoInfo::class.java)
//
//           for (item in videoList) {
//               Log.d(TAG, "视频：${item?.relativePath}")
//           }


            // 构建查询条件，查询jpeg、png、webp、heif类型的图片
            val selection = StringBuilder()
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?")
            selection.append(" or ")
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?")
            selection.append(" or ")
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?")
            selection.append(" or ")
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?")
            selection.append(" or ")
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?")

            // 图片
            val pictureList = MediaQuery.with(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .selection(selection.toString())
                .selectionArgs(IMG_TYPES)
                .build()
                .query(DefaultImageInfo::class.java)
            Log.d(TAG, "大小：${pictureList?.size}")
            for (item in pictureList) {
                Log.d(TAG, "图片：${item.contentUri}")
            }
        }

    }
}