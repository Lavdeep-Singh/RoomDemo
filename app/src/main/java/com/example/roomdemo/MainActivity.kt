package com.example.roomdemo

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //val recycler_layout=findViewById<RecyclerView>(R.id.recycler_layout)
    val layoutManager=LinearLayoutManager(this)
    lateinit var itemList:ArrayList<Entity>
    lateinit var adapter:MainAdapter
    //val btn_add=findViewById<Button>(R.id.btn_add)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemList= DbAsyncGetAll(this).execute().get() as ArrayList<Entity>
        adapter=MainAdapter(this,itemList)
        recycler_layout.adapter=adapter
        recycler_layout.layoutManager=layoutManager

        if(itemList.size<1){
            tv_inserted.visibility=View.GONE
            tv_nothing.visibility=View.VISIBLE
        }else{
            tv_inserted.visibility=View.VISIBLE
            tv_nothing.visibility=View.GONE
        }

        btn_add.setOnClickListener {
            if(et_name.text.toString().isNotBlank() and et_email.text.toString().isNotBlank()){
                val ent=Entity(
                    0,et_name.text.toString(),et_email.text.toString()
                )
                DbAsyncTask(this,ent,1).execute()
                itemList.clear()
                itemList.addAll(DbAsyncGetAll(this).execute().get() as ArrayList<Entity>)
                adapter.notifyDataSetChanged()
            }
            Toast.makeText(this,"Data Inserted",Toast.LENGTH_SHORT).show()
            et_email.text.clear()
            et_name.text.clear()
        }

    }

    class DbAsyncTask(val context: Context, val entity: Entity,val mode:Int) : AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, Database::class.java, "emp-db").build()
        override fun doInBackground(vararg params: Void?):Boolean {

            when(mode){
                1->{
                    db.dao().insertData(entity)
                    db.close()
                    return true
                }
                2->{
                    db.dao().deleteData(entity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
    class DbAsyncGetAll(val context: Context):AsyncTask<Void,Void,List<Entity>>(){
        val db = Room.databaseBuilder(context, Database::class.java, "emp-db").build()
        override fun doInBackground(vararg params: Void?): List<Entity> {
            return db.dao().getAllData()
        }

    }
}
