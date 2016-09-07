package io.github.kanedafromparis.ose.fakedatagen;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;

@RequestScoped
@Path("/data")
public class FakeDataGenResource {
    @GET()
    @Produces("application/json")
    public HashMap<String, String> getData(
            @DefaultValue("False") @QueryParam("saveToDtb") Boolean saveToDtb,
            @DefaultValue("False") @QueryParam("saveToFile") Boolean saveToFile,
            @DefaultValue("100") @QueryParam("deep") Integer deep
    ) {
        HashMap<String, String> theInsult = new HashMap<String, String>();
        theInsult.put("insult", new FakeDataGenerator().generateFakeData());
        return theInsult;
    }
}