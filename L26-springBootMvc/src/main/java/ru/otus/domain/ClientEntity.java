package ru.otus.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ClientEntity {
  @Id
  private Long id;

  private String name;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Client toClient() {
    return new Client(id, name);
  }

  public ClientEntity(final Long id, final String name) {
    this.id = id;
    this.name = name;
  }

  public ClientEntity() {
    this.id = null;
    this.name = null;
  }

  public ClientEntity(final Client client) {
    this.id = client.getId();
    this.name = client.getName();
  }
}
