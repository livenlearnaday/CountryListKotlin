package io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import io.github.livenlearnaday.countrylistkotlin.databinding.CallingCodesItemBinding
import javax.inject.Inject

@FragmentScoped
class CallingCodesAdapter @Inject constructor(
    val mLifecycle: Lifecycle,
    val mCountryDetailEvent: (CallingCodeEvent) -> Unit
) :  RecyclerView.Adapter<CallingCodesAdapter.CallingCodesViewHolder>() {


    private var mItems = mutableListOf<String>()

    fun updateList(callingCodes: List<String>) {
        mItems.clear()
        mItems.addAll(callingCodes)
        callingCodes.forEachIndexed{ pos, code ->
            if(mItems[pos] != code ){
                notifyItemChanged(pos)
            }
        }
    }

    override fun getItemCount(): Int = mItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallingCodesViewHolder {
        val binding =
            CallingCodesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallingCodesViewHolder(binding, mCountryDetailEvent)
    }

    override fun onBindViewHolder(holder: CallingCodesViewHolder, position: Int) =   holder.bind(mItems[position])


    class CallingCodesViewHolder (
        private val binding: CallingCodesItemBinding,
        val mCountryDetailEvent: (CallingCodeEvent) -> Unit
      ) : RecyclerView.ViewHolder(binding.root){

        private lateinit var callCode: String


        fun bind(item: String) {
            this.callCode = item
            binding.callingCodeTextview.text = item

            initListeners()

        }

        private fun initListeners() {
            binding.callImageview.setOnClickListener {
                mCountryDetailEvent.invoke(CallingCodeEvent.callingCodeClicked(callCode))
            }

        }


    }


}






