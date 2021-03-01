package com.example.messengerdemo.adapters

import com.example.messengerdemo.R
import com.example.messengerdemo.model.Kullanici
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.groupie_row_yeni_mesaj.view.*

class KullaniciAraclari(val user: Kullanici) : Item<GroupieViewHolder>(){

    override fun getLayout(): Int {
        return R.layout.groupie_row_yeni_mesaj
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_yeni_mesaj.text = user.username
        //Görsel
        Picasso.get().load(user.profilImageUrl).into(viewHolder.itemView.yeni_mesaj_image)
    }
}