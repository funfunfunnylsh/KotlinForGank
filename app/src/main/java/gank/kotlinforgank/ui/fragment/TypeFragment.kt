package gank.kotlinforgank.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import gank.kotlinforgank.R
import gank.kotlinforgank.network.Entity.typeData
import gank.kotlinforgank.network.HttpManager
import gank.kotlinforgank.network.RxSchedulers
import gank.kotlinforgank.ui.activity.WebActivity
import gank.kotlinforgank.ui.adapter.BaseRecycleViewAdapter
import gank.kotlinforgank.ui.adapter.GankAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_gank.*

/**
 * Created by admin on 2017/9/5.
 */
class TypeFragment : BaseFragment() {

    var typeStr: String = "Android"
    var pages = 0
    var isRefresh = true
    lateinit var adapter: GankAdapter

    override fun getLayoutResources(): Int {
        return R.layout.fragment_gank
    }

    override fun initView() {
        recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = GankAdapter()
        recyclerview.adapter = adapter
        adapter.setItemClickListener(object : BaseRecycleViewAdapter.onItemClickListener {
            override fun onItemClick(position: Int, v: View) {
                val data = adapter.getItem(position)

                startActivity(Intent(activity, WebActivity::class.java)
                        .putExtra("URL", data?.url))

            }

        })
        refreshLayout.setHeaderView(ProgressLayout(activity))
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


    fun setType(type: String): TypeFragment {
        typeStr = type
        return this
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