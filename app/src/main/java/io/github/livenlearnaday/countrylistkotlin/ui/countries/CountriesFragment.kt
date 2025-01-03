package io.github.livenlearnaday.countrylistkotlin.ui.countries


import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.livenlearnaday.countrylistkotlin.R
import io.github.livenlearnaday.countrylistkotlin.databinding.CountryFragmentBinding
import io.github.livenlearnaday.countrylistkotlin.ui.MainActivity
import io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter.CountriesAdapter
import io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter.CountryEvent
import io.github.livenlearnaday.countrylistkotlin.utils.Status
import io.github.livenlearnaday.countrylistkotlin.utils.autoCleared
import timber.log.Timber


@AndroidEntryPoint
class CountriesFragment : Fragment() {


    //use autocleared class, so that binding is nullified in onDestroyView
    private var binding: CountryFragmentBinding by autoCleared()
    private val viewModel: CountriesViewModel by viewModels()
    private val detailViewModel: CountryDetailViewModel by viewModels()
    private lateinit var mSearchItem: MenuItem
    private lateinit var mCancelItem: MenuItem
    private lateinit var mAdapter: CountriesAdapter
    private var mPrevQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setToolBarTitle(getString(R.string.title_fragment_country_list))

        initRecyclerView()
        setupCountriesObserver()

    }


    private fun initRecyclerView() {
        binding.countriesRv.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = CountriesAdapter(lifecycle) {
            onCountryClicked(it)
        }
        binding.countriesRv.addItemDecoration(
            DividerItemDecoration(
                binding.countriesRv.context,
                (binding.countriesRv.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.countriesRv.adapter = mAdapter

    }


    private fun setupCountriesObserver() {
        viewModel.getCountriesFromApi.observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.countriesRv.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { countries ->
                            mAdapter.updateList(countries)
                        }
                    }
                    Status.ERROR -> {
                        binding.countriesRv.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.countriesRv.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun setObserverForFavList() {
        mCancelItem.isVisible = true
        mSearchItem.isVisible = false
        viewModel.favCountries().observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.countriesRv.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { countries ->
                            mAdapter.updateList(countries)
                        }
                    }
                    Status.ERROR -> {
                        binding.countriesRv.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.countriesRv.visibility = View.GONE
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_country_fragment, menu)

        val refreshItem: MenuItem = menu.findItem(R.id.refresh_menu)
        refreshItem.isVisible = true

        val clearAllFavs: MenuItem = menu.findItem(R.id.clear_all_fav_menu)
        val showFavs: MenuItem = menu.findItem(R.id.show_fav_menu)
        viewModel.countriesFav.observe(viewLifecycleOwner) {
            clearAllFavs.isVisible = it.isNotEmpty()
            showFavs.isVisible = it.isNotEmpty()
        }

        mCancelItem = menu.findItem(R.id.cancel_menu)
        mCancelItem.isVisible = false


        mSearchItem = menu.findItem(R.id.search_menu)
        mSearchItem.isVisible = true
        val searchManager =
            (activity as MainActivity).getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = mSearchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo((activity as MainActivity).componentName))


        mSearchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                searchView.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(query: String): Boolean {
                        if (query != mPrevQuery && query != "" && query != "%") {
                            filter(query)
                        }
                        return true

                    }
                })
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                setupCountriesObserver()
                return true
            }
        })


    }


    private fun filter(query: String) {
//        var searchText = "$query%"
        if (query != mPrevQuery && query != "" && query != "%") {
            val fixTextForQuery = "%$query%"
            mPrevQuery = fixTextForQuery

            viewModel.searchedCountryResult(
                name = fixTextForQuery,
                capital = fixTextForQuery
            ).observe(
                viewLifecycleOwner
            ) {

                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.countriesRv.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            resource.data?.let { countries ->
                                mAdapter.updateList(countries)
                            }
                        }
                        Status.ERROR -> {
                            binding.countriesRv.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE

                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.countriesRv.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.d("menu: Enter menu selection")
        super.onOptionsItemSelected(item)
        Timber.d("menu, %d %s: navigate up selected", item.itemId, item.itemId.toString())

        return when (item.itemId) {
            R.id.cancel_menu -> {
                Timber.d("menu, cancel menu selected")
                mCancelItem.isVisible = false
                mSearchItem.isVisible = true
                setupCountriesObserver()
                true
            }
            R.id.refresh_menu -> {
                confirmRefresh()
                true
            }
            R.id.show_fav_menu -> {
                setObserverForFavList()
                true
            }
            R.id.clear_all_fav_menu -> {
                confirmResetFav()
                true
            }

            else -> super.onOptionsItemSelected(item)

        }
    }

    private fun confirmRefresh() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setMessage(R.string.dialog_reload_countries_message)
            .setCancelable(true)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                mCancelItem.isVisible = false
                mSearchItem.isVisible = true
                viewModel.refreshedCountries.observe(viewLifecycleOwner) {
                    it.data?.let { countries ->
                        mAdapter.updateList(countries)
                    }
                }
            }
            .setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ ->
                dialog.dismiss()

            }

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.dialog_reload_countries_title)
        alert.show()


    }


    private fun confirmResetFav() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setMessage(R.string.dialog_reset_fav_countries_message)
            .setCancelable(true)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                mCancelItem.isVisible = false
                mSearchItem.isVisible = true
                viewModel.clearAllFavCountries()
                viewModel.countriesFromDb.observe(viewLifecycleOwner) {
                    it.data?.let { countries ->
                        mAdapter.updateList(countries)
                    }
                }
            }
            .setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ ->
                dialog.dismiss()

            }

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.dialog_reset_fav_countries_title)
        alert.show()


    }




    private fun onCountryClicked(countryEvent: CountryEvent) {
        when (countryEvent) {
            is CountryEvent.countryClicked -> {
                findNavController().navigate(
                    R.id.action_countriesFragment_to_countryDetailFragment,
                    bundleOf(
                        "countryName" to countryEvent.country.name
                    )
                )
            }

            is CountryEvent.countryFavClicked -> {
                detailViewModel.updateFavCountry(
                    countryEvent.country.favorite,
                    countryEvent.country.name
                )
            }
        }
    }


}


