package com.example.retrofitpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var retService: AlbumService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textview = findViewById<TextView>(R.id.text_view)
        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        val pathResponse:LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.getAlbum(3)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title:String? = it.body()?.title
            Toast.makeText(applicationContext,title,Toast.LENGTH_SHORT).show()
        })
        val responseLiveData:LiveData<Response<Albums>> = liveData {
            val response = retService.getSortedAlbums(3)
            emit(response)
        }
        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if(albumsList!=null){
                while (albumsList.hasNext()){
                    val albumsItem = albumsList.next()
                    val result:String =" "+"Album Title : ${albumsItem.title}"+"\n"+
                                " "+"Album id : ${albumsItem.id}"+"\n"+
                                " "+"User id : ${albumsItem.userId}"+"\n\n\n"
                    textview.append(result)
                }
            }
        })
    }

}