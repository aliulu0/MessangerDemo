package com.example.messengerdemo.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.messengerdemo.R
import com.example.messengerdemo.SohbetMesajı
import com.example.messengerdemo.adapters.GecmisMesajSatirlari
import com.example.messengerdemo.model.Kullanici
import com.example.messengerdemo.registerlogin.KayitOlActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_gecmis_mesajlar.*

class GecmisMesajlarActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    companion object{
        var guncelKullanici : Kullanici? = null
    }
    val adapter = GroupAdapter<GroupieViewHolder>()
    val gecmisMesajHashMap = HashMap<String,SohbetMesajı>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gecmis_mesajlar)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        gecmisMesajlariDinle()
        guncelKullaniciyiGetir()
        girisKontrol()
        recyclerViewGecmisMesajlar.adapter = adapter
        recyclerViewGecmisMesajlar.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this,MesajActivity::class.java)
            val row = item as GecmisMesajSatirlari
            val mesajlasilanKisi = row.sohbetKisisi
            intent.putExtra("user",mesajlasilanKisi)
            startActivity(intent)
        }
    }

    private fun gecmisMesajlariDinle(){
        val gelenId = mAuth.uid
        val refrence = database.getReference("/lates-messages/$gelenId")
        refrence.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val sohbetMesajı = snapshot.getValue(SohbetMesajı::class.java)
                sohbetMesajı?.let {
                    adapter.add(GecmisMesajSatirlari(it))
                    gecmisMesajHashMap[snapshot.key!!] = sohbetMesajı
                    mesajlariYenile()
                }

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val sohbetMesajı = snapshot.getValue(SohbetMesajı::class.java)
                sohbetMesajı?.let {
                    adapter.add(GecmisMesajSatirlari(it))
                    gecmisMesajHashMap[snapshot.key!!] = sohbetMesajı
                    mesajlariYenile()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })
        recyclerViewGecmisMesajlar.scrollToPosition(adapter.itemCount)
    }
    private fun mesajlariYenile(){
        adapter.clear()
         gecmisMesajHashMap.values.forEach {
             adapter.add(GecmisMesajSatirlari(it))
         }
    }

    private fun girisKontrol(){
        val uid = mAuth.uid
        if (uid == null){
            val intent = Intent(this, KayitOlActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
    private fun guncelKullaniciyiGetir(){
        val uid = mAuth.uid
        val refrence = database.getReference("users/$uid")
        refrence.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                guncelKullanici = snapshot.getValue(Kullanici::class.java)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.yeni_mesaj ->{
                val intent = Intent(this, YeniMesajlarActivity::class.java)
                startActivity(intent)
            }
            R.id.cikis ->{
                mAuth.signOut()
                val intent = Intent(this, KayitOlActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}