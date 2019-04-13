package com.capitalplus.myapps

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_one.view.*

/**
 * Created by Mehul Patel
 * @mehulp89
 * on 13-04-2019.
 */
class FragmentOne : Fragment() {
    var fragment: Fragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_one, container,
            false
        )
        view.textView1.setOnClickListener {
            fragment = FragmentOne()
            val someFragment = FragmentFour()
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(
                R.id.content_frame,
                someFragment
            )
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return view
    }
}
