package com.arcadix.extranetschool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.appupdate.AppUpdateManager
import androidx.navigation.NavController

class MainActivity : AppCompatActivity() {
    var appUpdate : AppUpdateManager? = null
    val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Update
        appUpdate = AppUpdateManagerFactory.create(this)
        checkForUpdate()

        //functionality
        val emailField = findViewById<EditText>(R.id.emailLayoutText)
        val email = emailField.text
        val passwordField = findViewById<EditText>(R.id.passwordLayoutText)
        val password = passwordField.text
        val loginBtn = findViewById<Button>(R.id.button_sign_in)
        loginBtn.setOnClickListener {
           onLoginClick(email.toString(), password.toString())
        }
    }


    private fun onLoginClick( email: String, password: String){
        if((email.isNotEmpty() || password.isNotEmpty()) && (email == "m@a.com" && password == "abcd")){
            val intent = Intent(this, Teacher::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Field empty", Toast.LENGTH_SHORT).show()
        }
    }
    private  fun checkForUpdate(){
        appUpdate?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if(updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                appUpdate?.startUpdateFlowForResult(updateInfo, AppUpdateType.FLEXIBLE, this, REQUEST_CODE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }
    private fun inProgressUpdate(){
        appUpdate?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if(updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS ){
                appUpdate?.startUpdateFlowForResult(updateInfo, AppUpdateType.FLEXIBLE, this, REQUEST_CODE)
            }
        }
    }

}

