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

@Path("/test")
public class TestWebService {

    @Inject
    EJBService ejbService;

    @Inject
    DataBean dataBean;

    @GET
    @Produces("application/json")
    public String test(){
//        return dataBean.getAllAlgorithmJson();
        return ejbService.getAllAlgorithmJson();
    }

    @GET
    @Produces("text/plain")
    @Path("/task")
    public String runTask(){
        String algorithm = "multiply();function multiply(){var array = parseData.data.split(\" \"); var result = 1;for (var i = 0; i < array.length; i++) {result = result * array[i]; }alert(result);}";
        String data = "1 2 3 4 5 6 7 8 9";
        String command = "run";

        ejbService.makeTask(new Task(command,algorithm,data));
        return "task Started";
    }

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
    @Produces("application/json")
    @Path("/gettask")
    public String getTask(){

        return ejbService.prepareTask();
    }


    @GET
    @Produces("text/plain")
    @Path("/sendtask")
    public String sendTask(){

         ejbService.sendTask(ejbService.prepareTask());
        return "sended";
    }
}
