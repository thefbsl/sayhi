package com.example.sayhi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mBdRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            val username = et_username.text.toString()
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Fill the gaps", Toast.LENGTH_SHORT).show()
            }else{
                register(username, email, password)
            }
        }
    }

    private fun register(username: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful){
                    addUserToDatabase(username, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this, MainUserListActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "Some error", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun addUserToDatabase(username: String, email: String, uid: String){
        mBdRef = FirebaseDatabase.getInstance().getReference()
        mBdRef.child("users").child(uid).setValue(User(username, email, uid))
    }
}