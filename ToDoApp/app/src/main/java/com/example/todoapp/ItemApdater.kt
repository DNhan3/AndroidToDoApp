package com.example.todoapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ItemAdapter(private val itemList: MutableList<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleDetails)
        val deleteButton: Button = itemView.findViewById(R.id.delete_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.text // Assuming your item class has a text property
        holder.deleteButton.setOnClickListener {
            removeItem(position)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            val description = item.description

            intent.putExtra("des", description)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount() = itemList.size

    private fun removeItem(position: Int) {
        val db = Firebase.firestore
        val itemToRemove = itemList[position]

        db.collection("tasks").document(itemToRemove.id).delete()
            .addOnSuccessListener {
                itemList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemList.size - position)
            }
    }
}
