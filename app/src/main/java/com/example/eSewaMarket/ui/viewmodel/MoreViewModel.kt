package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.Faqs
import com.example.eSewaMarket.data.repository.FaqRepository
import kotlinx.coroutines.launch

class MoreViewModel : ViewModel() {

    private val faqRepository = FaqRepository()
    val faq: LiveData<List<Faqs>> = faqRepository.faqs

    init {
        loadAllData()
    }

    private fun loadAllData(){
        loadFaqs()
    }

    fun loadFaqs() {
        viewModelScope.launch {
            faqRepository.fetchFaqs()
        }
    }
}