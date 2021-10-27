package com.example.meme

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connect()
        loadmeme()
    }

    private fun connect()
    {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkinfo = cm.activeNetworkInfo
        if(null != networkinfo)
        {
            if (networkinfo.type == ConnectivityManager.TYPE_WIFI)
            {
                Toast.makeText(this, "WIFI IS CONNECTED", Toast.LENGTH_SHORT).show()
            }
            else if (networkinfo.type == ConnectivityManager.TYPE_MOBILE)
            {
                Toast.makeText(this, "MOBILE DATA IS CONNECTED", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadmeme(){
        // Instantiate the RequestQueue.
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

     // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentImageUrl = response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(imageView)
            },
            {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            })

     // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
    fun nextmeme(view: View) {
        loadmeme()
    }
    fun sharememe(view: View) {
      val intent = Intent(Intent.ACTION_SEND)
       intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey! checkout this cool meme I got it from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using")
        startActivity(chooser)
    }
}






