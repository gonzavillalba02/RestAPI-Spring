package med.voll.api.infra.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {//se extiende pq es una clase abstracta

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //obtengo el token del header que se envia
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null){
            var token = authHeader.replace("Bearer ",""); //replace porque por defecto viene el prefijo bearer y no lo queremos, solo queremos el token
            var subject = tokenService.getSubject(token);
            if (subject != null) {
                //token valido
                var usuario = usuarioRepository.findByLogin(subject);
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response); //con esto le digo que ejecute el filtro y luego pase el request y todo
    }

    //en este metodo basicamente tomamos el token del header, si no llega nulo le sacamos el Bearer y nos quedamos solo con el token
    //de ahi nos quedamos con el subject del token y si este no es nulo significa que el usuario es valido
    // ahora encontramos al usuario por el login y dsp le decimos a spring que el login es valido pq verifico que el usuario existe, forzamos un inicio de sesion
    //despues seteamos manualmente la autenticacion de ese inicio de sesion por lo que para los demas request el usuario ya va a estar autenticado
}
