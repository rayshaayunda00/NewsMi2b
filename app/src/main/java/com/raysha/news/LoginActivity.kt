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
import com.raysha.news.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Initialize views
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Login button action
        btnLogin.setOnClickListener {
            if (validateInput()) {
                performLogin()
            }
        }
    }

    // Validate input fields
    private fun validateInput(): Boolean {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (username.isEmpty()) {
            etUsername.error = "Username harus diisi"
            return false
        }
        if (password.isEmpty()) {
            etPassword.error = "Password harus diisi"
            return false
        }
        return true
    }

    // Perform login process
    private fun performLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Show ProgressBar and disable button
        progressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false

        ApiClient.apiService.login(username, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.success == true) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login berhasil! Selamat datang, ${loginResponse.username ?: "User"}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to DashboardActivity
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            loginResponse?.message ?: "Login gagal. Cek username dan password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login gagal: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true
                Toast.makeText(
                    this@LoginActivity,
                    "Terjadi kesalahan: ${t.localizedMessage ?: "Periksa koneksi internet Anda"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
