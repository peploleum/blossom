package blossom.persistence;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import org.junit.Test;

import blossom.persistence.entity.AbstractBlossomEntity;
import blossom.persistence.entity.CharacterEntity;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PersistenceJsonTest {

    @Test
    public void serializeEntity() throws JsonGenerationException, JsonMappingException, IOException {
        final CharacterEntity entity = new CharacterEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCatchphrase("I'm walking on sunshine!");
        entity.setFirstname("Phillip");
        entity.setLastname("Fry");
        entity.setName("fry");
        final CharacterEntity entity2 = new CharacterEntity();
        entity2.setId(UUID.randomUUID().toString());
        entity2.setCatchphrase("I'm already in my pyjamas!");
        entity2.setFirstname("Hubert");
        entity2.setLastname("Farnsworth");
        entity2.setName("farnsworth");
        // we will have to check how it stack overflows for relexive relations
        entity.setLinkedEntities(Collections.singleton((AbstractBlossomEntity) entity2));
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out, entity);
    }
}
