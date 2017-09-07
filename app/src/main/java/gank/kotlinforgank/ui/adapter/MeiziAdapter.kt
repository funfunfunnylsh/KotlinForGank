package gank.kotlinforgank.ui.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import gank.kotlinforgank.MyApplication
import gank.kotlinforgank.R
import gank.kotlinforgank.network.Entity.DetailsData


/**
 * Created by admin on 2017/9/5.
 */
class MeiziAdapter : BaseRecycleViewAdapter<DetailsData>() {
    override fun bindTheData(holder: BaseViewHolder, data: DetailsData, position: Int) {
        val imageView = holder.getView(R.id.iv_pic) as ImageView

        val options = RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(MyApplication.context())
                .load(data.url)
                .apply(options)
                .into(imageView)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_photo
    }


}