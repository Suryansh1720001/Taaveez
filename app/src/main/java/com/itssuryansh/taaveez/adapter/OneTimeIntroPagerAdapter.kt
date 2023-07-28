package com.itssuryansh.taaveez.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import com.itssuryansh.taaveez.R
import com.itssuryansh.taaveez.activity.OneTimeIntroActivity


class OneTimeIntroPagerAdapter(private val context: OneTimeIntroActivity,
                               )
    : PagerAdapter() {

    private val viewReferences: MutableList<View> = mutableListOf()
    private var listener: ViewPagerAdapterListener? = null


    interface ViewPagerAdapterListener {
        fun onReadyButtonClick()
    }


    private val images = intArrayOf(
        R.drawable.one_time_activity_image_1,
        R.drawable.one_time_activity_image_2,
        R.drawable.one_time_activity_image_3,

    )

    private val headings = intArrayOf(
        R.string.about_us,
        R.string.our_mission,
        R.string.our_vision,

    )

    private val descriptions = intArrayOf(
        R.string.desc_1_for_onetimeAcitivity,
        R.string.desc_2_for_onetimeActivity,
        R.string.desc_3_for_onetimeActivity,

    )

    override fun getCount(): Int {
        return headings.size

    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as LinearLayout
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {



        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.slider_layout_for_one_time_intro_activity, container, false)



        val slideTitleImage = view.findViewById<ImageView>(R.id.titleImage)
        val slideHeading = view.findViewById<TextView>(R.id.texttitle)
        val slideDescription = view.findViewById<TextView>(R.id.textdeccription)
        val ready = view.findViewById<ImageView>(R.id.ready)


        val next = view.findViewById<FrameLayout>(R.id.flNextButton)


        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        progressBar.progress =position+1

        if(position==2){
           ready.setImageResource(R.drawable.ready_imaage)
            progressBar.visibility = View.INVISIBLE;
        }
            slideTitleImage.setImageResource(images[position])

        next.setOnClickListener {
            listener?.onReadyButtonClick()
        }




        slideHeading.setText(headings[position])
        slideDescription.setText(descriptions[position])



        container.addView(view)

        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }


    fun setListener(listener: ViewPagerAdapterListener) {
        this.listener = listener
    }

}

