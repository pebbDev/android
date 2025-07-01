package com.example.infinite_track.data.repository.contact

import com.example.infinite_track.data.soucre.dummy.ContactDummyData
import com.example.infinite_track.domain.model.contact.ContactModel
import com.example.infinite_track.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor() : ContactRepository {
    override fun getContacts(): Flow<List<ContactModel>> {
        return flowOf(ContactDummyData.dummyContacts)
    }
}
