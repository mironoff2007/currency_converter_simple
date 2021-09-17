package com.mironov.currency_converter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


class CustomAdapter(
    context: Context,
    titles: Array<String>,
    images: IntArray,
) : ArrayAdapter<String?>(context, R.layout.custom_spinner_row) {

    var spinnerTitles: Array<String> = titles
    var spinnerImages: IntArray = images
    var mContext: Context = context

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return spinnerTitles.size
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var mViewHolder = ViewHolder()
        if (convertView == null) {
            val mInflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.custom_spinner_row, null, false)
            mViewHolder.mFlag = convertView.findViewById<View>(R.id.ivFlag) as ImageView
            mViewHolder.mName = convertView.findViewById<View>(R.id.tvName) as TextView
            convertView.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as ViewHolder
        }
        mViewHolder.mFlag!!.setImageResource(spinnerImages[position])
        mViewHolder.mName!!.text = spinnerTitles[position]
        return convertView!!
    }

    private class ViewHolder {
        var mFlag: ImageView? = null
        var mName: TextView? = null

    }

}
