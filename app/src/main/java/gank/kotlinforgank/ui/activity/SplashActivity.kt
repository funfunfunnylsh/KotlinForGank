package gank.kotlinforgank.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.DecelerateInterpolator
import gank.kotlinforgank.R
import kotlinx.android.synthetic.main.activity_splash.*



/**
 * Created by admin on 2017/8/30.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animatorSet = AnimatorSet()//组合动画
        val scaleX = ObjectAnimator.ofFloat(tv_icon_splash, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(tv_icon_splash, "scaleY", 0f, 1f)

        animatorSet.duration = 2000
        animatorSet.interpolator = DecelerateInterpolator()
//        animatorSet.run {
//            play(scaleX).with(scaleY)//两个动画同时开始
//            start()
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator?) {
//                    super.onAnimationEnd(animation)
//
//                }
//            })
//        }
        animatorSet.play(scaleX).with(scaleY)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        })
        animatorSet.start()
    }

}