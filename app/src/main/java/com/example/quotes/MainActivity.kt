package com.example.quotes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quotes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mShare : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mShare=findViewById(R.id.share_btn)
        mShare.setOnClickListener {
            val myIntent=Intent(Intent.ACTION_SEND)
            myIntent.type="type/palin"
            val shareBody="You are body"
            val shareSub="You subject here"
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody)
            myIntent.putExtra(Intent.EXTRA_TEXT,shareSub)
            startActivity(Intent.createChooser(myIntent,"Share your Quotes"))
        }
        binding.nextBtn.setOnClickListener {
            getQuote()
        }
    }
    private fun getQuote(){
        setInProgress(true)
        GlobalScope.launch {
            try {
                val response = RetrofitInstance.quoteApi.getRandomQuote()
                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let {
                        setUI(it)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
    private fun setUI(quote: QuoteModel) {
        binding.quoteTv.text = quote.q
        binding.authorTv.text = quote.a
    }
    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.nextBtn.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.nextBtn.visibility = View.VISIBLE
        }
    }

}



