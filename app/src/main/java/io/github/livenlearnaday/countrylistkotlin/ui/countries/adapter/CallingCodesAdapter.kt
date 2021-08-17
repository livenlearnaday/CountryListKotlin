package io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import io.github.livenlearnaday.countrylistkotlin.databinding.CallingCodesItemBinding
import javax.inject.Inject


@FragmentScoped
class CallingCodesAdapter @Inject constructor(
    private val callingCodesList: ArrayList<String>?,
    private val listener: CallingCodesItemListener
) :
    RecyclerView.Adapter<CallingCodesAdapter.CallingCodesViewHolder>() {

    interface CallingCodesItemListener {
        fun onClickedCallingCode(
            callCode: String
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallingCodesViewHolder {
        val binding =
            CallingCodesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallingCodesViewHolder(binding, listener)
    }


    override fun getItemCount(): Int = callingCodesList!!.size

    override fun onBindViewHolder(holder: CallingCodesViewHolder, position: Int) =
        holder.bind(callingCodesList!![position])


    class CallingCodesViewHolder(
        private val itemBinding: CallingCodesItemBinding,
        private val listener: CallingCodesItemListener
      ) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        private lateinit var callCode: String

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: String) {
            this.callCode = item
            itemBinding.callingCodeTextview.text = item


        }

        override fun onClick(v: View?) {
            callCode.let { listener.onClickedCallingCode(callCode) }
        }
    }


}


