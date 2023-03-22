package com.example.parkingapp.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.parkingapp.R
import com.example.parkingapp.data.Preferences
import com.example.parkingapp.databinding.*
import com.example.parkingapp.ui.PlatesActivity
import com.example.parkingapp.ui.zones.ZonesActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: HomeViewModel
    lateinit var platesChooserDialog: BottomSheetDialog
    lateinit var platesChooserBinding :BottomSheetChoosePlatesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Preferences.init(this)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.init(this.assets.readAssetsFile("parking_zones.json"))

        binding.zonesTextView.setOnClickListener {
            val zonesIntent = Intent(this, ZonesActivity::class.java)
            startActivity(zonesIntent)
        }

        binding.carsTextView.setOnClickListener{
            val platesIntent = Intent(this,PlatesActivity::class.java)
            platesIntent.putExtra("a", viewModel.parkingZonesList[0])
            startActivity(platesIntent)
        }
        binding.editPlates.setOnClickListener {
            platesChooserDialog.show()
        }
        binding.editZoneButton.setOnClickListener { showAreaChooser() }
        initPlatesChooser()

        viewModel.payNowEnabled.observe(this){
            binding.payNow.isEnabled = it
            updatePaymentDetails()
        }
        binding.payNow.setOnClickListener {
            sendSms()
        }
        binding.setReminder.setOnClickListener {
            Timer().schedule(3000000){
                showNotification()
            }
        }
        createNotificationChannel()
    }

    override fun onResume() {
        super.onResume()
        initUI()
    }
    private fun initUI(){
        platesChooserBinding.platesHolder.removeAllViews()
        viewModel.plates = Preferences.getLastUsedPlate()
        viewModel.zone = viewModel.parkingZonesList.find{it.zoneName == Preferences.getLastUsedZone()}
        binding.platesName.text = viewModel.plates
        binding.zoneName.text = viewModel.zone?.zoneName
        val platesList = Preferences.getCarPlates()
        platesList?.forEach {
            addPlates(it)
        }
    }

    private fun initPlatesChooser(){
        platesChooserBinding = BottomSheetChoosePlatesBinding.inflate(layoutInflater)
        platesChooserDialog = BottomSheetDialog(this)
        platesChooserDialog.setContentView(platesChooserBinding.root)
        platesChooserBinding.addNewPlates.setOnClickListener {
            showAddPlatesDialog()
        }

        platesChooserBinding.closeButton.setOnClickListener {
            platesChooserDialog.dismiss()
        }
    }
    private fun addPlates(plates:String){
        val platesBinding = ItemCarPlatesChooserBinding.inflate(layoutInflater)
        platesBinding.plates.setText(plates)
        platesBinding.platesHolder.setOnClickListener {
            binding.platesName.setText(plates)
            viewModel.plates = plates
            platesChooserDialog.dismiss()
            Preferences.saveLastUsedCarPlate(plates)
        }
       platesChooserBinding.platesHolder.addView(platesBinding.root)
    }
    private fun showAddPlatesDialog(){
        val bottomSheetDialog = BottomSheetDialog(this)
        val dialogBinding = BottomSheetAddPlatesBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.platesInput.doOnTextChanged { text, start, before, count ->
            if(text?.length == 2 || text?.length == 6){
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

    private fun showAreaChooser(){
        val areaDialog= BottomSheetDialog(this)
        val areaDialogBinding = BottomSheetChooseAreaBinding.inflate(layoutInflater)
        areaDialog.setContentView(areaDialogBinding.root)
        areaDialogBinding.closeButton.setOnClickListener {areaDialog.dismiss() }
        viewModel.parkingZonesList.forEach{zone->
            val zoneItemBinding = ItemCarPlatesChooserBinding.inflate(layoutInflater)
            zoneItemBinding.plates.setText(zone.zoneName)
            zoneItemBinding.root.setOnClickListener{
                binding.zoneName.text = zone.zoneName
                viewModel.zone = zone
                Preferences.saveLastUsedZone(zone.zoneName)
                areaDialog.dismiss()
            }
            areaDialogBinding.areasHolder.addView(zoneItemBinding.root)
        }
        areaDialog.show()
    }
    private fun sendSms(){
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("sms:${viewModel.zone?.phoneNumber}"))
        intent.putExtra("sms_body",viewModel.plates)
        startActivity(intent)
    }
    private fun updatePaymentDetails(){
        viewModel.zone?.let{
            binding.cityAreaValue.text = it.zoneName
            binding.maxStayDurationValue.text = "${it.zoneMaxDuration} hr"
            binding.totalToPayValue.text =  "${it.zonePrice} Euro"
        }
       viewModel.plates?.let{
           binding.carPlatesValue.text = it
       }
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1000", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun showNotification(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, "1000")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Parking app reminder")
            .setContentText("U are running out of time to the parking payment")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(10001, builder.build())
        }
    }
}