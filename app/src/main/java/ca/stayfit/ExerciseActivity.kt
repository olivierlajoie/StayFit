package ca.stayfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.stayfit.databinding.ActivityExerciseBinding
import ca.stayfit.databinding.ActivityMainBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}