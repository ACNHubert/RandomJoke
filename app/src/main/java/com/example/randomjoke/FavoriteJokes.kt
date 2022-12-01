package com.example.randomjoke

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomjoke.adapter.JokeAdapter
import com.example.randomjoke.database.FavoriteJokeDatabase
import com.example.randomjoke.database.FavoriteJokeRepository
import com.example.randomjoke.database.ManageFavoriteJoke
import com.example.randomjoke.databinding.ActivityFavoriteJokesBinding
import com.example.randomjoke.viewmodel.FavJokeViewModelFactory
import com.example.randomjoke.viewmodel.FavoriteJokeViewModel


class FavoriteJokes : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteJokesBinding
    private lateinit var jokeViewModel: FavoriteJokeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_jokes)
        val dao = FavoriteJokeDatabase.getInstance(application).jokeDAO
        val repository = FavoriteJokeRepository(dao)
        val factory = FavJokeViewModelFactory(repository)
        jokeViewModel = ViewModelProvider(this, factory)[FavoriteJokeViewModel::class.java]
        binding.myViewModel = jokeViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

    }

    fun initRecyclerView(){
        binding.jokeRecyclerView.layoutManager = LinearLayoutManager(this)
        displayCityList()
    }


    private fun displayCityList() {
        jokeViewModel.jokes.observe(this, Observer {
            Log.i("MYTAG", it.toString())
            binding.jokeRecyclerView.adapter =
                JokeAdapter(it, { selectedItem: ManageFavoriteJoke -> listItemClicked(selectedItem) })
        })
    }

    private fun listItemClicked(manageJokes: com.example.randomjoke.database.ManageFavoriteJoke) {
        jokeViewModel.initUpdateAndDelete(manageJokes)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_action_bar,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.remove_to_favorite-> {
                binding.myViewModel?.Delete()
                Toast.makeText(
                    this,
                    "Removed to Favorites",
                    Toast.LENGTH_SHORT
                ).show()
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}