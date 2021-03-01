package com.example.messengerdemo.adapters

import com.example.messengerdemo.R
import com.example.messengerdemo.model.Kullanici
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.gelen_mesaj_row.view.*

class GelenMesajAraclari(val mesaj:String,val user:Kullanici?) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.gelen_mesaj_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textViewGelenMesaj.text = mesaj
        //gönderenin görselini gösterme
        if (user != null){
            val uri = user.profilImageUrl
            val gelenGorsel = viewHolder.itemView.imageViewGelenMesaj
            Picasso.get().load(uri).into(gelenGorsel)
        }

    }
}