package ca.stayfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flStartBtn : FrameLayout = findViewById(R.id.flStart)

        flStartBtn.setOnClickListener {
            Toast.makeText(
                this,
                "Here we start the excercise.",
                Toast.LENGTH_SHORT)
                .show()
        }
    }
}