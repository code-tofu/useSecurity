# useSecurity
User Endpoints
- [ ] CreateAccount
- [ ] Login

Admin Endpoints
- [ ] DeleteUser
- [ ] ChangeUserStatus

ROLE_SUPERUSER
ROLE_ADMIN
ROLE_STAFF
ROLE_CUSTOMER

# Implementing Spring Security
 - include dependencies
   - JWT
   - Spring Data, JPA
   - Spring-Security, Spring Web
 - implement controller
 - implement UserDetails
 - implement authexceptionhandling for web security config
 - implement WebSecurityConfig
   - Bean securityFilterChain
     - inject authenticationProvider or authenticationProvider
       - inject userDetailsService
       - inject pwEncoder
     - inject filters

Put test json in application properties
AOP parse
print all filter chains

Role enums
Signin DTOs
Signup DTOs
JWT service
JWT AuthenticationFilter
UserDetailsRepository
Wire Up everything

SecurityContextHolder.getContext().getAuthentication() == null?
authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request))?

# Notes
Password encoder is always a new instance? What if we use secureRandom
DAO Authentication Provider does the checking for you

During login
AuthenticationManager takes in a username and password authentication token which contains the raw username and password. AuthenticationManager delegates to providerManager and DaoAuthenticationprovider which gets the username and hash password from its userdetailsservice dependency and compares it to the provided password which is then hashed by its password encoder.


The JWT token is signed with a secret key and returned as a response

-
Http request intercepted by the ones per request filter which uses JWT service to parse the provided JWT token with the same secret key. If the token is valid the principal from the JWT token is used to get the credentials and authorities from the user details service and add it to the security context 