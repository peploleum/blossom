package blossom.restful.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import blossom.exception.TopLevelBlossomException;
import blossom.restful.service.tech.entity.dto.CharacterBean;
import blossom.restful.service.tech.entity.transfer.CharacterTransfer;

@Path("/character")
public class CharacterRestService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createCharcter(final CharacterBean characterBean) throws TopLevelBlossomException {
        final CharacterTransfer characterTransfer = new CharacterTransfer();
        characterTransfer.createCharacter(characterBean);
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    public CharacterBean getCharcterById(final String id) {
        final CharacterTransfer characterTransfer = new CharacterTransfer();
        return characterTransfer.getCharcterById(id);
    }

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    public void removeCharacter(final String id) throws TopLevelBlossomException {
        final CharacterTransfer characterTransfer = new CharacterTransfer();
        characterTransfer.deleteCharacter(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public CharacterBean updateCharacter(final CharacterBean characterBean) {
        final CharacterTransfer characterTransfer = new CharacterTransfer();
        return characterTransfer.updateCharacter(characterBean);
    }

}
