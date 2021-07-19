package com.example.roomdemo

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.alert_custom.*

class MainAdapter(val context: Context,val itemList:ArrayList<Entity>):RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    class MainViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tv_name_single_row=view.findViewById<TextView>(R.id.tv_name_single_row)
        val tv_email_single_row=view.findViewById<TextView>(R.id.tv_email_single_row)
        val ic_edit=view.findViewById<ImageView>(R.id.ic_edit)
        val ic_delete=view.findViewById<ImageView>(R.id.ic_delete)
        val tv_id_single_row=view.findViewById<TextView>(R.id.tv_id_single_row)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.single_row,parent,false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val single=itemList[position]

        holder.tv_name_single_row.text=single.name
        holder.tv_email_single_row.text=single.email
        holder.tv_id_single_row.text=single.id.toString()
        val ent=Entity(
            single.id,
            single.name,
            single.email
        )

        holder.ic_edit.setOnClickListener {
               val dialog=Dialog(context)
            dialog.setContentView(R.layout.alert_custom)
            dialog.btn_update.setOnClickListener {
                if(dialog.et_name_updated.text.toString().isNotBlank() and dialog.et_email_updated.text.toString().isNotBlank()){
                    val enti=Entity(
                        single.id,dialog.et_name_updated.text.toString(),
                        dialog.et_email_updated.text.toString()
                    )
                    if(DbAsyncTask(context,enti,3).execute().get()){
                        itemList.clear()
                        itemList.addAll(DbAsyncGetAll(context).execute().get() as ArrayList<Entity>)
                        notifyDataSetChanged()
                        Toast.makeText(context,"Data updated",Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }
            dialog.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        holder.ic_delete.setOnClickListener {
           if(DbAsyncTask(context,ent,2).execute().get()) {
            itemList.clear()
            itemList.addAll(DbAsyncGetAll(context).execute().get() as ArrayList<Entity>)
            notifyDataSetChanged()
            Toast.makeText(context,"Data deleted",Toast.LENGTH_SHORT).show()
           }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size

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
                3->{
                    db.dao().updateData(entity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
    class DbAsyncGetAll(val context: Context): AsyncTask<Void, Void, List<Entity>>(){
        val db = Room.databaseBuilder(context, Database::class.java, "emp-db").build()
        override fun doInBackground(vararg params: Void?): List<Entity> {
            return db.dao().getAllData()
        }

    }
}