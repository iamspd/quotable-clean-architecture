package com.example.quotes.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.quotes.domain.usecase.GetQuotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getQuotesUseCase: GetQuotesUseCase
) : ViewModel() {
    val pagingFlow = getQuotesUseCase().cachedIn(scope = viewModelScope)
}