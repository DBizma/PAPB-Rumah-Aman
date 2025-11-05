package com.example.rumahaman.domain.usecase.user

import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.model.User
import com.example.rumahaman.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Flow<Result<Unit>> {
        return userRepository.updateUserData(user)
    }
}
