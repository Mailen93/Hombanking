package com.mindhub.homebanking.Configurations;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {             //OBJ Q USA SPRING SEC PARA SABER COMO BUSCARA LOS DETALLES DE USUARIO
    @Autowired
    private ClientRepositories clientRepositories;
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception { //CAMBIAR POR UNO NUEVO QUE IMPLEMENTE LA BUSQUEDA DE LOS DETALLES DE USUARIO A TRAVES DEL REPO CLIENT

        auth.userDetailsService(inputName -> {
            Client client = clientRepositories.findByEmail(inputName);

            if (client != null) {
                if (client.getEmail().contains("admin")) {return new User(client.getEmail(), client.getPassword(),
                        AuthorityUtils.createAuthorityList("ADMIN"));

                } else {return new User(client.getEmail(), client.getPassword(),
                        AuthorityUtils.createAuthorityList("CLIENT"));}

            } else {throw new UsernameNotFoundException("Unknown user: " + inputName);}
        });
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

//IR A HOMEBANKING AP