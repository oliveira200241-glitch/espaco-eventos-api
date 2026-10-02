package com.example.espacoeventosapi.repository;

import com.example.espacoeventosapi.espaco.Espaco;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EspacoRepository extends MongoRepository<Espaco, String> {
}