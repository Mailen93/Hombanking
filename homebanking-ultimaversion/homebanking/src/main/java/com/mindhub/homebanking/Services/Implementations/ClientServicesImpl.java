package com.mindhub.homebanking.Services.Implementations;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServicesImpl implements ClientService {

    @Autowired
    private ClientRepositories clientRepositories;
    @Override
    public List<Client> findAll(){
        return clientRepositories.findAll();
    }
    @Override
    public Optional<Client> findById(Long id) {
        return clientRepositories.findById(id);
    }
    @Override
    public Client findByEmail(String email) {
        return clientRepositories.findByEmail(email);
    }
    @Override
    public Client save(Client client) {
        return clientRepositories.save(client);
    }


}
