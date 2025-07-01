package com.example.infinite_track.domain.repository

import com.example.infinite_track.domain.model.contact.ContactModel
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getContacts(): Flow<List<ContactModel>>
}
