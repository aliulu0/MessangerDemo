package com.example.messengerdemo.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messengerdemo.R
import com.example.messengerdemo.SohbetMesajı
import com.example.messengerdemo.adapters.GelenMesajAraclari
import com.example.messengerdemo.adapters.GidenMesajAraclari
import com.example.messengerdemo.model.Kullanici
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_mesaj.*
import kotlinx.android.synthetic.main.gelen_mesaj_row.*
import kotlinx.android.synthetic.main.giden_mesaj_row.*
import java.time.Instant.now

class MesajActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private  var gidenKullanici: Kullanici? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mesaj)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adapter = GroupAdapter<GroupieViewHolder>()
        recyclerViewMesaj.adapter = adapter

        gidenKullanici = intent.getParcelableExtra<Kullanici>("user")
        supportActionBar?.title = gidenKullanici?.username

        mesajDinleyici()

        send_button.setOnClickListener {
            mesajGonder()
            mesajText.setText("")
        }
    }

    private fun mesajDinleyici() {
        val gelenId = mAuth.uid
        val gidenId = gidenKullanici?.uid
        val refrence = database.getReference("/user-messages/$gelenId/$gidenId")
        refrence.addChildEventListener(object : ChildEventListener{

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
               val sohbetMesajı =  snapshot.getValue(SohbetMesajı::class.java)
                sohbetMesajı?.let {

                    if (mAuth.currentUser != null){
                        if (it.gelenId == mAuth.currentUser!!.uid){
                            val guncelKullanici = GecmisMesajlarActivity.guncelKullanici
                            adapter.add(GidenMesajAraclari(it.mesaj,guncelKullanici))
                        }
                        else{
                            gidenKullanici = intent.getParcelableExtra<Kullanici>("user")
                            adapter.add(GelenMesajAraclari(it.mesaj,gidenKullanici))
                        }
                    }
                    recyclerViewMesaj.scrollToPosition(adapter.itemCount -1)
                }

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })
    }

    private fun mesajGonder(){
        val mesaj = mesajText.text.toString()
        val gelenId = mAuth.uid
        val user = intent.getParcelableExtra<Kullanici>("user")
        val gidenId = user?.uid
        val tarih = System.currentTimeMillis() / 1000


        val refrence = database.getReference("/user-messages/$gelenId/$gidenId").push()
        val gidenRefrence = database.getReference("/user-messages/$gidenId/$gelenId").push()

        if (!refrence.key.isNullOrEmpty()){
            if (gidenId != null){
                val sohbetMesajı = SohbetMesajı(refrence.key!!,mesaj,gelenId!!,gidenId,tarih)
                refrence.setValue(sohbetMesajı)
                    .addOnSuccessListener { void ->
                        mesajText.text.clear()
                        recyclerViewMesaj.scrollToPosition(adapter.itemCount - 1)
                    }
                gidenRefrence.setValue(sohbetMesajı)
                val gecmisMesajlarRefrence = database.getReference("/lates-messages/$gelenId/$gidenId")
                gecmisMesajlarRefrence.setValue(sohbetMesajı)

                val gecmisMesajlarGidenRefrence = database.getReference("/lates-messages/$gidenId/$gelenId")
                gecmisMesajlarGidenRefrence.setValue(sohbetMesajı)
            }

        }


    }
}