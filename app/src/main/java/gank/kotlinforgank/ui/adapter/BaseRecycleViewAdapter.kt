package gank.kotlinforgank.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * Created by admin on 2017/9/4.
 * D(实体类)代表了 数据源
 */
abstract class BaseRecycleViewAdapter<D> : RecyclerView.Adapter<BaseRecycleViewAdapter.BaseViewHolder>(), View.OnClickListener {

    private var mList = ArrayList<D>()
    private var clickListener: onItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecycleViewAdapter.BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getItemLayoutId(), parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseRecycleViewAdapter.BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener(this)
        holder.itemView.tag = position
        bindTheData(holder, mList!![position], position)
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onClick(v: View) {
        //点击回调
        if (clickListener != null) {
            clickListener!!.onItemClick(v.tag as Int, v)
        }
    }

    //添加数据
    fun addData(list: List<D>) {
        val size = this.mList!!.size
        this.mList!!.addAll(list)
        notifyItemInserted(size + 1)
    }

    //更新数据
    fun setData(list: List<D>) {
        if (mList!!.size > 0) {
            mList!!.clear()
        }
        mList!!.addAll(list)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): D? {
        if (position < mList!!.size) {
            return mList!![position]
        }
        return null
    }

    /**
     * 绑定数据

     * @param holder 视图管理者
     * *
     * @param data   数据源
     */
    protected abstract fun bindTheData(holder: BaseRecycleViewAdapter.BaseViewHolder, data: D, position: Int)

    /**
     * 获取视图
     */
    protected abstract fun getItemLayoutId(): Int

    /**
     * 自定义ViewHolder
     */
    class BaseViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun getView(viewId: Int): View {
            val view = itemView.findViewById<View>(viewId)
            return view
        }

        /**
         * 设置文本资源

         * @param viewId view id
         * *
         * @param s      字符
         */
        fun setText(viewId: Int, s: CharSequence): TextView {
            val view = itemView.findViewById<TextView>(viewId)
            view.text = s
            return view
        }
    }

    /**
     * 设置点击监听

     * @param clickListener 监听器
     */
    fun setItemClickListener(clickListener: onItemClickListener) {
        this.clickListener = clickListener
    }

    interface onItemClickListener {
        fun onItemClick(position: Int, v: View)
    }
}