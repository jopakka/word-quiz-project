/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.joonasniemi.wordquizproject.game.Quiz

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if(Quiz.started){
            Log.i("MainMenu", "Game is on")
            val builder = AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Do you really wanna quit the game?")
                .setPositiveButton(R.string.yes) { _, _ ->
                    super.onBackPressed()
                    Quiz.resetGame()
                }.setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create().show()
        } else
            super.onBackPressed()
    }
}