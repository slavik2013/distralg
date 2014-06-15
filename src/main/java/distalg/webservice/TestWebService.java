package distalg.webservice;

import distalg.ejb.EJBService;
import distalg.ejb.Task;
import distalg.model.Algorithm;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Random;

/**
 * Created by home on 27.05.14.
 */

@Path("/")
public class TestWebService {

    @Inject
    EJBService ejbService;

    @Inject
    DataBean dataBean;

    @GET
    @Produces("text/plain")
    @Path("/generate/{min_length}/{max_length}/{count}")
    public String generateData(@PathParam("max_length") int max_length, @PathParam("count") int count
    , @PathParam("min_length") int min_length){
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            dataBean.generateData(min_length + random.nextInt(max_length - min_length));
        }

        return "data generated : max_length = " + max_length + " , min_length = " + min_length + " , count = " + count;
    }

    @GET
    @Produces("text/plain")
    @Path("/sendtask")
    public String sendTask(){
        ejbService.prepareTask();
        return "sended";
    }

    @GET
    @Produces("text/plain")
    @Path("/test/{limit}")
    public String makeMeasure(@PathParam("limit") int limit){
        ejbService.makeMeasure(limit);
        return "measured";
    }

    @GET
    @Produces("text/plain")
    @Path("/clear")
    public String clear(){
        ejbService.clearData();
        return "cleared";
    }
}
