package com.github.amitsureshchandra.urlshortner.seeder

import com.github.amitsureshchandra.urlshortner.entity.UrlMap
import com.github.amitsureshchandra.urlshortner.entity.User
import com.github.amitsureshchandra.urlshortner.repo.UserRepo
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class UserSeeder(val userRepo: UserRepo) {

    @PostConstruct
    fun init(){
        createUser()
    }

    private fun createUser() {
        if(userRepo.existsUserByEmail("amit@example.com")) return

        val user = User(
            "amit",
            "amit@example.com",
            "1234567890",
            null,
        )
        userRepo.save(user)
        print("User created")
    }
}