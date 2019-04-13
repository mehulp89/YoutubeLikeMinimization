package com.capitalplus.myapps

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Mehul Patel
 * @mehulp89
 * on 13-04-2019.
 */
class FragmentThree : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(
            R.layout.fragment_three, container, false
        )
    }
}
