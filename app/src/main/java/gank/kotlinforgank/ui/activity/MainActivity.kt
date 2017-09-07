package gank.kotlinforgank.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import gank.kotlinforgank.R
import gank.kotlinforgank.ui.fragment.MeiziFragment
import gank.kotlinforgank.ui.fragment.TypeFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by admin on 2017/8/30.
 */
class MainActivity : AppCompatActivity() {
    val titles = arrayOf("Android","iOS","前端","休息视频","福利","拓展资源")
    var fragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (title in titles){
            tablayout.addTab(tablayout.newTab().setText(title))
            if (title.equals("福利")) fragments.add(MeiziFragment())
            else fragments.add(TypeFragment().setType(title))

        }

        var mAdapter = MyFragmentAdapter()
        container.offscreenPageLimit = 5
        container.adapter = mAdapter
        tablayout.setupWithViewPager(container)
    }


    inner class MyFragmentAdapter : FragmentStatePagerAdapter(supportFragmentManager){
        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles.get(position)
        }

    }

}