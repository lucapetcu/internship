package com.example.locationapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.locationapp.R
import com.example.locationapp.ShowUserLocationActivity
import com.example.locationapp.models.User

class UserAdapter(private val context: Context,
                  private var list: ArrayList<User>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = list[position]
        if (holder is MyViewHolder) {
            holder.tvName.text = user.user_name
            holder.tvEmail.text = user.user_email
            holder.llUser.setOnClickListener{
                val intent = Intent(context, ShowUserLocationActivity::class.java)
                intent.putExtra("username", user.user_name)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.rvItemName)
        val tvEmail: TextView = view.findViewById(R.id.rvItemEmail)
        val llUser: LinearLayout = view.findViewById(R.id.rvLinearLayout)
    }
}