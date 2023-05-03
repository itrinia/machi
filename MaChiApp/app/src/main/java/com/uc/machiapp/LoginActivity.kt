package com.uc.machiapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.uc.machiapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            // User is already logged in, start the main activity
            val intent = Intent(this, LayarUtamaActivity::class.java)
            startActivity(intent)
        } else {
            // User is not logged in, show the login screen

            val intent = Intent(this, LoginActivity::class.java)

            // Register Button Action
            binding.RegisterButton.setOnClickListener {
                val intentRegister = Intent(this, RegisterActivity::class.java)
                startActivity(intentRegister)
            }

            // Login Button Action
            binding.LoginButton.setOnClickListener {
                login()
            }
        }
    }

    private fun login() {
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.20.119:8080")
            .build()

        // Get the text from the TextInputEditText field
        val email = binding.EmailInputLogin.text.toString()
        val password = binding.PasswordInputLogin.text.toString()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {

            // Do the POST request and get response
            val response = service.MasukLogin(requestBody)

            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {

//                    // Convert raw JSON to pretty JSON using GSON library
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val prettyJson = gson.toJson(
//                        JsonParser.parseString(
//                            response.body()
//                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
//                        )
//                    )

//                    Log.d("Pretty Printed JSON :", prettyJson)

                    // Parse the ResponseBody object into a Java object or a JSONObject that has a token property
                    val responseBody = response.body()
                    val responseObject = responseBody?.string()
                    val jsonObject = JSONObject(responseObject)
                    val token = jsonObject.getString("token")
                    val user_id = jsonObject.getString("id")

                    // Save the login session data (e.g. token)
                    val sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    // Save the login session data (e.g. token)
                    editor.putString("token", token)

                    // Save the user ID
                    editor.putString("user_id", user_id)

                    // Save the changes
                    editor.apply()

                    // Toast message if success
                    val message = response.message()
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()

                    // Start the main activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    this@LoginActivity.startActivity(intent)

                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
    }


}