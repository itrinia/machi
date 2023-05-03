package com.uc.machiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.uc.machiapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, LoginActivity::class.java)

        //BackButton Action
        binding.backButtonRegister.setOnClickListener{
            startActivity(intent)
        }

        //RegisterButton Action
        binding.RegisterButton.setOnClickListener {
        postMethod()
        }
    }

    private fun postMethod() {
        rawJSON()
    }

    private fun rawJSON() {
        //Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.20.119:8080")
            .build()

        //Get the text from the TextInputEditText field
        val nickname = binding.UsernameInputRegister.text.toString()
        val email = binding.EmailInputRegister.text.toString()
        val password = binding.PasswordInputRegister.text.toString()

        //Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("nickname", nickname )
        jsonObject.put("email", email )
        jsonObject.put("password", password)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.BuatRegister(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)

                    // Toast message if success
                    val message = response.message()
                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()


                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.putExtra("json_results", prettyJson)
                    this@RegisterActivity.startActivity(intent)

                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()

//                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }


    }


    override fun onBackPressed() {
    }

}