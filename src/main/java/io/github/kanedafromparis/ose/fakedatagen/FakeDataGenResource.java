package io.github.kanedafromparis.ose.fakedatagen;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;

@RequestScoped
@Path("/insult")
public class FakeDataGenResource {
    @GET()
    @Produces("application/json")
    public HashMap<String, String> getInsult() {
        HashMap<String, String> theInsult = new HashMap<String, String>();
        theInsult.put("insult", new FakeDataGenerator().generateFakeData());
        return theInsult;
    }
}