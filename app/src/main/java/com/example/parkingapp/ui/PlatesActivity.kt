package com.example.parkingapp.ui

import android.app.Activity
import android.content.ClipData.Item
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.parkingapp.data.Preferences
import com.example.parkingapp.data.Zone
import com.example.parkingapp.databinding.ActivityPlatesBinding
import com.example.parkingapp.databinding.BottomSheetAddPlatesBinding
import com.example.parkingapp.databinding.ItemCarPlatesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PlatesActivity:Activity() {
    lateinit var binding:ActivityPlatesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val zones = intent.getParcelableExtra<Zone>("a")
        binding.backTextView.setOnClickListener { onBackPressed() }
        binding.addNewPlatesButton.setOnClickListener { showAddPlatesDialog() }

        val platesList = Preferences.getCarPlates()
        platesList?.forEach {
            addPlates(it)
        }
    }

    private fun showAddPlatesDialog(){
        val bottomSheetDialog = BottomSheetDialog(this)
        val dialogBinding = BottomSheetAddPlatesBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.platesInput.doOnTextChanged { text, start, before, count ->
            if(text.toString().length == 2 || text.toString().length == 6){
                dialogBinding.platesInput.setText("$text-")
                dialogBinding.platesInput.setSelection(dialogBinding.platesInput.text.length);
            }
            dialogBinding.addPlatesButton.isEnabled = (dialogBinding.platesInput.text.length == 9)
        }
        dialogBinding.addPlatesButton.setOnClickListener {
            val plates = dialogBinding.platesInput.text.toString()
            addPlates(plates)
            Preferences.saveCarPlates(plates)
            bottomSheetDialog.dismiss()
        }
        dialogBinding.closeButton.setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetDialog.show()
    }
    private fun addPlates(plates:String){
        val platesBinding = ItemCarPlatesBinding.inflate(layoutInflater)
        platesBinding.platesTextView.setText(plates)
        platesBinding.deletePlate.setOnClickListener {
            binding.platesHolder.removeView(platesBinding.root)
            val plate = platesBinding.platesTextView.text.toString()
            Preferences.removeCarPlate(plate)
        }
        binding.platesHolder.addView(platesBinding.root)
    }

}