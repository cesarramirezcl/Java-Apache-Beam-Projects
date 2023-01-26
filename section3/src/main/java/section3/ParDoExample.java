package section3;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

class CustFilter extends DoFn<String, String>{
	
	@ProcessElement
	public void processElement(ProcessContext c) {
		String line = c.element();
		String arr[] = line.split(",");
		if(arr[3].equals("Los Angeles")) {
			c.output(line);
		}
	}
		
}

public class ParDoExample {

	public static void main(String[] args) {
		Pipeline p = Pipeline.create();
		PCollection<String> pCustList = p.apply(TextIO.read().from("/home/cesar/Desktop/customer_pardo.csv"));
		PCollection<String> pOutput = pCustList.apply(ParDo.of(new CustFilter()));
		pOutput.apply(TextIO.write().to("customer_pardo_output.csv").withHeader("Id,Name,Last Name,City").withNumShards(1).withSuffix(".csv"));
		p.run();
		System.out.print("Done");
	}
	
}
