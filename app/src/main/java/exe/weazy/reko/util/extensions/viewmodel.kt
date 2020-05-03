package exe.weazy.reko.util.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> ViewModel.subscribe(
    observable: Observable<T>,
    onNext: (T) -> Unit,
    onError: (t: Throwable) -> Unit
): Disposable = observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(onNext, onError)

fun <T : ViewModel> ViewModelStoreOwner.useViewModel(owner: ViewModelStoreOwner, vmClass: Class<T>): T {
    return ViewModelProvider(owner, ViewModelProvider.NewInstanceFactory()).get(vmClass)
}