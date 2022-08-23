import java.util.*;

public class HW {


    public static void main(String[] args) {

        List<Map<String,String>> animalList = new ArrayList<Map<String, String>>();

        animalList.add(new HashMap<String, String>() {{put("name", "shark");put("habitat","ocean");}});
        animalList.add(new HashMap<String, String>() {{put("name", "bear");put("habitat","land");}});
        animalList.add(new HashMap<String, String>() {{put("name", "moose");put("habitat","land");}});
        animalList.add(new HashMap<String, String>() {{put("name", "frog");put("habitat","swamp");}});
        animalList.add(new HashMap<String, String>() {{put("name", "jelly fish");put("habitat","ocean");}});
        animalList.add(new HashMap<String, String>() {{put("name", "heron");put("habitat","swamp");}});
        animalList.add(new HashMap<String, String>() {{put("name", "whale");put("habitat","ocean");}});

        Map<String, List<String>>  animalList2 = new HashMap<>();

        for (Map<String,String> animal : animalList) {
            String habitat = animal.get("habitat");
            if(!animalList2.containsKey(habitat))
                 animalList2.put(habitat,new ArrayList<>(){{add(animal.get("name"));}});
            else{
                animalList2.get(habitat).add(animal.get("name"));
            }
        }

        animalList2.forEach((k,v)->{
            System.out.println(k +" : "+ String.join(" , ",v));
        });

        System.out.println("-----------------------------");
        Map<String, String> capitalMap = new HashMap<String, String>();
        capitalMap.put("USA", "shark");
        capitalMap.put("Japan", "bear");
        capitalMap.put("Thailand","Bangkok");
        capitalMap.put("UK","London");
        capitalMap.put("Australia","Canberra");
        capitalMap.put("Denmark","Copenhagen");
        capitalMap.put("Egypt","Cairo");
        capitalMap.put( "Vietnam","Hanoi");
        capitalMap.put("Italy","Rome");
        capitalMap.put("Brazil","Brazilia");

        for (String key : capitalMap.keySet()) {
            System.out.println(key + ": "+capitalMap.get(key));
        }
    }
}
