package io.github.livenlearnaday.countrylistkotlin.ui.countries


import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat.recreate
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.livenlearnaday.countrylistkotlin.R
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.databinding.CountryFragmentBinding
import io.github.livenlearnaday.countrylistkotlin.ui.MainActivity
import io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter.CountriesAdapter
import io.github.livenlearnaday.countrylistkotlin.utils.Status
import io.github.livenlearnaday.countrylistkotlin.utils.autoCleared
import timber.log.Timber


@AndroidEntryPoint
class CountriesFragment : Fragment(), CountriesAdapter.CountryItemListener {

    private var binding: CountryFragmentBinding by autoCleared()
    private val viewModel: CountriesViewModel by viewModels()
    private val detailViewModel: CountryDetailViewModel by viewModels()
    private lateinit var mSearchItem: MenuItem
    private lateinit var mCancelItem: MenuItem
    private lateinit var mAdapter: CountriesAdapter
    private var mPrevQuery: String = ""

    private var loaded: Boolean = false


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

        viewModel.getLoadedFlow().asLiveData().observe(viewLifecycleOwner, {
            if (it != null) {
                loaded = it
            }
        })

        Timber.v("onViewCreated loaded: %s", loaded)
        if(!loaded){
            viewModel.clearCountryTable()
        }
        setupUI()
        setupObservers()

    }


    private fun setupUI() {
        binding.countriesRv.layoutManager = LinearLayoutManager(requireContext())

        mAdapter = CountriesAdapter(arrayListOf(), this)
        binding.countriesRv.addItemDecoration(
            DividerItemDecoration(
                binding.countriesRv.context,
                (binding.countriesRv.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.countriesRv.adapter = mAdapter

    }

    private fun setupObserverDataFromDb() {
        Timber.v("setupObserverDataFromDb loaded: %s", loaded)
        viewModel.getAllCountriesFromDb().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        mCancelItem.isVisible = false
                        mSearchItem.isVisible = true
                        binding.countriesRv.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { countries ->
                            retrieveList(countries)
                        }


                    }
                    Status.ERROR -> {

                        binding.countriesRv.visibility = View.GONE
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
        })
    }


    private fun setupObserverDataFromApi() {
        Timber.v("setupObserverDataFromApi loaded: %s", loaded)
        viewModel.getAllCountries().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {

                        binding.countriesRv.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { countries ->
                            retrieveList(countries)
                        }

                        viewModel.insertAllCountries(resource.data!!)

                        Timber.v("setupObserverDataFromApi before setting is loaded: %s", loaded)

                        loaded = true

                        viewModel.setLoaded(loaded)

                        Timber.v("setupObserverDataFromApi after setting is loaded: %s", loaded)


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
        })
    }

    override fun onClickedCountry(countryName: String) {
        findNavController().navigate(
            R.id.action_countriesFragment_to_countryDetailFragment,
            bundleOf(
                "countryName" to countryName
            )
        )
    }

    override fun onClickedFav(country: Country) {
        detailViewModel.updateFavCountry(country.favorite, country.name)
    }

    private fun retrieveList(countries: List<Country>) {
        mAdapter.apply {
            addAllCountries(countries)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_country_fragment, menu)
        val refreshItem: MenuItem = menu.findItem(R.id.refresh_menu)
        refreshItem.isVisible = true


        val clearAllFavs: MenuItem = menu.findItem(R.id.clear_all_fav_menu)
        clearAllFavs.isVisible = true

        val showFavs: MenuItem = menu.findItem(R.id.show_fav_menu)
        showFavs.isVisible = true

        mCancelItem = menu.findItem(R.id.cancel_menu)
        mCancelItem.isVisible = false


        mSearchItem = menu.findItem(R.id.search_menu)
        mSearchItem.isVisible = true
        val searchManager =
            (activity as MainActivity).getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = mSearchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo((activity as MainActivity).componentName))


        mSearchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {

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

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                //call all countries from database
                setupObserverDataFromDb()
                return true
            }
        })


    }


    private fun filter(query: String) {
        if (query != mPrevQuery && query != "" && query != "%") {
            val fixTextForQuery = "%$query%"
            mPrevQuery = fixTextForQuery

            viewModel.searchForItems(name = fixTextForQuery, capital = fixTextForQuery).observe(viewLifecycleOwner,
                {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding.countriesRv.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                resource.data?.let { countries ->
                                    retrieveList(countries)
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
                })

        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.v("menu: Enter menu selection")
        return when (item.itemId) {
            R.id.cancel_menu -> {
                Timber.v("menu: navigate up selected")
                setupObservers()
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
            .setPositiveButton(android.R.string.ok, { dialog, id ->
                mCancelItem.isVisible = false
                mSearchItem.isVisible = true
                viewModel.clearCountryTable()
                setupObserverDataFromApi()
            })
            .setNegativeButton(
                android.R.string.cancel,
                { dialog, id ->
                    dialog.dismiss()

                })

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.dialog_reload_countries_title)
        alert.show()


    }

    private fun clearAllFav() {
        viewModel.clearAllFavCountries()
    }


    private fun setObserverForFavList() {
        mCancelItem.isVisible = true
        mSearchItem.isVisible = false
        viewModel.getAllFavCountries().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.countriesRv.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { countries ->
                            retrieveList(countries)
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
        })

    }


    private fun setupObservers() {
        if (loaded) {
            setupObserverDataFromDb()
        } else {
            setupObserverDataFromApi()
        }
    }


    private fun confirmResetFav() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setMessage(R.string.dialog_reset_fav_countries_message)
            .setCancelable(true)
            .setPositiveButton(android.R.string.ok, { dialog, id ->
                clearAllFav()
                mCancelItem.isVisible = false
                mSearchItem.isVisible = true
                setupObservers()
            })
            .setNegativeButton(
                android.R.string.cancel,
                { dialog, id ->
                    dialog.dismiss()

                })

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.dialog_reset_fav_countries_title)
        alert.show()


    }



}


