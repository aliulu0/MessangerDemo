package com.example.messengerdemo.adapters

import com.example.messengerdemo.R
import com.example.messengerdemo.SohbetMesajı
import com.example.messengerdemo.model.Kullanici
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.gecmis_mesajlar_row.view.*

class GecmisMesajSatirlari(val sohbetMesajı: SohbetMesajı) : Item<GroupieViewHolder>() {
    val mAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    var sohbetKisisi :Kullanici? = null
    override fun getLayout(): Int {
        return R.layout.gecmis_mesajlar_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val sohbetMesajiKisisi : String
        if (sohbetMesajı.gelenId == mAuth.uid){
            sohbetMesajiKisisi = sohbetMesajı.gidenId
        }
        else{
            sohbetMesajiKisisi = sohbetMesajı.gelenId
        }
        val refrence = database.getReference("/users/$sohbetMesajiKisisi")
        refrence.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot){
                sohbetKisisi = snapshot.getValue(Kullanici::class.java)
                sohbetKisisi?.let {
                    viewHolder.itemView.textViewGecmisMesajlarUsername.text = sohbetKisisi!!.username
                    viewHolder.itemView.textViewGecimisMesajlarMesaj.text = sohbetMesajı.mesaj
                    Picasso.get().load(sohbetKisisi!!.profilImageUrl).into(viewHolder.itemView.imageViewGecmisMesajlar)
                }
            }

        })

    }

}