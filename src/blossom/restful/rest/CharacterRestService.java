package blossom.restful.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.tech.entity.CharacterDao;
import blossom.restful.service.tech.entity.dto.CharacterBean;

@Path("/character")
public class CharacterRestService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createCharcter(final CharacterBean characterBean) throws TopLevelBlossomException {
        final CharacterDao characterTransfer = new CharacterDao();
        characterTransfer.createCharacter(characterBean);
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    public CharacterBean getCharcterById(final String id) {
        final CharacterDao characterTransfer = new CharacterDao();
        return characterTransfer.getCharcterById(id);
    }

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    public void removeCharacter(final String id) throws TopLevelBlossomException {
        final CharacterDao characterTransfer = new CharacterDao();
        characterTransfer.deleteCharacter(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public CharacterBean updateCharacter(final CharacterBean characterBean) {
        final CharacterDao characterTransfer = new CharacterDao();
        return characterTransfer.updateCharacter(characterBean);
    }

}
