package gank.kotlinforgank.ui.activity

import android.Manifest
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.nightssky.gankforkotlin.utils.FileUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import gank.kotlinforgank.MyApplication
import gank.kotlinforgank.R
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File


class ImageActivity : AppCompatActivity() {

    val rxPermissions: RxPermissions by lazy {
        RxPermissions(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_image)
        initView()
    }

    private fun initView() {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val url = intent.getStringExtra("url")
        url.let {
            val options = RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(this)
                    .load(url)
                    .apply(options)
                    .into(photo_view)
        }

        down.setOnClickListener {
            downImage(rxPermissions, this, url)
        }
    }

    /**
     * 保存图片
     *
     */
    fun downImage(rxPermissions: RxPermissions, activity: Activity, url: String) {
        rxPermissions.let {
            rxPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        if (granted) { // Always true pre-M
                            // I can control the camera now
                            Observable.just(url)
                                    .subscribeOn(Schedulers.io())
                                    .map {
                                        val bitmap = Glide.with(MyApplication.context())
                                                .asBitmap()
                                                .load(url)
                                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                                .get()
                                        try {

                                            FileUtils.saveBitmap(bitmap,
                                                    Environment
                                                            .getExternalStorageDirectory().absolutePath
                                                            + File.separator + Math.random())

                                            true
                                        } catch(e: Exception) {
                                            e.printStackTrace()
                                            false
                                        } finally {
                                            bitmap.recycle()
                                        }
                                    }
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object : Observer<Boolean> {
                                        override fun onComplete() {
                                        }

                                        override fun onNext(t: Boolean) {
                                            if (t) {
                                                Toast.makeText(activity, "下载成功啦", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onSubscribe(d: Disposable) {
                                        }

                                        override fun onError(e: Throwable) {
                                        }

                                    })
                        } else {
                            // Oups permission denied
                            Toast.makeText(activity, "下载失败,请在权限设置中打开所需权限", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }
}
