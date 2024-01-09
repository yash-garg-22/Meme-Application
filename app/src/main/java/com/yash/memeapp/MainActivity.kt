package com.yash.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isGone
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    lateinit var meme : ImageView
    lateinit var next : Button
    lateinit var share : Button
    lateinit var determinateBar: ProgressBar
    var data: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        meme = findViewById(R.id.meme)
        next = findViewById(R.id.next)
        share = findViewById(R.id.share)
        determinateBar = findViewById(R.id.determinateBar)

        loadMeme()

        next.setOnClickListener {
            loadMeme()
        }

        share.setOnClickListener {
            val textToShare = "Hello, Check this cool meme by Redit $data"

            // Create an Intent with ACTION_SEND
            val shareIntent = Intent(Intent.ACTION_SEND)

            // Set the MIME type to plain text
            shareIntent.type = "text/plain"

            // Put the text to be shared as an extra to the Intent
            shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)


            // Start the activity with the SHARE intent
            startActivity(Intent.createChooser(shareIntent, "Share via..."))
        }

    }

    fun loadMeme(){
        determinateBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)

        val url = "https://meme-api.com/gimme"

         data = url

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val url = response.getString("url");
                Glide.with(this)
                    .load(url)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_LONG).show()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            determinateBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(meme)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Some error Occurred",Toast.LENGTH_LONG).show();
            }
        )
        queue.add(jsonObjectRequest);
    }
}