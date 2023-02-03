package com.dybich.findwords

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val inputLettersLayout: TextInputLayout by lazy {
        findViewById(R.id.inputLetters_Layout)
    }
    private val inputLettersET: TextInputEditText by lazy {
        findViewById(R.id.inputLetters_ET)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }
    private val placeholder: TextView by lazy {
        findViewById(R.id.placeholder_TV)
    }



    private lateinit var searchWordsBTN: Button
    private lateinit var buttonText: TextView

    private lateinit var fadeIN: Animation
    private lateinit var fadeOUT: Animation


    private lateinit var recyclerView: RecyclerView

    private var wordslist = arrayListOf<Word>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        searchWordsBTN = findViewById(R.id.searchWords_BTN)
        buttonText = findViewById(R.id.buttonText)



        recyclerView = findViewById(R.id.wordsList_RV)

        fadeIN = AnimationUtils.loadAnimation(this, com.google.android.material.R.anim.abc_fade_in)
        fadeOUT = AnimationUtils.loadAnimation(this, com.google.android.material.R.anim.abc_fade_out)


        lettersCheck()


        if(savedInstanceState!=null){
            wordslist = savedInstanceState.getParcelableArrayList<Word>("restoreList")!!
            if(wordslist.size > 0){
                placeholder.visibility = View.GONE
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = WordsAdapter(wordslist)
                recyclerView.visibility = View.VISIBLE
            }
        }

        searchWordsBTN.setOnClickListener() {

            if ((inputLettersLayout.error == null) && (inputLettersET.text?.length != 0)) {


                buttonText.startAnimation(fadeOUT)
                buttonText.visibility = View.GONE


                startLoading()

                getData(inputLettersET.text.toString())


            } else if (inputLettersET.text?.length == 0) {
                inputLettersLayout.error = "Enter Letters"

            }

        }
    }
    private fun getData(letters: String,) {
        val url ="http://IPv4:Port/getWords/${letters}"

        var requestQueue: RequestQueue = Volley.newRequestQueue(this)



        var postRequest  =  StringRequest(Request.Method.GET,url, { response->

                var list = response.split(' ').toTypedArray()


                if(response.contains("error")){
                    Toast.makeText(this, "Connection Error", Toast.LENGTH_LONG).show()
                }

                else{
                    if(wordslist.size > 0){
                        wordslist.clear()
                    }
                    for(i in list){
                        var j = i.trim()
                        if(j != ""){
                            Log.d("TAG", Word(j).toString())
                            wordslist.add(Word(j))
                        }

                    }

                    if(wordslist.size > 0){
                        placeholder.visibility = View.GONE
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.setHasFixedSize(true)
                        recyclerView.adapter = WordsAdapter(wordslist)
                        recyclerView.visibility = View.VISIBLE

                    }
                    else{
                        placeholder.text = "No results found"
                        recyclerView.visibility = View.INVISIBLE
                        placeholder.visibility = View.VISIBLE
                    }
                    recyclerView.setOnTouchListener { v, event -> false }

                }
                stopLoading()

        }, { error ->

           if(error is NetworkError){
               Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show()
           }
            else if(error.message.toString() == "null"){
               Toast.makeText(this, "Connection Error", Toast.LENGTH_LONG).show()
           }
            else{
               Log.d("TAG", error.message.toString())
               Toast.makeText(this, "ERROR LISTENER: " +error.message.toString(), Toast.LENGTH_LONG).show()
           }

            recyclerView.setOnTouchListener { v, event -> false }
            stopLoading()
        })
        requestQueue.add(postRequest)

  }


    private fun lettersCheck(){
        inputLettersET.addTextChangedListener {
            var letters = it.toString()
            if(letters == ""){
                inputLettersLayout.error ="Enter some letters"
            }
            else if(letters.length >15){
                inputLettersLayout.error ="Too much letters"
            }
            else{
                inputLettersLayout.error = null
            }
        }
    }


    private fun startLoading(){

        recyclerView.setOnTouchListener { v, event -> true }
        buttonText.startAnimation(fadeOUT)
        buttonText.visibility = View.GONE

        inputLettersET.isEnabled = false
        searchWordsBTN.isEnabled = false
        progressBar.startAnimation(fadeIN)
        progressBar.visibility = View.VISIBLE

        recyclerView.isNestedScrollingEnabled = false;

    }

     private fun stopLoading(){

            progressBar.visibility = View.GONE
            progressBar.startAnimation(fadeOUT)
            buttonText.visibility = View.VISIBLE
            buttonText.startAnimation(fadeIN)
            searchWordsBTN.isEnabled = true
            inputLettersET.isEnabled = true

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("restoreList",wordslist)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}