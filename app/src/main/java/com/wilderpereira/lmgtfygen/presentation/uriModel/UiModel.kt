package com.wilderpereira.lmgtfygen.presentation.uriModel

sealed class UiModel
object InProgressUiModel : UiModel()
open class FailureUiModel : UiModel()
open class SuccessUiModel : UiModel()