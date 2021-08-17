package io.github.livenlearnaday.countrylistkotlin.ui.countries

import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import dagger.hilt.android.AndroidEntryPoint
import io.github.livenlearnaday.countrylistkotlin.R
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.entity.Language
import io.github.livenlearnaday.countrylistkotlin.databinding.CountryDetailFragmentBinding
import io.github.livenlearnaday.countrylistkotlin.ui.MainActivity
import io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter.CallingCodesAdapter
import io.github.livenlearnaday.countrylistkotlin.utils.MessageUtils
import io.github.livenlearnaday.countrylistkotlin.utils.Status
import io.github.livenlearnaday.countrylistkotlin.utils.autoCleared


@AndroidEntryPoint
class CountryDetailFragment : Fragment(), CallingCodesAdapter.CallingCodesItemListener {

    private var binding: CountryDetailFragmentBinding by autoCleared()

    private val viewModel: CountryDetailViewModel by viewModels()

    private var countryName: String = ""

    private lateinit var adapter: CallingCodesAdapter

    private lateinit var mCountry: Country

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryDetailFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as MainActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_cancel_24)

        countryName = arguments?.getString("countryName").toString()

        setupObserverDataFromDb()


    }


    private fun setupObserverDataFromDb() {

        viewModel.getCountryFromDb(countryName).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    it.data?.let { countryResponse -> bindCountryFromDb(countryResponse) }
                    binding.progressBar.visibility = View.GONE
                    binding.countryDetailLayout.visibility = View.VISIBLE

                    viewModel.setUpFavToggle(binding.favCheckbox, mCountry)

                }
                Status.ERROR -> {

                    binding.countryDetailLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                        .show()
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.countryDetailLayout.visibility = View.GONE
                }
            }
        })


    }


    private fun bindCountryFromDb(country: Country) {
        binding.nameTextview.text = country.name
        binding.capitalTextview.text = country.capital
        binding.regionTextview.text = country.region
        binding.subregionTextview.text = country.subregion

        val languages: List<Language>? = country.languages
        val languageTxt = languages?.joinToString { language -> language.name.toString() }
        binding.languageTextview.text = languageTxt

        setupCallingCodesUI(country.callingCodes)


        val uri = Uri.parse(country.flag)
        GlideToVectorYou
            .init()
            .with(binding.flagImage.context)
            .setPlaceHolder(
                R.drawable.border,
                R.drawable.ic_baseline_error_outline_24
            ) //loading,error
            .load(uri, binding.flagImage)

        mCountry = country


    }

    private fun setupCallingCodesUI(callingCodeList: ArrayList<String>?) {
        binding.callingCodeRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = CallingCodesAdapter(callingCodeList, this)
        binding.callingCodeRv.adapter = adapter

    }

    override fun onClickedCallingCode(callCode: String) {

        if (isTelephonyEnabled()) {
            val callIntent: Intent = Uri.parse("tel:+$callCode").let { number ->
                Intent(Intent.ACTION_DIAL, number)
            }
            (activity as MainActivity).startActivity(callIntent)


        } else {
            MessageUtils.showAlertDialog(
                requireContext(),
                null,
                "Enable phone permission to use this feature."
            )
        }

    }

    private fun isTelephonyEnabled(): Boolean {
        val telephonyManager =
            (activity as MainActivity).getSystemService(TELEPHONY_SERVICE) as TelephonyManager?
        return telephonyManager != null && telephonyManager.simState == TelephonyManager.SIM_STATE_READY
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                goToCountriesFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }

    }


    fun goToCountriesFragment() {
        findNavController().navigate(
            R.id.action_countryDetailFragment_to_countriesFragment
        )
    }

}





