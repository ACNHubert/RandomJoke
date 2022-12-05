package com.example.randomjoke.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.randomjoke.R
import com.example.randomjoke.model.database.ManageFavoriteJoke
import com.example.randomjoke.databinding.JokeListItemBinding


class JokeAdapter (private val jokelist: List<ManageFavoriteJoke>
                   ,private val clickListener:(ManageFavoriteJoke)->Unit) : RecyclerView.Adapter<MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : JokeListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.joke_list_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return jokelist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(jokelist[position],clickListener)
    }

}

class MyViewHolder(val binding: JokeListItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(manageJokes: ManageFavoriteJoke, clickListener:(ManageFavoriteJoke)->Unit){
        binding.nameTextView.text = manageJokes.favoriteJoke
        binding.listItemLayout.setOnClickListener{
            clickListener(manageJokes)
        }
    }
}