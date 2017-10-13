package com.wilderpereira.lmgtfygen.presentation.main

import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxAdapterView
import com.jakewharton.rxbinding.widget.RxCompoundButton
import com.jakewharton.rxbinding.widget.RxTextView
import com.kobakei.ratethisapp.RateThisApp
import com.kobakei.ratethisapp.RateThisApp.showRateDialogIfNeeded
import com.wilderpereira.lmgtfygen.App
import com.wilderpereira.lmgtfygen.R
import com.wilderpereira.lmgtfygen.extensions.hideKeyboard
import com.wilderpereira.lmgtfygen.presentation.main.shortenUrl.ShortenUrlSuccessUiModel
import com.wilderpereira.lmgtfygen.presentation.uriModel.FailureUiModel
import com.wilderpereira.lmgtfygen.presentation.uriModel.InProgressUiModel
import com.wilderpereira.lmgtfygen.presentation.uriModel.UiModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.indeterminateProgressDialog
import rx.Observable
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainPresenter.View {

    @Inject lateinit var presenter: MainPresenter

    private val mLoadingDialog: ProgressDialog by lazy { indeterminateProgressDialog("Shortening url...", "Please wait")
            .apply { setCancelable(false) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onDestroy() {
        presenter.detatchView()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun displayRateDialogIfNeeded() {
        showRateDialogIfNeeded(this)
    }

    private fun updateGeneratedUrl(newString: String) {
        generatedUrlTv.text = newString
    }

    private fun displayToast(message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun showLoading() = mLoadingDialog.show()

    private fun hideLoading() = mLoadingDialog.dismiss()

    private fun copyToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("url", generatedUrlTv.text)
        clipboard.primaryClip = clip
        displayToast(R.string.url_copied)
    }

    private fun shareUrl() = ShareCompat.IntentBuilder
            .from(this)
            .setText(generatedUrlTv.text)
            .setType("text/plain")
            .startChooser()

    private fun init() {
        (application as App).component.inject(this)
        presenter.bindView(this)

        val config = RateThisApp.Config(3, 5)
        RateThisApp.init(config)
        RateThisApp.onCreate(this)

        loadSpinner()

        setListeners()
    }

    private fun loadSpinner() {
        val adapter = ArrayAdapter.createFromResource(
                this, R.array.search_types, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchTypeSpinner.adapter = adapter
    }

    private fun setListeners() {
        shareButton.setOnClickListener({ shareUrl() })

        copyButton.setOnClickListener({ copyToClipboard() })

        RxView.clicks(shortenButton)
                .flatMap { Observable.just(generatedUrlTv.text.toString()) }
                .flatMap { presenter.shortenUrl(it) }
                .subscribe { updateView(it) }

        RxAdapterView.itemSelections(searchTypeSpinner)
                .flatMap { Observable.just(searchTypeSpinner.selectedItem.toString()) }
                .flatMap { presenter.updateSearchType(it) }
                .subscribe { updateGeneratedUrl(it) }

        RxTextView.textChanges(searchBar)
                .flatMap { text -> presenter.updateSearchValue(text.toString()) }
                .subscribe { updateGeneratedUrl(it) }

        RxCompoundButton.checkedChanges(includeInternetExplorerCb)
                .flatMap { checked -> presenter.includeInternetExplainer(checked) }
                .subscribe { updateGeneratedUrl(it) }

        RxView.focusChanges(searchBar).subscribe { hasFocus -> if (!hasFocus) searchBar.hideKeyboard() }
    }

    private fun updateView(uiModel: UiModel) {
        when(uiModel) {
            is InProgressUiModel -> showLoading()
            is ShortenUrlSuccessUiModel -> {
                hideLoading()
                updateGeneratedUrl(uiModel.shortenedUrl)
                displayToast(R.string.url_shortened)
            }
            is FailureUiModel -> {
                hideLoading()
                displayToast(R.string.url_not_shortened)
            }
        }
    }
}
