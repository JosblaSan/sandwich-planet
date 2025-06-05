package cl.josbla.sandwichplanet.security.authserver.auth;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import java.util.stream.Collectors;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import cl.josbla.sandwichplanet.security.authserver.repository.UserRepository;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsGlobalConfiguration corsGlobalConfiguration;

    SecurityConfig(CorsGlobalConfiguration corsGlobalConfiguration) {
        this.corsGlobalConfiguration = corsGlobalConfiguration;
    }

	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
			throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
				OAuth2AuthorizationServerConfigurer.authorizationServer();
		
		authorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> 
			tokenEndpoint.accessTokenRequestConverter(new OAuth2AuthorizationCodeRequestAuthenticationConverter()));

		http
			
			.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
			.with(authorizationServerConfigurer, (authorizationServer) ->
				authorizationServer
					.oidc(Customizer.withDefaults())	// Enable OpenID Connect 1.0
			)
			.authorizeHttpRequests((authorize) ->
				authorize
					.anyRequest().authenticated()
			)
			// Redirect to the login page when not authenticated from the
			// authorization endpoint
			.exceptionHandling((exceptions) -> exceptions
				.defaultAuthenticationEntryPointFor(
					new LoginUrlAuthenticationEntryPoint("/login"),
					new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
				)
			)
            .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/api/usuarios/registro")
				.permitAll()
				.anyRequest().authenticated()
			)
			// Form login handles the redirect to the login page from the
			// authorization server filter chain
            .csrf(csrf -> csrf.disable())
        	.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))// Sesión solo si es necesaria
			.formLogin(Customizer.withDefaults());

		return http.build();
	}

	/*@Bean 
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.builder()
				.username("pepe")
				.password("{noop}12345")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(userDetails);
	}*/

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository){
		return userMail -> {
			cl.josbla.sandwichplanet.security.authserver.models.User user = userRepository.findByMail(userMail)
				.orElseThrow(()-> new RuntimeException("Code: -4 Error del servidor"));

			return org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername() + " " + user.getApellidoPat())
				.password(user.getPassword())
				.roles(user.getRoles())
				.build();
		};
	}

	@Bean 
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
		RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("client-app")
				.clientSecret(passwordEncoder.encode("12345"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("http://localhost:4200/callback") // esto es clave
				.postLogoutRedirectUri("http://localhost:8080/logout")
                .scope("read")
                .scope("write")
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.tokenSettings(TokenSettings.builder()
					.accessTokenTimeToLive(java.time.Duration.ofHours(24))
					.refreshTokenTimeToLive(java.time.Duration.ofDays(30))
					.build())
				.clientSettings(
					ClientSettings.builder()
					.requireAuthorizationConsent(false)
					.requireProofKey(true) // PKCE obligatorio
					.build())
				.build();

		return new InMemoryRegisteredClientRepository(oidcClient);
	}

	@Bean 
	public JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	private static KeyPair generateRsaKey() { 
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	/*@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}*/

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encriptación BCrypt
    }

	@Bean
	public OAuth2AuthorizationService authorizationService(
			RegisteredClientRepository registeredClientRepository) {
		return new InMemoryOAuth2AuthorizationService();
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				.issuer("http://localhost:9000") // ¡muy importante! Debe coincidir con `issuer` en Angular
				.build();
	}

	@Bean
	public CookieSameSiteSupplier sameSiteSupplier() {
		return CookieSameSiteSupplier.ofNone().whenHasName("AUTH_SESSION_ID");
	}

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
		return context -> {
			if (context.getTokenType().getValue().equals("access_token")) {
				// Agregar roles
				Authentication principal = context.getPrincipal();
				if (principal != null && principal.getAuthorities() != null) {
					context.getClaims().claim(
						"roles",
						principal.getAuthorities().stream()
							.map(GrantedAuthority::getAuthority)
							.collect(Collectors.toList())
					);
				}
				
				// Agregar más claims personalizados si es necesario
				context.getClaims().claim("custom_claim", "valor_personalizado");
			}
		};
	}
}