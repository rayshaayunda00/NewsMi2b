package com.raysha.news

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raysha.news.api.ApiClient
import com.raysha.news.models.RegisterResponse

import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etFullname: EditText
    private lateinit var etemail: EditText
    private lateinit var btnSignUp: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etFullname = findViewById(R.id.etFullname)
        etemail = findViewById(R.id.etEmail)
        btnSignUp = findViewById(R.id.btnsignUp)
        progressBar = findViewById(R.id.progressBar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnSignUp.setOnClickListener{
            prosesRegister()
        }
    }

    //Method Process Register
    fun prosesRegister(){
        progressBar.visibility = View.VISIBLE
        ApiClient.apiService.register(
            etUsername.text.toString(),
            etPassword.text.toString(),
            etFullname.text.toString(),
            etemail.text.toString()
        ).enqueue(object : retrofit2.Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                //Response Berhasil
                if (response.isSuccessful){
                    // Jika Berhasil Menambahkan Data User
                    //Kondisi True => Succes
                    if (response.body()!!.success){
                        //Arahkan ke LoginActivity
                        startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                        //pesan
                        Toast.makeText(
                            this@RegisterActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }//kondisi false
                    else{
                        //pesan
                        Toast.makeText(
                            this@RegisterActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                //Response Gagal
                Toast.makeText(
                    this@RegisterActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
            }
            })
    }
}

