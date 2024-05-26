package com.bigOne.stayfittrainer.ui.mycourse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.FragmentWorkoutDetailsBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class WorkoutDetailsFragment : Fragment() {

    private val args: WorkoutDetailsFragmentArgs by navArgs()

    lateinit var binding: FragmentWorkoutDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        init()


    }
    private fun observe() {

    }

    private fun init() {
        val workout =args.workout
        binding.toolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.apply {
            textWorkoutName.text=workout.name
            workoutCalorie.text = workout.calorie
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady( youTubePlayer: YouTubePlayer) {
                    val videoId = getIdFromUrl(workout.url)
                    Log.e("videoId",videoId.toString())
                    if (videoId != null) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                }
            })
        }
    }

    private fun getIdFromUrl(url: String): String? {
        val startIndex = url.indexOf("?v=")

        if (startIndex == -1) {
            return null
        }

        val linkParamSymbolIndex = url.indexOf("&")

        val lastIndex = if (linkParamSymbolIndex == -1) {
            url.length
        } else {
            linkParamSymbolIndex
        }

        return url.substring(startIndex + 3, lastIndex)

    }

    companion object {

    }
}