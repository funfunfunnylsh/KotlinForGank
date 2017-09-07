package gank.kotlinforgank.network

import com.nightssky.gankforkotlin.Model.GankService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by admin on 2017/8/31.
 */
class HttpManager
// 写在类名后的constructor()是主构造方法
private constructor() {
    val GnakUrl = "http://gank.io/api/"
    val DEFAULT_TIMEOUT:Long = 20
    var okHttpClient: OkHttpClient ?= null
    var retrofit : Retrofit ?= null

    init {
        //缓存地址
        okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()
        //retrofit创建了
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(GnakUrl)
                .build()

    }

    //companion object只能在类中使用，相当于java中的静态内部类
    companion object {
        val getInstence:HttpManager
                get() = SingletonHolder.instence

    }

    private object SingletonHolder {
        val instence = HttpManager()
    }

    fun create(): GankService? {
        return retrofit?.create(GankService::class.java)
    }

}