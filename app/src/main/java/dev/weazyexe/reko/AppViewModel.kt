package dev.weazyexe.reko

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.weazyexe.reko.data.repository.FirebaseAuthRepository
import javax.inject.Inject

/**
 * Main [ViewModel] for the app
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    /**
     * Check user for log in status
     */
    fun checkUser() = firebaseAuthRepository.checkUser()
}