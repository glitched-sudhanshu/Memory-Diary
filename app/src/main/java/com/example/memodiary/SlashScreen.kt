package com.example.memodiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.memodiary.databinding.ActivitySlashScreenBinding

class SlashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflating the content view with ViewBinding
        val splashScreenBinding : ActivitySlashScreenBinding = ActivitySlashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        //to hide status and system bars
        hideSystemBars()

        val splashTopMiddleAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_top_to_middle)
        val splashBottomMiddleAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_bottom_to_middle)
        splashScreenBinding.splashScreenTxt1stHalf.animation = splashTopMiddleAnimation
        splashScreenBinding.splashScreenTxt2ndHalf.animation = splashBottomMiddleAnimation
        animationListener(splashTopMiddleAnimation)
    }

    private fun animationListener(animation: Animation?) {
        //setAnimationListener requires a AnimationListener
        animation?.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                //
            }

            override fun onAnimationEnd(p0: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    /**
                     * Handler class offers us to handle things with a delay
                     */
                    intent = Intent(this@SlashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 700)
            }

            override fun onAnimationRepeat(p0: Animation?) {
                //
            }
        })
    }

    private fun hideSystemBars() {
        /**
         * * did not use this piece of code because
         * 1. else statement is deprecated
         * 2. once we interact with the status bar it will not disappear again
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else
        {
        @Suppress("DEPRECATION")
        window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )
        }
         **/

        /**IMMERSIVE MODE**/
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}