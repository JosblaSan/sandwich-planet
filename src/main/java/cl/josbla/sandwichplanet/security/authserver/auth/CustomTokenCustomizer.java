package cl.josbla.sandwichplanet.security.authserver.auth;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        
        // Agregar roles al token
        if (context.getPrincipal() != null && context.getPrincipal().getAuthorities() != null) {
            claims.claim(
                "roles", 
                context.getPrincipal().getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .toList()
            );
        }
        
        // Puedes agregar más claims personalizados aquí
        claims.claim("custom_claim", "valor_personalizado");
    }
}