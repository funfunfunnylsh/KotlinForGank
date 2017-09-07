package gank.kotlinforgank.ui.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import gank.kotlinforgank.MyApplication
import gank.kotlinforgank.R
import gank.kotlinforgank.network.Entity.DetailsData
import java.text.SimpleDateFormat


/**
 * Created by admin on 2017/9/4.
 */
class GankAdapter : BaseRecycleViewAdapter<DetailsData>() {

    override fun bindTheData(holder: BaseViewHolder, data: DetailsData, position: Int) {
        holder.setText(R.id.gank_title, data.desc)

        val image = holder.getView(R.id.image) as ImageView

        if (data.images == null) {
            image.visibility = View.GONE
        } else {
            image.visibility = View.VISIBLE

            val options = RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(MyApplication.context())
                    .load(data.images[0])
                    .apply(options)
                    .into(image)
        }

        holder.setText(R.id.type, data.type)
        // 初始化时设置 日期和时间模式
        var sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        // 修改日期和时间模式
        sdf.applyPattern("yyyy-MM-dd ")
        var dateStr = sdf.format(data.publishedAt)
        holder.setText(R.id.time, dateStr)
        data.who?.let {
            holder.setText(R.id.who, data.who)
        }
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_gank_recycle_view
    }


}