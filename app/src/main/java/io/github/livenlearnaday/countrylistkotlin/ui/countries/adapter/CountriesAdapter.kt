package io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import dagger.hilt.android.scopes.FragmentScoped
import io.github.livenlearnaday.countrylistkotlin.R
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.databinding.ItemCountryBinding
import javax.inject.Inject

@FragmentScoped
class CountriesAdapter @Inject constructor(
    private var countries: ArrayList<Country>,
    private val listener: CountryItemListener
) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {


    interface CountryItemListener {
        fun onClickedCountry(countryName: String)
        fun onClickedFav(country: Country)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding, listener)
    }


    override fun getItemCount(): Int = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])

    }

    class CountryViewHolder(
        private val itemBinding: ItemCountryBinding,
        private val listener: CountryItemListener
    ) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        private lateinit var country: Country

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: Country) {
            this.country = item
            itemBinding.name.text = item.name
            itemBinding.capital.text = item.capital

            val uri = Uri.parse(country.flag)
            GlideToVectorYou
                .init()
                .with(itemBinding.image.context)
                .setPlaceHolder(
                    R.drawable.border,
                    R.drawable.ic_baseline_error_outline_24
                ) //loading,error
                .load(uri, itemBinding.image)


            itemBinding.favCheckbox.setOnCheckedChangeListener { _, b ->
                country.favorite = b
                country.let { listener.onClickedFav(country) }

            }

            itemBinding.favCheckbox.isChecked = country.favorite!!


        }


        override fun onClick(v: View) {

            country.let { listener.onClickedCountry(country.name) }

        }


    }


    fun addAllCountries(countries: List<Country>) {
        this.countries.apply {
            clear()
            addAll(countries)
            notifyDataSetChanged()
        }
    }


}


