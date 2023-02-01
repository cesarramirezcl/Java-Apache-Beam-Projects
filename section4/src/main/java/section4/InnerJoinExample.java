package section4;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.transforms.join.CoGroupByKey;
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TupleTag;

class OrderParsing extends DoFn<String, KV<String, String>>{
	
	@ProcessElement
	public void processElement(ProcessContext c) {
		String arr[] = c.element().split(",");
		String strKey = arr[0];
		String strVal = arr[1]+","+arr[2]+","+arr[3];
		c.output(KV.of(strKey, strVal));
	}
}

class UserParsing extends DoFn<String, KV<String, String>>{
	
	@ProcessElement
	public void processElement(ProcessContext c) {
		String arr[] = c.element().split(",");
		String strKey = arr[0];
		String strVal = arr[1];
		c.output(KV.of(strKey, strVal));
	}
}

public class InnerJoinExample {

	public static void main(String[] args) {
		Pipeline p = Pipeline.create();
		PCollection<KV<String, String>> pOrderCollection = p.apply(TextIO.read().from("/home/cesar/Desktop/user_order.csv")).apply(ParDo.of(new OrderParsing()));
		PCollection<KV<String, String>> pUserCollection = p.apply(TextIO.read().from("/home/cesar/Desktop/p_user.csv")).apply(ParDo.of(new UserParsing()));
		final TupleTag<String> orderTuple = new TupleTag<String>();
		final TupleTag<String> userTuple = new TupleTag<String>();
		PCollection<KV<String, CoGbkResult>> result= KeyedPCollectionTuple.of(orderTuple, pOrderCollection).and(userTuple, pUserCollection).apply(CoGroupByKey.<String>create());
		PCollection<String> output = result.apply(ParDo.of(new DoFn<KV<String, CoGbkResult>, String>(){
			
			@ProcessElement
			public void processElement(ProcessContext c) {
				String strKey = c.element().getKey();
				CoGbkResult valObject = c.element().getValue();
				Iterable<String> orderTable= valObject.getAll(orderTuple);
				Iterable<String> userTable= valObject.getAll(userTuple);
				
				for (String order : orderTable) {
					for (String user: userTable) {
						c.output(strKey+","+order+","+user);
					}
				}
			}
		}));
		output.apply(TextIO.write().to("cogroup_by_key.csv").withNumShards(1).withSuffix(".csv"));
		p.run();
		System.out.println("Done");
	}

}
