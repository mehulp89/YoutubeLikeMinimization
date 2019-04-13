package com.capitalplus.myapps

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlaybackControlView
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_four.view.*
import java.util.HashMap

/**
 * Created by Mehul Patel
 * @mehulp89
 * on 13-04-2019.
 */
class FragmentFour : Fragment() {
    var fragment: Fragment? = null
    lateinit var sheetBehavior: BottomSheetBehavior<*>
    private var simpleExoPlayerView: SimpleExoPlayerView? = null
    private var player: SimpleExoPlayer? = null
    val url = "http://techslides.com/demos/sample-videos/small.mp4"
    private var bandwidthMeter: BandwidthMeter? = null
    private var mediaDataSourceFactory: DataSource.Factory? = null
    private lateinit var mFullScreenDialog: Dialog
    private var mExoPlayerFullscreen = false
    private lateinit var mFullScreenIcon: ImageView
    private lateinit var mFullScreenButton: FrameLayout
    private var shouldAutoPlay: Boolean = false
    private var trackSelector: DefaultTrackSelector? = null
    private val STATE_RESUME_WINDOW = "resumeWindow"
    private val STATE_RESUME_POSITION = "resumePosition"
    private val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
    private var mResumeWindow: Int = 0
    private var mResumePosition: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_four, container,
            false
        )
        super.onActivityCreated(savedInstanceState)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        simpleExoPlayerView = view.findViewById(R.id.videoview)
        simpleExoPlayerView!!.player = player
        initializePlayer(view)
        view.play.setOnClickListener {
            toggle()
        }
        view.iv_close.setOnClickListener {
            releasePlayer()
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        view.play.setImageBitmap(getthumbnail(url))
        sheetBehavior = BottomSheetBehavior.from<RelativeLayout>(view.bottom_sheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        view.play.visibility = View.GONE
                        view.ll_close.visibility = View.GONE
                        view.main_media_frame.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            400
                        )
                        view.main_media_frame.requestLayout()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        view.play.visibility = View.VISIBLE
                        view.ll_close.visibility = View.VISIBLE
                        view.main_media_frame.layoutParams.height = 80
                        view.main_media_frame.layoutParams.width = 180
                        view.main_media_frame.requestLayout()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState!!.putInt(STATE_RESUME_WINDOW, mResumeWindow)
        outState!!.putLong(STATE_RESUME_POSITION, mResumePosition)
        outState!!.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen)
        super.onSaveInstanceState(outState)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
        }
    }

    private fun initializePlayer(view: View) {

        try {
            initFullscreenDialog(view)
            initFullscreenButton(view)

            if (!TextUtils.isEmpty(url)) {
                bandwidthMeter = DefaultBandwidthMeter()
                val trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
                player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

                val videoURI = Uri.parse(url)

                mediaDataSourceFactory = DefaultHttpDataSourceFactory("YouTube Sample")
                val extractorsFactory = DefaultExtractorsFactory()
                val mediaSource = ExtractorMediaSource(videoURI, mediaDataSourceFactory, extractorsFactory, null, null)

                simpleExoPlayerView!!.player = player
                simpleExoPlayerView!!.defaultArtwork = getthumbnail(url)

                player!!.prepare(mediaSource)

            }

        } catch (e: Exception) {
            Log.e("Fragment Four", " ExoPlayer error " + e.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        if (android.os.Build.VERSION.SDK_INT > 23) {
            initializePlayer(view!!)
        }
    }

    override fun onResume() {
        super.onResume()
        if (android.os.Build.VERSION.SDK_INT <= 23 || player == null) {
            initializePlayer(view!!)
        }
    }


    override fun onPause() {
        super.onPause()
        if (android.os.Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (android.os.Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }


    override fun onDestroy() {
        if (android.os.Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
        super.onDestroy()
    }


    private fun initFullscreenDialog(view: View) {
        mFullScreenDialog = object : Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            override fun onBackPressed() {
                if (mExoPlayerFullscreen) {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    closeFullscreenDialog(view)
                }

                super.onBackPressed()
            }
        }
    }

    private fun closeFullscreenDialog(view: View) {
        (simpleExoPlayerView!!.parent as ViewGroup).removeView(simpleExoPlayerView)
        (view.main_media_frame as FrameLayout).addView(simpleExoPlayerView)
        mExoPlayerFullscreen = false
        mFullScreenDialog.dismiss()
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_fullscreen_expand))

    }

    private fun initFullscreenButton(view: View) {
        var controlView = (simpleExoPlayerView!!.findViewById(R.id.exo_controller) as PlaybackControlView)
        mFullScreenIcon = controlView!!.findViewById<ImageView>(R.id.exo_fullscreen_icon)
        mFullScreenButton = controlView!!.findViewById<FrameLayout>(R.id.exo_fullscreen_button)
        mFullScreenButton.setOnClickListener {
            if (!mExoPlayerFullscreen) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                openFullscreenDialog(view)
            } else {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                closeFullscreenDialog(view)
            }
        }
    }

    private fun openFullscreenDialog(view: View) {
        (simpleExoPlayerView!!.parent as ViewGroup).removeView(simpleExoPlayerView)
        mFullScreenDialog.addContentView(
            simpleExoPlayerView,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_fullscreen_skrink))
        mExoPlayerFullscreen = true
        mFullScreenDialog.show()
    }

    private fun releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player!!.getPlayWhenReady()
            player!!.release()
            player = null
            trackSelector = null
        }
    }

    fun getthumbnail(videoURL: String): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever = MediaMetadataRetriever()

        if (Build.VERSION.SDK_INT >= 14)
            mediaMetadataRetriever!!.setDataSource(videoURL, HashMap<String, String>())
        else
            mediaMetadataRetriever!!.setDataSource(videoURL)
        bitmap = mediaMetadataRetriever!!.frameAtTime
        return bitmap
    }

    private fun toggle() {
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}
