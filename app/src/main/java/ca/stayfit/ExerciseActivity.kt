package ca.stayfit

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ca.stayfit.databinding.ActivityExerciseBinding
import ca.stayfit.databinding.DialogBackConfirmationBinding
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExerciseIndex = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null
    private var restTimerDuration: Long = 1
    private var excerciseTimerDuration: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Working out"
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogBackConfirmation()
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        setupRestView()
        setupRecyclerView()
    }

    override fun onBackPressed() {
        customDialogBackConfirmation()
    }

    private fun customDialogBackConfirmation() {
        val dialog = Dialog(this)
        val dialogBinding = DialogBackConfirmationBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvYes.setOnClickListener {
            this@ExerciseActivity.finish()
            dialog.dismiss()
        }

        dialogBinding.tvNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView() {
        try {
            val soundURI = Uri.parse("android.resource://" + packageName + "/" + R.raw.ding.toString())
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding?.flProgressBarRest?.visibility = View.VISIBLE
        binding?.tvRest?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE

        binding?.flProgressBarExercise?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.ivExercise?.visibility = View.INVISIBLE

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        speakOut("Get ready for the next exercise")

        binding?.tvUpcomingExercise?.text = exerciseList!![currentExerciseIndex + 1].getName()

        setRestProgressBar()
    }

    private fun setupExerciseView() {
        binding?.flProgressBarRest?.visibility = View.INVISIBLE
        binding?.tvRest?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE

        binding?.flProgressBarExercise?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.ivExercise?.visibility = View.VISIBLE

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExerciseIndex].getName())

        binding?.tvExerciseName?.text = exerciseList!![currentExerciseIndex].getName()
        binding?.ivExercise?.let {
            Glide.with(this)
                .load(exerciseList!![currentExerciseIndex].getImage())
                .into(it)
        }

        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBarRest?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration * 1000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBarRest?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExerciseIndex++
                exerciseList!![currentExerciseIndex].setIsSelected(true)
                exerciseAdapter!!.notifyItemChanged(currentExerciseIndex)
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(excerciseTimerDuration * 1000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                exerciseList!![currentExerciseIndex].setIsSelected(false)
                exerciseList!![currentExerciseIndex].setIsCompleted(true)
                exerciseAdapter!!.notifyItemChanged(currentExerciseIndex)

                if(currentExerciseIndex < exerciseList?.size!! - 1) {
                    setupRestView()
                } else {
                    finish()
                    startActivity(Intent(this@ExerciseActivity, FinishActivity::class.java))
                }
            }
        }.start()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                Log.e("TTS", "The language specified is not supported!")
        }
        else
            Log.e("TTS", "Init failed!")
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if(tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null) {
            player!!.stop()
        }

        binding = null
    }
}