package com.example.infinite_track.presentation.screen.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.domain.model.contact.ContactModel
import com.example.infinite_track.domain.use_case.contact.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {

    // All contacts from the repository
    private val _allContacts = MutableStateFlow<List<ContactModel>>(emptyList())

    // Search query input from UI
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Filtered contacts based on search query
    private val _filteredContacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val filteredContacts: StateFlow<List<ContactModel>> = _filteredContacts.asStateFlow()

    init {
        // Load all contacts once
        loadAllContacts()

        // Setup search filtering with debounce
        viewModelScope.launch {
            _searchQuery
                .debounce(300L) // Wait 300ms after typing stops
                .distinctUntilChanged()
                .collect { query ->
                    applyFilter(query)
                }
        }
    }

    private fun loadAllContacts() {
        viewModelScope.launch {
            _isLoading.value = true
            getContactsUseCase()
                .catch { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
                .collect { contacts ->
                    _allContacts.value = contacts
                    _filteredContacts.value = contacts // Initially show all contacts
                    _isLoading.value = false
                }
        }
    }

    private fun applyFilter(query: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // Simulate a short delay to show loading state
            kotlinx.coroutines.delay(200L)

            val filtered = if (query.isEmpty()) {
                _allContacts.value
            } else {
                _allContacts.value.filter {
                    it.fullName.contains(query, ignoreCase = true) ||
                            it.division.contains(query, ignoreCase = true)
                }
            }

            _filteredContacts.value = filtered
            _isLoading.value = false
        }
    }

    // Public function for UI to call when search query changes
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
