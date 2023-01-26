package section3;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;

class MyFilter implements SerializableFunction<String, Boolean> {
	
	@Override
	public Boolean apply(String input) {
		// TODO Auto-generated method stub
		return input.contains("Los Angeles");
	}
	
}

public class FilterExample {

	public static void main(String[] args) {
		Pipeline p = Pipeline.create();
		PCollection<String> pCustList = p.apply(TextIO.read().from("/home/cesar/Desktop/customer_pardo.csv"));
		PCollection<String> pOutput = pCustList.apply(Filter.by(new MyFilter()));
		pOutput.apply(TextIO.write().to("customer_filter_output.csv").withHeader("Id,Name,Last Name, City").withNumShards(1).withSuffix(".csv"));
		p.run();
	}

}
