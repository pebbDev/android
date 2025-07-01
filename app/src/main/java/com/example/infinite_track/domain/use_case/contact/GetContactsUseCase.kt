package com.example.infinite_track.domain.use_case.contact

import com.example.infinite_track.domain.model.contact.ContactModel
import com.example.infinite_track.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val contactRepository: ContactRepository
) {
    operator fun invoke(): Flow<List<ContactModel>> {
        return contactRepository.getContacts()
    }
}
