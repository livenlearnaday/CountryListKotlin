package io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.databinding.ItemCountryBinding
import io.github.livenlearnaday.countrylistkotlin.utils.loadSvgImage
import javax.inject.Inject

@FragmentScoped
class CountriesAdapter @Inject constructor(
    val mLifecycle: Lifecycle,
    val mCountryEvent: (CountryEvent) -> Unit
) : ListAdapter<Country, CountriesAdapter.CountryViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Country>() {
            override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mItems = mutableListOf<Country>()

    fun updateList(countries: List<Country>) {
        mItems.clear()
        mItems.addAll(countries)
        submitList(countries)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding, mCountryEvent)

    }


    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CountryViewHolder(
        private val binding: ItemCountryBinding,
        val mCountryEvent: (CountryEvent) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var country: Country


        fun bind(item: Country) {
            this.country = item
            binding.name.text = item.name
            binding.capital.text = item.capital

            loadSvgImage(binding.image, country.flag)

            binding.favCheckbox.setOnCheckedChangeListener { _, b ->
                country.favorite = b
            }

            binding.favCheckbox.isChecked = country.favorite

            initListeners()

        }

        private fun initListeners() {
            binding.itemContainer.setOnClickListener {
                mCountryEvent.invoke(CountryEvent.countryClicked(country))
            }

            binding.favCheckbox.setOnClickListener {
                mCountryEvent.invoke(CountryEvent.countryFavClicked(country))
            }

        }


    }


}





