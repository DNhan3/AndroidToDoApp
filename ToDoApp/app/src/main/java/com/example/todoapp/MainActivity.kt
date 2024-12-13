package com.example.todoapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val CHANNEL_ID = "toDoAppNotification"

class MainActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private val items = mutableStateListOf<Item>()
    private lateinit var adapter: ItemAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mainactitvity)

        recyclerView = findViewById(R.id.recycler_view)
        adapter = ItemAdapter(items)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addTask: Button = findViewById(R.id.addTask_B)

        loadTasksFromFirestore()
        onClickBAddTask(addTask, this)

        createNotificationChannel()
    }



    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = "Test"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTasksFromFirestore() {
        val db = Firebase.firestore
        db.collection("tasks").get().addOnSuccessListener { querySnapshot ->
            items.clear() // Clear existing items before adding new ones
            for (document in querySnapshot.documents) {
                val title = document.getString("text") ?: ""
                val des = document.getString("description") ?: ""
                val documentId = document.id
                items.add(Item(title, des, documentId))
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun onClickBAddTask(addTask: Button, context: Context){
        addTask.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.addtask, null)
            val dialog = AlertDialog.Builder(context, R.style.MyDialogTheme)
                .setView(dialogView)
                .setTitle("Add Task")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val taskTitleEditText = dialogView.findViewById<EditText>(R.id.taskTitleEditText)
                val taskDescriptionEditText = dialogView.findViewById<EditText>(R.id.taskDescriptionEditText)
                val taskTitle = taskTitleEditText.text.toString()
                val taskDescription = taskDescriptionEditText.text.toString()

                val db = Firebase.firestore
                val newItem = Item_to_push(taskTitle, taskDescription)
                db.collection("tasks").add(newItem)
                    .addOnSuccessListener { documentReference ->
                        val newDocumentId = documentReference.id
                        val newItem1 = Item(taskTitle, taskDescription, newDocumentId)
                        items.add(newItem1)
                        adapter.notifyItemInserted(items.size - 1)
                    }

                dialog.dismiss()

            }
        }
    }
}




