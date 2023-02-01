package section4;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;


public class CountExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Pipeline p = Pipeline.create();

		PCollection<String> pCount = p.apply(TextIO.read().from("C:\\Beam\\Count.csv"));

		/*PCollection<Long> pLong = pCount.apply(Count.globally());
		
		pLong.apply(ParDo.of(new DoFn<Long, Void>() {
			
			@ProcessElement
			public void processElement(ProcessContext c) {
				System.out.println(c.element());
			}
		}));
		
				
		p.run();*/
	}

}