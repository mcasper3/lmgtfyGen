package com.wilderpereira.lmgtfygen.domain.entity

import java.net.URLEncoder
import javax.inject.Inject

/**
 * Created by Wilder on 22/01/17.
 */
class SearchUrl @Inject constructor(){

    private val TYPE_REGEX = "(?<=t=)\\w*".toRegex()
    private val SEARCH_REGEX = "(?<=q=).*$".toRegex()
    private val INTERNET_EXPLAINER_REGEX = "(?<=iie=)\\d".toRegex()

    var url: String = "lmgtfy.com/?iie=0&t=&q="
    var kind = ""
    var shortUrl = ""
    var longUrl = ""

    override fun toString() = "SearchUrl(kind=$kind, shortUrl=$shortUrl, longUrl=$longUrl, url='$url')"

    fun updateSearchType(type: String): String = url.replace(TYPE_REGEX, URLEncoder.encode(type[0].toLowerCase().toString(), "UTF-8"))
            .also { url = it }

    fun updateSearchValue(searchValue: String) = url.replace(SEARCH_REGEX, URLEncoder.encode(searchValue, "UTF-8"))
            .also { url = it }

    fun includeInternetExplainer(shouldInclude: Boolean) = url.replace(INTERNET_EXPLAINER_REGEX, if(shouldInclude) "1" else "0" )
            .also { url = it }
}
