package com.raysha.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.raysha.news.api.ApiClient
import com.raysha.news.models.TambahBeritaResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class TambahBeritaActivity : AppCompatActivity() {
    private lateinit var etJudul: EditText
    private lateinit var btnGambar: Button
    private lateinit var imgGambar: ImageView
    private lateinit var etIsi: EditText
    private lateinit var btnTambah : Button
    private lateinit var progressBar: ProgressBar
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_berita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etJudul= findViewById(R.id.etJudul)
        btnGambar= findViewById(R.id.btnGambar)
        imgGambar= findViewById(R.id.imgGambar)
        etIsi=findViewById(R.id.etIsi)
        btnTambah= findViewById(R.id.btnTambah)
        progressBar=findViewById(R.id.progressBar)

        btnGambar.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080,1080)
                .start()
        }
        btnTambah.setOnClickListener{
            imageFile?.let { file ->
                tambahBerita(etJudul.text.toString(), file, etIsi.text.toString())
            }
        }
    }

    //menampilkan override method
    override fun onActivityResult(

    requestCode: Int,
    resultCode: Int,
    data:Intent?
    ){
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK && data !=null){
            val uri = data.data!!
            imageFile= File(uri.path!!)
            imgGambar.visibility= View.VISIBLE
            imgGambar.setImageURI(uri)
        }
    }

    //proses tambah berita
    private fun tambahBerita(judul: String, fileGambar: File, isiBerita: String) {
        progressBar.visibility = View.VISIBLE
        val requestBody = fileGambar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("fileGambar", fileGambar.name, requestBody)

        val title = judul.toRequestBody("text/plain".toMediaTypeOrNull())
        val description = isiBerita.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.apiService.addBerita(title, description,part )
            .enqueue(object : Callback<TambahBeritaResponse>{
                override fun onResponse(
                    call: Call<TambahBeritaResponse>,
                    response: Response<TambahBeritaResponse>

                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.success) {

                            startActivity(
                                Intent(
                                    this@TambahBeritaActivity,
                                    DashboardActivity::class.java
                                )
                            )
                        } else {
                            Toast.makeText(
                                this@TambahBeritaActivity,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@TambahBeritaActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    progressBar.visibility = View.GONE

                }
                override fun onFailure(call: Call<TambahBeritaResponse>, t: Throwable) {

                }
            })
    }


    }
