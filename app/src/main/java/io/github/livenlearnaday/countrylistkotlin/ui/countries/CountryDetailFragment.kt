package io.github.livenlearnaday.countrylistkotlin.ui.countries

import android.content.ActivityNotFoundException
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.livenlearnaday.countrylistkotlin.R
import io.github.livenlearnaday.countrylistkotlin.data.entity.Country
import io.github.livenlearnaday.countrylistkotlin.data.entity.Language
import io.github.livenlearnaday.countrylistkotlin.databinding.CountryDetailFragmentBinding
import io.github.livenlearnaday.countrylistkotlin.ui.MainActivity
import io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter.CallingCodeEvent
import io.github.livenlearnaday.countrylistkotlin.ui.countries.adapter.CallingCodesAdapter
import io.github.livenlearnaday.countrylistkotlin.utils.MessageUtils
import io.github.livenlearnaday.countrylistkotlin.utils.Status
import io.github.livenlearnaday.countrylistkotlin.utils.autoCleared
import io.github.livenlearnaday.countrylistkotlin.utils.loadSvgImage
import timber.log.Timber


@AndroidEntryPoint
class CountryDetailFragment : Fragment() {

    private var binding: CountryDetailFragmentBinding by autoCleared()

    private val viewModel: CountryDetailViewModel by viewModels()

    private var countryName: String = ""

    private lateinit var mCallingCodesAdapter: CallingCodesAdapter


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
        if ((activity as MainActivity).supportActionBar != null) {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as MainActivity).supportActionBar?.setHomeButtonEnabled(true)
            (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_cancel_24)
        }

        (activity as MainActivity).setToolBarTitle(getString(R.string.title_fragment_country_detail))

        countryName = arguments?.getString("countryName").toString()

        setupObserverDataFromDb()

    }

    private fun setupObserverDataFromDb() {

        viewModel.getCountry(countryName).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        binding.countryDetailLayout.visibility = View.VISIBLE
//                    binding.bottomAppBar.visibility = View.VISIBLE
//                    setBottomBarNav()
                        resource.data?.let { country ->
                            bindCountry(country)
                            viewModel.setUpFavToggle(binding.favCheckbox, country)

                        }
                    }
                    Status.ERROR -> {

                        binding.countryDetailLayout.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
//                    binding.bottomAppBar.visibility = View.GONE

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.countryDetailLayout.visibility = View.GONE
//                    binding.bottomAppBar.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun bindCountry(country: Country) {
        binding.nameTextview.text = country.name
        binding.capitalTextview.text = country.capital
        binding.regionTextview.text = country.region
        binding.subregionTextview.text = country.subregion

        val languages: List<Language>? = country.languages
        val languageTxt = languages?.joinToString { language -> language.name.toString() }
        binding.languageTextview.text = languageTxt

        loadSvgImage(binding.flagImage, country.flag)

        if(country.callingCodes.isNotEmpty()) {
            binding.callingCodeRv.layoutManager = LinearLayoutManager(requireContext())
            mCallingCodesAdapter = CallingCodesAdapter(lifecycle) {
                onClickedCallingCode(it)
            }
            binding.callingCodeRv.addItemDecoration(
                DividerItemDecoration(
                    binding.callingCodeRv.context,
                    (binding.callingCodeRv.layoutManager as LinearLayoutManager).orientation
                )
            )
            binding.callingCodeRv.adapter = mCallingCodesAdapter
            mCallingCodesAdapter.updateList(country.callingCodes)
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


    private fun onClickedCallingCode(callingCodeEvent: CallingCodeEvent) {
        when (callingCodeEvent) {
            is CallingCodeEvent.callingCodeClicked -> {
                if (isTelephonyEnabled()) {
                    val callCodeWithPlusSign = "+${callingCodeEvent.callingCode}"
                    Timber.v("call callCodeWithPlusSign: %s", callCodeWithPlusSign)
//            val callIntent = Intent(Intent.ACTION_DIAL)
//            callIntent.type = "*/*"
//            callIntent.data = Uri.parse("tel:" + callCodeWithPlusSign)
////            callIntent.data = Uri.fromParts("tel",callCodeWithPlusSign,null)

//            Timber.v("call callIntent.data : " + callIntent.data.toString())

                    //            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", callCode, null))
                    /*  val intent = Intent(Intent.ACTION_DIAL);
                  intent.data = Uri.parse("tel:+$callCode")*/


                    try {

                        val callIntent: Intent =
                            Uri.parse("tel:+${callingCodeEvent.callingCode}").let { number ->
                                Intent(Intent.ACTION_DIAL, number)
                            }
//            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", callCode, null))
                        /*  val intent = Intent(Intent.ACTION_DIAL);
                          intent.data = Uri.parse("tel:+$callCode")*/
                        (activity as MainActivity).startActivity(callIntent)
/*
                val callIntent: Intent = Uri.parse("tel:$callCodeWithPlusSign").let { number ->
                    Intent(Intent.ACTION_DIAL, number)
                }

                Timber.v("call callIntent.data : " + callIntent.data.toString())


                (activity as MainActivity).startActivity(callIntent)*/

                    } catch (e: ActivityNotFoundException) {
                        MessageUtils.showAlertDialog(
                            requireContext(),
                            null,
                            getString(R.string.error_no_activity_to_handle_intent)
                        )
                    }

                } else {
                    MessageUtils.showAlertDialog(
                        requireContext(),
                        null,
                        getString(R.string.permission_enable_phone_permission)
                    )
                }
            }
        }
    }

}





