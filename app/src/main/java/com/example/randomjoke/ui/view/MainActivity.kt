package com.example.randomjoke.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.randomjoke.Constants
import com.example.randomjoke.R
import com.example.randomjoke.model.database.FavoriteJokeDatabase
import com.example.randomjoke.model.database.FavoriteJokeRepository
import com.example.randomjoke.databinding.ActivityMainBinding
import com.example.randomjoke.model.datamodel.JokeService
import com.example.randomjoke.model.datamodel.JokesDataModel
import com.example.randomjoke.ui.viewmodel.FavJokeViewModelFactory
import com.example.randomjoke.ui.viewmodel.FavoriteJokeViewModel
import com.google.gson.Gson
import retrofit.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var bindind: ActivityMainBinding
    private lateinit var jokeViewModel: FavoriteJokeViewModel
    var selectedFavoriteJoke : String = ""
    var allListed : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        getRandomJoke()
        val dao = FavoriteJokeDatabase.getInstance(application).jokeDAO
        val repository = FavoriteJokeRepository(dao)
        val factory = FavJokeViewModelFactory(repository)
        jokeViewModel = ViewModelProvider(this, factory)[FavoriteJokeViewModel::class.java]
        bindind.myViewModel = jokeViewModel
        bindind.lifecycleOwner = this
        displayCityList()
    }

    private fun displayCityList() {
        jokeViewModel.jokes.observe(this, Observer {
            Log.i("MYTAG", it.toString())
            allListed = it.toString()
            for(z in allListed.indices){
                if (z.toString() == bindind.selectedJoke.text){
                    Toast.makeText(this, "Already Added to Favorites", Toast.LENGTH_SHORT).show()

                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_action_bar,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorites -> {
                val intent = Intent(this@MainActivity, FavoriteJokes::class.java)
                startActivity(intent)
                return true
            } R.id.shuffle_joke -> {
                getRandomJoke()
                return true
            }
            R.id.add_to_favorite -> {
                bindind.myViewModel?.inputJoke?.value = selectedFavoriteJoke
                bindind.myViewModel?.save()
                getRandomJoke()
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    fun getRandomJoke() {
        if (Constants.isNetworkAvailable(this@MainActivity)) {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: JokeService =
                retrofit.create<JokeService>(JokeService::class.java)
            val listCall: Call<JokesDataModel> = service.getJokes()

            listCall.enqueue(object : Callback<JokesDataModel> {
                @RequiresApi(Build.VERSION_CODES.N)
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    response: Response<JokesDataModel>,
                    retrofit: Retrofit
                ) {
                    if (response.isSuccess) {
                        val jokeList: JokesDataModel = response.body()
                        Log.i("Response Result", "$jokeList")
                        val jokeResponseJsonString = Gson().toJson(jokeList)
                        val editor = mSharedPreferences.edit()
                        editor.putString(Constants.JOKE_RESPONSE_DATA, jokeResponseJsonString)
                        editor.apply()
                        setupUI()
                    } else {
                        val sc = response.code()
                        when (sc) {
                            400 -> { Log.e("Error 400", "Bad Request")
                                Toast.makeText(this@MainActivity, "Invalid City", Toast.LENGTH_LONG).show()
                            }
                            404 -> {
                                Log.e("Error 404", "Not Found")
                                Toast.makeText(this@MainActivity, "Invalid City", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                Log.e("Error", "Generic Error")
                            }
                        }
                    }
                }
                override fun onFailure(t: Throwable) {
                    Log.e("Errorrrrr", t.message.toString())
                }
            })
        } else {
            Toast.makeText(this@MainActivity, "No internet connection available.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    fun setupUI() {
        val jokeResponseJsonString =
            mSharedPreferences.getString(Constants.JOKE_RESPONSE_DATA, "")
        if (!jokeResponseJsonString.isNullOrEmpty()) {
            val jokeList =
                Gson().fromJson(jokeResponseJsonString, JokesDataModel::class.java)
            val randomIndex = Random.nextInt(jokeList.size)
            Log.i("id", jokeList[randomIndex].id.toString())
            bindind.selectedJoke.text = "' " + jokeList[randomIndex].setup + " '"
            bindind.punchline.text = "- " +jokeList[randomIndex].punchline
            selectedFavoriteJoke = "' " + jokeList[randomIndex].setup + " '\n -"+jokeList[randomIndex].punchline
        }
    }
}