package com.clangengineer.surveymodus.web.rest

import com.clangengineer.surveymodus.config.LOGIN_REGEX
import com.clangengineer.surveymodus.domain.User
import com.clangengineer.surveymodus.repository.UserRepository
import com.clangengineer.surveymodus.security.ADMIN
import com.clangengineer.surveymodus.service.MailService
import com.clangengineer.surveymodus.service.UserService
import com.clangengineer.surveymodus.service.dto.AdminUserDTO
import com.clangengineer.surveymodus.web.rest.errors.BadRequestAlertException
import com.clangengineer.surveymodus.web.rest.errors.EmailAlreadyUsedException
import com.clangengineer.surveymodus.web.rest.errors.LoginAlreadyUsedException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.PaginationUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Pattern

/**
 * REST controller for managing users.
 *
 * This class accesses the {@link com.clangengineer.surveymodus.domain.User} entity, and needs to fetch its collection of authorities.
 *
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 *
 * We use a View Model and a DTO for 3 reasons:
 *
 * * We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.
 * *  Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).
 * *  As this manages users, for security reasons, we'd rather have a DTO layer.
 *
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api/admin")
class UserResource(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val mailService: MailService
) {
    companion object {
        private val ALLOWED_ORDERED_PROPERTIES = arrayOf("id", "login", "firstName", "lastName", "email", "activated", "langKey", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate")
    }

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private val applicationName: String? = null

    /**
     * `POST  /admin/users`  : Creates a new user.
     *
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the `ResponseEntity` with status `201 (Created)` and with body the new user, or with status `400 (Bad Request)` if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException `400 (Bad Request)` if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"$ADMIN\")")
    @Throws(URISyntaxException::class)
    fun createUser(@Valid @RequestBody userDTO: AdminUserDTO): ResponseEntity<User> {
        log.debug("REST request to save User : $userDTO")

        if (userDTO.id != null) {
            throw BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists")
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.login!!.lowercase(Locale.getDefault())).isPresent) {
            throw LoginAlreadyUsedException()
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.email).isPresent) {
            throw EmailAlreadyUsedException()
        } else {
            val newUser = userService.createUser(userDTO)
            mailService.sendCreationEmail(newUser)
            return ResponseEntity.created(URI("/api/admin/users/${newUser.login}"))
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.login))
                .body(newUser)
        }
    }

    /**
     * `PUT /admin/users` : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the `ResponseEntity` with status `200 (OK)` and with body the updated user.
     * @throws EmailAlreadyUsedException `400 (Bad Request)` if the email is already in use.
     * @throws LoginAlreadyUsedException `400 (Bad Request)` if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"$ADMIN\")")
    fun updateUser(@Valid @RequestBody userDTO: AdminUserDTO): ResponseEntity<AdminUserDTO> {
        log.debug("REST request to update User : $userDTO")
        var existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.email)
        if (existingUser.isPresent && existingUser.get().id != userDTO.id) {
            throw EmailAlreadyUsedException()
        }
        existingUser = userRepository.findOneByLogin(userDTO.login!!.lowercase(Locale.getDefault()))
        if (existingUser.isPresent && existingUser.get().id != userDTO.id) {
            throw LoginAlreadyUsedException()
        }
        val updatedUser = userService.updateUser(userDTO)

        return ResponseUtil.wrapOrNotFound(
            updatedUser,
            HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.login)
        )
    }

    /**
     * `GET /admin/users` : get all users with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the `ResponseEntity` with status `200 (OK)` and with body all users.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"$ADMIN\")")
    fun getAllUsers(@org.springdoc.api.annotations.ParameterObject pageable: Pageable): ResponseEntity<List<AdminUserDTO>> {
        log.debug("REST request to get all User for an admin")
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build()
        }
        val page = userService.getAllManagedUsers(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity(page.content, headers, HttpStatus.OK)
    }

    private fun onlyContainsAllowedProperties(pageable: Pageable) =
        pageable.sort.map(Sort.Order::getProperty).all { ALLOWED_ORDERED_PROPERTIES.contains(it) }

    /**
     * `GET /admin/users/:login` : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the `ResponseEntity` with status `200 (OK)` and with body the "login" user, or with status `404 (Not Found)`.
     */
    @GetMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"$ADMIN\")")
    fun getUser(@PathVariable @Pattern(regexp = LOGIN_REGEX) login: String): ResponseEntity<AdminUserDTO> {
        log.debug("REST request to get User : $login")
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map { AdminUserDTO(it) }
        )
    }

    /**
     * `DELETE /admin/users/:login` : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the `ResponseEntity` with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"$ADMIN\")")
    fun deleteUser(@PathVariable @Pattern(regexp = LOGIN_REGEX) login: String): ResponseEntity<Void> {
        log.debug("REST request to delete User: $login")
        userService.deleteUser(login)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.deleted", login)).build()
    }
}
