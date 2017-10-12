package com.wilderpereira.lmgtfygen.presentation.main

import android.util.Log
import com.wilderpereira.lmgtfygen.R
import com.wilderpereira.lmgtfygen.domain.entity.SearchUrl
import com.wilderpereira.lmgtfygen.domain.entity.ShortenerBody
import com.wilderpereira.lmgtfygen.domain.repository.urlShortening.UrlShortenerApi
import com.wilderpereira.lmgtfygen.presentation.main.shortenUrl.ShortenUrlSuccessUiModel
import com.wilderpereira.lmgtfygen.presentation.uriModel.FailureUiModel
import com.wilderpereira.lmgtfygen.presentation.uriModel.UiModel
import com.wilderpereira.lmgtfygen.utils.TextProvider
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Wilder on 22/01/17.
 */
class MainPresenter @Inject constructor(
        private val urlShortener: UrlShortenerApi,
        private var searchUrl: SearchUrl,
        private val textProvider: TextProvider
) {
    private val TAG = "MainPresenter"

    private var view: View? = null

    fun bindView(view: View) {
        this.view = view
    }

    fun detatchView() {
        this.view = null
    }

    fun onResume() = view?.displayRateDialogIfNeeded()

    fun updateSearchType(type: String): Observable<String> = Observable.just(searchUrl.updateSearchType(type))

    fun updateSearchValue(searchValue: String): Observable<String> = Observable.just(searchUrl.updateSearchValue(searchValue))

    fun includeInternetExplainer(include: Boolean): Observable<String> = Observable.just(searchUrl.includeInternetExplainer(include))

    fun shortenUrl(bigUrl: String): Observable<UiModel> = urlShortener
            .shortenUrl(textProvider.getText(R.string.api_key), ShortenerBody(bigUrl.trimEnd().trimStart().replace(' ', '+')))
            .flatMap { (kind, shortUrl, longUrl) ->
                updateSearchUrl(kind, shortUrl, longUrl)
                Observable.just<UiModel>(ShortenUrlSuccessUiModel(shortUrl))
            }
            .onErrorReturn {
                Log.e(TAG, "Failed to shorten URL", it)
                FailureUiModel()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private fun updateSearchUrl(kind: String, shortUrl: String, longUrl: String) {
        searchUrl.kind = kind
        searchUrl.shortUrl = shortUrl
        searchUrl.longUrl = longUrl
    }

    interface View{
        fun displayRateDialogIfNeeded()
    }
}
