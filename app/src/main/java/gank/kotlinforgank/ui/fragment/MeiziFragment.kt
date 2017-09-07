package gank.kotlinforgank.ui.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import gank.kotlinforgank.R
import gank.kotlinforgank.network.Entity.typeData
import gank.kotlinforgank.network.HttpManager
import gank.kotlinforgank.network.RxSchedulers
import gank.kotlinforgank.ui.activity.ImageActivity
import gank.kotlinforgank.ui.adapter.BaseRecycleViewAdapter
import gank.kotlinforgank.ui.adapter.MeiziAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_gank.*
import kotlinx.android.synthetic.main.item_photo.*


/**
 * Created by admin on 2017/9/5.
 */
class MeiziFragment : BaseFragment() {

    var pages = 0
    var isRefresh = true
    lateinit var adapter: MeiziAdapter
    var typeStr: String = "福利"

    override fun getLayoutResources(): Int {
        return R.layout.fragment_gank
    }

    override fun initView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerview.layoutManager = layoutManager
        adapter = MeiziAdapter()
        recyclerview.adapter = adapter
        adapter.setItemClickListener(object : BaseRecycleViewAdapter.onItemClickListener {
            override fun onItemClick(position: Int, v: View) {
                startActivity(Intent(activity, ImageActivity::class.java)
                        .putExtra("url", adapter.getItem(position)?.url),
                        ActivityOptions.makeSceneTransitionAnimation(activity, iv_pic as ImageView, "image")
                                .toBundle())
            }

        })
        refreshLayout.setHeaderView(ProgressLayout(activity))
//        val loadingView = BallPulseView(activity)
//        refreshLayout.setBottomView(loadingView)

        refreshLayout.setOnRefreshListener(mOnRefreshListener())
        refreshLayout.startRefresh()
    }

    inner class mOnRefreshListener : RefreshListenerAdapter() {
        override fun onRefresh(refreshLayout: TwinklingRefreshLayout) {
            isRefresh = true
            getTypeData()
        }


        override fun onLoadMore(refreshLayout: TwinklingRefreshLayout) {
            isRefresh = false
            getTypeData()

        }
    }

    fun getTypeData() {
        if (isRefresh) pages = 1
        else pages++
        val observable: Observable<typeData> = activity.let { HttpManager.getInstence.create()!!.getTypeData(typeStr, 10, pages) }
        observable.RxSchedulers().subscribe { typeData: typeData ->
            if (isRefresh) {
                adapter.setData(typeData.results)
                refreshLayout.finishRefreshing()
                isRefresh = false
            } else {
                adapter.addData(typeData.results)
                refreshLayout.finishLoadmore()
            }
        }

    }
}