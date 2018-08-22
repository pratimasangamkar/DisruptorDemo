package com.instrument.process.business.partitioner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class SampleProducerPartition implements Partitioner {

	private static Map<String, Integer> partitionMap;

	@Override
	public void configure(Map<String, ?> arg0) {
		// TODO Auto-generated method stub
		//System.out.println("Inside SampleProducerPartition Configure: ");
		partitionMap = new HashMap<>();
		arg0.entrySet().forEach(data -> {
			if (data.getKey().startsWith("partitions.")) {
				String keyName = data.getKey();
				String value = (String) data.getValue();
				System.out.println(keyName.substring(11));
				int partitionId = Integer.parseInt(keyName.substring(11));
				partitionMap.put(value, partitionId);

			}
		});

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		// TODO Auto-generated method stub
		/*List partitions = cluster.availablePartitionsForTopic(topic);
		System.out.println("List::: " + partitions);
		
		System.out.println("Value: "+value);
	*/
		String jsonString = (String) value;
	//	System.out.println("String Json: "+jsonString);
		
		JSONParser parser = new JSONParser();
		
		//convert from JSON string to JSONObject
		JSONObject newJObject = null;
		try {
			newJObject = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//System.out.println("New Json Object: "+newJObject);
		String valueStr = (String)newJObject.get("Type");
		//System.out.println("ValueStr: "+valueStr);
		String partitionName = valueStr;
		if (partitionMap.containsKey(partitionName)) {
			return partitionMap.get(partitionName);
		} else {
			int noOfPartitions = cluster.topics().size();
			return value.hashCode() % noOfPartitions + partitionMap.size();
		}

	}

}
