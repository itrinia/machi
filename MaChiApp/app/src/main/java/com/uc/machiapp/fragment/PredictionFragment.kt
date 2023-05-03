package com.uc.machiapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.uc.machiapp.R
import com.uc.machiapp.databinding.FragmentPredictionBinding
import com.uc.machiapp.databinding.FragmentProfileBinding
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PredictionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PredictionFragment : Fragment() {

    private lateinit var binding: FragmentPredictionBinding

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
        binding = FragmentPredictionBinding.inflate(inflater, container, false)

//        val textWatcher = object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                // Remove thousand separators from the input string
//                val input: String = s.toString().replace(",", "")
//
//                // Parse the input string as a Double
//                val inputNumber: Double = input.toDouble()
//
//                // Format the input as a number with a thousand separator
//                val formatter = NumberFormat.getInstance()
//                val formattedInput = formatter.format(inputNumber)
//
//                // Update the input field with the formatted input
//                binding.inputPenghasilan.removeTextChangedListener(this)
//                binding.inputPenghasilan.setText(formattedInput)
//                binding.inputPenghasilan.setSelection(formattedInput.length)
//                binding.inputPenghasilan.addTextChangedListener(this)
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        }
//
//        // Add the TextWatcher to the input field
//        binding.inputPenghasilan.addTextChangedListener(textWatcher)

        binding.buttonHitungPrediction.setOnClickListener{
//            // Remove the TextWatcher from the input field
//            binding.inputPenghasilan.removeTextChangedListener(textWatcher)

            val inputPenghasilan: Int = binding.inputPenghasilan.text.toString().toInt()
            if (inputPenghasilan <=0 ) {
                // Handle invalid input
                val toast = Toast.makeText(context, "Invalid Input! Cant Be Null!", Toast.LENGTH_SHORT)
                toast.show()

                binding.inputPenghasilan.setText("")
                binding.penjelasan.setText("Perlu Inputan Terlebih Dahulu!")
            } else {
                val formatter = NumberFormat.getInstance()
                // Use the integer value
                val hasil = inputPenghasilan * 12
                val formattedHasil = formatter.format(hasil)
                binding.Perhitungan5Tahun.text = formattedHasil.toString()

                //11.666.000 Juta per Bulannya --> Kebutuhan Anak
                //140000000 / Tahunnya --> Biaya Anak = 21 Tahun --> 294000000000 == 2.49 Milliar
                if(hasil >= 11666000){
                    val biayakebutuhan = 11666000
                    val lebih = hasil - biayakebutuhan
                    val formattedlebih = formatter.format(lebih)
                    binding.penjelasan.setText("Anda Sudah Bisa Membiayai 1 Anak per Bulannya! Sebesar 11,6 Juta, Kelebihan Uang yang Diperoleh yakni sebesar, Rp." + formattedlebih)
                }else{
                    val biayakebutuhan = 11666000
                    val kurang = biayakebutuhan - hasil
                    val formattedKurang = formatter.format(kurang)
                    binding.penjelasan.setText("Anda Masih Belum Siap Untuk Memiliki Anak! Kurang Rp. " + formattedKurang + " Untuk membiayai anak per bulannya. Kebutuhan anak per tahunnya adalah Rp.140,000,000 sampai umur 21")
                }
            }
        }



        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PredictionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PredictionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}