package distalg.webservice;

import distalg.model.Algorithm;
import distalg.model.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by home on 13.06.14.
 */
@Stateless(name = "DataEJB")
public class DataBean {

    @PersistenceContext(name = "distralgunit")
    private EntityManager entityManager;

    public DataBean() {
    }


    public String getAllAlgorithmJson(){
        TypedQuery<Algorithm> typedQuery = entityManager.createQuery("SELECT alg FROM Algorithm alg", Algorithm.class);

        List<Algorithm> list = typedQuery.getResultList() ;

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(out, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] data = out.toByteArray();
        return new String(data);
    }


    public void generateData(int data_length){
        TypedQuery<Algorithm> typedQuery = entityManager.createQuery("SELECT alg FROM Algorithm alg", Algorithm.class);

        List<Algorithm> list = typedQuery.getResultList() ;
        Algorithm singleAlgorithm = list.get(0);

        List<Data> dataList = new ArrayList<Data>();
      // for(int i = 0; i < 10 ; i++){
            Data data = new Data();
            data.setAlgorithmByAlgorithmId(singleAlgorithm);
            data.setData(getRandomString(data_length));
            data.setSize(data_length);
           dataList.add(data);
        entityManager.persist(data);
      // }


//        for (int i = 0; i < dataList.size(); i++) {
//            entityManager.persist(dataList.get(i));
//        }

    }

    public String getRandomString(int length){
        String characters = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }
}
