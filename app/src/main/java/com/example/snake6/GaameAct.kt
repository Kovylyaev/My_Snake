package com.example.snake6

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.LinkedList
import java.util.Queue
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.Pair
import java.io.File


var body : Queue<Pair<Int, Int>> = LinkedList()
var direction: Int = 0      // 1 -> RIGHT
// 2 -> LEFT
// 3 -> UP
// 4 -> DOWN
var views: ArrayList<ArrayList<ImageView>> = ArrayList()
var apple = Pair(5, 8)
var non_body_cells : MutableSet<Pair<Int, Int>> = generateSequence(Pair(0, 0)) { if (it.first == 9 && it.second == 9) null else (if (it.second < 9) Pair(it.first, it.second + 1) else Pair(it.first + 1, 0)) }.toMutableSet()

class GaameAct : AppCompatActivity() {

    private var timer = object : CountDownTimer(10000000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val success = myOnTick()
            if (!success) {
                val text: String = if (body.size >= 99) {
                    "You win \nScore: ${body.size}"
                } else {
                    "Увы) Проигрыш \nScore: ${body.size}"
                }
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()

                if (body.size > Score) {
                    Score = body.size
                    var file = File(filePath, "Score.txt")
                    file.delete()
                    file = File(filePath, "Score.txt")
                    file.writeText(Score.toString())
                }

                score_txtVeiw.text = "Best score: " + Score
                stopAct()
            }
        }

        override fun onFinish() {
            stopAct()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaame)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        direction = 0
        body = LinkedList()
        views = ArrayList()
        apple = Pair(5, 8)
        non_body_cells = generateSequence(
            Pair(
                0,
                0
            )
        ) {
            if (it.first == 9 && it.second == 9) null else (if (it.second < 9) Pair(
                it.first,
                it.second + 1
            ) else Pair(it.first + 1, 0))
        }.toMutableSet()


        var game_paused = false
        val btnUp = findViewById<Button>(R.id.btn_up)
        val btnDown = findViewById<Button>(R.id.btn_down)
        val btnLeft = findViewById<Button>(R.id.btn_left)
        val btnRight = findViewById<Button>(R.id.btn_right)
        btnUp.setOnClickListener {
            if (!game_paused) {
                if (direction != 4 && direction != 3) {
                    direction = 3
                    timer.restart()
                }
            }
        }
        btnDown.setOnClickListener {
            if (!game_paused) {
                if (direction != 3 && direction != 4) {
                    direction = 4
                    timer.restart()

                }
            }
        }
        btnLeft.setOnClickListener {
            if (!game_paused) {
                if (direction != 1 && direction != 2) {
                    direction = 2
                    timer.restart()
                }
            }
        }
        btnRight.setOnClickListener {
            if (!game_paused) {
                if (direction != 2 && direction != 1) {
                    direction = 1
                    timer.restart()
                }
            }
        }
        val btnBack = findViewById<Button>(R.id.btn_back_from_game)
        btnBack.setOnClickListener {
            finish()
        }
        val btnPause = findViewById<Button>(R.id.pause)
        btnPause.setOnClickListener {
            if (game_paused){
                timer.start()
            }
            else{
                timer.cancel()
            }
            game_paused = !game_paused
        }
        val btnStart = findViewById<Button>(R.id.btn_start)
        btnStart.setOnClickListener {
            btnUp.visibility = View.VISIBLE
            btnDown.visibility = View.VISIBLE
            btnLeft.visibility = View.VISIBLE
            btnRight.visibility = View.VISIBLE
            btnPause.visibility = View.VISIBLE
            btnStart.visibility = View.INVISIBLE
            btnBack.visibility = View.INVISIBLE
            addHead(5, 1)
            paintApple()
            direction = 1
            timer.start()
        }


        val rows: ArrayList<TableRow> = ArrayList()
        val table = findViewById<TableLayout>(R.id.Table)
        var count = table.childCount
        for (i in 0 until count) {
            rows.add(table.getChildAt(i) as TableRow)
        }

        for (row in rows) {
            val viewsRow: ArrayList<ImageView> = ArrayList()
            count = row.childCount
            for (i in 0 until count) {
                viewsRow.add(row.getChildAt(i) as ImageView)
            }
            views.add(viewsRow)
        }
    }

    fun stopAct() {
        timer.cancel()
        body.clear()
        views.clear()
        val data = Intent()
        data.putExtra("key", 0)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}


fun CountDownTimer.restart() {
    this.cancel()
    this.start()
}

fun myOnTick() : Boolean{
    if (direction != 0) {
        when (direction) {
            1 -> {
                if (body.last()?.second == 9) return false
                if (body.contains(Pair(body.last().first, body.last().second + 1)) && Pair(body.last().first, body.last().second + 1) != Pair(body.peek()?.first, body.peek()?.second)) return false

                addHead(body.last().first, body.last().second + 1)
            }

            2 -> {
                if (body.last()?.second == 0) return false
                if (body.contains(Pair(body.last().first, body.last().second - 1)) && Pair(body.last().first, body.last().second - 1) != Pair(body.peek()?.first, body.peek()?.second)) return false

                addHead(body.last().first, body.last().second - 1)
            }

            3 -> {
                if (body.last()?.first == 0) return false
                if (body.contains(Pair(body.last().first - 1, body.last().second)) && Pair(body.last().first - 1, body.last().second) != Pair(body.peek()?.first, body.peek()?.second)) return false

                addHead(body.last().first - 1, body.last().second)
            }

            4 -> {
                if (body.last()?.first == 9) return false
                if (body.contains(Pair(body.last().first + 1, body.last().second)) && Pair(body.last().first + 1, body.last().second) != Pair(body.peek()?.first, body.peek()?.second)) return false

                addHead(body.last().first + 1, body.last().second)
            }
        }

        if (body.last().first == apple.first && body.last().second == apple.second) {
            apple = non_body_cells.random()
            paintApple()
        } else {
            if (body.last() != body.peek()) {
                deleteTail()
            }
            else
            {
                body.remove()
            }
        }
    }
    return true
}

fun addHead(y : Int, x : Int){
    if (body.isNotEmpty()) {
        views[body.last().first][body.last().second].setImageResource(R.drawable.body)
    }
    body.add(Pair(y, x))
    when (direction) {
        1 -> views[y][x].setImageResource(R.drawable.head1)
        2 -> views[y][x].setImageResource(R.drawable.head2)
        3 -> views[y][x].setImageResource(R.drawable.head3)
        4 -> views[y][x].setImageResource(R.drawable.head4)
    }
    non_body_cells.remove(Pair(body.last().first, body.last().second))
}

fun deleteTail()
{
    if (body.isNotEmpty()) {
        views[body.peek()!!.first][body.peek()!!.second].setImageDrawable(null)
        non_body_cells.add(Pair(body.peek()!!.first, body.peek()!!.second))
        body.remove()
    }
}

fun paintApple(){
    views[apple.first][apple.second].setImageResource(R.drawable.apple)
}