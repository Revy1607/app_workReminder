package com.datnt.app_workreminder


import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private var rcvTask: RecyclerView? = null
    private var taskAdapter: TaskAdapter? = null
    private var tvTitle: TextView? = null
    private var array: ArrayList<Task> = ArrayList()
    private var hander: Handler = Handler()
    private var runnable: Runnable? = null
    private var dataViewModel: DataViewModel? = null
    private val uri: Uri =
        Uri.parse("content://com.datnt.app_quanlycongviec.TaskContentProvider/task")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rcvTask = findViewById(R.id.rcvData)
        tvTitle = findViewById(R.id.tvTitle)

        taskAdapter = TaskAdapter(array)
        rcvTask?.adapter = taskAdapter


        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        val intent = packageManager.getLaunchIntentForPackage("com.datnt.app_quanlycongviec")
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra("", 1)
            startActivity(intent)
            getData()
        }else {
            Toast.makeText(this, "không xác định đươc ứng dụng", Toast.LENGTH_SHORT).show()
        }

        if(array.isEmpty()){
            tvTitle?.visibility = View.VISIBLE
            rcvTask?.visibility = View.GONE
        }else{
            tvTitle?.visibility = View.GONE
            rcvTask?.visibility = View.VISIBLE
        }
    }

    private fun getData() {
        runnable = object : Runnable {
            override fun run() {
                val cursor: Cursor? = contentResolver.query(uri, arrayOf("id", "name", "description", "time"), null, null, null
                )
                if (cursor != null) {
                    cursor.moveToFirst()
                    while (!cursor.isAfterLast) {
                        val taskId = cursor.getInt(0)
                        val taskName = cursor.getString(1)
                        val taskDescription = cursor.getString(2)
                        val taskTime = cursor.getString(3)


//                        array.add(array.size, Task(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)))
                        array.add(array.size, Task(taskId, taskName,taskDescription,taskTime))
                        val intent = Intent(this@MainActivity, TaskReceiver::class.java)
                        intent.action = "com.datnt.app_quanlycongviec.NEW_TASK"
                        intent.putExtra("taskId", taskId)
                        intent.putExtra("taskName", taskName)
                        intent.putExtra("taskDescription", taskDescription)
                        intent.putExtra("taskTime", taskTime)
                        this@MainActivity.sendBroadcast(intent)
                        cursor.moveToNext()
                    }
                    hander.removeCallbacks(runnable!!)
                }
                hander.postDelayed(this, 1000)
            }
        }
        runnable!!.run()
    }

}

