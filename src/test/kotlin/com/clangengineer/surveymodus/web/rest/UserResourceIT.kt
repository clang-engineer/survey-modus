package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.IntegrationTest
import com.clangengineer.surveymodus.domain.Authority
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.repository.UserRepository
import com.clangengineer.surveymodus.security.ADMIN
import com.clangengineer.surveymodus.security.USER
import com.clangengineer.surveymodus.service.dto.AdminUserDTO
import com.clangengineer.surveymodus.service.mapper.UserMapper
import com.clangengineer.surveymodus.web.rest.vm.ManagedUserVM
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.cache.CacheManager
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import javax.persistence.EntityManager
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Integration tests for the [UserResource] REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = [ADMIN])
@IntegrationTest
class UserResourceIT {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var restUserMockMvc: MockMvc

    private lateinit var user: User

    @BeforeEach
    fun setup() {
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)!!.clear()
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)!!.clear()
    }

    @BeforeEach
    fun initTest() {
        user = initTestUser(userRepository, em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUser() {
        val databaseSizeBeforeCreate = userRepository.findAll().size
        // Create the User
        val managedUserVM = ManagedUserVM().apply {
            login = DEFAULT_LOGIN
            password = DEFAULT_PASSWORD
            firstName = DEFAULT_FIRSTNAME
            lastName = DEFAULT_LASTNAME
            email = DEFAULT_EMAIL
            activated = true
            imageUrl = DEFAULT_IMAGEURL
            langKey = DEFAULT_LANGKEY
            authorities = mutableSetOf(USER)
        }

        restUserMockMvc.perform(
            post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(managedUserVM))
        )
            .andExpect(status().isCreated)

        assertPersistedUsers { userList ->
            // Validate the User in the database
            assertThat(userList).hasSize(databaseSizeBeforeCreate + 1)
            val testUser = userList.first { it.login == DEFAULT_LOGIN }
            assertThat(testUser.login).isEqualTo(DEFAULT_LOGIN)
            assertThat(testUser.firstName).isEqualTo(DEFAULT_FIRSTNAME)
            assertThat(testUser.lastName).isEqualTo(DEFAULT_LASTNAME)
            assertThat(testUser.email).isEqualTo(DEFAULT_EMAIL)
            assertThat(testUser.imageUrl).isEqualTo(DEFAULT_IMAGEURL)
            assertThat(testUser.langKey).isEqualTo(DEFAULT_LANGKEY)
        }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserWithExistingId() {
        val databaseSizeBeforeCreate = userRepository.findAll().size

        val managedUserVM = ManagedUserVM().apply {
            id = DEFAULT_ID
            login = DEFAULT_LOGIN
            password = DEFAULT_PASSWORD
            firstName = DEFAULT_FIRSTNAME
            lastName = DEFAULT_LASTNAME
            email = DEFAULT_EMAIL
            activated = true
            imageUrl = DEFAULT_IMAGEURL
            langKey = DEFAULT_LANGKEY
            authorities = mutableSetOf(USER)
        }

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc.perform(
            post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(managedUserVM))
        )
            .andExpect(status().isBadRequest)

        assertPersistedUsers { userList ->
            // Validate the User in the database
            assertThat(userList).hasSize(databaseSizeBeforeCreate)
        }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserWithExistingLogin() {
        // Initialize the database
        userRepository.saveAndFlush(user)
        val databaseSizeBeforeCreate = userRepository.findAll().size

        val managedUserVM = ManagedUserVM().apply {
            login = DEFAULT_LOGIN // this login should already be used
            password = DEFAULT_PASSWORD
            firstName = DEFAULT_FIRSTNAME
            lastName = DEFAULT_LASTNAME
            email = "anothermail@localhost"
            activated = true
            imageUrl = DEFAULT_IMAGEURL
            langKey = DEFAULT_LANGKEY
            authorities = mutableSetOf(USER)
        }

        // Create the User
        restUserMockMvc.perform(
            post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(managedUserVM))
        )
            .andExpect(status().isBadRequest)

        assertPersistedUsers { userList -> assertThat(userList).hasSize(databaseSizeBeforeCreate) }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserWithExistingEmail() {
        // Initialize the database
        userRepository.saveAndFlush(user)
        val databaseSizeBeforeCreate = userRepository.findAll().size

        val managedUserVM = ManagedUserVM().apply {
            login = "anotherlogin"
            password = DEFAULT_PASSWORD
            firstName = DEFAULT_FIRSTNAME
            lastName = DEFAULT_LASTNAME
            email = DEFAULT_EMAIL // this email should already be used
            activated = true
            imageUrl = DEFAULT_IMAGEURL
            langKey = DEFAULT_LANGKEY
            authorities = mutableSetOf(USER)
        }

        // Create the User
        restUserMockMvc.perform(
            post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(managedUserVM))
        )
            .andExpect(status().isBadRequest)

        assertPersistedUsers { userList -> assertThat(userList).hasSize(databaseSizeBeforeCreate) }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUsers() {
        // Initialize the database
        userRepository.saveAndFlush(user)
        // Get all the users
        restUserMockMvc.perform(
            get("/api/admin/users?sort=id,desc")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("\$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("\$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("\$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("\$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("\$.[*].imageUrl").value(hasItem(DEFAULT_IMAGEURL)))
            .andExpect(jsonPath("\$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUser() {
        // Initialize the database
        userRepository.saveAndFlush(user)

        assertNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)!!.get(user.login!!))

        // Get the user
        restUserMockMvc.perform(get("/api/admin/users/{login}", user.login))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("\$.login").value(user.login!!))
            .andExpect(jsonPath("\$.firstName").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("\$.lastName").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("\$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("\$.imageUrl").value(DEFAULT_IMAGEURL))
            .andExpect(jsonPath("\$.langKey").value(DEFAULT_LANGKEY))

        assertNotNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)!!.get(user.login!!))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingUser() {
        restUserMockMvc.perform(get("/api/admin/users/unknown"))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun updateUser() {
        // Initialize the database
        userRepository.saveAndFlush(user)
        val databaseSizeBeforeUpdate = userRepository.findAll().size

        // Update the user
        val updatedUser = userRepository.findById(user.id!!).get()
        assertNotNull(updatedUser)

        val managedUserVM = ManagedUserVM().apply {
            id = updatedUser.id
            login = updatedUser.login
            password = UPDATED_PASSWORD
            firstName = UPDATED_FIRSTNAME
            lastName = UPDATED_LASTNAME
            email = UPDATED_EMAIL
            activated = updatedUser.activated
            imageUrl = UPDATED_IMAGEURL
            langKey = UPDATED_LANGKEY
            createdBy = updatedUser.createdBy
            createdDate = updatedUser.createdDate
            lastModifiedBy = updatedUser.lastModifiedBy
            lastModifiedDate = updatedUser.lastModifiedDate
            authorities = mutableSetOf(USER)
        }

        restUserMockMvc.perform(
            put("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(managedUserVM))
        )
            .andExpect(status().isOk)

        assertPersistedUsers { userList ->
            assertThat(userList).hasSize(databaseSizeBeforeUpdate)
            val testUser = userList.first { it.id == updatedUser.id }
            assertThat(testUser.firstName).isEqualTo(UPDATED_FIRSTNAME)
            assertThat(testUser.lastName).isEqualTo(UPDATED_LASTNAME)
            assertThat(testUser.email).isEqualTo(UPDATED_EMAIL)
            assertThat(testUser.imageUrl).isEqualTo(UPDATED_IMAGEURL)
            assertThat(testUser.langKey).isEqualTo(UPDATED_LANGKEY)
        }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun updateUserLogin() {
        // Initialize the database
        userRepository.saveAndFlush(user)
        val databaseSizeBeforeUpdate = userRepository.findAll().size

        // Update the user
        val updatedUser = userRepository.findById(user.id!!).get()
        assertNotNull(updatedUser)

        val managedUserVM = ManagedUserVM().apply {
            id = updatedUser.id
            login = UPDATED_LOGIN
            password = UPDATED_PASSWORD
            firstName = UPDATED_FIRSTNAME
            lastName = UPDATED_LASTNAME
            email = UPDATED_EMAIL
            activated = updatedUser.activated
            imageUrl = UPDATED_IMAGEURL
            langKey = UPDATED_LANGKEY
            createdBy = updatedUser.createdBy
            createdDate = updatedUser.createdDate
            lastModifiedBy = updatedUser.lastModifiedBy
            lastModifiedDate = updatedUser.lastModifiedDate
            authorities = mutableSetOf(USER)
        }

        restUserMockMvc.perform(
            put("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(managedUserVM))
        )
            .andExpect(status().isOk)

        assertPersistedUsers { userList ->
            assertThat(userList).hasSize(databaseSizeBeforeUpdate)
            val testUser = userList.first { it.id == updatedUser.id }
            assertThat(testUser.login).isEqualTo(UPDATED_LOGIN)
            assertThat(testUser.firstName).isEqualTo(UPDATED_FIRSTNAME)
            assertThat(testUser.lastName).isEqualTo(UPDATED_LASTNAME)
            assertThat(testUser.email).isEqualTo(UPDATED_EMAIL)
            assertThat(testUser.imageUrl).isEqualTo(UPDATED_IMAGEURL)
            assertThat(testUser.langKey).isEqualTo(UPDATED_LANGKEY)
        }
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun updateUserExistingEmail() {
        // Initialize the database with 2 users
        userRepository.saveAndFlush(user)

        val anotherUser = User(
            login = "jhipster",
            password = RandomStringUtils.randomAlphanumeric(60),
            activated = true,
            email = "jhipster@localhost",
            firstName = "java",
            lastName = "hipster",
            imageUrl = "",
            langKey = "en"
        )
        userRepository.saveAndFlush(anotherUser)

        // Update the user
        val updatedUser = userRepository.findById(user.id!!).get()
        assertNotNull(updatedUser)

        val managedUserVM = ManagedUserVM().apply {
            id = updatedUser.id
            login = updatedUser.login
            password = updatedUser.password
            firstName = updatedUser.firstName
            lastName = updatedUser.lastName
            email = "jhipster@localhost" // this email should already be used by anotherUser
            activated = updatedUser.activated
            imageUrl = updatedUser.imageUrl
            langKey = updatedUser.langKey
            createdBy = updatedUser.createdBy
            createdDate = updatedUser.createdDate
            lastModifiedBy = updatedUser.lastModifiedBy
            lastModifiedDate = updatedUser.lastModifiedDate
            authorities = mutableSetOf(USER)
        }

        restUserMockMvc.perform(
            put("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(managedUserVM))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun updateUserExistingLogin() {
        // Initialize the database
        userRepository.saveAndFlush(user)

        val anotherUser = User(
            login = "jhipster",
            password = RandomStringUtils.randomAlphanumeric(60),
            activated = true,
            email = "jhipster@localhost",
            firstName = "java",
            lastName = "hipster",
            imageUrl = "",
            langKey = "en"
        )
        userRepository.saveAndFlush(anotherUser)

        // Update the user
        val updatedUser = userRepository.findById(user.id!!).get()
        assertNotNull(updatedUser)

        val managedUserVM = ManagedUserVM().apply {
            id = updatedUser.id
            login = "jhipster" // this login should already be used by anotherUser
            password = updatedUser.password
            firstName = updatedUser.firstName
            lastName = updatedUser.lastName
            email = updatedUser.email
            activated = updatedUser.activated
            imageUrl = updatedUser.imageUrl
            langKey = updatedUser.langKey
            createdBy = updatedUser.createdBy
            createdDate = updatedUser.createdDate
            lastModifiedBy = updatedUser.lastModifiedBy
            lastModifiedDate = updatedUser.lastModifiedDate
            authorities = mutableSetOf(USER)
        }

        restUserMockMvc.perform(
            put("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(managedUserVM))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteUser() {
        // Initialize the database
        userRepository.saveAndFlush(user)
        val databaseSizeBeforeDelete = userRepository.findAll().size

        // Delete the user
        restUserMockMvc.perform(
            delete("/api/admin/users/{login}", user.login)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)

        assertNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)!!.get(user.login!!))

        assertPersistedUsers { userList -> assertThat(userList).hasSize(databaseSizeBeforeDelete - 1) }
    }

    @Test
    @Throws(Exception::class)
    fun testUserEquals() {
        equalsVerifier(User::class)
        val user1 = User(id = DEFAULT_ID)
        val user2 = User(id = user1.id)
        assertThat(user1).isEqualTo(user2)
        user2.id = 2L
        assertThat(user1).isNotEqualTo(user2)
        user1.id = null
        assertThat(user1).isNotEqualTo(user2)
    }

    @Test
    fun testUserDTOtoUser() {
        val userDTO = AdminUserDTO(
            id = DEFAULT_ID,
            login = DEFAULT_LOGIN,
            firstName = DEFAULT_FIRSTNAME,
            lastName = DEFAULT_LASTNAME,
            email = DEFAULT_EMAIL,
            activated = true,
            imageUrl = DEFAULT_IMAGEURL,
            langKey = DEFAULT_LANGKEY,
            createdBy = DEFAULT_LOGIN,
            lastModifiedBy = DEFAULT_LOGIN,
            authorities = mutableSetOf(USER)
        )

        val user = userMapper.userDTOToUser(userDTO)
        assertNotNull(user)
        assertThat(user.id).isEqualTo(DEFAULT_ID)
        assertThat(user.login).isEqualTo(DEFAULT_LOGIN)
        assertThat(user.firstName).isEqualTo(DEFAULT_FIRSTNAME)
        assertThat(user.lastName).isEqualTo(DEFAULT_LASTNAME)
        assertThat(user.email).isEqualTo(DEFAULT_EMAIL)
        assertThat(user.activated).isTrue
        assertThat(user.imageUrl).isEqualTo(DEFAULT_IMAGEURL)
        assertThat(user.langKey).isEqualTo(DEFAULT_LANGKEY)
        assertThat(user.createdBy).isNull()
        assertThat(user.createdDate).isNotNull()
        assertThat(user.lastModifiedBy).isNull()
        assertThat(user.lastModifiedDate).isNotNull()
        assertThat(user.authorities).extracting("name").containsExactly(USER)
    }

    @Test
    fun testUserToUserDTO() {
        user.id = DEFAULT_ID
        user.createdBy = DEFAULT_LOGIN
        user.createdDate = Instant.now()
        user.lastModifiedBy = DEFAULT_LOGIN
        user.lastModifiedDate = Instant.now()
        user.authorities = mutableSetOf(Authority(name = USER))

        val userDTO = userMapper.userToAdminUserDTO(user)

        assertThat(userDTO.id).isEqualTo(DEFAULT_ID)
        assertThat(userDTO.login).isEqualTo(DEFAULT_LOGIN)
        assertThat(userDTO.firstName).isEqualTo(DEFAULT_FIRSTNAME)
        assertThat(userDTO.lastName).isEqualTo(DEFAULT_LASTNAME)
        assertThat(userDTO.email).isEqualTo(DEFAULT_EMAIL)
        assertThat(userDTO.activated).isTrue
        assertThat(userDTO.imageUrl).isEqualTo(DEFAULT_IMAGEURL)
        assertThat(userDTO.langKey).isEqualTo(DEFAULT_LANGKEY)
        assertThat(userDTO.createdBy).isEqualTo(DEFAULT_LOGIN)
        assertThat(userDTO.createdDate).isEqualTo(user.createdDate)
        assertThat(userDTO.lastModifiedBy).isEqualTo(DEFAULT_LOGIN)
        assertThat(userDTO.lastModifiedDate).isEqualTo(user.lastModifiedDate)
        assertThat(userDTO.authorities).containsExactly(USER)
        assertThat(userDTO.toString()).isNotNull()
    }

    @Test
    fun testAuthorityEquals() {
        val authorityA = Authority()
        assertThat(authorityA)
            .isNotEqualTo(null)
            .isNotEqualTo(Any())
        assertThat(authorityA.hashCode()).isEqualTo(31)
        assertThat(authorityA.toString()).isNotNull()

        val authorityB = Authority()
        assertThat(authorityA.name).isEqualTo(authorityB.name)

        authorityB.name = ADMIN
        assertThat(authorityA).isNotEqualTo(authorityB)

        authorityA.name = USER
        assertThat(authorityA).isNotEqualTo(authorityB)

        authorityB.name = USER
        assertThat(authorityA)
            .isEqualTo(authorityB)
            .hasSameHashCodeAs(authorityB)
    }

    companion object {

        private const val DEFAULT_LOGIN = "johndoe"
        private const val UPDATED_LOGIN = "jhipster"

        private const val DEFAULT_ID = 1L

        private const val DEFAULT_PASSWORD = "passjohndoe"
        private const val UPDATED_PASSWORD = "passjhipster"

        private const val DEFAULT_EMAIL = "johndoe@localhost"
        private const val UPDATED_EMAIL = "jhipster@localhost"

        private const val DEFAULT_FIRSTNAME = "john"
        private const val UPDATED_FIRSTNAME = "jhipsterFirstName"

        private const val DEFAULT_LASTNAME = "doe"
        private const val UPDATED_LASTNAME = "jhipsterLastName"

        private const val DEFAULT_IMAGEURL = "http://placehold.it/50x50"
        private const val UPDATED_IMAGEURL = "http://placehold.it/40x40"

        private const val DEFAULT_LANGKEY = "en"
        private const val UPDATED_LANGKEY = "fr"

        /**
         * Create a User.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which has a required relationship to the User entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager?): User {
            return User(
                login = DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5),
                password = RandomStringUtils.randomAlphanumeric(60),
                activated = true,
                email = RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL,
                firstName = DEFAULT_FIRSTNAME,
                lastName = DEFAULT_LASTNAME,
                imageUrl = DEFAULT_IMAGEURL,
                langKey = DEFAULT_LANGKEY
            )
        }

        /**
         * Setups the database with one user.
         */
        @JvmStatic
        fun initTestUser(userRepository: UserRepository, em: EntityManager): User {
            userRepository.deleteAll()
            val user = createEntity(em)
            user.login = DEFAULT_LOGIN
            user.email = DEFAULT_EMAIL
            return user
        }
    }

    fun assertPersistedUsers(userAssertion: (List<User>) -> Unit) {
        userAssertion(userRepository.findAll())
    }
}
