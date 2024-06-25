# NOTES

## Implementation

Project dependencies
 - JWT libraries
 - Spring Data, JPA
 - Spring-Security, Spring Web
 
WebSecurityConfig
 - provides beans:
 	- PasswordEncoder
 	- AuthenticationManager ?
 	- SecurityFilterChain
 	- DAOAuthenticationProvider (requires UserDetailServiceI and PasswordEncoder) - added to fitlerChain
 - Requires
 	- UserDetailsServiceImpl
 	- JWTAuthenticationFilter to be added to the filterChain
 - Can configure
 	- Session management
 	- csrf
 	- requestmatching
 	
 JWTAuthenticationFilter
 - implement doFilterInternal (OncePerRequustFilter)
 	- extractToken from auth header
 	- Validate Token
 	- gets the username from the Token and get userDetails from userDetailsService
 	- creates the UsernamePasswordAuthenticationToken to be added to the securityContextHolder.context.Authentication
 - requires:
 	- UserDetailsService
 	- JWTService

JWTService
- get authorization bearer token
- use the signing key to parse claims
	- check username exists from subject
	- checkexpiry
- create new token
	- set claims
	- set subject
	- set expiration dates set
	- sign with secret keys

AuthController
- requires passwordEncoder
- signup
	- check if credentials aready exist
	- save credentials, username and password(encoded)
- signin
	- create a new UsernamePasswordAuthenticationToken using username and password from request
	- use authenticationManager to authenticate, returning an authentication object
	- able to retreive principal (usually userDetails object
	- respond with JWT containing authority claims of user
	- ? do you need to add to the securityContext?

UserDetails/service
- implement

- ? difference between Roles vs Authorities


Security Context
- stores details of currently authenticated user (principal) - context.getAuthentication returns the userDetailsObject
- uses a thread local object to store security context = spring takes care of cleaning and thread local memory leaks. security context is always available to the same thread of execution

## Notes

During authentication, AuthenticationManager performs the authentication of the UsernamePasswordAuthToken which contains the raw username and password. AuthenticationManager delegates to providerManager and DaoAuthenticationprovider , which gets the stored credentials using UserDetailsService and compares it to the provided password that has been hashed by its password encoder. It returns an Authentication object if successful. The JWT token is signed with a secret key and returned as a response. UserDetails can be retrieved from the authentication object's principal

Client side makes request with the JWT in the Authorization header. During authorisation, Http request intercepted by the ones per request filter which uses JWT service to parse the provided JWT token with the same secret key. If the token is valid the principal from the JWT token is used to get the credentials and authorities from the user details service and add it to the security context. Spring security uses the authorities of the principal to check if the request can access the resource.


- if the token is expired, server will ExpiredException and client has to send a refresh token request
- enablewebsecurity annotation applies the secuirty config to the global web security
- authentication entry point commence will trigger an expection every time an authorized user accesses a secured http resources. It rejects every unauthenticated requests and sends a 401?
- method security is applied to the methods of the controller while httpsecurity is applied to the URLs
- authentication manager can throw authenticaion exception, disabled exception, locked exception and bad credentials exception
- websecurityconfigureradaptor is deprecated in spring 3 onwards

	The JWTAuthorization checks the JWT token is valid (authentication) and then gets the authorities of the token subject from userdetails, and adds the authentication with the authorization to the security context?


- ? should OPRF check if token = user details? username?
- ? registered vs recommended claims? private vs public claims?

