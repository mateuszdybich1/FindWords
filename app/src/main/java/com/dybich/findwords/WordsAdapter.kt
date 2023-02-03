package com.dybich.findwords


import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class WordsAdapter(private val wordsList: List<Word>) : RecyclerView.Adapter<Words_ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Words_ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.card_cell, parent,false)
        return Words_ViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    override fun onBindViewHolder(holder: Words_ViewHolder, position: Int) {
       holder.cellWORD.text = wordsList[position].word

                holder.cellBUTTON.setOnClickListener(){

                    val intent = Intent(it.context, DefinitionActivity::class.java)
                    intent.putExtra("WORD", holder.cellWORD.text)
                    it.context.startActivity(intent)
                }

    }



}
class Words_ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
            val cellWORD : TextView = itemView.findViewById(R.id.card_cell_TV)
            val cellBUTTON : Button = itemView.findViewById(R.id.card_cell_BTN)



}


