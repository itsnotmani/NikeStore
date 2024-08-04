package com.example.nikestore.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nikestore.R
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.databinding.FragmentLoginBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener(){
            viewModel.login(binding.emailEt.text.toString(),binding.passwordEt.text.toString())
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        requireActivity().finish()
                    }
                })
        }

        binding.signUpLinkBtn.setOnClickListener(){
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer,SignUpFragment())
            }.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
