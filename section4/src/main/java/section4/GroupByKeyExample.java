package section4;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.DoFn.ProcessContext;
import org.apache.beam.sdk.transforms.DoFn.ProcessElement;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

class StringToKV extends DoFn<String, KV<String, Integer>> {
	@ProcessElement
	public void processElement(ProcessContext c) {
		
		String input=c.element();
		String arr[] = input.split(",");
		c.output(KV.of(arr[0], Integer.valueOf(arr[3])));

	}
	
}

class KVToString extends DoFn<KV<String, Iterable<Integer>>, String>{
	@ProcessElement
	public void processElement(ProcessContext c) {
		String strKey = c.element().getKey();
		Iterable<Integer> vals = c.element().getValue();
		Integer sum = 0;
		for(Integer integer : vals) {
			sum = sum + integer; 
		}
		c.output(strKey+","+sum.toString());
	}
}

public class GroupByKeyExample {

	public static void main(String[] args) {
		Pipeline p = Pipeline.create();
		PCollection<String> pCustOrderList = p.apply(TextIO.read().from("/home/cesar/Desktop/GroupByKey_data.csv"));
		PCollection<KV<String, Integer>> kvOrder = pCustOrderList.apply(ParDo.of(new StringToKV()));
		PCollection<KV<String, Iterable<Integer>>> kvOrder2 = kvOrder.apply(GroupByKey.<String, Integer>create());
		PCollection<String> output = kvOrder2.apply(ParDo.of(new KVToString()));
		output.apply(TextIO.write().to("group_by_output.csv").withHeader("Id, amout").withNumShards(1).withSuffix(".csv"));
		p.run();
		System.out.println("Done");
	}

}
