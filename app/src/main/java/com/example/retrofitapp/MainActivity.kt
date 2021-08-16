package com.example.retrofitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitapp.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val Tag="Error"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible=true
            val response=try {
                RetrofitInstance.api.getTodos()
            }
            catch (e: IOException){
                Log.e(Tag,"IO Exception")
                return@launchWhenCreated
            }
            catch (e :HttpException){
                Log.e(Tag,"Htpp Exception")
                return@launchWhenCreated
            }
            if(response.isSuccessful&&response.body()!=null){
                todoAdapter.todos=response.body()!!
            }else{
                Log.e(Tag,"Response not successful")
            }
            binding.progressBar.isVisible=false
        }

    }

    private fun setupRecyclerView()=binding.rvTodos.apply {
        todoAdapter= TodoAdapter()
        adapter=todoAdapter
        layoutManager=LinearLayoutManager(this@MainActivity)

    }
}