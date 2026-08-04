package ru.otus.services;

import java.security.SecureRandom;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.domain.Client;
import ru.otus.domain.ClientEntity;
import ru.otus.generators.ClientIdGenerator;
import ru.otus.repostory.ClientEntityRepository;
import ru.otus.repostory.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

  private final ClientEntityRepository clientRepository;

  private final SecureRandom random = new SecureRandom();

  private final ClientIdGenerator generator;

  public ClientServiceImpl(ClientEntityRepository clientRepository,
      final ClientIdGenerator generator
  ) {
    this.clientRepository = clientRepository;
    this.generator = generator;
  }

  @Override
  public List<Client> findAll() {
    return clientRepository.findAll().stream().map(ClientEntity::toClient).toList();
  }

  @Override
  public Client findById(long id) {
    return clientRepository.findById(id).map(ClientEntity::toClient).orElse(null);
  }

  @Override
  public Client findByName(String name) {
    return clientRepository.findByName(name).stream().findFirst().map(ClientEntity::toClient)
        .orElse(null);
  }

  @Override
  public Client findRandom() {
    List<ClientEntity> clients = clientRepository.findAll();
    return clients.stream()
        .skip(random.nextInt(clients.size() - 1))
        .findFirst()
        .map(ClientEntity::toClient)
        .orElse(null);
  }

  @Override
  public Client save(Client client) {
    var entity = new ClientEntity(client);
    entity.setId(generator.generateId());
    return clientRepository.save(entity).toClient();
  }
}
