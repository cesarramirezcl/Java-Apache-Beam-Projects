package section1;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.values.PCollection;

public class InstallTest {

	public static void main(String[] args) {
		Pipeline pipeline = Pipeline.create();
		System.out.println("done");
		//PCollection<String> outputList = pipeline.apply(TextIO.read().from("C:\\Beam\\input.csv"));
		//outputList.apply(TextIO.write().to("C:\\Beam\\output.csv").withNumShards(1).withSuffix(".csv"));
		//pipeline.run();
	}

}
