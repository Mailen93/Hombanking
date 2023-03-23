package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource // le indica a Spring que debe generar el código necesario para que se pueda administrar la data de la aplicación desde el navegador usando JSON, es decir se crea una API REST automática que expone los recursos de cada repositorio anotado con @RepositoryRestResource.
public interface ClientRepositories extends JpaRepository <Client, Long>{
    Client findByEmail(String email);
}
