package com.uc.machiapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.uc.machiapp.LoginActivity
import com.uc.machiapp.databinding.FragmentProfileBinding
import com.uc.machiapp.model.User
import com.uc.machiapp.model.UserData
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.uc.machiapp.APIService
import retrofit2.http.GET
import retrofit2.http.Path

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    // Create a Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.20.119:8080")
        .build()

    // Create a service interface
    interface APIService {
        @GET("/users/{id}")
        fun getUserData(@Path("id") id: String): Call<ResponseBody>
    }

    // Create an implementation of the service
    private val service = retrofit.create(APIService::class.java)

    private lateinit var binding: FragmentProfileBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and bind the data
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        // Get the user ID from shared preferences
        val sharedPreferences = activity?.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getString("user_id", null)

        binding.idUser.text = userId
        //Set Logout Button
        binding.logoutprofile.setOnClickListener{
            logout()
        }

//        // Get the user ID from shared preferences
//        val sharedPreferences = activity?.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
//        val userId = sharedPreferences?.getString("user_id", null)

        if (userId != null) {
            // Make the GET request
            val call = service.getUserData(userId)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Parse the response body into a Java object or a JSON object
                        val responseBody = response.body()
                        val gson = Gson()
                        val userData = gson.fromJson(responseBody?.charStream(), UserData::class.java)

                        // Update the UI with the user data\
                        binding.UsernameEditProfile.setText(userData.nickname)
                        binding.EmailEditProfile.setText(userData.email)
//                        binding.PasswordEditProfile.setText(userData.password)

                    } else {
                        // Handle error cases
                        // ...
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle failure cases
                    // ...
                }
            })
        } else {
            // Handle the case where the user is not logged in
            // ...
        }

        return binding.root
    }

    private fun logout(){
        // Clear the token from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        // Clear the token and user ID
        editor.remove("token")
        editor.remove("user_id")
        editor.apply()

        // Redirect the user to the login screen
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}