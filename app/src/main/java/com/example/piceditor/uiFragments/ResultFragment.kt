package com.example.piceditor.uiFragments


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.piceditor.databinding.FragmentResultBinding


class   ResultFragment : Fragment() {

    lateinit var mBinding: FragmentResultBinding

    private val args: ResultFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Toast.makeText(context, "Photo is Saved.", Toast.LENGTH_SHORT).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentResultBinding.inflate(inflater, container, false)

        val uri = args.uri

        mBinding.ivResultImage.setImageURI(Uri.parse(uri))

        eventListeners(uri)
        return mBinding.root
    }

    /**
     * Listen all social media & share button on click listeners.
     */
    private fun eventListeners(uri: String) {


        mBinding.ivHome.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
