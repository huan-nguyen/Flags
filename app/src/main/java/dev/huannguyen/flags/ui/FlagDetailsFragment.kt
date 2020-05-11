package dev.huannguyen.flags.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.squareup.picasso.Picasso
import dev.huannguyen.flags.R
import dev.huannguyen.flags.domain.Flag
import kotlinx.android.synthetic.main.details_fragment.capital
import kotlinx.android.synthetic.main.details_fragment.currency
import kotlinx.android.synthetic.main.details_fragment.imageView
import kotlinx.android.synthetic.main.details_fragment.language
import kotlinx.android.synthetic.main.details_fragment.population
import kotlinx.android.synthetic.main.details_fragment.timeZone

class FlagDetailsFragment : Fragment() {

    companion object {
        const val KEY_FLAG = "flag"
        const val KEY_TRANSITION_NAME = "transition_name"
    }

    private val flag by lazy { arguments?.getParcelable<Flag>(KEY_FLAG) as Flag }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Picasso.with(context)
            .load(flag.url)
            .into(imageView)

        capital.text = flag.capital
        population.text = flag.population.toString()
        language.text = flag.language
        currency.text = flag.currency
        timeZone.text = flag.timeZone

        imageView.transitionName = arguments?.getString(KEY_TRANSITION_NAME)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = flag.country
        setHasOptionsMenu(true)
        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }
}
