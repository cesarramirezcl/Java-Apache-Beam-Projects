package section4;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.Distinct;
import org.apache.beam.sdk.values.PCollection;

public class DistinctExample {

	public static void main(String[] args) {
		Pipeline p = Pipeline.create();
		PCollection<String> pCustList = p.apply(TextIO.read().from("/home/cesar/Desktop/Distinct.csv"));
		PCollection<String> uniqueCust = pCustList.apply(Distinct.<String>create());
		uniqueCust.apply(TextIO.write().to("distinct_out.csv").withNumShards(1).withSuffix(".csv"));
		p.run();
		System.out.println("Done");
	}

}
