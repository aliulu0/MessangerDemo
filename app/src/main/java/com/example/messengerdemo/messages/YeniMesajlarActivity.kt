package com.example.messengerdemo.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messengerdemo.model.Kullanici
import com.example.messengerdemo.adapters.KullaniciAraclari
import com.example.messengerdemo.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_yeni_mesajlar.*

class YeniMesajlarActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yeni_mesajlar)

        supportActionBar?.title = "Kullanıcı Seçiniz"
        //val adapter = GroupAdapter<GroupieViewHolder>()
        //recyclerViewYeniMesaj.adapter = adapter

        database = FirebaseDatabase.getInstance()

        kullanicilariGetir()
    }
    private fun kullanicilariGetir(){
      val refrence = database.getReference("users")
        refrence.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach {
                    val user = it.getValue(Kullanici::class.java)
                    if (user != null){
                        adapter.add(KullaniciAraclari(user)
                        )
                    }

                    adapter.setOnItemClickListener { item, view ->
                        val kullaniciAraci = item as KullaniciAraclari
                        val intent = Intent(view.context,MesajActivity::class.java)
                        intent.putExtra("user",item.user)
                        startActivity(intent)
                        finish()
                    }

                }
                recyclerViewYeniMesaj.adapter = adapter
            }

        })





    }
}

