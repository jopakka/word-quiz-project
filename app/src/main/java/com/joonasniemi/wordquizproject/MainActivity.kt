/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.joonasniemi.wordquizproject.game.Quiz

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if(Quiz.started){
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.inform_quitting_game)
                .setPositiveButton(R.string.yes) { _, _ ->
                    super.onBackPressed()
                    Quiz.resetGame()
                }.setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.cancel()
                }.create().show()
        } else
            super.onBackPressed()
    }
}